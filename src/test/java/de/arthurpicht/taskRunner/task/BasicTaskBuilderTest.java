package de.arthurpicht.taskRunner.task;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.*;

class BasicTaskBuilderTest {

    @Test
    void simple() {

        Task task = new TaskBuilder()
                .withName("test")
                .withDescription("description for task")
                .asTarget()
                .execute(() -> System.out.println("Hello world!"))
                .withDependencies("task1", "task2")
                .build();

        assertEquals("test", task.getName());
        assertEquals("description for task", task.getDescription());
        assertFalse(task.hasInputChangedFunction());
        assertFalse(task.hasOutputExistsFunction());
        assertEquals(new LinkedHashSet<>(Arrays.asList("task1", "task2")), task.getDependencies());
        assertTrue(task.isTarget());
    }

}