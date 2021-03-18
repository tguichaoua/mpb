package azerty.tguichaoua.mpb.command.argument;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import org.jetbrains.annotations.NotNull;

public class IntegerCommandArgument implements CommandArgument<@NotNull Integer> {
	public static final String INVALID_ARGUMENT = "arg.integer.invalid";
	public static final IntegerCommandArgument SINGLETON = new IntegerCommandArgument();

	private IntegerCommandArgument() {
	}

	@Override
	public @NotNull Integer parse(@NotNull final CommandExecution execution) throws CommandException {
		try {
			return Integer.parseInt(execution.nextArgument());
		} catch (final NumberFormatException e) {
			throw execution.invalidArgument(INVALID_ARGUMENT);
		}
	}
}
