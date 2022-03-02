package de.arthurpicht.taskRunner.runner;

public class TaskRunnerException extends RuntimeException {

    public TaskRunnerException() {
    }

    public TaskRunnerException(String message) {
        super(message);
    }

    public TaskRunnerException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskRunnerException(Throwable cause) {
        super(cause);
    }

    public TaskRunnerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
