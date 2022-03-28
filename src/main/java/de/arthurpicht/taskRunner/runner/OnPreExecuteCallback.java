package de.arthurpicht.taskRunner.runner;

import de.arthurpicht.taskRunner.task.Task;

public interface OnPreExecuteCallback {
    void onPreExecute(Task task);
}
