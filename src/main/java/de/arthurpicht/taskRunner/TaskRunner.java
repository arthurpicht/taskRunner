package de.arthurpicht.taskRunner;

import de.arthurpicht.taskRunner.runner.TaskRunnerException;
import de.arthurpicht.taskRunner.runner.TaskRunnerResult;
import de.arthurpicht.taskRunner.runner.TaskRunnerResultBuilder;
import de.arthurpicht.taskRunner.task.*;
import de.arthurpicht.taskRunner.taskRegistry.TaskRegistry;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class TaskRunner {

    private final TaskRegistry taskRegistry;

    public TaskRunner(TaskRegistry taskRegistry) {
        this.taskRegistry = taskRegistry;
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
                if (!skipExecution(task)) execute(task);
                taskRunnerResultBuilder.addTaskSuccess(taskName);
            } catch (TaskExecutionException e) {
                taskRunnerResultBuilder.withTaskExecutionException(e);
                taskRunnerResultBuilder.withTaskFailed(taskName);
                taskRunnerResultBuilder.withFail();
                taskRunnerResultBuilder.withTimestampFinish(LocalDateTime.now());
                return taskRunnerResultBuilder.build();
            } catch (RuntimeException e) {
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

    private boolean skipExecution(Task task) {
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

}
