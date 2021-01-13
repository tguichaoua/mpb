package azerty.tguichaoua.mpb.menu;

public enum Alignment {

	TOP_LEFT,
	TOP_CENTER,
	TOP_RIGHT,
	MIDDLE_LEFT,
	MIDDLE_CENTER,
	MIDDLE_RIGHT,
	BOTTOM_LEFT,
	BOTTOM_CENTER,
	BOTTOM_RIGHT;

	public boolean isTop() { return this == TOP_LEFT || this == TOP_CENTER || this == TOP_RIGHT; }
	public boolean isMiddle() { return this == MIDDLE_LEFT || this == MIDDLE_CENTER || this == MIDDLE_RIGHT; }
	public boolean isBottom() { return this == BOTTOM_LEFT || this == BOTTOM_CENTER || this == BOTTOM_RIGHT; }
	public boolean isLeft() { return this == TOP_LEFT || this == MIDDLE_LEFT || this == BOTTOM_LEFT; }
	public boolean isCenter() { return this == TOP_CENTER || this == MIDDLE_CENTER || this == BOTTOM_CENTER; }
	public boolean isRight() { return this == TOP_RIGHT || this == MIDDLE_RIGHT || this == BOTTOM_RIGHT; }

}
