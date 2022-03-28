package de.arthurpicht.taskRunner.runner;

import de.arthurpicht.taskRunner.task.Task;
import de.arthurpicht.taskRunner.task.TaskExecutionException;

public interface OnFailByTaskPreconditionExceptionCallback {
    void onFail(Task task, TaskExecutionException taskExecutionException);
}
