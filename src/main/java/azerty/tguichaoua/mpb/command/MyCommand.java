package azerty.tguichaoua.mpb.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
			exceptionCatcher.accept(e);
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
	private static @NotNull CommandExceptionCatcher exceptionCatcher = MyCommand::defaultCatcher;

	/**
	 * Sets the method called to handle {@link CommandException}.
	 * If the value is {@code null}, the default value is set.
	 *
	 * @param catcher the handler
	 */
	public static void setExceptionCatcher(final @Nullable CommandExceptionCatcher catcher) {
		exceptionCatcher = catcher == null ? MyCommand::defaultCatcher : catcher;
	}

	private static void defaultCatcher(@NotNull final CommandException exception) {
		String message = "";

		switch (exception.getType()) {
			case PERMISSION:
				message = "You have not the permission to run this command.";
				break;
			case UNKNOWN_COMMAND:
				message = String.format("Unknown command \"/%s\"", exception.getLabel());
				break;
			case MISSING_ARGUMENT:
				message = "More arguments are required.";
				break;
			case INVALID_ARGUMENT:
				message = String.format("Invalid argument \"%s\"", exception.getArgs()[exception.getAt() - 1]);
				break;
		}

		exception.getSender().sendMessage(new ComponentBuilder(message).color(ChatColor.RED).create());
	}
}
