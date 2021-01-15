package azerty.tguichaoua.mpb.menu;

import azerty.tguichaoua.mpb.model.ItemCreator;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public abstract class MenuBuilder {

	public static final ItemStack FILLER = filler(Material.GRAY_STAINED_GLASS_PANE);

	public static @NotNull ItemStack filler(@NotNull final Material material) {
		return filler(material, " ");
	}

	public static @NotNull ItemStack filler(@NotNull final Material material, @NotNull final String name) {
		return ItemCreator.of(material, name).hideTags(true).build().make();
	}

	// ----------------------------------------------------------------------

	@Getter(AccessLevel.PACKAGE) private final int preferredHeight;

	public MenuBuilder(final int preferredHeight) {
		this.preferredHeight = preferredHeight;
	}

	public MenuBuilder() {
		this(Menu.MAX_ROW_COUNT);
	}

	protected abstract void render(MenuRenderer renderer);

	/**
	 * Called when the menu is re-open after a back action, before {@link #render(MenuRenderer)} is called.
	 */
	protected void onReopen() {
	}

	public Menu display(@NotNull final Plugin plugin, @NotNull final Player player, final int rowCount) {
		return Menu.display(plugin, player, this, rowCount);
	}

	public Menu display(@NotNull final Plugin plugin, @NotNull final Player player) {
		return Menu.display(plugin, player, this);
	}
}
