package azerty.tguichaoua.mpb.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CommandExceptionMessageSupplier {
	/**
	 * Returns the message that should be used for the provided key.
	 * If the returned value is null, the default message is used.
	 *
	 * @param key the key
	 * @return the key's message
	 */
	@Nullable String get(@NotNull String key);
}
