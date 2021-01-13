package azerty.tguichaoua.mpb;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;

public abstract class MyCommand extends Command {

	protected CommandSender sender;
	protected String label;
	protected String[] args;

	/**
	 * @param label a `|` seperated list where first element is the command's label and others are aliases.
	 *              example: "message|msg|m" create a command that can be called with `/message`, `/msg` or `/m`
	 */
	protected MyCommand(final String label) {
		this(parseLabel(label), parseAliases(label));
	}

	protected MyCommand(final String label, final String[] aliases) {
		super(label);
		if (aliases != null) setAliases(Arrays.asList(aliases));
	}

	private static String parseLabel(final String label) {
		return label.trim().split("\\s*\\|\\s*")[0];
	}

	private static String[] parseAliases(final String label) {
		final String[] aliases = label.trim().split("\\s*\\|\\s*");
		return aliases.length > 0 ? Arrays.copyOfRange(aliases, 1, aliases.length) : null;
	}

	@Override
	public boolean execute(final @NotNull CommandSender sender, final @NotNull String label, final String[] args) {
		this.sender = sender;
		this.label = label;
		this.args = args;
		try {
			execute();
		} catch (final Exception e) {
			Bukkit.getLogger().severe("Fail to execute command (" + this.getLabel() + ") : " + e);
			e.printStackTrace();
			return false;
		}
		return true;
	}

	protected abstract void execute() throws Exception;

	public void register() {
		getCommandMap().register(this.getLabel(), this);
	}

	// static
	private static CommandMap commandMap;

	static CommandMap getCommandMap() {
		if (commandMap == null) {
			try {
				final Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
				field.setAccessible(true);
				commandMap = (CommandMap) field.get(Bukkit.getServer());
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		return commandMap;
	}
}
