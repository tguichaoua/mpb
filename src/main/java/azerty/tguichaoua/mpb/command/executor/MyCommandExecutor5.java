package azerty.tguichaoua.mpb.command.executor;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import azerty.tguichaoua.mpb.command.argument.CommandArgument;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface MyCommandExecutor5<A, B, C, D, E> {
	void execute(@NotNull CommandExecution execution, A a, B b, C c, D d, E e) throws CommandException;

	default MyCommandExecutor asMyCommandExecutor(
			@NotNull final CommandArgument<A> a,
			@NotNull final CommandArgument<B> b,
			@NotNull final CommandArgument<C> c,
			@NotNull final CommandArgument<D> d,
			@NotNull final CommandArgument<E> e
	) {
		return execution -> execute(execution, execution.get(a), execution.get(b), execution.get(c), execution.get(d), execution.get(e));
	}
}