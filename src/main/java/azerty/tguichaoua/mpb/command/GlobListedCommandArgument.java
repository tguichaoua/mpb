package azerty.tguichaoua.mpb.command;

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

	private final @NotNull Supplier<Stream<String>> values;
	private final @NotNull Function<String, T> get;

	@Override
	public List<T> parse(@NotNull final CommandExecution execution) throws CommandException {
		final String pattern = RegexUtils.fromGlob(execution.nextString());
		return values.get()
				.filter(s -> s.matches(pattern))
				.map(get)
				.collect(Collectors.toList());
	}

	@Override
	public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
		val start = execution.nextString();
		final List<String> complete = values.get().filter(s -> s.startsWith(start)).collect(Collectors.toList());
		complete.add("*"); // add the "all" pattern
		return complete;
	}
}
