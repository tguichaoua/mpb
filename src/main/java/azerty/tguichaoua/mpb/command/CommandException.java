package azerty.tguichaoua.mpb.command;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandException extends Exception {
	private static final long serialVersionUID = -2474129753665605873L;

	@Getter private final Type type;
	@Getter private final CommandSender sender;
	@Getter private final String label;
	@Getter private final String[] args;
	@Getter private final int at;

	CommandException(final @NotNull Type type, final @NotNull CommandExecution execution) {
		super();
		this.type = type;
		this.sender = execution.getSender();
		this.label = execution.getLabel();
		this.args = execution.getArgs();
		this.at = execution.getCurrentArg();
	}

	public enum Type {
		INVALID_ARGUMENT,
		MISSING_ARGUMENT,
		PERMISSION,
		UNKNOWN_COMMAND
	}
}
