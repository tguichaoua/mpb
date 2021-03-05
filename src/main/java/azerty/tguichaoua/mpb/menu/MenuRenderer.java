package azerty.tguichaoua.mpb.menu;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public abstract class MenuRenderer {

	@Getter private final MenuRegion region;
	@Getter private final Player viewer;

	@Getter @Setter private @Nullable String title = null;
	@Getter @Setter private boolean backOnClickOutside = true;

	public abstract void onClose(@NotNull Menu.OnClose onClose);
	protected abstract void put(final int i, final @Nullable ItemStack item, final @Nullable Menu.ButtonAction action);
	protected abstract void putItem(final int i, final @Nullable ItemStack item);

	private int getSlot(final int x, final int y) throws MenuRenderException {
		if (!region.contains(x, y)) throw new MenuRenderException(MenuRenderException.OUT_OF_REGION);
		return region.getSlot(x, y);
	}

	private void _fill(
			final @NotNull MenuRegion region,
			final @NotNull Function<MenuRegion.Location, @Nullable ItemStack> itemProvider,
			final @Nullable Function<MenuRegion.Location, Menu.@Nullable ButtonAction> actionProvider
	) {
		region.forEach(
				actionProvider == null ?
						l -> put(l.getSlot(), itemProvider.apply(l), null) :
						l -> {
							final ItemStack item = itemProvider.apply(l);
							put(l.getSlot(), item, item == null ? null : actionProvider.apply(l));
						}
		);
	}

	// --- Public API ------------------------------------------------------------------------------------------
	public final void requireSpace(final int width, final int height) throws MenuRenderException {
		if (region.getWidth() < width || region.getHeight() < height)
			throw new MenuRenderException(MenuRenderException.NOT_ENOUGH_SPACE);
	}

	public final void set(final int x, final int y, final @NotNull ItemStack item, final @Nullable Menu.ButtonAction action) {
		put(getSlot(x, y), item, action);
	}

	public final void set(final int x, final int y, final @NotNull ItemStack item) {
		put(getSlot(x, y), item, null);
	}

	public final void setItem(final int x, final int y, final @Nullable ItemStack item) {
		putItem(getSlot(x, y), item);
	}

	public final void setItem(final int x, final int y) {
		putItem(getSlot(x, y), null);
	}

	public final void clear(final int x, final int y) {
		put(getSlot(x, y), null, null);
	}

	public final void clear(@NotNull final MenuRegion region) {
		if (!this.region.contains(region)) throw new MenuRenderException(MenuRenderException.OUT_OF_REGION);
		region.forEach(l -> put(l.getSlot(), null, null));
	}

	public final void clear(@NotNull final MenuRegion region, @NotNull final Predicate<MenuRegion.Location> predicate) {
		if (!this.region.contains(region)) throw new MenuRenderException(MenuRenderException.OUT_OF_REGION);
		region.forEach(l -> { if(predicate.test(l)) put(l.getSlot(), null, null); });
	}

	public final void fillAll(
			final @NotNull Function<MenuRegion.Location, @Nullable ItemStack> itemProvider,
			final @Nullable Function<MenuRegion.Location, Menu.@Nullable ButtonAction> actionProvider
	)  {
		_fill(region, itemProvider, actionProvider);
	}

	public final void fillAll(
			final @NotNull Function<MenuRegion.Location, @Nullable ItemStack> itemProvider
	)  {
		_fill(region, itemProvider, null);
	}

	public final void fillAll(final @NotNull ItemStack item) {
		fillAll(l -> item);
	}

	public final void fill(
			final @NotNull MenuRegion region,
			final @NotNull Function<MenuRegion.Location, @Nullable ItemStack> itemProvider,
			final @Nullable Function<MenuRegion.Location, Menu.@Nullable ButtonAction> actionProvider
	) {
		if (!this.region.contains(region)) throw new MenuRenderException(MenuRenderException.OUT_OF_REGION);
		_fill(region, itemProvider, actionProvider);
	}

	public final void fill(
			final @NotNull MenuRegion region,
			final @NotNull Function<MenuRegion.Location, @Nullable ItemStack> itemProvider
	) throws MenuRenderException {
		if (!this.region.contains(region)) throw new MenuRenderException(MenuRenderException.OUT_OF_REGION);
		_fill(region, itemProvider, null);
	}

	public final void fill(final @NotNull MenuRegion region, final @NotNull ItemStack item) {
		fill(region, l -> item);
	}

	public final void fillItem(
			final @NotNull MenuRegion region,
			final @NotNull Function<MenuRegion.Location, @Nullable ItemStack> itemProvider
	) throws MenuRenderException {
		if (!this.region.contains(region)) throw new MenuRenderException(MenuRenderException.OUT_OF_REGION);
		region.forEach(l -> putItem(l.getSlot(), itemProvider.apply(l)));
	}

	public final void fillItem(final @NotNull MenuRegion region, final @Nullable ItemStack item) throws MenuRenderException {
		fillItem(region, l -> item);
	}

	public final void fillItem(final @NotNull MenuRegion region) {
		fillItem(region, (ItemStack) null);
	}

	/**
	 * @deprecated use {@link MenuRenderer#of(MenuRegion)} instead.
	 */
	@Deprecated
	public final MenuRenderer render(@NotNull final MenuRegion region) {
		if (!this.region.contains(region)) throw new MenuRenderException(MenuRenderException.OUT_OF_REGION);
		return new Proxy(region, this);
	}

	/**
	 * @deprecated use {@link MenuRenderer#of(int, int, int, int)} instead.
	 */
	@Deprecated
	public final MenuRenderer render(final int fromX, final int fromY, final int toX, final int toY) {
		return new Proxy(region.subRegion(fromX, fromY, toX, toY), this);
	}

	/**
	 * @deprecated use {@link MenuRenderer#of(Alignment, int, int)} instead.
	 */
	@Deprecated
	public final MenuRenderer render(@NotNull final Alignment alignment, final int width, final int height) {
		return new Proxy(region.subRegion(alignment, width, height), this);
	}

	public final Data render(final @NotNull MenuBuilder builder) {
		final MenuRenderer proxy = new Proxy(region, this);
		builder.render(proxy);
		return Data.of(proxy);
	}

	public final Data render(final @NotNull MenuBuilder builder, final @NotNull MenuRegion region) {
		final MenuRenderer proxy = of(region);
		builder.render(proxy);
		return Data.of(proxy);
	}

	public final MenuRenderer of(@NotNull final MenuRegion region) {
		if (!this.region.contains(region)) throw new MenuRenderException(MenuRenderException.OUT_OF_REGION);
		return new Proxy(region, this);
	}

	public final MenuRenderer of(final int fromX, final int fromY, final int toX, final int toY) {
		return new Proxy(region.subRegion(fromX, fromY, toX, toY), this);
	}

	public final MenuRenderer of(@NotNull final Alignment alignment, final int width, final int height) {
		return new Proxy(region.subRegion(alignment, width, height), this);
	}

	public final MenuRenderer ofRow(final int from, final int to) {
		return new Proxy(region.row(from, to), this);
	}

	public final MenuRenderer ofRow(final int row) {
		return new Proxy(region.row(row), this);
	}

	public final MenuRenderer ofColumn(final int from, final int to) {
		return new Proxy(region.column(from, to), this);
	}

	public final MenuRenderer ofColumn(final int column) {
		return new Proxy(region.column(column), this);
	}

	public final MenuRenderer ofLine(final Direction direction, final int i) {
		return new Proxy(region.line(direction, i), this);
	}

	// --- Private Class --------------------------------------------------------------------------------

	@Getter
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public static final class Data {
		private final @Nullable String title;
		private final boolean backOnClickOutside;

		private static Data of(final MenuRenderer renderer) {
			return new Data(renderer.getTitle(), renderer.isBackOnClickOutside());
		}
	}

	static class Main extends MenuRenderer {

		@Getter(AccessLevel.PACKAGE)
		private final Set<Integer> isItemSlots = new HashSet<>();
		@Getter(AccessLevel.PACKAGE)
		private final Map<Integer, ItemStack> items = new HashMap<>();
		@Getter(AccessLevel.PACKAGE)
		private final Map<Integer, Menu.ButtonAction> actions = new HashMap<>();
		@Getter(AccessLevel.PACKAGE)
		private final List<Menu.OnClose> onCloseCallbacks = new ArrayList<>();

		Main(final MenuRegion region, final Player viewer) {
			super(region, viewer);
		}

		public void clear() {
			isItemSlots.clear();
			items.clear();
			actions.clear();
			onCloseCallbacks.clear();
		}

		@Override
		public void onClose(final Menu.@NotNull OnClose onClose) {
			this.onCloseCallbacks.add(onClose);
		}

		@Override
		protected void put(final int i, @Nullable final ItemStack item, final Menu.@Nullable ButtonAction action) {
			isItemSlots.remove(i);
			items.put(i, item);
			actions.put(i, action);
		}

		@Override
		protected void putItem(final int i, @Nullable final ItemStack item) {
			isItemSlots.add(i);
			items.put(i, item);
			actions.remove(i);
		}
	}

	private static class Proxy extends MenuRenderer {

		private final MenuRenderer target;

		Proxy(final MenuRegion region, final MenuRenderer target) {
			super(region, target.viewer);
			this.target = target;
		}

		@Override
		public void onClose(final Menu.@NotNull OnClose onClose) {
			target.onClose(onClose);
		}

		@Override
		protected void put(final int i, @Nullable final ItemStack item, final Menu.@Nullable ButtonAction action) {
			target.put(i, item, action);
		}

		@Override
		protected void putItem(final int i, @Nullable final ItemStack item) {
			target.putItem(i, item);
		}
	}
}
