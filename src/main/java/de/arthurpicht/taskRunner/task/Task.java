package de.arthurpicht.taskRunner.task;

import java.util.Set;

public interface Task {

    public String getName();

    public String getDescription();

    public boolean isTarget();

    public Set<String> getDependencies();

    public boolean hasInputChangedFunction();

    public InputChangedFunction inputChanged();

    public boolean hasOutputExistsFunction();

    public OutputExistsFunction outputExists();

    public TaskExecutionFunction getExecution();

}
