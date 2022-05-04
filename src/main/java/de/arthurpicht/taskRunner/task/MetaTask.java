package de.arthurpicht.taskRunner.task;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import static de.arthurpicht.utils.core.assertion.MethodPreconditions.assertArgumentNotNull;
import static de.arthurpicht.utils.core.assertion.MethodPreconditions.assertArgumentNotNullAndNotEmpty;

public class MetaTask implements Task {

    private final String name;
    private final String description;
    private final boolean isTarget;
    private final Set<String> dependencies;

    public MetaTask(
            String name,
            String description,
            boolean isTarget,
            Set<String> dependencies) {
        assertArgumentNotNullAndNotEmpty("name", name);
        this.name = name;
        this.description = description;
        this.isTarget = isTarget;
        this.dependencies = Collections.unmodifiableSet(dependencies);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public boolean isTarget() {
        return this.isTarget;
    }

    public boolean isMeta() {
        return true;
    }

    @Override
    public Set<String> getDependencies() {
        return this.dependencies;
    }

    @Override
    public TaskSkipFunction skip() {
        return null;
    }

    @Override
    public TaskPreconditionFunction precondition() {
        return null;
    }

    @Override
    public InputChangedFunction inputChanged() {
        return null;
    }

    @Override
    public OutputExistsFunction outputExists() {
        return null;
    }

    @Override
    public TaskExecutionFunction getExecution() {
        return new NoopTaskExecution();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetaTask basicTask = (MetaTask) o;
        return name.equals(basicTask.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
