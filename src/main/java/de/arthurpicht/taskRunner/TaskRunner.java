package de.arthurpicht.taskRunner;

import de.arthurpicht.taskRunner.runner.TaskRunnerException;
import de.arthurpicht.taskRunner.runner.TaskRunnerResult;
import de.arthurpicht.taskRunner.runner.TaskRunnerResultBuilder;
import de.arthurpicht.taskRunner.task.Task;
import de.arthurpicht.taskRunner.task.TaskExecutionException;
import de.arthurpicht.taskRunner.task.TaskExecutionFunction;
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

        LocalDateTime localDateTimeStart = LocalDateTime.now();

        for (String taskName : taskList) {
            Task task = this.taskRegistry.getTask(taskName);
            TaskExecutionFunction taskExecutionFunction = task.getExecution();
            try {
                taskExecutionFunction.execute();
                taskRunnerResultBuilder.addTaskSuccess(taskName);
            } catch (TaskExecutionException e) {
                taskRunnerResultBuilder.withTaskExecutionException(e);
                taskRunnerResultBuilder.withTaskFailed(taskName);
                taskRunnerResultBuilder.withFail();
                return taskRunnerResultBuilder.build();
            } catch (RuntimeException e) {
                taskRunnerResultBuilder.withRuntimeException(e);
                taskRunnerResultBuilder.withTaskFailed(taskName);
                taskRunnerResultBuilder.withFail();
                return taskRunnerResultBuilder.build();
            }
        }

        taskRunnerResultBuilder.withSuccess();
        return taskRunnerResultBuilder.build();
    }

}
