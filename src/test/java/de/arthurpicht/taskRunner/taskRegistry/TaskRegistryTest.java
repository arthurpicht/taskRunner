package de.arthurpicht.taskRunner.taskRegistry;

import de.arthurpicht.taskRunner.task.Task;
import de.arthurpicht.taskRunner.task.TaskBuilder;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TaskRegistryTest {

    private TaskRegistry createTaskRegistry() {

        TaskRegistryBuilder taskRegistryBuilder = new TaskRegistryBuilder();

        taskRegistryBuilder.withTask(new TaskBuilder()
                .withName("D")
                .withDescription("task D")
                .execute(() -> System.out.println("This is task D."))
                .build());

        taskRegistryBuilder.withTask(new TaskBuilder()
                .withName("E")
                .withDescription("task E")
                .execute(() -> System.out.println("This is task E."))
                .build());

        taskRegistryBuilder.withTask(new TaskBuilder()
                .withName("C")
                .withDescription("task C")
                .withDependencies("D", "E")
                .execute(() -> System.out.println("This is task C."))
                .build());

        taskRegistryBuilder.withTask(new TaskBuilder()
                .withName("B")
                .withDescription("task B")
                .withDependencies("C")
                .execute(() -> System.out.println("This is task B."))
                .build());

        taskRegistryBuilder.withTask(new TaskBuilder()
                .withName("A")
                .withDescription("task A")
                .asTarget()
                .withDependencies("B")
                .execute(() -> System.out.println("This is task A."))
                .build());

        taskRegistryBuilder.withTask(new TaskBuilder()
                .withName("Y")
                .withDescription("task Y")
                .withDependencies("C")
                .execute(() -> System.out.println("This is task Y."))
                .build());

        taskRegistryBuilder.withTask(new TaskBuilder()
                .withName("X")
                .withDescription("task X")
                .asTarget()
                .withDependencies("Y")
                .execute(() -> System.out.println("This is task X."))
                .build());

        return taskRegistryBuilder.build();
    }

    @Test
    public void targets() {
        TaskRegistry taskRegistry = createTaskRegistry();
        Set<String> targets = taskRegistry.getTargets();

        assertEquals(2, targets.size());
        assertTrue(targets.contains("A"));
        assertTrue(targets.contains("X"));
    }

    @Test
    public void hasTargets() {
        TaskRegistry taskRegistry = createTaskRegistry();

        assertTrue(taskRegistry.hasTarget("A"));
        assertTrue(taskRegistry.hasTarget("X"));
        assertFalse(taskRegistry.hasTarget("B"));
        assertFalse(taskRegistry.hasTarget("D"));
    }

    @Test
    public void taskListA() {
        TaskRegistry taskRegistry = createTaskRegistry();
        List<String> taskList = taskRegistry.getTaskList("A");

        assertEquals(Arrays.asList("D", "E", "C", "B", "A"), taskList);
    }

    @Test
    public void taskListX() {
        TaskRegistry taskRegistry = createTaskRegistry();
        List<String> taskList = taskRegistry.getTaskList("X");

        assertEquals(Arrays.asList("D", "E", "C", "Y", "X"), taskList);
    }

    @Test
    public void getTask() {
        TaskRegistry taskRegistry = createTaskRegistry();
        Task task = taskRegistry.getTask("A");
        assertEquals("A", task.getName());
    }

}
