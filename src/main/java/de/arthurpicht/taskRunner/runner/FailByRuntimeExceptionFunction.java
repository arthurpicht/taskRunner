package de.arthurpicht.taskRunner.runner;

import de.arthurpicht.taskRunner.task.Task;

public interface FailByRuntimeExceptionFunction {
    void onFail(Task task, RuntimeException runtimeException);
}
