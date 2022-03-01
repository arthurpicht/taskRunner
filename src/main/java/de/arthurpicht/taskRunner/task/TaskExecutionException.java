package de.arthurpicht.taskRunner.task;

public class TaskExecutionException extends Exception {

    public TaskExecutionException() {
    }

    public TaskExecutionException(String message) {
        super(message);
    }

    public TaskExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskExecutionException(Throwable cause) {
        super(cause);
    }

    public TaskExecutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
