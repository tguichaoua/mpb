package azerty.tguichaoua.mpb.command.function;

import azerty.tguichaoua.mpb.command.CommandArgument;
import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface CommandFunction5<A, B, C, D, E> {
	void execute(@NotNull CommandExecution execution, A a, B b, C c, D d, E e) throws CommandException;

	default CommandFunction asCommandFunction(
			@NotNull final CommandArgument<A> a,
			@NotNull final CommandArgument<B> b,
			@NotNull final CommandArgument<C> c,
			@NotNull final CommandArgument<D> d,
			@NotNull final CommandArgument<E> e
	) {
		return execution -> execute(execution, execution.get(a), execution.get(b), execution.get(c), execution.get(d), execution.get(e));
	}
}