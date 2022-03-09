package de.arthurpicht.taskRunner;

import de.arthurpicht.taskRunner.runner.*;
import de.arthurpicht.taskRunner.task.*;
import de.arthurpicht.taskRunner.taskRegistry.TaskRegistry;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class TaskRunner {

    private final TaskRegistry taskRegistry;
    private final PreExecuteFunction preExecuteFunction;
    private final SuccessExecuteFunction successExecuteFunction;
    private final SkipExecuteFunction skipExecuteFunction;
    private final FailByTaskExecutionExceptionFunction failByTaskExecutionExceptionFunction;
    private final FailByRuntimeExceptionFunction failByRuntimeExceptionFunction;

    public TaskRunner(TaskRegistry taskRegistry) {
        this.taskRegistry = taskRegistry;
        this.preExecuteFunction = null;
        this.successExecuteFunction = null;
        this.skipExecuteFunction = null;
        this.failByTaskExecutionExceptionFunction = null;
        this.failByRuntimeExceptionFunction = null;
    }

    public TaskRunner(
            TaskRegistry taskRegistry,
            PreExecuteFunction preExecuteFunction,
            SuccessExecuteFunction successExecuteFunction,
            SkipExecuteFunction skipExecuteFunction,
            FailByTaskExecutionExceptionFunction failByTaskExecutionExceptionFunction,
            FailByRuntimeExceptionFunction failByRuntimeExceptionFunction) {

        this.taskRegistry = taskRegistry;
        this.preExecuteFunction = preExecuteFunction;
        this.successExecuteFunction = successExecuteFunction;
        this.skipExecuteFunction = skipExecuteFunction;
        this.failByTaskExecutionExceptionFunction = failByTaskExecutionExceptionFunction;
        this.failByRuntimeExceptionFunction = failByRuntimeExceptionFunction;
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
            try {
                preExecution(task);
                if (isSkipExecution(task)) {
                    skipExecution(task);
                    taskRunnerResultBuilder.addTaskSuccess(taskName);
                    continue;
                }
                execute(task);
                postExecution(task);
                taskRunnerResultBuilder.addTaskSuccess(taskName);
            } catch (TaskExecutionException e) {
                failByTaskExecutionExceptionExecution(task, e);
                taskRunnerResultBuilder.withTaskExecutionException(e);
                taskRunnerResultBuilder.withTaskFailed(taskName);
                taskRunnerResultBuilder.withFail();
                taskRunnerResultBuilder.withTimestampFinish(LocalDateTime.now());
                return taskRunnerResultBuilder.build();
            } catch (RuntimeException e) {
                failByRuntimeExceptionExecution(task, e);
                taskRunnerResultBuilder.withRuntimeException(e);
                taskRunnerResultBuilder.withTaskFailed(taskName);
                taskRunnerResultBuilder.withFail();
                taskRunnerResultBuilder.withTimestampFinish(LocalDateTime.now());
                return taskRunnerResultBuilder.build();
            }
        }

        taskRunnerResultBuilder.withSuccess();
        taskRunnerResultBuilder.withTimestampFinish(LocalDateTime.now());
        return taskRunnerResultBuilder.build();
    }

    private void execute(Task task) throws TaskExecutionException {
        TaskExecutionFunction taskExecutionFunction = task.getExecution();
        taskExecutionFunction.execute();
    }

    private boolean isSkipExecution(Task task) {
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

    private void skipExecution(Task task) {
        if (this.skipExecuteFunction == null) return;
        this.skipExecuteFunction.onSkipExecute(task);
    }

    private void preExecution(Task task) {
        if (this.preExecuteFunction == null) return;
        this.preExecuteFunction.onPreExecute(task);
    }

    private void postExecution(Task task) {
        if (this.successExecuteFunction == null) return;
        this.successExecuteFunction.onSuccessExecute(task);
    }

    private void failByTaskExecutionExceptionExecution(Task task, TaskExecutionException taskExecutionException) {
        if (this.failByTaskExecutionExceptionFunction == null) return;
        try {
            this.failByTaskExecutionExceptionFunction.onFail(task, taskExecutionException);
        } catch (RuntimeException e) {
            // TODO log
        }
    }

    private void failByRuntimeExceptionExecution(Task task, RuntimeException runtimeException) {
        if (this.failByRuntimeExceptionFunction == null) return;
        try {
            this.failByRuntimeExceptionFunction.onFail(task, runtimeException);
        } catch (RuntimeException e) {
            // TODO log
        }
    }


}
