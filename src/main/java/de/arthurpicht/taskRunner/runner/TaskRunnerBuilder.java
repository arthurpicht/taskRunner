package de.arthurpicht.taskRunner.runner;

import de.arthurpicht.taskRunner.TaskRunner;
import de.arthurpicht.taskRunner.taskRegistry.TaskRegistry;
import de.arthurpicht.taskRunner.taskRegistry.TaskRegistryBuilder;

public class TaskRunnerBuilder {

    private TaskRegistry taskRegistry;
    private PreExecuteFunction preExecuteFunction;
    private SuccessExecuteFunction successExecuteFunction;
    private SkipExecuteFunction skipExecuteFunction;
    private FailByTaskExecutionExceptionFunction failByTaskExecutionExceptionFunction;
    private FailByTaskPreconditionExceptionFunction failByTaskPreconditionExceptionFunction;
    private FailByRuntimeExceptionFunction failByRuntimeExceptionFunction;

    public TaskRunnerBuilder() {
        this.taskRegistry = null;
        this.preExecuteFunction = null;
        this.successExecuteFunction = null;
        this.skipExecuteFunction = null;
        this.failByTaskExecutionExceptionFunction = null;
        this.failByTaskPreconditionExceptionFunction = null;
        this.failByRuntimeExceptionFunction = null;
    }

    public TaskRunnerBuilder withTaskRegistry(TaskRegistry taskRegistry) {
        this.taskRegistry = taskRegistry;
        return this;
    }

    public TaskRunnerBuilder withTaskRegistry(TaskRegistryBuilder taskRegistryBuilder) {
        this.taskRegistry = taskRegistryBuilder.build();
        return this;
    }

    public TaskRunnerBuilder withPreExecution(PreExecuteFunction preExecuteFunction) {
        this.preExecuteFunction = preExecuteFunction;
        return this;
    }

    public TaskRunnerBuilder withSuccessExecution(SuccessExecuteFunction successExecuteFunction) {
        this.successExecuteFunction = successExecuteFunction;
        return this;
    }

    public TaskRunnerBuilder withSkipExecution(SkipExecuteFunction skipExecutionFunction) {
        this.skipExecuteFunction = skipExecutionFunction;
        return this;
    }

    public TaskRunnerBuilder withFailByTaskExecutionException(FailByTaskExecutionExceptionFunction failByTaskExecutionExceptionFunction) {
        this.failByTaskExecutionExceptionFunction = failByTaskExecutionExceptionFunction;
        return this;
    }

    public TaskRunnerBuilder withFailByTaskPreconditionException(FailByTaskPreconditionExceptionFunction failByTaskPreconditionExceptionFunction) {
        this.failByTaskPreconditionExceptionFunction = failByTaskPreconditionExceptionFunction;
        return this;
    }

    public TaskRunnerBuilder withFailByRuntimeException(FailByRuntimeExceptionFunction failByRuntimeExceptionFunction) {
        this.failByRuntimeExceptionFunction = failByRuntimeExceptionFunction;
        return this;
    }

    public TaskRunner build() {
        if (this.taskRegistry == null)
            throw new IllegalStateException("Cannot build " + TaskRunner.class.getSimpleName()
                    + ". No " + TaskRegistry.class.getSimpleName() + " specified.");
        return new TaskRunner(
                this.taskRegistry,
                this.preExecuteFunction,
                this.successExecuteFunction,
                this.skipExecuteFunction,
                this.failByTaskPreconditionExceptionFunction,
                this.failByTaskExecutionExceptionFunction,
                this.failByRuntimeExceptionFunction
        );
    }

}
