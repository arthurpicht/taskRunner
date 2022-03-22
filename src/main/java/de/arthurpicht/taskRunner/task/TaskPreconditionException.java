package de.arthurpicht.taskRunner.task;

public class TaskPreconditionException extends TaskExecutionException {

    public TaskPreconditionException() {
    }

    public TaskPreconditionException(String message) {
        super(message);
    }

    public TaskPreconditionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskPreconditionException(Throwable cause) {
        super(cause);
    }

    public TaskPreconditionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
