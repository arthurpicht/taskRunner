package de.arthurpicht.taskRunner.integrationTest;

import de.arthurpicht.taskRunner.TaskRunner;
import de.arthurpicht.taskRunner.runner.TaskRunnerBuilder;
import de.arthurpicht.taskRunner.runner.TaskRunnerResult;
import de.arthurpicht.taskRunner.task.TaskBuilder;
import de.arthurpicht.taskRunner.taskRegistry.TaskRegistry;
import de.arthurpicht.taskRunner.taskRegistry.TaskRegistryBuilder;
import de.arthurpicht.utils.core.collection.Lists;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Integration1Test {

    private static List<String> executionSequenceCache = new ArrayList<>();

    private TaskRegistry createTaskRegistry() {

        TaskRegistryBuilder taskRegistryBuilder = new TaskRegistryBuilder();

        taskRegistryBuilder.withTask(new TaskBuilder()
                .withName("D")
                .withDescription("task D")
                .execute(() -> executionSequenceCache.add("This is task D."))
                .build());

        taskRegistryBuilder.withTask(new TaskBuilder()
                .withName("E")
                .withDescription("task E")
                .execute(() -> executionSequenceCache.add("This is task E."))
                .build());

        taskRegistryBuilder.withTask(new TaskBuilder()
                .withName("C")
                .withDescription("task C")
                .withDependencies("D", "E")
                .execute(() -> executionSequenceCache.add("This is task C."))
                .build());

        taskRegistryBuilder.withTask(new TaskBuilder()
                .withName("B")
                .withDescription("task B")
                .withDependencies("C")
                .execute(() -> executionSequenceCache.add("This is task B."))
                .build());

        taskRegistryBuilder.withTask(new TaskBuilder()
                .withName("A")
                .withDescription("task A")
                .asTarget()
                .withDependencies("B")
                .execute(() -> executionSequenceCache.add("This is task A."))
                .build());

        taskRegistryBuilder.withTask(new TaskBuilder()
                .withName("Y")
                .withDescription("task Y")
                .withDependencies("C")
                .execute(() -> executionSequenceCache.add("This is task Y."))
                .build());

        taskRegistryBuilder.withTask(new TaskBuilder()
                .withName("X")
                .withDescription("task X")
                .asTarget()
                .withDependencies("Y")
                .execute(() -> executionSequenceCache.add("This is task X."))
                .build());

        return taskRegistryBuilder.build();
    }

    @Test
    void demoSimple() {
        executionSequenceCache = new ArrayList<>();

        TaskRegistry taskRegistry = createTaskRegistry();
        TaskRunner taskRunner = new TaskRunner(taskRegistry);
        TaskRunnerResult taskRunnerResult = taskRunner.run("A");

        assertTrue(taskRunnerResult.isSuccess());
        assertEquals("A", taskRunnerResult.getTarget());
        assertEquals(taskRegistry.getTaskList("A"), taskRunnerResult.getTaskList());
        assertEquals(taskRegistry.getTaskList("A"), taskRunnerResult.getTaskListSuccess());
        assertThrows(IllegalStateException.class, taskRunnerResult::getTaskFailed);

        assertEquals("This is task D.", executionSequenceCache.get(0));
        assertEquals("This is task E.", executionSequenceCache.get(1));
        assertEquals("This is task C.", executionSequenceCache.get(2));
        assertEquals("This is task B.", executionSequenceCache.get(3));
        assertEquals("This is task A.", executionSequenceCache.get(4));
    }

    @Test
    void demoCallbackSequence() {
        executionSequenceCache = new ArrayList<>();

        TaskRunner taskRunner = new TaskRunnerBuilder()
                .withTaskRegistry(createTaskRegistry())
                .withOnPreExecuteCallback(task -> executionSequenceCache.add("pre: " + task.getName()))
                .withOnSuccessCallback(task -> executionSequenceCache.add("success: " + task.getName()))
                .build();
        TaskRunnerResult taskRunnerResult = taskRunner.run("A");

        assertTrue(taskRunnerResult.isSuccess());
        assertEquals("A", taskRunnerResult.getTarget());

        List<String> expectedSequence = Lists.newArrayList(
                "pre: D",
                "This is task D.",
                "success: D",
                "pre: E",
                "This is task E.",
                "success: E",
                "pre: C",
                "This is task C.",
                "success: C",
                "pre: B",
                "This is task B.",
                "success: B",
                "pre: A",
                "This is task A.",
                "success: A"
        );

        assertEquals(expectedSequence, executionSequenceCache);
    }

}
