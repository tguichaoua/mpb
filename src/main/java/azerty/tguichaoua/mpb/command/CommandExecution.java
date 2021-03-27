package azerty.tguichaoua.mpb.command;

import azerty.tguichaoua.mpb.command.argument.CommandArgument;
import azerty.tguichaoua.mpb.util.CommandUtils;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Stack;
import java.util.stream.Collectors;


public final class CommandExecution {

	@Getter private final @NotNull CommandSender sender;
	@Getter private @NotNull String label;
	private final @NotNull String[] args;

	private @Nullable Integer currentParsedArgument = null;
	@Getter(AccessLevel.PACKAGE) private int currentArg = 0;
	private @Nullable Stack<@NotNull Integer> checkpoints = null;

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

	int getCurrentParsedArgumentIndex() {
		return currentParsedArgument != null ? currentParsedArgument : currentArg;
	}

	@NotNull String getCurrentParsedArgument() {
		if (currentParsedArgument == null)
			return args[currentArg];
		else
			return Arrays.stream(args).skip(currentParsedArgument).limit(currentArg - currentParsedArgument + 1).collect(Collectors.joining(" "));
	}

	/**
	 * Returns the location of the {@link CommandExecution#sender}.
	 * If it's a {@link Block} returns the center of the block.
	 * If it's the console returns the {@link World#getSpawnLocation()} of the default world.
	 *
	 * @return the location of the sender
	 */
	public @NotNull Location getSenderLocation() {
		return CommandUtils.getCommandSenderLocation(sender);
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

	/**
	 * Returns the number of remaining arguments.
	 *
	 * @return the number of remaining arguments
	 */
	public int remains() {
		return args.length - currentArg;
	}

	/**
	 * Returns the current argument.
	 *
	 * @return the current argument
	 * @throws CommandException if {@link CommandExecution#remains()} is 0
	 */
	public String current() throws CommandException {
		if (currentArg == args.length) throw new CommandException(CommandException.Type.MISSING_ARGUMENT, this);
		return args[currentArg];
	}

	/**
	 * Returns the current argument and move next.
	 *
	 * @return the current argument
	 * @throws CommandException if {@link CommandExecution#remains()} is 0
	 */
	public String nextArgument() throws CommandException {
		if (currentArg == args.length) throw new CommandException(CommandException.Type.MISSING_ARGUMENT, this);
		return args[currentArg++];
	}

	/**
	 * Move to the next argument.
	 *
	 * @throws CommandException if {@link CommandExecution#remains()} is 0
	 */
	public void next() throws CommandException {
		if (currentArg == args.length) throw new CommandException(CommandException.Type.MISSING_ARGUMENT, this);
		currentArg++;
	}

	/**
	 * Move to the next arguments.
	 * Equivalent to call {@link CommandExecution#next()} multiple time.
	 *
	 * @param count how many argument to pass.
	 * @throws CommandException if {@link CommandExecution#remains()} is lower than {@code count}
	 */
	public void next(final int count) throws CommandException {
		currentArg += count;
		if (currentArg >= args.length) {
			currentArg = args.length;
			throw new CommandException(CommandException.Type.MISSING_ARGUMENT, this);
		}
	}

	/**
	 * Properly calls {@link CommandArgument#parse(CommandExecution)} and returns the result.
	 *
	 * @param argument the argument to parse
	 * @param <T>      type of the argument
	 * @return the parsed value
	 * @throws CommandException if the parsing fail
	 */
	public <T> T get(final CommandArgument<T> argument) throws CommandException {
		final Stack<@NotNull Integer> prevCheckpoints = this.checkpoints;
		final Integer prevCurrentParsedArgument = this.currentParsedArgument;

		this.checkpoints = new Stack<>();
		this.currentParsedArgument = null;

		try {
			return argument.parse(this);
		} finally {
			this.checkpoints = prevCheckpoints;
			this.currentParsedArgument = prevCurrentParsedArgument;
		}
	}

	/**
	 * Properly calls {@link CommandArgument#complete(CommandExecution)} and returns the result.
	 *
	 * @param argument the argument from which to get the completion list
	 * @return the completion list
	 * @throws CommandException if the parsing fail
	 */
	public @NotNull Collection<String> complete(@NotNull final CommandArgument<?> argument) throws CommandException {
		final Stack<@NotNull Integer> prevCheckpoints = this.checkpoints;
		final Integer prevCurrentParsedArgument = this.currentParsedArgument;

		this.checkpoints = new Stack<>();
		this.currentParsedArgument = null;

		try {
			return argument.complete(this);
		} finally {
			this.checkpoints = prevCheckpoints;
			this.currentParsedArgument = prevCurrentParsedArgument;
		}
	}

	/**
	 * Used to specified that a single argument that need to aggregate multiple value has start.
	 */
	public void beginArgument() {
		currentParsedArgument = currentArg;
	}

	/**
	 * Used to specified that a single argument that need to aggregate multiple value has end.
	 */
	public void endArgument() {
		currentParsedArgument = null;
	}

	/**
	 * Creates a {@link CommandException} to indicate that current argument cannot be parsed or is invalid.
	 *
	 * @param reasonKey  the reason key used to get the message to display
	 * @param formatArgs the argument passed to {@link String#format(String, Object...)}
	 * @return the {@link CommandException}
	 */
	public CommandException invalidArgument(@Nullable final String reasonKey, final String... formatArgs) {
		return new CommandException(CommandException.Type.INVALID_ARGUMENT, this, reasonKey, formatArgs);
	}

	/**
	 * Creates a {@link CommandException} to indicate that current argument cannot be parsed or is invalid.
	 *
	 * @return the {@link CommandException}
	 */
	public CommandException invalidArgument() {
		return invalidArgument(null);
	}

	/**
	 * Adds a check point at the current location.
	 */
	public void checkpoint() {
		if (checkpoints == null) throw new IllegalStateException();
		checkpoints.push(currentArg);
	}

	/**
	 * Removes the last check point.
	 */
	public void dropCheckpoint() {
		if (checkpoints == null) throw new IllegalStateException();
		if (!checkpoints.empty()) checkpoints.pop();
	}

	/**
	 * Removes all check points.
	 */
	public void dropAllCheckpoints() {
		if (checkpoints == null) throw new IllegalStateException();
		checkpoints.clear();
	}

	/**
	 * Returns at the last check point if exists and remove it.
	 */
	public void restore() {
		if (checkpoints == null) throw new IllegalStateException();
		if (!checkpoints.empty()) currentArg = checkpoints.pop();
	}

	/**
	 * Returns at the last check point if exists.
	 */
	public void back() {
		if (checkpoints == null) throw new IllegalStateException();
		if (!checkpoints.empty()) currentArg = checkpoints.peek();
	}
}
