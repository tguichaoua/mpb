package azerty.tguichaoua.mpb.menu;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

@RequiredArgsConstructor
public final class MenuListener implements Listener {

	private enum MenuClickLocation {
		MENU,
		PLAYER_INVENTORY,
		OUTSIDE
	}

	private final Plugin plugin;

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	private void onMenuClose(final InventoryCloseEvent event) {
		if (!(event.getPlayer() instanceof Player)) return;

		final Player player = (Player) event.getPlayer();
		final Menu menu = Menu.getMenu(plugin, player);
		if (menu == null) return;

		menu.handleMenuClose(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	private void onMenuClick(final InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) return;

		final Player player = (Player) event.getWhoClicked();
		final Menu menu = Menu.getMenu(plugin, player);
		if (menu == null) return;

		final Inventory clickedInventory = event.getClickedInventory();
		final MenuClickLocation whereClicked =
				clickedInventory == null ?
						MenuClickLocation.OUTSIDE :
						clickedInventory.getType() == InventoryType.CHEST ?
								MenuClickLocation.MENU :
								MenuClickLocation.PLAYER_INVENTORY;

		final InventoryAction action = event.getAction();

		if (action != InventoryAction.COLLECT_TO_CURSOR) {
			switch (whereClicked) {
				case OUTSIDE:
					if (action.toString().contains("DROP")) return;
					menu.handleClickOutside();
					return;
				case PLAYER_INVENTORY:
					return;
				case MENU:
					if (menu.isItemSlot(event.getSlot())) return;

					if (
							action.toString().contains("PICKUP")
									|| action.toString().contains("PLACE")
									|| action == InventoryAction.SWAP_WITH_CURSOR
									|| action == InventoryAction.CLONE_STACK
					) {
						menu.handleClick(event.getSlot(), event.getClick());
					}
					break;
			}
		}

		event.setResult(Event.Result.DENY);
		player.updateInventory();
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	private void onMenuDrag(final InventoryDragEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) return;

		final Player player = (Player) event.getWhoClicked();
		final Menu menu = Menu.getMenu(plugin, player);
		if (menu == null) return;

		final int highestMenuSlot = event.getInventory().getSize();
		if (! event.getRawSlots().stream()
						.allMatch(slot -> slot >= highestMenuSlot || menu.isItemSlot(slot))
		) {
			event.setResult(Event.Result.DENY);
			player.updateInventory();
		}
	}
}