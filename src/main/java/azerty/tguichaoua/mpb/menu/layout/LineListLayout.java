package azerty.tguichaoua.mpb.menu.layout;

import azerty.tguichaoua.mpb.menu.Direction;
import azerty.tguichaoua.mpb.menu.MenuRegion;
import azerty.tguichaoua.mpb.menu.MenuRenderer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class LineListLayout<T> extends AbstractListLayout<T> {

	private final @NotNull LineRenderer<T> lineRenderer;
	private final @NotNull Direction direction;

	public LineListLayout(
			@NotNull final ElementProvider<T> list,
			@NotNull final LineRenderer<T> lineRenderer,
			@NotNull final Direction direction
	) {
		super(list);
		this.lineRenderer = lineRenderer;
		this.direction = direction;
	}

	public LineListLayout(
			@NotNull final List<T> list,
			@NotNull final LineRenderer<T> lineRenderer,
			@NotNull final Direction direction
	) {
		this(ElementProvider.of(list), lineRenderer, direction);
	}

	@Override
	protected int getItemPerPage(final MenuRegion region) {
		return direction.isUpDown() ? region.getHeight() : region.getWidth();
	}

	@Override
	protected void fill(final MenuRenderer renderer) {
		for (int i = 0, ii = getItemPerPage(); i < ii; i++) {
			final int idx = getIndexOffset() + i;
			if (idx >= getList().size()) break;
			lineRenderer.render(renderer.ofLine(direction, i), getList().get(idx), idx, i);
		}
	}

	// --- Callback -------------------------------------------------------
	public interface LineRenderer<T> { void render(MenuRenderer renderer, T o, int i, int line); }

}
