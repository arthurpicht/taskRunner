package de.arthurpicht.taskRunner.runner;

import de.arthurpicht.taskRunner.task.Task;

public interface OnUpToDateCallback {
    void onUpToDateExecute(Task task);
}
