package azerty.tguichaoua.mpb.command;

import azerty.tguichaoua.mpb.command.argument.CommandArgument;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;


public final class CommandExecution {
	@Getter private final @NotNull CommandSender sender;
	@Getter private @NotNull String label;
	@Getter(AccessLevel.PACKAGE) private final @NotNull String[] args;

	@Getter(AccessLevel.PACKAGE) private int currentArg = 0;

	CommandExecution(
			@NotNull final CommandSender sender,
			@NotNull final String label,
			@NotNull final String[] args
	) {
		this.sender = sender;
		this.label = label;
		this.args = args;
	}

	void pushLabel(@NotNull final String label) {
		this.label += " " + label;
	}

	/**
	 * Returns the location of the {@link CommandExecution#sender}.
	 * If it's a {@link Block} returns the center of the block.
	 * If it's the console returns the {@link World#getSpawnLocation()} of the default world.
	 *
	 * @return the location of the sender
	 */
	public @NotNull Location getSenderLocation() {
		if (sender instanceof Entity) {
			return ((Entity) sender).getLocation();
		} else if (sender instanceof BlockCommandSender) {
			return ((BlockCommandSender) sender).getBlock().getLocation().add(0.5, 0.5, 0.5);
		} else {
			return sender.getServer().getWorlds().get(0).getSpawnLocation();
		}
	}

	/**
	 * Throws if the sender has not the required permission.
	 *
	 * @param permission the required permission
	 * @throws CommandException if the sender has not the required permission
	 */
	public void checkPermission(final @NotNull String permission) throws CommandException {
		if (!sender.hasPermission(permission)) throw new CommandException(CommandException.Type.PERMISSION, this);
	}

	/**
	 * Throws if the sender has not the required permission.
	 *
	 * @param permission the required permission
	 * @throws CommandException if the sender has not the required permission
	 */
	public void checkPermission(final @NotNull Permission permission) throws CommandException {
		if (!sender.hasPermission(permission)) throw new CommandException(CommandException.Type.PERMISSION, this);
	}

	public int remains() {
		return args.length - currentArg;
	}

	public String current() throws CommandException {
		if (currentArg == args.length) throw new CommandException(CommandException.Type.MISSING_ARGUMENT, this);
		return args[currentArg];
	}

	public void next() throws CommandException {
		if (currentArg == args.length) throw new CommandException(CommandException.Type.MISSING_ARGUMENT, this);
		currentArg++;
	}

	public void next(final int count) throws CommandException {
		currentArg += count;
		if (currentArg >= args.length) {
			currentArg = args.length;
			throw new CommandException(CommandException.Type.MISSING_ARGUMENT, this);
		}
	}

	public <T> T get(final CommandArgument<T> parser) throws CommandException {
		return parser.parse(this);
	}

	public String nextString() throws CommandException {
		if (currentArg == args.length) throw new CommandException(CommandException.Type.MISSING_ARGUMENT, this);
		return args[currentArg++];
	}

	public int nextInteger() throws CommandException {
		try {
			return Integer.parseInt(nextString());
		} catch (final NumberFormatException e) {
			throw invalidArgument();
		}
	}

	public double nextDouble() throws CommandException {
		try {
			return Double.parseDouble(nextString());
		} catch (final NumberFormatException e) {
			throw invalidArgument();
		}
	}

	public CommandException invalidArgument() {
		return new CommandException(CommandException.Type.INVALID_ARGUMENT, this);
	}
}
