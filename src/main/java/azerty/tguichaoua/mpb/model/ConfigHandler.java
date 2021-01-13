/*
Class based on this tutorial : https://bukkit.gamepedia.com/Configuration_API_Reference
 */

package azerty.tguichaoua.mpb.model;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ConfigHandler {
	private final @NotNull Plugin plugin;
	private final @NotNull String fileName;

	private final File configFile;
	private FileConfiguration fileConfiguration;

	public ConfigHandler(final @NotNull Plugin plugin, final @NotNull String fileName) {
		this.plugin = plugin;
		this.fileName = fileName;
		configFile = new File(plugin.getDataFolder(), fileName);
	}

	/**
	 * Reloads the configs from jar.
	 */
	public void reload() {
		fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
		final InputStream defaultConfigStream = plugin.getResource(fileName);
		if (defaultConfigStream != null) {
			final Reader defConfigStream = new InputStreamReader(defaultConfigStream, StandardCharsets.UTF_8);
			final YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			fileConfiguration.setDefaults(defConfig);
		}
	}

	public FileConfiguration getConfig() {
		if (fileConfiguration == null) {
			this.reload();
		}
		return fileConfiguration;
	}

	/**
	 * Save the current config.
	 */
	public void save() {
		if (fileConfiguration == null || configFile == null)
			return;

		try {
			getConfig().save(configFile);
		} catch (final IOException e) {
			plugin.getLogger().severe(String.format("[%s] Could not save config to %s : %s", plugin.getName(), configFile, e));
		}
	}

	/**
	 * Similar to {@link JavaPlugin#saveDefaultConfig}
	 */
	public void saveDefaultConfig() {
		if (!configFile.exists()) {
			this.plugin.saveResource(fileName, false);
		}
	}
}
