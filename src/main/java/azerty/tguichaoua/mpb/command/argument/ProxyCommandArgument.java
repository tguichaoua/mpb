package azerty.tguichaoua.mpb.command.argument;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@RequiredArgsConstructor
public abstract class ProxyCommandArgument<T, R> implements CommandArgument<R> {
	protected final CommandArgument<T> source;

	@Override public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
		return source.complete(execution);
	}
}
