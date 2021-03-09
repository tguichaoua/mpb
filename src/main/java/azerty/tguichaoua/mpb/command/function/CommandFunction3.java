package azerty.tguichaoua.mpb.command.function;

import azerty.tguichaoua.mpb.command.CommandArgument;
import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface CommandFunction3<A, B, C> {
	void execute(@NotNull CommandExecution execution, A a, B b, C c) throws CommandException;

	default CommandFunction asCommandFunction(
			@NotNull final CommandArgument<A> a,
			@NotNull final CommandArgument<B> b,
			@NotNull final CommandArgument<C> c
	) {
		return execution -> execute(execution, execution.get(a), execution.get(b), execution.get(c));
	}
}