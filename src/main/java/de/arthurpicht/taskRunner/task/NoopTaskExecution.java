package de.arthurpicht.taskRunner.task;

public class NoopTaskExecution implements TaskExecutionFunction {

    @Override
    public void execute() throws TaskExecutionException {
        // do intentionally nothing
    }

}
