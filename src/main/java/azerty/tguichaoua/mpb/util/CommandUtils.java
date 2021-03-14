package azerty.tguichaoua.mpb.util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class CommandUtils {
	/**
	 * Returns the location of the {@link CommandSender}.
	 * If it's a {@link Block} returns the center of the block.
	 * If it's the console returns the spawn location of the default world.
	 *
	 * @param sender the command sender
	 * @return the location of the sender
	 */
	public static @NotNull Location getCommandSenderLocation(@NotNull final CommandSender sender) {
		if (sender instanceof Entity) {
			return ((Entity) sender).getLocation();
		} else if (sender instanceof BlockCommandSender) {
			return ((BlockCommandSender) sender).getBlock().getLocation().add(0.5, 0.5, 0.5);
		} else {
			return sender.getServer().getWorlds().get(0).getSpawnLocation();
		}
	}
}
