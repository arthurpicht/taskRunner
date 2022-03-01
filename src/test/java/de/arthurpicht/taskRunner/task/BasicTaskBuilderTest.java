package de.arthurpicht.taskRunner.task;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BasicTaskBuilderTest {

    @Test
    void simple() {

        BasicTask task = new TaskBuilder()
                .name("test")
                .description("description for task")
                .isTarget()
                .execute(() -> System.out.println("Hello world!"))
                .dependencies("task1", "task2")
                .build();

        assertEquals("test", task.getName());
        assertEquals("description for task", task.getDescription());
        assertEquals(new LinkedHashSet<>(Arrays.asList("task1", "task2")), task.getDependencies());
        assertTrue(task.isTarget());
    }

}