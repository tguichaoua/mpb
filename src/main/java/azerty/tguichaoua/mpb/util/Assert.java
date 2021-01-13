package azerty.tguichaoua.mpb.util;

public class Assert {
	public static void nonNull(final Object o, final String text) {
		if (o == null) throw new RuntimeException(text);
	}

	public static void argNonNull(final Object arg, final String argName) {
		if (arg == null) throw new IllegalArgumentException(argName + " must not be null.");
	}

	public static void range(final int value, final int min, final int max, final String argName) {
		if (value < min || value > max)
			throw new IllegalArgumentException(
					String.format(
							"expecting a value between %d and %d for %s, got %d",
							min, max, argName, value
					)
			);
	}
}
