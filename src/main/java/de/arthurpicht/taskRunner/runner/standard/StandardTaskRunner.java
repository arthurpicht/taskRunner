package de.arthurpicht.taskRunner.runner.standard;

import com.diogonunes.jcolor.Ansi;
import com.diogonunes.jcolor.Attribute;
import de.arthurpicht.taskRunner.TaskRunner;
import de.arthurpicht.taskRunner.runner.TaskRunnerBuilder;
import de.arthurpicht.taskRunner.taskRegistry.TaskRegistry;
import de.arthurpicht.utils.core.strings.Strings;

@SuppressWarnings("ALL")
public class StandardTaskRunner {

    public static TaskRunner create(TaskRegistry taskRegistry, boolean showStacktrace, int taskColumnWidth) {
        return new TaskRunnerBuilder()
                .withTaskRegistry(taskRegistry)
                .withOnPreExecuteCallback(task -> {
                    System.out.print(
                            Strings.fillUpAfter("[" + task.getName() + "] ... ", ' ', taskColumnWidth));
                })
                .withOnSuccessCallback(task -> {
                    System.out.println(Ansi.colorize("OK", Attribute.GREEN_TEXT()));
                })
                .withOnSkipCallback(task -> {
                    System.out.println(Ansi.colorize("SKIPPED", Attribute.GREEN_TEXT()));
                })
                .withOnUpToDateCallback(task -> {
                    System.out.println(Ansi.colorize("UP-TO-DATE", Attribute.GREEN_TEXT()));
                })
                .withOnFailByTaskPreconditionExceptionCallback((task, taskPreconditionException) -> {
                    System.out.println(Ansi.colorize("Precondition failed: ",
                            Attribute.RED_TEXT()) + taskPreconditionException.getMessage());
                    if (showStacktrace) taskPreconditionException.printStackTrace();
                })
                .withOnFailByTaskExecutionExceptionCallback((task, taskExecutionException) -> {
                    System.out.println(Ansi.colorize("Error: ",
                            Attribute.RED_TEXT()) + taskExecutionException.getMessage());
                    if (showStacktrace) taskExecutionException.printStackTrace();
                })
                .withOnFailByRuntimeExceptionCallback((task, runtimeException) -> {
                    System.out.println(Ansi.colorize("RuntimeError: ",
                            Attribute.RED_TEXT()) + runtimeException.getMessage());
                    if (showStacktrace) runtimeException.printStackTrace();
                })
                .build();
    }

}
