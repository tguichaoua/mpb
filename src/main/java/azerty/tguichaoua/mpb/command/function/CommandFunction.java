package azerty.tguichaoua.mpb.command.function;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface CommandFunction {
	void execute(@NotNull CommandExecution execution) throws CommandException;
}