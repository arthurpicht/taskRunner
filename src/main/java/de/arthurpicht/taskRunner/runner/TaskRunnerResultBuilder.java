package de.arthurpicht.taskRunner.runner;

import de.arthurpicht.taskRunner.task.TaskExecutionException;
import de.arthurpicht.taskRunner.task.TaskPreconditionException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskRunnerResultBuilder {

    private boolean success;
    private final String target;
    private List<String> taskList;
    private final List<String> taskListSuccess;
    private String taskFailed;
    private TaskPreconditionException taskPreconditionException;
    private TaskExecutionException taskExecutionException;
    private RuntimeException runtimeException;
    private LocalDateTime timestampStart;
    private LocalDateTime timestampFinish;

    public TaskRunnerResultBuilder(String target) {
        this.target = target;
        this.taskList = new ArrayList<>();
        this.taskListSuccess = new ArrayList<>();
    }

    public TaskRunnerResultBuilder withSuccess() {
        this.success = true;
        return this;
    }

    public TaskRunnerResultBuilder withFail() {
        this.success = false;
        return this;
    }

    public TaskRunnerResultBuilder withTaskList(List<String> taskList) {
        this.taskList = taskList;
        return this;
    }

    public TaskRunnerResultBuilder addTaskSuccess(String taskSuccess) {
        this.taskListSuccess.add(taskSuccess);
        return this;
    }

    public TaskRunnerResultBuilder withTaskFailed(String taskFailed) {
        this.taskFailed = taskFailed;
        return this;
    }

    public TaskRunnerResultBuilder withTaskPreconditionException(TaskPreconditionException taskPreconditionException) {
        this.taskPreconditionException = taskPreconditionException;
        return this;
    }

    public TaskRunnerResultBuilder withTaskExecutionException(TaskExecutionException taskExecutionException) {
        this.taskExecutionException = taskExecutionException;
        return this;
    }

    public TaskRunnerResultBuilder withRuntimeException(RuntimeException runtimeException) {
        this.runtimeException = runtimeException;
        return this;
    }

    public void withTimestampStart(LocalDateTime localDateTime) {
        this.timestampStart = localDateTime;
    }

    public void withTimestampFinish(LocalDateTime localDateTime) {
        this.timestampFinish = localDateTime;
    }

    public TaskRunnerResult build() {
        if (this.timestampStart == null) throw new IllegalStateException("[timestampStart] not specified.");
        if (this.timestampFinish == null) throw new IllegalStateException("[timestampFinish] not specified.");

        Duration duration = Duration.between(this.timestampStart, this.timestampFinish);

        return new TaskRunnerResult(
                this.success,
                this.target,
                this.taskList,
                this.taskListSuccess,
                this.taskFailed,
                this.timestampStart,
                this.timestampFinish,
                duration,
                this.taskPreconditionException,
                this.taskExecutionException,
                this.runtimeException
        );
    }

}
