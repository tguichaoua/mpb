package azerty.tguichaoua.mpb.util;

public class MathUtils {
	public static <T extends Comparable<T>> T clamp(final T val, final T min, final T max) {
		return val.compareTo(min) < 0 ? min : val.compareTo(max) > 0 ? max : val;
	}

	public static <T extends Comparable<T>, U> U compare(final T a, final T b, final U aLTb, final U aEQb, final U aGTb) {
		final int comp = a.compareTo(b);
		return comp < 0 ? aLTb : comp == 0 ? aEQb : aGTb;
	}
}
