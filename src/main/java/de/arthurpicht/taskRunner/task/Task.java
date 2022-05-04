package de.arthurpicht.taskRunner.task;

import java.util.Set;

public interface Task {

    public String getName();

    public String getDescription();

    public boolean isTarget();

    public boolean isMeta();

    public Set<String> getDependencies();

    default public boolean hasSkipFunction() {
        return this.skip() != null;
    }

    public TaskSkipFunction skip();

    default public boolean hasPreconditionFunction() {
        return this.precondition() != null;
    }

    public TaskPreconditionFunction precondition();

    default public boolean hasInputChangedFunction() {
        return this.inputChanged() != null;
    }

    public InputChangedFunction inputChanged();

    default public boolean hasOutputExistsFunction() {
        return this.outputExists() != null;
    }

    public OutputExistsFunction outputExists();

    public TaskExecutionFunction getExecution();

}
