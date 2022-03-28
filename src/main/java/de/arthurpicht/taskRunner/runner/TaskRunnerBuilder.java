package de.arthurpicht.taskRunner.runner;

import de.arthurpicht.taskRunner.TaskRunner;
import de.arthurpicht.taskRunner.taskRegistry.TaskRegistry;
import de.arthurpicht.taskRunner.taskRegistry.TaskRegistryBuilder;
import org.slf4j.Logger;

public class TaskRunnerBuilder {

    private TaskRegistry taskRegistry;
    private OnPreExecuteCallback onPreExecuteCallback;
    private OnSuccessCallback onSuccessCallback;
    private OnSkipCallback onSkipCallback;
    private OnUpToDateCallback onUpToDateCallback;
    private OnFailByTaskExecutionExceptionCallback onFailByTaskExecutionExceptionCallback;
    private OnFailByTaskPreconditionExceptionCallback onFailByTaskPreconditionExceptionCallback;
    private OnFailByRuntimeExceptionFunctionCallback onFailByRuntimeExceptionFunctionCallback;
    private Logger logger;

    public TaskRunnerBuilder() {
        this.taskRegistry = null;
        this.onPreExecuteCallback = null;
        this.onSuccessCallback = null;
        this.onSkipCallback = null;
        this.onUpToDateCallback = null;
        this.onFailByTaskExecutionExceptionCallback = null;
        this.onFailByTaskPreconditionExceptionCallback = null;
        this.onFailByRuntimeExceptionFunctionCallback = null;
        this.logger = null;
    }

    public TaskRunnerBuilder withTaskRegistry(TaskRegistry taskRegistry) {
        this.taskRegistry = taskRegistry;
        return this;
    }

    public TaskRunnerBuilder withTaskRegistry(TaskRegistryBuilder taskRegistryBuilder) {
        this.taskRegistry = taskRegistryBuilder.build();
        return this;
    }

    public TaskRunnerBuilder withOnPreExecuteCallback(OnPreExecuteCallback onPreExecuteCallback) {
        this.onPreExecuteCallback = onPreExecuteCallback;
        return this;
    }

    public TaskRunnerBuilder withOnSuccessCallback(OnSuccessCallback onSuccessCallback) {
        this.onSuccessCallback = onSuccessCallback;
        return this;
    }

    public TaskRunnerBuilder withOnSkipCallback(OnSkipCallback skipExecutionFunction) {
        this.onSkipCallback = skipExecutionFunction;
        return this;
    }

    public TaskRunnerBuilder withOnUpToDateCallback(OnUpToDateCallback onUpToDateCallback) {
        this.onUpToDateCallback = onUpToDateCallback;
        return this;
    }

    public TaskRunnerBuilder withOnFailByTaskExecutionExceptionCallback(OnFailByTaskExecutionExceptionCallback onFailByTaskExecutionExceptionCallback) {
        this.onFailByTaskExecutionExceptionCallback = onFailByTaskExecutionExceptionCallback;
        return this;
    }

    public TaskRunnerBuilder withOnFailByTaskPreconditionExceptionCallback(OnFailByTaskPreconditionExceptionCallback onFailByTaskPreconditionExceptionCallback) {
        this.onFailByTaskPreconditionExceptionCallback = onFailByTaskPreconditionExceptionCallback;
        return this;
    }

    public TaskRunnerBuilder withOnFailByRuntimeExceptionCallback(OnFailByRuntimeExceptionFunctionCallback onFailByRuntimeExceptionFunctionCallback) {
        this.onFailByRuntimeExceptionFunctionCallback = onFailByRuntimeExceptionFunctionCallback;
        return this;
    }

    public TaskRunnerBuilder withLogger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public TaskRunner build() {
        if (this.taskRegistry == null)
            throw new IllegalStateException("Cannot build " + TaskRunner.class.getSimpleName()
                    + ". No " + TaskRegistry.class.getSimpleName() + " specified.");
        return new TaskRunner(
                this.taskRegistry,
                this.onPreExecuteCallback,
                this.onSuccessCallback,
                this.onSkipCallback,
                this.onUpToDateCallback,
                this.onFailByTaskPreconditionExceptionCallback,
                this.onFailByTaskExecutionExceptionCallback,
                this.onFailByRuntimeExceptionFunctionCallback,
                this.logger
        );
    }

}
