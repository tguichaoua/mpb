package azerty.tguichaoua.mpb.menu.layout;

import azerty.tguichaoua.mpb.menu.Menu;
import azerty.tguichaoua.mpb.menu.MenuRegion;
import azerty.tguichaoua.mpb.menu.MenuRenderer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public final class ListLayout<T> extends AbstractListLayout<T> {

	private final @NotNull Adapter<T> adapter;
	private final @Nullable OnItemClick<T> onItemClick;

	public ListLayout(
			@NotNull final ElementProvider<T> list,
			@NotNull final Adapter<T> adapter,
			@Nullable final OnItemClick<T> onItemClick
	) {
		super(list);
		this.adapter = adapter;
		this.onItemClick = onItemClick;
	}

	public ListLayout(
			@NotNull final List<T> list,
			@NotNull final Adapter<T> adapter,
			@Nullable final OnItemClick<T> onItemClick
	) {
		this(ElementProvider.of(list), adapter, onItemClick);
	}

	@Override
	protected int getItemPerPage(final MenuRegion region) {
		return region.getSlotCount();
	}

	@Override
	protected void fill(final MenuRenderer renderer) {
		final ElementProvider<T> list = getList();
		renderer.fillAll(
				l -> {
					final int i = l.getIndex() + getIndexOffset();
					return i < list.size() ? adapter.apply(list.get(i)) : null;
				},
				onItemClick == null ?
						null :
						l -> {
							final int i = l.getIndex() + getIndexOffset();
							return i < list.size() ?
									(e -> onItemClick.accept(new ItemClickEvent<>(e.getMenu(), e.getClick(), list.get(i), i))) :
									null;
						}
		);
	}

	// --- Callbacks --------------------------------------------------------------------
	@FunctionalInterface
	public interface Adapter<T> extends Function<T, @NotNull ItemStack> { }

	@FunctionalInterface
	public interface OnItemClick<T> extends Consumer<ItemClickEvent<T>> { }

	@Getter
	@RequiredArgsConstructor
	public static final class ItemClickEvent<T> {
		private final @NotNull Menu menu;
		private final @NotNull ClickType click;
		private final T value;
		private final int index;
	}
}
