package azerty.tguichaoua.mpb;

import azerty.tguichaoua.mpb.menu.MenuListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public abstract class MyPlugin extends JavaPlugin implements Listener {

	@Override
	public final void onLoad() {
		onPluginLoad();
	}

	@Override
	public final void onEnable() {
		registerEvent(this);
		registerEvent(new MenuListener(this));
		onPluginStart();
	}

	@Override
	public final void onDisable() {
		onPluginStop();
	}

	protected void onPluginLoad() { }
	protected void onPluginStart() { }
	protected void onPluginStop() { }

	// Utils
	public final void registerEvent(final Listener listener) {
		getServer().getPluginManager().registerEvents(listener, this);
	}

	public final File getDataFolder(final String child) {
		return new File(getDataFolder(), child);
	}
}