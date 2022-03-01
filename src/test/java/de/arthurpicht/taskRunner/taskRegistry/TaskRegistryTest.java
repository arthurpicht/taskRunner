package de.arthurpicht.taskRunner.taskRegistry;

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
                .name("D")
                .description("task D")
                .execute(() -> System.out.println("This is task D."))
                .build());

        taskRegistryBuilder.withTask(new TaskBuilder()
                .name("E")
                .description("task E")
                .execute(() -> System.out.println("This is task E."))
                .build());

        taskRegistryBuilder.withTask(new TaskBuilder()
                .name("C")
                .description("task C")
                .dependencies("D", "E")
                .execute(() -> System.out.println("This is task C."))
                .build());

        taskRegistryBuilder.withTask(new TaskBuilder()
                .name("B")
                .description("task B")
                .dependencies("C")
                .execute(() -> System.out.println("This is task B."))
                .build());

        taskRegistryBuilder.withTask(new TaskBuilder()
                .name("A")
                .description("task A")
                .isTarget()
                .dependencies("B")
                .execute(() -> System.out.println("This is task A."))
                .build());

        taskRegistryBuilder.withTask(new TaskBuilder()
                .name("Y")
                .description("task Y")
                .dependencies("C")
                .execute(() -> System.out.println("This is task Y."))
                .build());

        taskRegistryBuilder.withTask(new TaskBuilder()
                .name("X")
                .description("task X")
                .isTarget()
                .dependencies("Y")
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

        assertEquals(Arrays.asList("A", "B", "C", "E", "D"), taskList);
    }

    @Test
    public void taskListX() {
        TaskRegistry taskRegistry = createTaskRegistry();
        List<String> taskList = taskRegistry.getTaskList("X");

        assertEquals(Arrays.asList("X", "Y", "C", "E", "D"), taskList);
    }



}
