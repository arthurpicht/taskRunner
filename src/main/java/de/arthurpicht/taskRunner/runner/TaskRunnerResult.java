package de.arthurpicht.taskRunner.runner;

import de.arthurpicht.taskRunner.task.TaskExecutionException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class TaskRunnerResult {

    private final boolean success;
    private final String target;
    private final List<String> taskList;
    private final List<String> taskListSuccess;
    private final String taskFailed;
    private final LocalDateTime timestampStart;
    private final LocalDateTime timestampFinish;
    private final Duration duration;
    private final TaskExecutionException taskExecutionException;
    private final RuntimeException runtimeException;

    public TaskRunnerResult(
            boolean success,
            String target,
            List<String> taskList,
            List<String> taskListSuccess,
            String taskFailed,
            LocalDateTime timestampStart,
            LocalDateTime timestampFinish,
            Duration duration,
            TaskExecutionException taskExecutionException,
            RuntimeException runtimeException) {

        this.success = success;
        this.target = target;
        this.taskList = taskList;
        this.taskListSuccess = taskListSuccess;
        this.taskFailed = taskFailed;
        this.timestampStart = timestampStart;
        this.timestampFinish = timestampFinish;
        this.duration = duration;
        this.taskExecutionException = taskExecutionException;
        this.runtimeException = runtimeException;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getTarget() {
        return target;
    }

    public List<String> getTaskList() {
        return taskList;
    }

    public List<String> getTaskListSuccess() {
        return taskListSuccess;
    }

    public String getTaskFailed() {
        if (this.success) throw new IllegalStateException("No failed task.");
        return taskFailed;
    }

    public Duration getDuration() {
        return duration;
    }

    public TaskExecutionException getTaskExecutionException() {
        return taskExecutionException;
    }

    public RuntimeException getRuntimeException() {
        return runtimeException;
    }

    public LocalDateTime getTimestampStart() {
        return timestampStart;
    }

    public LocalDateTime getTimestampFinish() {
        return timestampFinish;
    }
}
