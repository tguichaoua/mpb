package azerty.tguichaoua.mpb.command.argument;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import org.jetbrains.annotations.NotNull;

public final class TransformCommandArgument<T, R> extends ProxyCommandArgument<T, R> {
	private final @NotNull Function<T, R> function;

	public TransformCommandArgument(
			@NotNull final CommandArgument<T> source,
			@NotNull final Function<T, R> function
	) {
		super(source);
		this.function = function;
	}

	@Override public R parse(@NotNull final CommandExecution execution) throws CommandException {
		return function.apply(execution, source.parse(execution));
	}

	@FunctionalInterface
	interface Function<T, R> {
		R apply(CommandExecution execution, T value) throws CommandException;
	}
}

