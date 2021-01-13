package azerty.tguichaoua.mpb.util;

import java.lang.reflect.Array;

public class ArrayUtils {

	public static <T> T[] nonNull(final T[] array) {
		int nullCount = 0;
		for (final T o : array) if (o == null) nullCount++;

		final T[] nonNulls = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length - nullCount);
		for (int i = 0, j = 0; i < array.length; i++) if (array[i] != null) nonNulls[j++] = array[i];
		return nonNulls;
	}
}
