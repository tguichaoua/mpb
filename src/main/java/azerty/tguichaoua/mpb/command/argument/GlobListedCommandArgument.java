package azerty.tguichaoua.mpb.command.argument;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import azerty.tguichaoua.mpb.util.RegexUtils;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public final class GlobListedCommandArgument<T> implements CommandArgument<List<T>> {

	private final @NotNull Supplier<Stream<String>> valueSupplier;
	private final @NotNull Function<String, T> parser;

	@Override
	public List<T> parse(@NotNull final CommandExecution execution) throws CommandException {
		final String pattern = RegexUtils.fromGlob(execution.nextString());
		return valueSupplier.get()
				.filter(s -> s.matches(pattern))
				.map(parser)
				.collect(Collectors.toList());
	}

	@Override
	public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
		val start = execution.nextString();
		final List<String> complete = valueSupplier.get().filter(s -> s.startsWith(start)).collect(Collectors.toList());
		complete.add("*"); // add the "all" pattern
		return complete;
	}
}
