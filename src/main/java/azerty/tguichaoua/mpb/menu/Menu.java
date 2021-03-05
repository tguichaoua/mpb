package azerty.tguichaoua.mpb.menu;

import azerty.tguichaoua.mpb.Constants;
import azerty.tguichaoua.mpb.util.MathUtils;
import azerty.tguichaoua.mpb.util.MetadataUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Menu {

	private static final String EXCEPTION_NOT_CURRENT = "This menu must be the current one to preform this action.";

	static final String CURRENT_MENU_TAG = Constants.DOMAIN + "CURRENT_MENU";
	public static final int MAX_ROW_COUNT = 6;

	private final Plugin plugin;
	@Getter private final Player viewer;
	private final MenuBuilder builder;
	private final int rowCount;
	private @NotNull RenderedMenu rendered;

	private final @Nullable Menu previousMenu;
	private boolean isCurrent;

	static @Nullable Menu getMenu(final @NotNull Plugin plugin, final @NotNull Player player) {
		final MetadataValue value = MetadataUtils.get(player, Menu.CURRENT_MENU_TAG, plugin);
		return value != null ? (Menu) value.value() : null;
	}

	public static Menu display(
			@NotNull final Plugin plugin,
			@NotNull final Player player,
			@NotNull final MenuBuilder builder,
			final int rowCount
	) {
		return display(plugin, player, builder, rowCount, null);
	}

	public static Menu display(
			@NotNull final Plugin plugin,
			@NotNull final Player player,
			@NotNull final MenuBuilder builder
	) {
		return display(plugin, player, builder, null, null);
	}

	private static Menu display(
			@NotNull final Plugin plugin,
			@NotNull final Player player,
			@NotNull final MenuBuilder builder,
			@Nullable Integer rowCount,
			final Menu previousMenu
	) {
		if (rowCount == null) rowCount = builder.getPreferredHeight();
		rowCount = MathUtils.clamp(rowCount, 1, MAX_ROW_COUNT);
		final Menu menu = new Menu(
				plugin, player, builder, rowCount,
				render(plugin, player, builder, rowCount),
				previousMenu, true
		);
		menu.open();

		return menu;
	}

	private static @NotNull RenderedMenu render(final Plugin plugin, final Player viewer, final MenuBuilder builder, final int rowCount) {
		final MenuRenderer.Main renderer = new MenuRenderer.Main(new MenuRegion(0, 0, 9, rowCount), viewer);

		try {
			builder.render(renderer);
		} catch (final Throwable t) {
			plugin.getLogger().severe("An error occurs while trying to render a menu.");
			t.printStackTrace();
			renderer.clear();
		}

		final String title = renderer.getTitle();
		final Inventory inventory = Bukkit.createInventory(
				null, rowCount * 9,
				title == null ? "" : title
		);

		for (final Map.Entry<Integer, ItemStack> e : renderer.getItems().entrySet())
			inventory.setItem(e.getKey(), e.getValue() == null ? new ItemStack(Material.AIR) : e.getValue());

		return new RenderedMenu(
				new HashSet<>(renderer.getIsItemSlots()),
				new HashMap<>(renderer.getActions()),
				new ArrayList<>(renderer.getOnCloseCallbacks()),
				inventory,
				renderer.isBackOnClickOutside()
		);
	}

	private void open() {
		viewer.setMetadata(Menu.CURRENT_MENU_TAG, new FixedMetadataValue(plugin, this));
		isCurrent = true;
		viewer.openInventory(rendered.inventory);
	}

	private void render() {
		rendered = render(plugin, viewer, builder, rowCount);
	}

	private void detach() {
		isCurrent = false;
		viewer.removeMetadata(Menu.CURRENT_MENU_TAG, plugin);
	}

	private void raiseOnClose() {
		final CloseEvent closeEvent = new CloseEvent(this);
		rendered.onCloseCallbacks.forEach(cb -> {
			try {
				cb.accept(closeEvent);
			} catch (final Throwable t) {
				plugin.getLogger().severe("An error occurs while trying to handle onClose event.");
				t.printStackTrace();
			}
		});
	}

	// --- Event Handlers ---------------------------------------------------------

	void handleMenuClose(final @NotNull InventoryCloseEvent event) {
		if (event.getInventory() != rendered.inventory) return;
		detach();
		raiseOnClose();
	}

	void handleClick(final int slot, @NotNull final ClickType clickType) {
		final ButtonAction action = rendered.buttonActions.get(slot);
		if (action != null) {
			try {
				action.accept(new ButtonClickEvent(this, clickType));
			} catch (final Throwable t) {
				plugin.getLogger().severe("An error occurs while trying to handle an click.");
				t.printStackTrace();
			}
		}
	}

	void handleClickOutside() {
		if (previousMenu != null && rendered.backOnClickOutside)
			back();
	}

	// --- Package API -----------------------------------------------------------

	boolean isItemSlot(final int slot) {
		return rendered.isItemSlot.contains(slot);
	}

	// --- Public API ------------------------------------------------------------

	/**
	 * Returns a copy of the inventory content.
	 * @return a copy of the inventory content
	 */
	public @NotNull ItemStack[] getInventoryContent() {
		return Arrays.stream(rendered.inventory.getContents())
				.map(ItemStack::clone)
				.toArray(ItemStack[]::new);
	}

	/**
	 * Returns a copy of the inventory content for the specified region.
	 * @param region the region to get
	 * @return a copy of the inventory content
	 */
	public @NotNull ItemStack[] getInventoryContent(@NotNull final MenuRegion region) {
		final ItemStack[] stacks = new ItemStack[region.getSlotCount()];
		final ItemStack[] inventoryContent = rendered.inventory.getContents();
		region.forEach(l -> {
			final ItemStack item = inventoryContent[l.getSlot()];
			stacks[l.getIndex()] = item == null ? new ItemStack(Material.AIR) : item.clone();
		});
		return stacks;
	}

	private void _open(@NotNull final MenuBuilder builder, final @Nullable Integer rowCount) {
		if (!isCurrent) throw new IllegalStateException(EXCEPTION_NOT_CURRENT);
		display(plugin, viewer, builder, rowCount, this);
		isCurrent = false;
	}

	public void open(@NotNull final MenuBuilder builder, final int rowCount) {
		_open(builder, rowCount);
	}

	public void open(@NotNull final MenuBuilder builder) {
		_open(builder, null);
	}

	public void back() {
		if (previousMenu == null)
			close();
		else {
			if (!isCurrent) return;
			isCurrent = false;
			raiseOnClose();
			previousMenu.builder.onReopen();
			previousMenu.render();
			previousMenu.open();
		}
	}

	public void close() {
		if (!isCurrent) return;
		detach();
		viewer.closeInventory();
	}

	public void redraw() {
		if (!isCurrent) throw new IllegalStateException(EXCEPTION_NOT_CURRENT);
		render();
		viewer.openInventory(rendered.inventory);
	}

	// --- Static Utils

	/**
	 * Gets the slot index based on its row and column position (zero based).
	 * Warning : {@code row} must be between {@literal 0} and {@literal 8} (include) and {@code column}
	 * must be between {@literal 0} and {@code rowCount}, or the result value will not be accurate.
	 * @param row the row of the slot
	 * @param column the column of the slot
	 * @return the slot index
	 */
	static int getSlot(final int row, final int column) {
		return column + row * 9;
	}

	// --- Interfaces / Classes --------------------------------------------------

	public interface ButtonAction extends Consumer<ButtonClickEvent> { }

	public interface OnClose extends Consumer<CloseEvent> { }

	@RequiredArgsConstructor
	private static final class RenderedMenu {
		private final Set<Integer> isItemSlot;
		private final Map<Integer, ButtonAction> buttonActions;
		private final List<Menu.OnClose> onCloseCallbacks;
		private final Inventory inventory;
		private final boolean backOnClickOutside;
	}

	@Getter
	@RequiredArgsConstructor
	public static final class ButtonClickEvent {
		private final @NotNull Menu menu;
		private final @NotNull ClickType click;
	}

	@Getter
	@RequiredArgsConstructor
	public static final class CloseEvent {
		private final @NotNull Menu menu;
	}
}
