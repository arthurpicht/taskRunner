package de.arthurpicht.taskRunner.runner;

import de.arthurpicht.taskRunner.task.Task;
import de.arthurpicht.taskRunner.task.TaskExecutionException;

public interface FailByTaskExecutionExceptionFunction {
    void onFail(Task task, TaskExecutionException taskExecutionException);
}
