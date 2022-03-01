package de.arthurpicht.taskRunner.task;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class TaskBuilder {

    private String name;
    private String description;
    private boolean isTarget;
    private TaskExecutionFunction taskExecution;
    private Set<String> dependencies;

    public TaskBuilder() {
        this.name = "";
        this.description = "";
        this.isTarget = false;
        this.taskExecution = null;
        this.dependencies = new LinkedHashSet<>();
    }

    public TaskBuilder name(String name) {
        this.name = name;
        return this;
    }

    public TaskBuilder description(String description) {
        this.description = description;
        return this;
    }

    public TaskBuilder isTarget() {
        this.isTarget = true;
        return this;
    }

    public TaskBuilder execute(TaskExecutionFunction taskExecution) {
        this.taskExecution = taskExecution;
        return this;
    }

    public TaskBuilder dependencies(String... dependencies) {
        this.dependencies = new LinkedHashSet<>(Arrays.asList(dependencies));
        return this;
    }

    public BasicTask build() {
        if (this.name.equals(""))
            throw new TaskDefinitionException("No name specified for task.");
        if (this.taskExecution == null)
            throw new TaskDefinitionException("No execution specified for task '" + this.name + "'.");
        return new BasicTask(this.name, this.description, this.isTarget, this.dependencies, this.taskExecution);
    }

}
