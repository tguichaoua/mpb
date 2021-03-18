package azerty.tguichaoua.mpb.command;

import azerty.tguichaoua.mpb.command.argument.DoubleCommandArgument;
import azerty.tguichaoua.mpb.command.argument.IntegerCommandArgument;
import azerty.tguichaoua.mpb.command.argument.ListedCommandArgument;
import azerty.tguichaoua.mpb.command.argument.TargetSelectorCommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

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
		final CommandExecution execution = new CommandExecution(commandSender, label, args);
		try {
			execute(execution);
		} catch (final CommandException e) {
			final StringBuilder message = new StringBuilder(ChatColor.RED + getExceptionMessage(e.getType().key));

			switch (e.getType()) {
				case UNKNOWN_COMMAND:
					message.append(" \"/");
					message.append(e.getLabel());
					message.append('"');
					break;
				case INVALID_ARGUMENT:
					message.append(ChatColor.RESET)
							.append('\n')
							.append(label)
							.append(" ")
							.append(
									Arrays.stream(args)
											.limit(execution.getCurrentParsedArgumentIndex())
											.collect(Collectors.joining(" "))
							)
							.append(" ")
							.append(ChatColor.RED)
							.append(ChatColor.UNDERLINE)
							.append(e.getArgument())
							.append("<--");

					if (e.getReasonKey() != null) {
						final String s = getExceptionMessage(e.getReasonKey());
						if (s != null) {
							message.append(ChatColor.RESET)
									.append('\n')
									.append(String.format(s, (Object[]) e.getFormatArgs()));
						}
					}
					break;
			}

			commandSender.sendMessage(message.toString());
		} catch (final Throwable t) {
			t.printStackTrace();
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
		try {
			return new ArrayList<>(complete(new CommandExecution(commandSender, label, args)));
		} catch (final Throwable t) {
			t.printStackTrace();
			return Collections.emptyList();
		}
	}

	// --- STATIC --------------------------------
	private static @Nullable CommandExceptionMessageSupplier exceptionMessageSupplier = null;
	private static final Map<String, String> DEFAULT_MESSAGE = new HashMap<>();

	static {
		DEFAULT_MESSAGE.put(CommandException.Type.PERMISSION.key, "You have not the permission to run this command.");
		DEFAULT_MESSAGE.put(CommandException.Type.UNKNOWN_COMMAND.key, "Unknown command");
		DEFAULT_MESSAGE.put(CommandException.Type.MISSING_ARGUMENT.key, "More arguments are required.");
		DEFAULT_MESSAGE.put(CommandException.Type.INVALID_ARGUMENT.key, "Incorrect argument");

		DEFAULT_MESSAGE.put(IntegerCommandArgument.INVALID_ARGUMENT, "The value should be an integer.");
		DEFAULT_MESSAGE.put(DoubleCommandArgument.INVALID_ARGUMENT, "The value should be a double.");

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
