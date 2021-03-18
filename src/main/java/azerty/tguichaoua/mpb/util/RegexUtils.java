package azerty.tguichaoua.mpb.util;

import org.jetbrains.annotations.NotNull;

public class RegexUtils {
	/**
	 * Converts a glob pattern into a Regex pattern.
	 *
	 * @param glob the glob pattern to convert
	 * @return the Regex pattern
	 */
	public static @NotNull String fromGlob(final @NotNull String glob) {
		final StringBuilder out = new StringBuilder("^");
		for (int i = 0; i < glob.length(); ++i) {
			final char c = glob.charAt(i);
			switch (c) {
				case '*':
					out.append(".*");
					break;
				case '?':
					out.append('.');
					break;
				case '.':
					out.append("\\.");
					break;
				case '\\':
					out.append("\\\\");
					break;
				default:
					out.append(c);
			}
		}
		out.append('$');
		return out.toString();
	}
}
