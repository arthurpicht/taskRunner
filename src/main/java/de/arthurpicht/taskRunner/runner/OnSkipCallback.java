package de.arthurpicht.taskRunner.runner;

import de.arthurpicht.taskRunner.task.Task;

public interface OnSkipCallback {
    void onSkipExecute(Task task);
}
