package azerty.tguichaoua.mpb.command.argument;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@RequiredArgsConstructor
public final class TransformCommandArgument<T, R> implements CommandArgument<R> {
	private final @NotNull CommandArgument<T> source;
	private final @NotNull Transform<T, R> transform;

	@Override
	public R parse(@NotNull final CommandExecution execution) throws CommandException {
		return transform.apply(execution, source);
	}

	@Override
	public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
		return source.complete(execution);
	}

	@FunctionalInterface
	interface Transform<T, R> {
		R apply(CommandExecution execution, CommandArgument<T> argument) throws CommandException;
	}
}
