package de.arthurpicht.taskRunner.taskRegistry;

import de.arthurpicht.taskRunner.task.Task;
import de.arthurpicht.taskRunner.task.TaskDefinitionException;
import de.arthurpicht.utils.core.strings.Strings;
import de.arthurpicht.utils.struct.dag.*;

import java.util.*;
import java.util.stream.Collectors;

public class TaskRegistryBuilder {

    private final Map<String, Task> taskMap;

    public TaskRegistryBuilder() {
        this.taskMap = new LinkedHashMap<>();
    }

    public TaskRegistryBuilder withTask(Task task) {
        assertTaskNotPreexisting(task);
        assertDependenciesArePreexisting(task);
        this.taskMap.put(task.getName(), task);
        return this;
    }

    public TaskRegistry build() {
        Dag<String> taskDag = createTaskDag();
        validate(taskDag);
        Map<String, List<String>> targetToTaskListMap = createOrderedTaskLists(taskDag);
        return new TaskRegistry(this.taskMap, targetToTaskListMap);
    }

    private void assertTaskNotPreexisting(Task task) {
        if (this.taskMap.containsKey(task.getName()))
            throw new TaskDefinitionException("Task already registered [" + task.getName() + "].");
    }

    private void assertDependenciesArePreexisting(Task task) {
        String taskName = task.getName();
        for (String dependency : task.getDependencies()) {
            if (!this.taskMap.containsKey(dependency))
                throw new TaskDefinitionException("Dependency [" + dependency + "] of task [" + taskName + "] not specified.");
        }
    }

    private Map<String, List<String>> createOrderedTaskLists(Dag<String> taskDag) {
        Map<String, List<String>> taskListMap = new LinkedHashMap<>();
        Set<String> targets = getAllTargets();
        for (String target : targets) {
            List<String> taskOrder = forTargetGetTaskOrder(taskDag, target);
            taskListMap.put(target, taskOrder);
        }
        return taskListMap;
    }

    private List<String> forTargetGetTaskOrder(Dag<String> dag, String target) {
        TopologicalSort<String> sort = new TopologicalSort<>(dag, target);
        return sort.getTopologicalSortedNodesInReversedOrder();
//        //TODO debug
//        System.out.println("unreversed: " + Strings.listing(targetOrder, " "));
//        Collections.reverse(targetOrder);
//        return targetOrder;
    }

    private Set<String> getAllTargets() {
        return this.taskMap.entrySet().stream()
                .filter(e -> e.getValue().isTarget())
                .map(Map.Entry::getKey)
                .collect(Collectors.toUnmodifiableSet());
    }

    private void validate(Dag<String> dag) throws TaskDefinitionException {
        try {
            AcyclicValidator.validate(dag);
        } catch (DagCycleException e) {
            List<String> taskNameCycleList = e.getCycleNodeList();
            Collections.reverse(taskNameCycleList);
            throw new TaskDefinitionException("Cycle found in task definition: "
                    + Strings.listing(taskNameCycleList, ", ", "{", "}", "[", "]"));
        }
    }

    private Dag<String> createTaskDag() {
        DagBuilder<String> dagBuilder = new DagBuilder<>();
        for (String taskName : this.taskMap.keySet()) {
            dagBuilder.withNode(taskName);
            Task task = this.taskMap.get(taskName);
            for (String dependencyTask : task.getDependencies()) {
                dagBuilder.withEdge(taskName, dependencyTask);
            }
        }
        return dagBuilder.build();
    }

}
