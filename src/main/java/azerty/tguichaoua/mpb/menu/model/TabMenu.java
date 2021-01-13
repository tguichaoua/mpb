package azerty.tguichaoua.mpb.menu.model;

import azerty.tguichaoua.mpb.menu.MenuBuilder;
import azerty.tguichaoua.mpb.menu.MenuRenderer;
import azerty.tguichaoua.mpb.menu.layout.ListLayout;
import azerty.tguichaoua.mpb.util.ItemStackUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public final class TabMenu extends MenuBuilder {

	private static final ItemStack SELECTED_TAB = filler(Material.GREEN_STAINED_GLASS_PANE);

	private final @Nullable String title;
	private final @NotNull List<Tab> tabs;
	private final @NotNull ListLayout<Tab> tabLayout;

	private int currentTab = 0;

	public TabMenu(
			@Nullable final String title,
			@NotNull final List<Tab> tabs
	) {
		this.title = title;
		this.tabs = tabs;
		this.tabLayout = new ListLayout<>(
				tabs,
				Tab::getTabItem,
				e -> {
					currentTab = e.getIndex();
					e.getMenu().redraw();
				}
		);
	}

	public TabMenu(@NotNull final List<Tab> tabs) {
		this(null, tabs);
	}

	@Override
	protected void render(final MenuRenderer renderer) {
		renderer.requireSpace(3, 3);

		// Render the tab header
		final MenuRenderer tabMenuRenderer = renderer.render(1, 0, -2, 0);
		tabLayout.render(tabMenuRenderer);

		if (tabLayout.hasPreviousPage())
			renderer.set(0, 0, ListMenu.PREVIOUS_PAGE_BUTTON, tabLayout::previousPageButton);

		if (tabLayout.hasNextPage())
			renderer.set(-1, 0, ListMenu.NEXT_PAGE_BUTTON, tabLayout::nextPageButton);

		renderer.fill(renderer.getRegion().row(1), FILLER);
		if (tabLayout.isOnTheCurrentPage(currentTab))
			renderer.set(currentTab - tabLayout.getIndexOffset() + 1, 1, SELECTED_TAB);

		// Render the tab content
		final Tab tab = tabs.get(currentTab);

		final MenuRenderer.Data data = renderer.render(tab.menu, renderer.getRegion().row(2, -1));
		final String tabTitle = data.getTitle() != null ? data.getTitle() : tab.getName();

		renderer.setTitle(
				title == null ?
						tabTitle :
						String.format("%s > %s", title, tabTitle)
		);
	}

	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	@Getter
	public static final class Tab {
		private final @NotNull String name;
		private final @NotNull ItemStack tabItem;
		private final @NotNull MenuBuilder menu;

		public static Tab of(
				@NotNull final String name,
				@NotNull final ItemStack tabItem,
				@NotNull final MenuBuilder menu
		) {
			final ItemStack item = tabItem.clone();
			ItemStackUtils.setName(item, name);

			return new Tab(name, item, menu);
		}
	}

	// --- Static ----------------------------------------------------------------

	public static Tab tab(@NotNull final String name, @NotNull final ItemStack tabItem, @NotNull final MenuBuilder menu) {
		return Tab.of(name, tabItem, menu);
	}

	public static TabMenu of(@Nullable final String title, @NotNull final Tab... tabs) {
		return new TabMenu(title, Arrays.asList(tabs));
	}

	public static TabMenu of(final @NotNull Tab... tabs) {
		return new TabMenu(Arrays.asList(tabs));
	}
}
