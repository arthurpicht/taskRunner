package de.arthurpicht.taskRunner.taskRegistry;

import de.arthurpicht.taskRunner.task.Task;

import java.util.*;

import static de.arthurpicht.utils.core.assertion.MethodPreconditions.assertArgumentNotNullAndNotEmpty;

public class TaskRegistry {

    private final Map<String, Task> taskMap;
    private final Map<String, List<String>> targetToTaskListMap;

    public TaskRegistry(Map<String, Task> taskMap, Map<String, List<String>> targetToTaskListMap) {
        assertArgumentNotNullAndNotEmpty("taskMap", taskMap);
        assertArgumentNotNullAndNotEmpty("targetToTaskListMap", targetToTaskListMap);

        this.taskMap = taskMap;
        this.targetToTaskListMap = new LinkedHashMap<>();
        for (String target : targetToTaskListMap.keySet()) {
            List<String> targetToTaskList = targetToTaskListMap.get(target);
            this.targetToTaskListMap.put(target, Collections.unmodifiableList(targetToTaskList));
        }
    }

    public Set<String> getTargets() {
        Set<String> targets = this.targetToTaskListMap.keySet();
        return Collections.unmodifiableSet(targets);
    }

    public boolean hasTarget(String targetTaskName) {
        return this.targetToTaskListMap.containsKey(targetTaskName);
    }

    public List<String> getTaskList(String targetTaskName) {
        if (!this.targetToTaskListMap.containsKey(targetTaskName))
            throw new IllegalArgumentException("No such target in " + TaskRegistry.class.getSimpleName()
                    + ": [" + targetTaskName + "].");
        return this.targetToTaskListMap.get(targetTaskName);
    }

    public Task getTask(String taskName) {
        if (!this.taskMap.containsKey(taskName))
            throw new IllegalArgumentException("No such task in " + TaskRegistry.class.getSimpleName()
                    + ": [" + taskName + "}.");
        return this.taskMap.get(taskName);
    }

}
