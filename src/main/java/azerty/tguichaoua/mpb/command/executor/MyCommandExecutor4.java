package azerty.tguichaoua.mpb.command.executor;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import azerty.tguichaoua.mpb.command.argument.CommandArgument;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface MyCommandExecutor4<A, B, C, D> {
	void execute(@NotNull CommandExecution execution, A a, B b, C c, D d) throws CommandException;

	default MyCommandExecutor asMyCommandExecutor(
			@NotNull final CommandArgument<A> a,
			@NotNull final CommandArgument<B> b,
			@NotNull final CommandArgument<C> c,
			@NotNull final CommandArgument<D> d
	) {
		return execution -> execute(execution, execution.get(a), execution.get(b), execution.get(c), execution.get(d));
	}
}