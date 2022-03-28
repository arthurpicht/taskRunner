package de.arthurpicht.taskRunner.runner;

import de.arthurpicht.taskRunner.task.Task;

public interface OnSuccessCallback {
    void onSuccess(Task task);
}
