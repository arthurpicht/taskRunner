package de.arthurpicht.taskRunner.runner;

import de.arthurpicht.taskRunner.task.Task;

public interface OnFailByRuntimeExceptionFunctionCallback {
    void onFail(Task task, RuntimeException runtimeException);
}
