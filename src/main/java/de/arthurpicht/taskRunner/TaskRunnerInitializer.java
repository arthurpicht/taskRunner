package de.arthurpicht.taskRunner;

import de.arthurpicht.taskRunner.task.BasicTask;

import java.util.HashMap;

public class TaskRunnerInitializer {

    private HashMap<String, BasicTask> targetMap;

    public TaskRunnerInitializer addTarget(String name, BasicTask targetTask) {

        this.targetMap.put(name, targetTask);
        return this;
    }

}
