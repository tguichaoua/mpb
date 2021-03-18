package azerty.tguichaoua.mpb.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommandException extends Exception {
	private static final long serialVersionUID = -2474129753665605873L;

	@Getter private final Type type;
	@Getter private final String label;
	@Getter private final String argument;
	@Getter private final @Nullable String reasonKey;
	@Getter private final String[] formatArgs;

	CommandException(
			final @NotNull Type type,
			final @NotNull CommandExecution execution,
			@Nullable final String reasonKey,
			final String... formatArgs
	) {
		super();
		this.type = type;
		this.label = execution.getLabel();
		this.argument = execution.getCurrentParsedArgument();
		this.reasonKey = reasonKey;
		this.formatArgs = formatArgs;
	}

	CommandException(final @NotNull Type type, final @NotNull CommandExecution execution) {
		this(type, execution, null);
	}

	@RequiredArgsConstructor
	public enum Type {
		INVALID_ARGUMENT("type.invalid_argument"),
		MISSING_ARGUMENT("type.missing_argument"),
		PERMISSION("type.permission"),
		UNKNOWN_COMMAND("type.unknown_command");

		public final @NotNull String key;
	}
}
