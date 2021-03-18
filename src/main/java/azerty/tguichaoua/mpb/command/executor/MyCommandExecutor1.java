package azerty.tguichaoua.mpb.command.executor;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import azerty.tguichaoua.mpb.command.argument.CommandArgument;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface MyCommandExecutor1<A> {
	void execute(@NotNull CommandExecution execution, A a) throws CommandException;

	default MyCommandExecutor asMyCommandExecutor(
			@NotNull final CommandArgument<A> a
	) {
		return execution -> execute(execution, execution.get(a));
	}
}