package azerty.tguichaoua.mpb.menu;

public class MenuRenderException extends RuntimeException {
	private static final long serialVersionUID = -8527873681879740177L;

	static final String NOT_ENOUGH_SPACE = "Not enough space to render.";
	static final String OUT_OF_REGION = "This zone is not contained in the rendered region.";

	MenuRenderException(final String message) {
		super(message);
	}
}
