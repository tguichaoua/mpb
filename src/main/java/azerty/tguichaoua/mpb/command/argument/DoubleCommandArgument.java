package azerty.tguichaoua.mpb.command.argument;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import org.jetbrains.annotations.NotNull;

public class DoubleCommandArgument implements CommandArgument<@NotNull Double> {
	public static final String INVALID_ARGUMENT = "arg.double.invalid";
	public static final DoubleCommandArgument SINGLETON = new DoubleCommandArgument();

	private DoubleCommandArgument() {
	}

	@Override
	public @NotNull Double parse(@NotNull final CommandExecution execution) throws CommandException {
		try {
			return Double.parseDouble(execution.nextArgument());
		} catch (final NumberFormatException e) {
			throw execution.invalidArgument(INVALID_ARGUMENT);
		}
	}
}
