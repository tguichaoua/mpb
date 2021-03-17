package azerty.tguichaoua.mpb.command.argument;

import azerty.tguichaoua.mpb.command.CommandException;
import azerty.tguichaoua.mpb.command.CommandExecution;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public class LocationCommandArgument implements CommandArgument<Location> {

	public static final LocationCommandArgument SINGLETON = new LocationCommandArgument();

	private LocationCommandArgument() {
	}

	@Override
	public Location parse(final @NotNull CommandExecution execution) throws CommandException {
		final Location location = execution.getSenderLocation();
		return new Location(
				location.getWorld(),
				getCoordinate(execution, location.getX()),
				getCoordinate(execution, location.getY()),
				getCoordinate(execution, location.getZ())
		);
	}

	private double getCoordinate(final CommandExecution execution, final double relative) throws CommandException {
		double coord = 0;
		String arg = execution.nextArgument();
		if (arg.startsWith("~")) {
			if (arg.length() == 1) {
				return relative;
			}
			arg = arg.substring(1);
			coord += relative;
		}

		try {
			return coord + Double.parseDouble(arg);
		} catch (final NumberFormatException exception) {
			throw execution.invalidArgument(DoubleCommandArgument.INVALID_ARGUMENT);
		}
	}

	@Override
	public @NotNull Collection<String> complete(@NotNull final CommandExecution execution) throws CommandException {
		switch (execution.remains()) {
			case 1:
				return Collections.singleton(complete(execution.nextArgument()) + " ~ ~");
			case 2:
				execution.next();
				return Collections.singleton(complete(execution.nextArgument()) + " ~");
			default:
				execution.next(2);
				return Collections.singleton(complete(execution.nextArgument()));
		}
	}

	private String complete(final String s) {
		return StringUtils.isEmpty(s) ? "~" : s;
	}
}
