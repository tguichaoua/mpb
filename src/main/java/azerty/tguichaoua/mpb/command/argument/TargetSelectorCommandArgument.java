package azerty.tguichaoua.mpb.command.argument;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import azerty.tguichaoua.mpb.model.TargetSelector;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public class TargetSelectorCommandArgument implements CommandArgument<TargetSelector> {

	public static final String INVALID_FORMAT = "arg.target_selector.format";
	public static final String INVALID_SELECTOR = "arg.target_selector.selector";
	public static final String INVALID_ARGUMENT = "arg.target_selector.argument";
	public static final String INVALID_ARGUMENT_VALUE = "arg.target_selector.argument_value";

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
		try {
			return TargetSelector.parse(sb.toString());
		} catch (final TargetSelector.InvalidFormatTargetSelectorParseException e) {
			throw execution.invalidArgument(INVALID_FORMAT);
		} catch (final TargetSelector.InvalidSelectorTargetSelectorParseException e) {
			throw execution.invalidArgument(INVALID_SELECTOR, e.getSelector());
		} catch (final TargetSelector.InvalidArgumentTargetSelectorParseException e) {
			throw execution.invalidArgument(INVALID_ARGUMENT, e.getArgument());
		} catch (final TargetSelector.InvalidArgumentValueTargetSelectorParseException e) {
			throw execution.invalidArgument(INVALID_ARGUMENT_VALUE, e.getValue());
		}
	}

	@Override public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) {
		return Collections.emptyList(); // TODO
	}
}
