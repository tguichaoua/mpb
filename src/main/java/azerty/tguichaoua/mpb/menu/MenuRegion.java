package azerty.tguichaoua.mpb.menu;

import azerty.tguichaoua.mpb.util.Assert;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public final class MenuRegion implements Iterable<MenuRegion.Location> {

	public static int MIN_X = 0;
	public static int MAX_X = 8;
	public static int MIN_Y = 0;
	public static int MAX_Y = Menu.MAX_ROW_COUNT - 1;

	private final int x, y;
	@Getter final int width, height;

	MenuRegion(final int x, final int y, final int width, final int height) {
		Assert.range(x, MIN_X, MAX_X, "x");
		Assert.range(y, MIN_Y, MAX_Y, "y");
		Assert.range(width, 1, MAX_X - x + 1, "width");
		Assert.range(height, 1, MAX_Y - y + 1, "height");

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	MenuRegion(final MenuRegion region) {
		this(region.x, region.y, region.width, region.height);
	}

	boolean contains(int x, int y) {
		if (x < 0) x += width;
		if (y < 0) y += height;
		return x >= 0 && x < width && y >= 0 && y < height;
	}

	boolean contains(final MenuRegion region) {
		return x <= region.x &&
				y <= region.y &&
				x + width >= region.x + region.width &&
				y + height >= region.y + region.height;
	}

	/**
	 * Converts a local point into an inventory slot id.
	 * @param x x of the local point
	 * @param y y of the local point
	 * @return the corresponding inventory slot id
	 */
	int getSlot(int x, int y) {
		if (x < 0) x += width;
		if (y < 0) y += height;
		return Menu.getSlot(this.y + y, this.x + x);
	}

	/**
	 * Converts a local point into an inventory slot id.
	 * @param loc the local point
	 * @return the corresponding inventory slot id
	 */
	int getSlot(final Location loc) {
		return getSlot(loc.x, loc.y);
	}

	public int getSlotCount() {
		return width * height;
	}

	public int getCenterColumn() {
		return width / 2;
	}

	public int getCenterRow() {
		return height / 2;
	}

	public Location getCenter() {
		return new Location(this, getCenterColumn(), getCenterRow());
	}

	public MenuRegion subRegion(int fromX, int fromY, int toX, int toY) {
		if (fromX < 0) fromX += width;
		if (fromY < 0) fromY += height;
		if (toX < 0) toX += width;
		if (toY < 0) toY += height;

		if (!contains(fromX, fromY) || !contains(toX, toY))
			throw new IllegalArgumentException("The sub region must be contained in the current region.");
		return new MenuRegion(x + fromX, y + fromY, toX - fromX + 1, toY - fromY + 1);
	}

	public MenuRegion subRegion(final Alignment alignment, final int width, final int height) {
		if (width > this.width || height > this.height)
			throw new IllegalArgumentException("The sub region must be contained in the current region.");

		final int x = alignment.isLeft() ? 0 : alignment.isRight() ? this.width - width : (this.width - width) / 2;
		final int y = alignment.isTop() ? 0 : alignment.isBottom() ? this.height - height : (this.height - height) / 2;

		return  new MenuRegion(this.x + x, this.y + y, width, height);
	}

	public MenuRegion row(final int from, final int to) {
		return subRegion(0, from, -1, to);
	}

	public MenuRegion row(final int row) {
		return row(row, row);
	}

	public MenuRegion column(final int from, final int to) {
		return subRegion(from, 0, to, -1);
	}

	public MenuRegion column(final int column) {
		return column(column, column);
	}

	public MenuRegion line(final Direction direction, final int i) {
		final int rc = direction.isPositive() ? i : -1 - i;
		return direction.isUpDown() ? row(rc) : column(rc);
	}

	@NotNull
	@Override
	public Iterator<Location> iterator() {
		return new Iterator<Location>() {
			int x = 0, y = 0;

			@Override
			public boolean hasNext() {
				return x < width && y < height;
			}

			@Override
			public Location next() {
				final Location loc = new Location(MenuRegion.this, x, y);
				x++;
				if (x >= width) {
					x = 0;
					y++;
				}
				return loc;
			}
		};
	}

	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Location {
		final MenuRegion region;
		@Getter final int x, y;

		public int getIndex() {
			return x + y * region.width;
		}

		int getSlot() {
			return region.getSlot(this);
		}
	}

}
