package azerty.tguichaoua.mpb.menu.model;

import azerty.tguichaoua.mpb.menu.Direction;
import azerty.tguichaoua.mpb.menu.MenuBuilder;
import azerty.tguichaoua.mpb.menu.MenuRenderException;
import azerty.tguichaoua.mpb.menu.MenuRenderer;
import azerty.tguichaoua.mpb.menu.layout.AbstractListLayout;
import azerty.tguichaoua.mpb.menu.layout.LineListLayout;
import azerty.tguichaoua.mpb.menu.layout.ListLayout;
import azerty.tguichaoua.mpb.model.ItemCreator;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@RequiredArgsConstructor
public final class ListMenu<T> extends MenuBuilder {

	static final ItemStack NEXT_PAGE_BUTTON = ItemCreator.of(Material.ARROW, ">>>").hideTags(true).build().make();
	static final ItemStack PREVIOUS_PAGE_BUTTON = ItemCreator.of(Material.ARROW, "<<<").hideTags(true).build().make();

	// --- Properties -----------------------------------------------------------------------------------
	private final @Nullable String title;
	private final @NotNull AbstractListLayout<T> listLayout;
	private final @NotNull Direction buttonBarPosition;

	// --- Constructors -----------------------------------------------------------------------------------
	public ListMenu(
			@Nullable final String title,
			@NotNull final AbstractListLayout<T> listLayout
	) {
		this(title, listLayout, Direction.DOWN);
	}

	public ListMenu(
			@NotNull final AbstractListLayout<T> listLayout,
			@NotNull final Direction buttonBarPosition
	) {
		this(null, listLayout, buttonBarPosition);
	}

	public ListMenu(@NotNull final AbstractListLayout<T> listLayout) {
		this(null, listLayout, Direction.DOWN);
	}
	
	// --- Rendering -------------------------------------------------------------------------------------------------
	@Override
	protected void render(final MenuRenderer renderer) throws MenuRenderException {
		renderer.requireSpace(3, 2);

		listLayout.render(renderer.of(
				buttonBarPosition == Direction.LEFT ? 1 : 0,
				buttonBarPosition == Direction.UP ? 1 : 0,
				buttonBarPosition == Direction.RIGHT ? -2 : -1,
				buttonBarPosition == Direction.DOWN ? -2 : -1
		));

		renderer.setTitle(
				title == null ?
						String.format(
								"【 %d / %d 】",
								listLayout.getCurrentPage() + 1,
								listLayout.getLastPage() + 1
						) :
						String.format(
								"%s%s -【 %d / %d 】",
								title,
								ChatColor.RESET,
								listLayout.getCurrentPage() + 1,
								listLayout.getLastPage() + 1
						)
		);

		final MenuRenderer buttonBarRenderer = renderer.ofLine(buttonBarPosition, -1);

		if (listLayout.hasPreviousPage()) buttonBarRenderer.set(0, 0, PREVIOUS_PAGE_BUTTON, listLayout::previousPageButton);
		if (listLayout.hasNextPage()) buttonBarRenderer.set(-1, -1, NEXT_PAGE_BUTTON, listLayout::nextPageButton);
	}

	// --- Static Constructor ---------------------------------------------------------------------
	public static <T> ListMenu<T> of(
			@Nullable final String title,
			@NotNull final Direction buttonBarPosition,
			@NotNull final List<T> list,
			@NotNull final ListLayout.Adapter<T> adapter,
			@Nullable final ListLayout.OnItemClick<T> onItemClick
	) {
		return new ListMenu<>(title, new ListLayout<>(list, adapter, onItemClick), buttonBarPosition);
	}

	public static <T> ListMenu<T> of(
			@NotNull final List<T> list,
			@NotNull final ListLayout.Adapter<T> adapter,
			@Nullable final ListLayout.OnItemClick<T> onItemClick
	) {
		return new ListMenu<>(new ListLayout<>(list, adapter, onItemClick));
	}

	public static <T> ListMenu<T> of(
			@Nullable final String title,
			@NotNull final Direction buttonBarPosition,
			@NotNull final List<T> list,
			@NotNull final LineListLayout.LineRenderer<T> lineRenderer,
			@NotNull final Direction direction
	) {
		return new ListMenu<>(title, new LineListLayout<>(list, lineRenderer, direction), buttonBarPosition);
	}

	public static <T> ListMenu<T> of(
			@NotNull final List<T> list,
			@NotNull final LineListLayout.LineRenderer<T> lineRenderer,
			@NotNull final Direction direction
	) {
		return new ListMenu<>(new LineListLayout<>(list, lineRenderer, direction));
	}
}
