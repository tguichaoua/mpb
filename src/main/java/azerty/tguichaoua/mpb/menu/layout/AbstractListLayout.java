package azerty.tguichaoua.mpb.menu.layout;

import azerty.tguichaoua.mpb.menu.Menu;
import azerty.tguichaoua.mpb.menu.MenuRegion;
import azerty.tguichaoua.mpb.menu.MenuRenderer;
import azerty.tguichaoua.mpb.util.MathUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractListLayout<T> {

	@Getter(AccessLevel.PROTECTED) private final @NotNull ElementProvider<T> list;

	@Getter	private int itemPerPage = 0;
	@Getter private int lastPage = 0;
	@Getter private int indexOffset = 0;
	@Getter @Setter	private int currentPage = 0;

	private @Nullable Integer target = null;

	public final boolean hasPreviousPage() {
		return currentPage != 0;
	}

	public final boolean hasNextPage() {
		return currentPage != lastPage;
	}

	public final void nextPage() {
		currentPage++;
	}

	public final void previousPage() {
		currentPage--;
	}

	public final boolean isOnTheCurrentPage(final int index) {
		return index >= indexOffset && index < indexOffset + itemPerPage;
	}

	public final int pageOf(final int index) {
		return index / itemPerPage;
	}

	public final void openOn(final int index) {
		target = index;
	}

	public final void previousPageButton(final Menu.ButtonClickEvent event) {
		previousPage();
		event.getMenu().redraw();
	}

	public final void nextPageButton(final Menu.ButtonClickEvent event) {
		nextPage();
		event.getMenu().redraw();
	}

	public final void update(final MenuRegion region) {
		itemPerPage = getItemPerPage(region);
		final int size = list.size();
		lastPage = size == 0 ? 0 : (size - 1) / itemPerPage;

		if (target != null) {
			currentPage = pageOf(target);
			target = null;
		}
		
		currentPage = MathUtils.clamp(currentPage, 0, lastPage);
		indexOffset = currentPage * itemPerPage;
	}

	public final void update(final MenuRenderer renderer) {
		update(renderer.getRegion());
	}

	public void render(final MenuRenderer renderer) {
		update(renderer);
		fill(renderer);
	}

	protected abstract int getItemPerPage(MenuRegion region);

	protected abstract void fill(MenuRenderer renderer);

	// --- Data interfaces / classes ---------------------------------------------------------------
	public interface ElementProvider<T> {
		T get(int i);

		int size();

		static <T> ElementProvider<T> of(final List<T> list) {
			return new ListProvider<>(list);
		}

		/**
		 * Values from {@code from} (include) to {@code to} (exclude).
		 * @param from first number of the provider
		 * @param to last number (excluded) of the provider
		 * @return an {@link ElementProvider} for number from {@code from} to {@code to}
		 */
		static ElementProvider<Integer> range(final int from, final int to) {
			if (from > to) throw new IllegalArgumentException("to must be greater than from.");
			return new RangeProvider(from, to - from);
		}

		static ElementProvider<Integer> range(final int from) {
			return new RangeProvider(from, Integer.MAX_VALUE);
		}
	}

	@RequiredArgsConstructor
	private final static class ListProvider<T> implements ElementProvider<T> {
		private final List<T> list;

		@Override
		public T get(final int i) { return list.get(i); }

		@Override
		public int size() {	return list.size(); }
	}

	@RequiredArgsConstructor
	private final static class RangeProvider implements ElementProvider<Integer> {
		private final int from, size;

		@Override
		public Integer get(final int i) {
			return from + i;
		}

		@Override
		public int size() {
			return size;
		}
	}
}
