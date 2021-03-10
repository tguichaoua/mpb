package azerty.tguichaoua.mpb.model;

import lombok.Builder;
import lombok.Singular;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Builder(builderClassName = "Builder")
final public class ItemCreator {

	/**
	 * The material of the item
	 */
	private final Material material;

	/**
	 * The displayed name of the item
	 */
	private final String name;

	/**
	 * The amount of the item
	 */
	@lombok.Builder.Default
	private final int amount = 1;

	/**
	 * The lore of the item
	 */
	@Singular
	private final List<String> lores;

	/**
	 * The item flags
	 */
	@Singular
	private final Set<ItemFlag> flags;

	/**
	 * Is the item unbreakable
	 */
	private final boolean unbreakable;

	/**
	 * Is all flags of the item should be hide
	 */
	private final boolean hideTags;

	/**
	 * Add a glow effect on the item. (Add an enchantment and hide it with {@link ItemFlag#HIDE_ENCHANTS})
	 */
	private final boolean glow;

	/**
	 * The enchantments applied on the item
	 */
	@Singular
	private final Map<Enchantment, @NotNull Integer> enchantments;

	/**
	 * The head owner
	 */
	private final OfflinePlayer headPlayerOwner;

	/**
	 * The Base64 string that contains a head texture
	 */
	private final String headBase64;

	/**
	 * Creates and gives the item stack to the player.
	 *
	 * @param player The player to which give the item
	 */
	public void give(final Player player) {
		player.getInventory().addItem(make());
	}

	/**
	 * Creates the item stack.
	 *
	 * @return The created item stack
	 */
	public ItemStack make() {
		if (material == null)
			throw new RuntimeException("material must be defined.");

		final ItemStack stack = new ItemStack(material, amount);
		final ItemMeta itemMeta = stack.getItemMeta();

		if (name != null && !"".equals(name))
			itemMeta.setDisplayName(name);

		if (lores != null && !lores.isEmpty())
			itemMeta.setLore(lores);

		if (glow) {
			itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
			itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}

		if (enchantments != null)
			for (final Map.Entry<Enchantment, Integer> e : enchantments.entrySet())
				itemMeta.addEnchant(e.getKey(), e.getValue(), true);

		itemMeta.setUnbreakable(unbreakable);

		if (hideTags) {
			for (final ItemFlag f : ItemFlag.values()) {
				itemMeta.addItemFlags(f);
			}
		} else if (flags != null) {
			for (final ItemFlag f : flags) {
				itemMeta.addItemFlags(f);
			}
		}

		if (itemMeta instanceof SkullMeta) {
			if (headPlayerOwner != null)
				((SkullMeta) itemMeta).setOwningPlayer(headPlayerOwner);
			else if (headBase64 != null)
				SkullCreator.mutateItemMeta(((SkullMeta) itemMeta), headBase64);
		}

		stack.setItemMeta(itemMeta);
		return stack;
	}

	public static Builder of(final Material material) {
		return builder().material(material);
	}

	public static Builder of(final Material material, final String name, final Collection<String> lore) {
		return builder().material(material).name(name).lores(lore);
	}

	public static Builder of(final Material material, final String name, final String... lore) {
		return of(material, name, Arrays.asList(lore));
	}

	public static Builder of(final OfflinePlayer player) {
		return of(Material.PLAYER_HEAD).headPlayerOwner(player);
	}

	public static Builder of(final UUID playerUUID) {
		return of(Bukkit.getOfflinePlayer(playerUUID));
	}

	public static Builder ofHead(final String base64) {
		return of(Material.PLAYER_HEAD).headBase64(base64);
	}

	public static Builder ofHead(final String base64, final String name, final Collection<String> lore) {
		return of(Material.PLAYER_HEAD).headBase64(base64).name(name).lores(lore);
	}

	public static Builder ofHead(final String base64, final String name, final String... lore) {
		return ofHead(base64, name, Arrays.asList(lore));
	}

	// must be kept to avoid javadoc warning
	public static class Builder { }
}
