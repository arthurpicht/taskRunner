package de.arthurpicht.taskRunner.task;

public interface TaskPreconditionFunction {

    void execute() throws TaskPreconditionException;

}
