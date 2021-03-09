package azerty.tguichaoua.mpb.command.function;

import azerty.tguichaoua.mpb.command.CommandArgument;
import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface CommandFunction1<A> {
	void execute(@NotNull CommandExecution execution, A a) throws CommandException;

	default CommandFunction asCommandFunction(
			@NotNull final CommandArgument<A> a
	) {
		return execution -> execute(execution, execution.get(a));
	}
}