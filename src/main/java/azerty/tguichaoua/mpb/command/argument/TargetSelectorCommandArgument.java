package azerty.tguichaoua.mpb.command.argument;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import azerty.tguichaoua.mpb.model.TargetSelector;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public class TargetSelectorCommandArgument implements CommandArgument<TargetSelector> {

	public static final TargetSelectorCommandArgument SINGLETON = new TargetSelectorCommandArgument();

	private TargetSelectorCommandArgument() {
	}

	@Override public TargetSelector parse(@NotNull final CommandExecution execution) throws CommandException {
		final StringBuilder sb = new StringBuilder(execution.nextString());
		if (sb.indexOf("[") != -1 && sb.indexOf("]") == -1) {
			while (true) {
				final String s = execution.nextString();
				sb.append(s);
				if (s.contains("]")) {
					break;
				}
			}
		}
		return TargetSelector.parse(sb.toString());
	}

	@Override public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) {
		return Collections.emptyList(); // TODO
	}
}
