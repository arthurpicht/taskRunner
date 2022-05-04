package de.arthurpicht.taskRunner.integrationTest;

import de.arthurpicht.taskRunner.TaskRunner;
import de.arthurpicht.taskRunner.runner.TaskRunnerResult;
import de.arthurpicht.taskRunner.task.TaskBuilder;
import de.arthurpicht.taskRunner.taskRegistry.TaskRegistry;
import de.arthurpicht.taskRunner.taskRegistry.TaskRegistryBuilder;
import de.arthurpicht.utils.core.strings.Strings;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FailRuntimeTest {

    private static final List<String> executionSequenceCache = new ArrayList<>();

    private TaskRegistry createTaskRegistry() {

        TaskRegistryBuilder taskRegistryBuilder = new TaskRegistryBuilder();

        taskRegistryBuilder.withTask(new TaskBuilder()
                .withName("C")
                .withDescription("task C")
                .execute(() -> executionSequenceCache.add("This is task C."))
                .build());

        taskRegistryBuilder.withTask(new TaskBuilder()
                .withName("B")
                .withDescription("task B")
                .withDependencies("C")
                .execute(() -> {throw new RuntimeException("Bye.");})
                .build());

        taskRegistryBuilder.withTask(new TaskBuilder()
                .withName("A")
                .withDescription("task A")
                .asTarget()
                .withDependencies("B")
                .execute(() -> executionSequenceCache.add("This is task A."))
                .build());

        return taskRegistryBuilder.build();
    }

    @Test
    void demoFailTaskExecutionException() {
        TaskRegistry taskRegistry = createTaskRegistry();
        TaskRunner taskRunner = new TaskRunner(taskRegistry);
        TaskRunnerResult taskRunnerResult = taskRunner.run("A");

        assertFalse(taskRunnerResult.isSuccess());
        assertEquals("A", taskRunnerResult.getTarget());
        System.out.println("TaskList: " + Strings.listing(taskRegistry.getTaskList("A"), ", "));

        assertEquals(taskRegistry.getTaskList("A"), taskRunnerResult.getTaskList());

        assertEquals(1, taskRunnerResult.getTaskListSuccess().size());
        assertEquals("C", taskRunnerResult.getTaskListSuccess().get(0));
        assertEquals("B", taskRunnerResult.getTaskFailed());

        assertNotNull(taskRunnerResult.getRuntimeException());
        assertEquals("Bye.", taskRunnerResult.getRuntimeException().getMessage());

        assertNull(taskRunnerResult.getTaskExecutionException());

        assertEquals(1, executionSequenceCache.size());
        assertEquals("This is task C.", executionSequenceCache.get(0));
    }

}
