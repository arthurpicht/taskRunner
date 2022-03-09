package de.arthurpicht.taskRunner.integrationTest;

import de.arthurpicht.taskRunner.TaskRunner;
import de.arthurpicht.taskRunner.runner.TaskRunnerResult;
import de.arthurpicht.taskRunner.task.TaskBuilder;
import de.arthurpicht.taskRunner.taskRegistry.TaskRegistry;
import de.arthurpicht.taskRunner.taskRegistry.TaskRegistryBuilder;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Integration1Test {

    private static final List<String> executionSequenceCache = new ArrayList<>();

    private TaskRegistry createTaskRegistry() {

        TaskRegistryBuilder taskRegistryBuilder = new TaskRegistryBuilder();

        taskRegistryBuilder.withTask(new TaskBuilder()
                .name("D")
                .description("task D")
                .execute(() -> executionSequenceCache.add("This is task D."))
                .build());

        taskRegistryBuilder.withTask(new TaskBuilder()
                .name("E")
                .description("task E")
                .execute(() -> executionSequenceCache.add("This is task E."))
                .build());

        taskRegistryBuilder.withTask(new TaskBuilder()
                .name("C")
                .description("task C")
                .dependencies("D", "E")
                .execute(() -> executionSequenceCache.add("This is task C."))
                .build());

        taskRegistryBuilder.withTask(new TaskBuilder()
                .name("B")
                .description("task B")
                .dependencies("C")
                .execute(() -> executionSequenceCache.add("This is task B."))
                .build());

        taskRegistryBuilder.withTask(new TaskBuilder()
                .name("A")
                .description("task A")
                .isTarget()
                .dependencies("B")
                .execute(() -> executionSequenceCache.add("This is task A."))
                .build());

        taskRegistryBuilder.withTask(new TaskBuilder()
                .name("Y")
                .description("task Y")
                .dependencies("C")
                .execute(() -> executionSequenceCache.add("This is task Y."))
                .build());

        taskRegistryBuilder.withTask(new TaskBuilder()
                .name("X")
                .description("task X")
                .isTarget()
                .dependencies("Y")
                .execute(() -> executionSequenceCache.add("This is task X."))
                .build());

        return taskRegistryBuilder.build();
    }

    @Test
    void demoSimple() {
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

}
