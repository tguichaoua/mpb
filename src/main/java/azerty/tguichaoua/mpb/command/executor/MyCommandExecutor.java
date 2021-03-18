package azerty.tguichaoua.mpb.command.executor;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface MyCommandExecutor {
	void execute(@NotNull CommandExecution execution) throws CommandException;
}