package de.arthurpicht.taskRunner.taskRegistry;

import java.util.*;

public class TaskRegistry {

    private final Map<String, List<String>> targetToTaskListMap;

    public TaskRegistry(Map<String, List<String>> targetToTaskListMap) {
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

    public boolean hasTarget(String target) {
        return this.targetToTaskListMap.containsKey(target);
    }

    public List<String> getTaskList(String target) {
        if (!this.targetToTaskListMap.containsKey(target))
            throw new IllegalArgumentException("No such target in " + TaskRegistry.class.getSimpleName()
                    + ": [" + target + "].");
        return this.targetToTaskListMap.get(target);
    }

}
