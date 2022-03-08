package de.arthurpicht.taskRunner.runner;

import de.arthurpicht.taskRunner.task.Task;

public interface SkipExecuteFunction {
    void onSkipExecute(Task task);
}
