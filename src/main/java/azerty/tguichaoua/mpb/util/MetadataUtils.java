package azerty.tguichaoua.mpb.util;

import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class MetadataUtils {

	public static @Nullable
	MetadataValue get(final @NotNull Metadatable table, final @NotNull String key, final @NotNull Plugin plugin) {
		for (final MetadataValue value : table.getMetadata(key)) {
			if (value.getOwningPlugin() == plugin) return value;
		}
		return null;
	}

	public static <T> T getValue(final @NotNull Metadatable table, final @NotNull String key, final @NotNull Plugin plugin, @NotNull final Class<T> clazz, final T defaultValue) {
		final MetadataValue mv = get(table, key, plugin);
		final Object v = mv != null ? mv.value() : null;
		if (v == null) return defaultValue;
		try {
			return clazz.cast(v);
		} catch (final ClassCastException e) {
			return defaultValue;
		}
	}
}
