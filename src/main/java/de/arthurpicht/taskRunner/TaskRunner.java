package de.arthurpicht.taskRunner;

import de.arthurpicht.taskRunner.runner.*;
import de.arthurpicht.taskRunner.task.*;
import de.arthurpicht.taskRunner.taskRegistry.TaskRegistry;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class TaskRunner {

    private final TaskRegistry taskRegistry;
    private final OnPreExecuteCallback onPreExecuteCallback;
    private final OnSuccessCallback onSuccessCallback;
    private final OnSkipCallback onSkipCallback;
    private final OnUpToDateCallback onUpToDateCallback;
    private final OnFailByTaskExecutionExceptionCallback onFailByTaskExecutionExceptionCallback;
    private final OnFailByTaskPreconditionExceptionCallback onFailByTaskPreconditionExceptionCallback;
    private final OnFailByRuntimeExceptionFunctionCallback onFailByRuntimeExceptionFunctionCallback;
    private final Logger logger;

    public TaskRunner(TaskRegistry taskRegistry) {
        this.taskRegistry = taskRegistry;
        this.onPreExecuteCallback = null;
        this.onSuccessCallback = null;
        this.onSkipCallback = null;
        this.onUpToDateCallback = null;
        this.onFailByTaskPreconditionExceptionCallback = null;
        this.onFailByTaskExecutionExceptionCallback = null;
        this.onFailByRuntimeExceptionFunctionCallback = null;
        this.logger = null;
    }

    public TaskRunner(
            TaskRegistry taskRegistry,
            OnPreExecuteCallback onPreExecuteCallback,
            OnSuccessCallback onSuccessCallback,
            OnSkipCallback onSkipCallback,
            OnUpToDateCallback onUpToDateCallback,
            OnFailByTaskPreconditionExceptionCallback onFailByTaskPreconditionExceptionCallback,
            OnFailByTaskExecutionExceptionCallback onFailByTaskExecutionExceptionCallback,
            OnFailByRuntimeExceptionFunctionCallback onFailByRuntimeExceptionFunctionCallback,
            Logger logger) {

        this.taskRegistry = taskRegistry;
        this.onPreExecuteCallback = onPreExecuteCallback;
        this.onSuccessCallback = onSuccessCallback;
        this.onSkipCallback = onSkipCallback;
        this.onUpToDateCallback = onUpToDateCallback;
        this.onFailByTaskPreconditionExceptionCallback = onFailByTaskPreconditionExceptionCallback;
        this.onFailByTaskExecutionExceptionCallback = onFailByTaskExecutionExceptionCallback;
        this.onFailByRuntimeExceptionFunctionCallback = onFailByRuntimeExceptionFunctionCallback;
        this.logger = logger;
    }

    public Set<String> getTargets() {
        return this.taskRegistry.getTargets();
    }

    public TaskRunnerResult run(String target) {
        if (!this.taskRegistry.hasTarget(target))
            throw new TaskRunnerException("No such target: [" + target + "].");

        List<String> taskList = this.taskRegistry.getTaskList(target);
        TaskRunnerResultBuilder taskRunnerResultBuilder = new TaskRunnerResultBuilder(target);
        taskRunnerResultBuilder.withTaskList(taskList);
        taskRunnerResultBuilder.withTimestampStart(LocalDateTime.now());

        for (String taskName : taskList) {
            Task task = this.taskRegistry.getTask(taskName);

            executeHandlerPreExecution(task);

            try {
                if (skip(task)) {
                    executeHandlerOnSkip(task);
                    taskRunnerResultBuilder.addTaskSuccess(taskName);
                    continue;
                }
            } catch (RuntimeException e) {
                executeHandlerFailByRuntimeException(task, e);
                return completeResultWithRuntimeException(taskRunnerResultBuilder, taskName, e).build();
            }

            try {
                checkPrecondition(task);
            } catch (TaskPreconditionException e) {
                executeHandlerFailByTaskPreconditionException(task, e);
                return completeResultWithTaskPreconditionException(taskRunnerResultBuilder, taskName, e).build();
            } catch (RuntimeException e) {
                executeHandlerFailByRuntimeException(task, e);
                return completeResultWithRuntimeException(taskRunnerResultBuilder, taskName, e).build();
            }

            try {
                if (isUpToDate(task)) {
                    executeHandlerOnUpToDateExecution(task);
                    taskRunnerResultBuilder.addTaskSuccess(taskName);
                    continue;
                }
            } catch (RuntimeException e) {
                executeHandlerFailByRuntimeException(task, e);
                return completeResultWithRuntimeException(taskRunnerResultBuilder, taskName, e).build();
            }

            try {
                execute(task);
                executeHandlerPostExecution(task);
                taskRunnerResultBuilder.addTaskSuccess(taskName);
            } catch (TaskExecutionException e) {
                executeHandlerFailByTaskExecutionException(task, e);
                return completeResultWithTaskExecutionException(taskRunnerResultBuilder, taskName, e).build();
            } catch (RuntimeException e) {
                executeHandlerFailByRuntimeException(task, e);
                return completeResultWithRuntimeException(taskRunnerResultBuilder, taskName, e).build();
            }
        }

        taskRunnerResultBuilder.withSuccess();
        taskRunnerResultBuilder.withTimestampFinish(LocalDateTime.now());
        return taskRunnerResultBuilder.build();
    }

    public List<TaskRunnerResult> run(String... targets) {
        List<TaskRunnerResult> taskRunnerResultList = new ArrayList<>();
        for (String target : targets) {
            TaskRunnerResult taskRunnerResult = run(target);
            taskRunnerResultList.add(taskRunnerResult);
            if (!taskRunnerResult.isSuccess()) break;
        }
        return taskRunnerResultList;
    }

    private TaskRunnerResultBuilder completeResultWithTaskExecutionException(TaskRunnerResultBuilder taskRunnerResultBuilder, String taskName, TaskExecutionException e) {
        taskRunnerResultBuilder.withFail();
        taskRunnerResultBuilder.withTaskFailed(taskName);
        taskRunnerResultBuilder.withTaskExecutionException(e);
        taskRunnerResultBuilder.withTimestampFinish(LocalDateTime.now());
        return taskRunnerResultBuilder;
    }

    private TaskRunnerResultBuilder completeResultWithTaskPreconditionException(TaskRunnerResultBuilder taskRunnerResultBuilder, String taskName, TaskPreconditionException e) {
        taskRunnerResultBuilder.withFail();
        taskRunnerResultBuilder.withTaskFailed(taskName);
        taskRunnerResultBuilder.withTaskPreconditionException(e);
        taskRunnerResultBuilder.withTimestampFinish(LocalDateTime.now());
        return taskRunnerResultBuilder;
    }

    private TaskRunnerResultBuilder completeResultWithRuntimeException(TaskRunnerResultBuilder taskRunnerResultBuilder, String taskName, RuntimeException e) {
        taskRunnerResultBuilder.withFail();
        taskRunnerResultBuilder.withTaskFailed(taskName);
        taskRunnerResultBuilder.withRuntimeException(e);
        taskRunnerResultBuilder.withTimestampFinish(LocalDateTime.now());
        return taskRunnerResultBuilder;
    }

    private void checkPrecondition(Task task) throws TaskPreconditionException {
        if (task.hasPreconditionFunction()) {
            TaskPreconditionFunction taskPreconditionFunction = task.precondition();
            taskPreconditionFunction.execute();
        }
    }

    private void execute(Task task) throws TaskExecutionException {
        TaskExecutionFunction taskExecutionFunction = task.getExecution();
        taskExecutionFunction.execute();
    }

    private boolean isUpToDate(Task task) {
        return !inputChanged(task) && outputExists(task);
    }

    private boolean inputChanged(Task task) {
        if (task.hasInputChangedFunction()) {
            InputChangedFunction inputChangedFunction = task.inputChanged();
            return inputChangedFunction.inputChanged();
        } else {
            return true;
        }
    }

    private boolean outputExists(Task task) {
        if (task.hasOutputExistsFunction()) {
            OutputExistsFunction outputExistsFunction = task.outputExists();
            return outputExistsFunction.outputExists();
        } else {
            return false;
        }
    }

    private boolean skip(Task task) {
        if (task.hasSkipFunction()) {
            TaskSkipFunction taskSkipFunction = task.skip();
            return taskSkipFunction.skip();
        } else {
            return false;
        }
    }

    private void executeHandlerOnSkip(Task task) {
        if (this.onSkipCallback == null) return;
        this.onSkipCallback.onSkipExecute(task);
    }

    private void executeHandlerOnUpToDateExecution(Task task) {
        if (this.onUpToDateCallback == null) return;
        this.onUpToDateCallback.onUpToDateExecute(task);
    }

    private void executeHandlerPreExecution(Task task) {
        if (this.onPreExecuteCallback == null) return;
        this.onPreExecuteCallback.onPreExecute(task);
    }

    private void executeHandlerPostExecution(Task task) {
        if (this.onSuccessCallback == null) return;
        this.onSuccessCallback.onSuccess(task);
    }

    private void executeHandlerFailByTaskPreconditionException(Task task, TaskPreconditionException taskPreconditionException) {
        if (this.onFailByTaskPreconditionExceptionCallback == null) return;
        try {
            this.onFailByTaskPreconditionExceptionCallback.onFail(task, taskPreconditionException);
        } catch (RuntimeException e) {
            if (this.logger != null) {
                this.logger.error("RuntimeException occurred when calling "
                        + OnFailByTaskPreconditionExceptionCallback.class.getSimpleName() + ": ", e);
            }
        }
    }

    private void executeHandlerFailByTaskExecutionException(Task task, TaskExecutionException taskExecutionException) {
        if (this.onFailByTaskExecutionExceptionCallback == null) return;
        try {
            this.onFailByTaskExecutionExceptionCallback.onFail(task, taskExecutionException);
        } catch (RuntimeException e) {
            if (this.logger != null) {
                this.logger.error("RuntimeException occurred when calling "
                        + OnFailByTaskExecutionExceptionCallback.class.getSimpleName() + ": ", e);
            }
        }
    }

    private void executeHandlerFailByRuntimeException(Task task, RuntimeException runtimeException) {
        if (this.onFailByRuntimeExceptionFunctionCallback == null) return;
        try {
            this.onFailByRuntimeExceptionFunctionCallback.onFail(task, runtimeException);
        } catch (RuntimeException e) {
            if (this.logger != null) {
                this.logger.error("RuntimeException occurred when calling "
                        + OnFailByRuntimeExceptionFunctionCallback.class.getSimpleName() + ": ", e);
            }
        }
    }

}
