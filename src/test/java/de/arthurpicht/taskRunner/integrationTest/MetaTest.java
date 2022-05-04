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

public class MetaTest {

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
                .execute(() -> executionSequenceCache.add("This is task B."))
                .build());

        taskRegistryBuilder.withTask(new TaskBuilder()
                .withName("A")
                .withDescription("task A")
                .withDependencies("B")
                .execute(() -> executionSequenceCache.add("This is task A."))
                .build());

        taskRegistryBuilder.withTask(new TaskBuilder()
                .withName("THE-TASK")
                .withDescription("task")
                .asTarget()
                .asMetaTask()
                .withDependencies("A")
                .build());

        return taskRegistryBuilder.build();
    }

    @Test
    void demoMetaTask() {
        TaskRegistry taskRegistry = createTaskRegistry();
        TaskRunner taskRunner = new TaskRunner(taskRegistry);
        TaskRunnerResult taskRunnerResult = taskRunner.run("THE-TASK");

        assertTrue(taskRunnerResult.isSuccess());
        assertEquals("THE-TASK", taskRunnerResult.getTarget());
        System.out.println("TaskList: " + Strings.listing(taskRegistry.getTaskList("THE-TASK"), ", "));

        assertEquals(taskRegistry.getTaskList("THE-TASK"), taskRunnerResult.getTaskList());
        assertEquals(taskRegistry.getTaskList("THE-TASK"), taskRunnerResult.getTaskListSuccess());
        assertThrows(IllegalStateException.class, taskRunnerResult::getTaskFailed);
        assertEquals("This is task C.", executionSequenceCache.get(0));
        assertEquals("This is task B.", executionSequenceCache.get(1));
        assertEquals("This is task A.", executionSequenceCache.get(2));
    }

}
