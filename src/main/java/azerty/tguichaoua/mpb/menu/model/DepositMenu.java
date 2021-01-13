package azerty.tguichaoua.mpb.menu.model;

import azerty.tguichaoua.mpb.menu.*;
import azerty.tguichaoua.mpb.model.ItemCreator;
import azerty.tguichaoua.mpb.util.ItemStackUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@RequiredArgsConstructor
public final class DepositMenu extends MenuBuilder {

	private static final ItemStack SUBMIT_BUTTON = ItemCreator.of(Material.GREEN_STAINED_GLASS_PANE, "Submit").hideTags(true).build().make();
	private static final ItemStack CANCEL_BUTTON = ItemCreator.of(Material.RED_STAINED_GLASS_PANE, "Cancel").hideTags(true).build().make();

	private final @Nullable String title;
	private final @NotNull OnAction onSubmit;
	private final @NotNull OnAction onCancel;

	private boolean ignoreCloseEvent = false;

	public DepositMenu(
			final @NotNull OnAction onSubmit,
			final @NotNull OnAction onCancel
	) {
		this(null, onSubmit, onCancel);
	}

	public DepositMenu(
			final @Nullable String title,
			final @NotNull OnAction onSubmit
	) {
		this(title, onSubmit, DepositMenu::onCancel);
	}

	public DepositMenu(final @NotNull OnAction onSubmit) {
		this(null, onSubmit, DepositMenu::onCancel);
	}

	@Override
	protected void render(final MenuRenderer renderer) throws MenuRenderException {
		renderer.requireSpace(2, 2);

		final MenuRegion itemRegion = renderer.getRegion().row(0, -2);

		renderer.setTitle(title);
		renderer.setBackOnClickOutside(false);
		renderer.onClose(e -> {
			if (ignoreCloseEvent) return;
			onCancel.accept(new ActionEvent(e.getMenu(), getItems(e.getMenu(), itemRegion)));
		});

		renderer.fillItem(itemRegion);

		renderer.fill(renderer.getRegion().row(-1), FILLER);
		renderer.set(-1, -1, SUBMIT_BUTTON, e -> {
			ignoreCloseEvent = true;
			onSubmit.accept(new ActionEvent(e.getMenu(), getItems(e.getMenu(), itemRegion)));
			ignoreCloseEvent = false;
		});
		renderer.set(0, -1, CANCEL_BUTTON, e -> {
			ignoreCloseEvent = true;
			onCancel.accept(new ActionEvent(e.getMenu(), getItems(e.getMenu(), itemRegion)));
			ignoreCloseEvent = false;
		});
	}

	private @NotNull ItemStack[] getItems(final Menu menu, final MenuRegion itemRegion) {
		return ItemStackUtils.aggregate(menu.getInventoryContent(itemRegion));
	}

	private static void onCancel(final ActionEvent event) {
		event.getMenu().getViewer().getInventory().addItem(event.getItems());
		event.getMenu().back();
	}

	// ---------------------------------------------------------------------------------------

	@FunctionalInterface
	public interface OnAction extends Consumer<ActionEvent> { }

	@Getter
	@RequiredArgsConstructor
	public static final class ActionEvent {
		private final @NotNull Menu menu;
		private final @NotNull ItemStack[] items;
	}
}
