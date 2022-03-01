package de.arthurpicht.taskRunner;

public class TaskRunnerInitializationException extends RuntimeException {

    public TaskRunnerInitializationException() {
    }

    public TaskRunnerInitializationException(String message) {
        super(message);
    }

    public TaskRunnerInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskRunnerInitializationException(Throwable cause) {
        super(cause);
    }

    public TaskRunnerInitializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
