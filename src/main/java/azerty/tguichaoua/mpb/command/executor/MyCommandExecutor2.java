package azerty.tguichaoua.mpb.command.executor;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import azerty.tguichaoua.mpb.command.argument.CommandArgument;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface MyCommandExecutor2<A, B> {
	void execute(@NotNull CommandExecution execution, A a, B b) throws CommandException;

	default MyCommandExecutor asMyCommandExecutor(
			@NotNull final CommandArgument<A> a,
			@NotNull final CommandArgument<B> b
	) {
		return execution -> execute(execution, execution.get(a), execution.get(b));
	}
}