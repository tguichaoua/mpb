package azerty.tguichaoua.mpb.command.argument;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import azerty.tguichaoua.mpb.model.TargetSelector;
import org.apache.commons.lang.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TargetSelectorCommandArgument implements CommandArgument<TargetSelector> {

	public static final String INVALID_FORMAT = "arg.target_selector.format";
	public static final String INVALID_SELECTOR = "arg.target_selector.selector";
	public static final String INVALID_ARGUMENT = "arg.target_selector.argument";
	public static final String INVALID_ARGUMENT_VALUE = "arg.target_selector.argument_value";

	public static final TargetSelectorCommandArgument SINGLETON = new TargetSelectorCommandArgument();

	private TargetSelectorCommandArgument() {
	}

	@Override public TargetSelector parse(@NotNull final CommandExecution execution) throws CommandException {
		final TargetSelector.Parser parser = new TargetSelector.Parser();

		try {
			parser.consume(execution.nextArgument());
		} catch (final TargetSelector.InvalidFormatTargetSelectorParseException e) {
			throw execution.invalidArgument(INVALID_FORMAT);
		} catch (final TargetSelector.InvalidSelectorTargetSelectorParseException e) {
			throw execution.invalidArgument(INVALID_SELECTOR, e.getSelector());
		} catch (final TargetSelector.InvalidArgumentTargetSelectorParseException e) {
			throw execution.invalidArgument(INVALID_ARGUMENT, e.getArgument());
		} catch (final TargetSelector.InvalidArgumentValueTargetSelectorParseException e) {
			throw execution.invalidArgument(INVALID_ARGUMENT_VALUE, e.getValue());
		}

		if (parser.getState().equals(TargetSelector.Parser.State.BEFORE_ARGUMENTS)) {
			return parser.get();
		}

		while (!parser.getState().equals(TargetSelector.Parser.State.END)) {
			if (execution.remains() == 0) throw execution.invalidArgument(INVALID_FORMAT);
			try {
				parser.consume(" " + execution.nextArgument());
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

		return parser.get();
	}

	@Override public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
		final TargetSelector.Parser parser = new TargetSelector.Parser();

		final String str = execution.nextArgument();

		try {
			parser.consume(str);
		} catch (final IllegalArgumentException e) {
			return Collections.emptyList();
		}

		switch (parser.getState()) {
			case AT:
			case SELECTOR:
				return TargetSelector.Selector.names;
			case BEFORE_ARGUMENTS:
				return Collections.singletonList(str + "[");
			case END:
				return Collections.emptyList();
			case ARGUMENT_NAME:
			case ARGUMENT_VALUE:
				String last = str;

				while (execution.remains() != 0 && !parser.getState().equals(TargetSelector.Parser.State.END)) {
					last = execution.nextArgument();
					try {
						parser.consume(" " + last);
					} catch (final IllegalArgumentException e) {
						return Collections.emptyList();
					}
				}

				switch (parser.getState()) {
					case ARGUMENT_NAME:
						int i = last.lastIndexOf(',');
						if (i == -1) i = last.indexOf('[');
						final String start = last.substring(0, i + 1);
						final String finalStart = StringUtils.isEmpty(start) ? "" : start + " ";
						final String name = parser.getCurrentValue();
						return Arrays.stream(TargetSelector.Property.values())
								.filter(p -> !parser.isSet(p))
								.map(Enum::toString)
								.filter(s -> s.startsWith(name))
								.map(s -> finalStart + s + "=")
								.collect(Collectors.toList());
					case ARGUMENT_VALUE:
						switch (parser.getProperty()) {
							case gamemode:
								return completeEnumProperty(last, parser.getCurrentValue(), GameMode.class);
							case type:
								return completeEnumProperty(last, parser.getCurrentValue(), EntityType.class);
							default:
								return Collections.emptyList();
						}
					case END:
						return Collections.emptyList();
				}
		}
		return Collections.emptyList();
	}

	private Collection<String> completeEnumProperty(final String last, final String currentValue, final Class<? extends Enum<?>> clazz) {
		final int i = Math.max(last.lastIndexOf('!'), last.lastIndexOf('='));
		final String start = last.substring(0, i + 1);
		final String name = currentValue.startsWith("!") ? currentValue.substring(1).trim() : currentValue;
		final List<String> complete = Arrays.stream(clazz.getEnumConstants())
				.map(e -> e.toString().toLowerCase())
				.filter(s -> s.startsWith(name))
				.map(s -> start + s)
				.collect(Collectors.toList());
		if (
				(complete.size() == 1 && complete.get(0).equals(name)) ||
						(last.equals("") && !name.equals(""))
		) {
			return Collections.emptyList();
		}
		return complete;
	}
}
