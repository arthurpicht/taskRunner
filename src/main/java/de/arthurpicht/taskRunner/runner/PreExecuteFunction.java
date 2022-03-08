package de.arthurpicht.taskRunner.runner;

import de.arthurpicht.taskRunner.task.Task;

public interface PreExecuteFunction {
    void onPreExecute(Task task);
}
