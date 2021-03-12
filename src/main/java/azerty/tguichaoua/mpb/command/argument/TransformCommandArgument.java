package azerty.tguichaoua.mpb.command.argument;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@RequiredArgsConstructor
public final class TransformCommandArgument<T, R> implements CommandArgument<R> {
	private final CommandArgument<T> source;
	private final Function<T, R> function;

	@Override public R parse(@NotNull final CommandExecution execution) throws CommandException {
		return function.apply(execution, source.parse(execution));
	}

	@Override public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
		return source.complete(execution);
	}

	@Override public int multiplicity() {
		return source.multiplicity();
	}

	@FunctionalInterface
	interface Function<T, R> {
		R apply(CommandExecution execution, T value) throws CommandException;
	}
}

