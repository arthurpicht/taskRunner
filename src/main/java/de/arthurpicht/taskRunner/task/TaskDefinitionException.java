package de.arthurpicht.taskRunner.task;

public class TaskDefinitionException extends RuntimeException {

    public TaskDefinitionException() {
    }

    public TaskDefinitionException(String message) {
        super(message);
    }

    public TaskDefinitionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskDefinitionException(Throwable cause) {
        super(cause);
    }

    public TaskDefinitionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
