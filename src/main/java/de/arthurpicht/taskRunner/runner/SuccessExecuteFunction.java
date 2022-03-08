package de.arthurpicht.taskRunner.runner;

import de.arthurpicht.taskRunner.task.Task;

public interface SuccessExecuteFunction {
    void onSuccessExecute(Task task);
}
