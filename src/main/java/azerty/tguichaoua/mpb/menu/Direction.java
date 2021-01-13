package azerty.tguichaoua.mpb.menu;

public enum Direction {
	UP, DOWN, RIGHT, LEFT;
	public boolean isUpDown() { return this == UP || this == DOWN; }
	public boolean isPositive() { return this == DOWN || this == RIGHT; }
}
