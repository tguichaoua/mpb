package azerty.tguichaoua.mpb.command;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public final class ListedCommandArgument<T> implements CommandArgument<T> {

	private final @NotNull Collection<String> values;
	private final @NotNull Function<String, T> get;

	@Override
	public T parse(@NotNull final CommandExecution execution) throws CommandException {
		final String s = execution.nextString();
		if (values.contains(s)) return get.apply(s);
		else throw execution.invalidArgument();
	}

	@Override
	public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
		val start = execution.nextString();
		if (StringUtils.isEmpty(start)) {
			return values;
		} else {
			return values.stream().filter(s -> s.startsWith(start)).collect(Collectors.toList());
		}
	}

	public final GlobListedCommandArgument<T> asGlob() {
		return new GlobListedCommandArgument<>(values, get);
	}
}
