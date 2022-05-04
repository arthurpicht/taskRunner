package de.arthurpicht.taskRunner.task;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class TaskBuilder {

    private String name;
    private String description;
    private boolean isTarget;
    private boolean isMeta;
    private Set<String> dependencies;
    private TaskSkipFunction taskSkipFunction;
    private TaskPreconditionFunction taskPrecondition;
    private InputChangedFunction inputChanged;
    private OutputExistsFunction outputExists;
    private TaskExecutionFunction taskExecution;

    public TaskBuilder() {
        this.name = "";
        this.description = "";
        this.isTarget = false;
        this.isMeta = false;
        this.dependencies = new LinkedHashSet<>();
        this.taskSkipFunction = null;
        this.taskPrecondition = null;
        this.inputChanged = null;
        this.outputExists = null;
        this.taskExecution = null;
    }

    public TaskBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TaskBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public TaskBuilder asTarget() {
        this.isTarget = true;
        return this;
    }

    public TaskBuilder asMetaTask() {
        this.isMeta = true;
        return this;
    }

    public TaskBuilder withDependencies(String... dependencies) {
        this.dependencies = new LinkedHashSet<>(Arrays.asList(dependencies));
        return this;
    }

    public TaskBuilder skip(TaskSkipFunction taskSkipFunction) {
        this.taskSkipFunction = taskSkipFunction;
        return this;
    }

    public TaskBuilder precondition(TaskPreconditionFunction taskPrecondition) {
        this.taskPrecondition = taskPrecondition;
        return this;
    }

    public TaskBuilder execute(TaskExecutionFunction taskExecution) {
        this.taskExecution = taskExecution;
        return this;
    }

    public TaskBuilder inputChanged(InputChangedFunction inputChanged) {
        this.inputChanged = inputChanged;
        return this;
    }

    public TaskBuilder outputExists(OutputExistsFunction outputExists) {
        this.outputExists = outputExists;
        return this;
    }

    public TaskBuilder isUpToDate(OutputExistsFunction outputExists) {
        this.inputChanged = () -> false;
        this.outputExists = outputExists;
        return this;
    }

    public Task build() {
        if (this.name.equals(""))
            throw new TaskDefinitionException("No name specified for task.");
        if (this.isMeta) {
            if (this.taskSkipFunction != null ||
                    this.taskPrecondition != null ||
                    this.inputChanged != null ||
                    this.outputExists != null ||
                    this.taskExecution != null)
                throw new TaskDefinitionException("No definition of functions allowed as task is specified as meta task.");

            return new MetaTask(
                    this.name,
                    this.description,
                    this.isTarget,
                    this.dependencies);
        } else {
            if (this.taskExecution == null)
                throw new TaskDefinitionException("No execution specified for task '" + this.name + "'.");
            return new BasicTask(
                    this.name,
                    this.description,
                    this.isTarget,
                    this.dependencies,
                    this.taskSkipFunction,
                    this.taskPrecondition,
                    this.inputChanged,
                    this.outputExists,
                    this.taskExecution);
        }
    }

}
