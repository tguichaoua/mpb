package azerty.tguichaoua.mpb.command;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public final class ListedCommandArgument<T> implements CommandArgument<T> {

	private final @NotNull Supplier<Stream<String>> valueSupplier;
	private final @NotNull Function<String, T> parser;

	@Override
	public T parse(@NotNull final CommandExecution execution) throws CommandException {
		final String s = execution.nextString();
		if (valueSupplier.get().anyMatch(str -> str.equals(s))) return parser.apply(s);
		else throw execution.invalidArgument();
	}

	@Override
	public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
		val start = execution.nextString();
		return valueSupplier.get().filter(s -> s.startsWith(start)).collect(Collectors.toList());
	}

	public final GlobListedCommandArgument<T> asGlob() {
		return new GlobListedCommandArgument<>(valueSupplier, parser);
	}
}
