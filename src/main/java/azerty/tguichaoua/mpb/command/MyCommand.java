package azerty.tguichaoua.mpb.command;

import azerty.tguichaoua.mpb.command.argument.ListedCommandArgument;
import azerty.tguichaoua.mpb.command.argument.TargetSelectorCommandArgument;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class MyCommand implements CommandExecutor, TabCompleter {

	protected abstract void execute(@NotNull CommandExecution execution) throws CommandException;

	protected abstract @NotNull Collection<String> complete(@NotNull CommandExecution execution);

	public void attach(@NotNull final JavaPlugin plugin, @NotNull final String commandName) {
		final PluginCommand command = Objects.requireNonNull(plugin.getCommand(commandName));
		command.setExecutor(this);
		command.setTabCompleter(this);
	}

	// --- Bukkit Command --------------------------
	@Override
	public boolean onCommand(
			@NotNull final CommandSender commandSender,
			@NotNull final Command command,
			@NotNull final String label,
			@NotNull final String[] args
	) {
		try {
			execute(new CommandExecution(commandSender, label, args));
		} catch (final CommandException e) {
			String message = "";

			switch (e.getType()) {
				case PERMISSION:
				case MISSING_ARGUMENT:
					message = getExceptionMessage(e.getType().key);
					break;
				case UNKNOWN_COMMAND:
					message = String.format("%s \"/%s\"", getExceptionMessage(e.getType().key), e.getLabel());
					break;
				case INVALID_ARGUMENT:
					message = String.format("%s \"%s\"", getExceptionMessage(e.getType().key), args[e.getAt() - 1]);
					if (e.getReasonKey() != null) {
						final String s = getExceptionMessage(e.getReasonKey());
						if (s != null) {
							message += "\n" + String.format(s, (Object[]) e.getFormatArgs());
						}
					}
					break;
			}

			commandSender.sendMessage(new ComponentBuilder(message).create());
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(
			@NotNull final CommandSender commandSender,
			@NotNull final Command command,
			@NotNull final String label,
			@NotNull final String[] args
	) {
		return new ArrayList<>(complete(new CommandExecution(commandSender, label, args)));
	}

	// --- STATIC --------------------------------
	private static @Nullable CommandExceptionMessageSupplier exceptionMessageSupplier = null;
	private static final Map<String, String> DEFAULT_MESSAGE = new HashMap<>();

	static {
		DEFAULT_MESSAGE.put(CommandException.Type.PERMISSION.key, "You have not the permission to run this command.");
		DEFAULT_MESSAGE.put(CommandException.Type.UNKNOWN_COMMAND.key, "Unknown command");
		DEFAULT_MESSAGE.put(CommandException.Type.MISSING_ARGUMENT.key, "More arguments are required.");
		DEFAULT_MESSAGE.put(CommandException.Type.INVALID_ARGUMENT.key, "Invalid argument");

		DEFAULT_MESSAGE.put(CommandExecution.INVALID_INTEGER, "The value should be an integer.");
		DEFAULT_MESSAGE.put(CommandExecution.INVALID_DOUBLE, "The value should be a double.");

		DEFAULT_MESSAGE.put(ListedCommandArgument.INVALID_ARGUMENT, "The value should be one of the following : %s");

		DEFAULT_MESSAGE.put(TargetSelectorCommandArgument.INVALID_FORMAT, "Invalid format for target selector.");
		DEFAULT_MESSAGE.put(TargetSelectorCommandArgument.INVALID_SELECTOR, "Invalid selector \"%s\", expecting \"@p\", \"@r\", \"@a\", \"@e\" or \"@s\".");
		DEFAULT_MESSAGE.put(TargetSelectorCommandArgument.INVALID_ARGUMENT, "Invalid argument \"%s\".");
		DEFAULT_MESSAGE.put(TargetSelectorCommandArgument.INVALID_ARGUMENT_VALUE, "Invalid argument's value \"%s\".");
	}

	private static @Nullable String getExceptionMessage(@NotNull final String key) {
		if (exceptionMessageSupplier == null) {
			return DEFAULT_MESSAGE.get(key);
		} else {
			final String s = exceptionMessageSupplier.getExceptionMessage(key);
			return s != null ? s : DEFAULT_MESSAGE.get(key);
		}
	}

	/**
	 * Sets the supplier used to build the message send when an exception occurs.
	 *
	 * @param supplier the message supplier
	 */
	public static void setExceptionMessageSupplier(@Nullable final CommandExceptionMessageSupplier supplier) {
		exceptionMessageSupplier = supplier;
	}
}
