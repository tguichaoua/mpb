package azerty.tguichaoua.mpb.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ItemStackUtils {
	public static String getName(final @NotNull ItemStack stack) {
		return stack.getItemMeta() != null && stack.getItemMeta().hasDisplayName() ? stack.getItemMeta().getDisplayName() : stack.getType().name().replace("_", " ").toLowerCase();
	}

	public static void setName(@NotNull final ItemStack item, @Nullable final String name) {
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
	}

	public static @NotNull ItemStack[] aggregate(final ItemStack[] stacks) {
		final ArrayList<ItemStack> aggregated = new ArrayList<>();
		for (final ItemStack s : stacks) {
			if (s == null) continue;
			boolean added = false;
			for (final ItemStack a : aggregated)
				if (a.isSimilar(s)) {
					a.setAmount(a.getAmount() + s.getAmount());
					added = true;
					break;
				}
			if (!added) aggregated.add(s.clone());
		}
		return aggregated.toArray(new ItemStack[0]);
	}
}
