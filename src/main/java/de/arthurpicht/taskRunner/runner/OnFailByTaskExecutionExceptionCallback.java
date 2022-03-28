package de.arthurpicht.taskRunner.runner;

import de.arthurpicht.taskRunner.task.Task;
import de.arthurpicht.taskRunner.task.TaskExecutionException;

public interface OnFailByTaskExecutionExceptionCallback {
    void onFail(Task task, TaskExecutionException taskExecutionException);
}
