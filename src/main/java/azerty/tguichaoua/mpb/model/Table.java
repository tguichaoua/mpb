package azerty.tguichaoua.mpb.model;

import com.google.gson.annotations.JsonAdapter;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@JsonAdapter(TableTypeAdapter.class)
public final class Table {

	@Getter(AccessLevel.PACKAGE)
	private final Map<String, Object> values = new HashMap<>();

	public Table() {
	}

	public Table(final Map<String, Object> map) {
		for (final Map.Entry<String, Object> e : map.entrySet()) {
			if (hasValidType(e.getValue()))
				values.put(e.getKey(), e.getValue());
		}
	}

	public static boolean hasValidType(final Object o) {
		if (o == null) return false;
		final Class<?> clazz = o.getClass();
		return String.class.isAssignableFrom(clazz) ||
				Boolean.class.isAssignableFrom(clazz) ||
				Number.class.isAssignableFrom(clazz);
	}

	private <T> T get(final Class<T> clazz, final String key, final T defaultValue) {
		final Object o = values.getOrDefault(key, defaultValue);
		try {
			return clazz.cast(o);
		} catch (final ClassCastException e) {
			return defaultValue;
		}
	}

	public boolean isSet(final String key) {
		return values.containsKey(key);
	}

	public boolean isEmpty() {
		return values.isEmpty();
	}

	public void remove(final String key) {
		values.remove(key);
	}

	public void clear() {
		values.clear();
	}

	public Set<Map.Entry<String, Object>> entrySet() {
		return values.entrySet();
	}

	// Setter

	public void set(final String key, final String value) {
		values.put(key, value);
	}

	public void set(final String key, final boolean value) {
		values.put(key, value);
	}

	public void set(final String key, final long value) {
		values.put(key, value);
	}

	public void set(final String key, final int value) {
		values.put(key, value);
	}

	public void set(final String key, final double value) {
		values.put(key, value);
	}

	public void set(final String key, final float value) {
		values.put(key, value);
	}

	// Getter

	public String getString(final String key, final String defaultValue) {
		return get(String.class, key, defaultValue);
	}

	public boolean getBoolean(final String key, final boolean defaultValue) {
		return get(Boolean.class, key, defaultValue);
	}

	public long getLong(final String key, final long defaultValue) {
		final Number n = get(Number.class, key, null);
		return n == null || (n.doubleValue() % 1) != 0 ? defaultValue : n.longValue();
	}

	public int getInt(final String key, final int defaultValue) {
		try {
			return Math.toIntExact(getLong(key, defaultValue));
		} catch (final ArithmeticException e) {
			return defaultValue;
		}
	}

	public double getDouble(final String key, final double defaultValue) {
		final Number n = get(Number.class, key, null);
		return n == null ? defaultValue : n.doubleValue();
	}

	public float getFloat(final String key, final float defaultValue) {
		final Number n = get(Number.class, key, null);
		return n == null ? defaultValue : n.floatValue();
	}

}
