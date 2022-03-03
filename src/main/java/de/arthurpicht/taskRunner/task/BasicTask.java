package de.arthurpicht.taskRunner.task;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import static de.arthurpicht.utils.core.assertion.MethodPreconditions.assertArgumentNotNull;
import static de.arthurpicht.utils.core.assertion.MethodPreconditions.assertArgumentNotNullAndNotEmpty;

public class BasicTask implements Task {

    private final String name;
    private final String description;
    private final boolean isTarget;
    private final Set<String> dependencies;
    private final InputChangedFunction inputChanged;
    private final OutputExistsFunction outputExists;
    private final TaskExecutionFunction taskExecution;

    public BasicTask(
            String name,
            String description,
            boolean isTarget,
            Set<String> dependencies,
            InputChangedFunction inputChanged,
            OutputExistsFunction outputExists,
            TaskExecutionFunction taskExecution) {
        assertArgumentNotNullAndNotEmpty("name", name);
        assertArgumentNotNull("taskExecution", taskExecution);
        this.name = name;
        this.description = description;
        this.inputChanged = inputChanged;
        this.outputExists = outputExists;
        this.isTarget = isTarget;
        this.dependencies = Collections.unmodifiableSet(dependencies);
        this.taskExecution = taskExecution;
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

    @Override
    public Set<String> getDependencies() {
        return this.dependencies;
    }

    @Override
    public boolean hasInputChangedFunction() {
        return this.inputChanged != null;
    }

    @Override
    public InputChangedFunction inputChanged() {
        return this.inputChanged;
    }

    @Override
    public boolean hasOutputExistsFunction() {
        return this.outputExists != null;
    }

    @Override
    public OutputExistsFunction outputExists() {
        return this.outputExists;
    }

    @Override
    public TaskExecutionFunction getExecution() {
        return this.taskExecution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicTask basicTask = (BasicTask) o;
        return name.equals(basicTask.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
