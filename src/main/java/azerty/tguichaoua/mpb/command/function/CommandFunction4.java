package azerty.tguichaoua.mpb.command.function;

import azerty.tguichaoua.mpb.command.CommandArgument;
import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface CommandFunction4<A, B, C, D> {
	void execute(@NotNull CommandExecution execution, A a, B b, C c, D d) throws CommandException;

	default CommandFunction asCommandFunction(
			@NotNull final CommandArgument<A> a,
			@NotNull final CommandArgument<B> b,
			@NotNull final CommandArgument<C> c,
			@NotNull final CommandArgument<D> d
	) {
		return execution -> execute(execution, execution.get(a), execution.get(b), execution.get(c), execution.get(d));
	}
}