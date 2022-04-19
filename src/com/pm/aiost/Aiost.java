package com.pm.aiost;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.pm.aiost.event.AiostListener;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.effect.group.EffectGroupBuilder;
import com.pm.aiost.item.Items;
import com.pm.aiost.misc.SpigotConfig;
import com.pm.aiost.misc.SpigotConfigManager;
import com.pm.aiost.misc.SpigotLogger;
import com.pm.aiost.misc.command.Commands;
import com.pm.aiost.misc.dataAccess.DataAccess;
import com.pm.aiost.misc.dataAccess.SpigotDatabaseAccess;
import com.pm.aiost.misc.dataAccess.SpigotFileAccess;
import com.pm.aiost.misc.database.DatabaseManager;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.particleEffect.particle.ParticleBuilder;
import com.pm.aiost.misc.registry.AiostRegistry;
import com.pm.aiost.misc.utils.scheduler.AiostScheduler;
import com.pm.aiost.player.PlayerManager;
import com.pm.aiost.player.unlockable.UnlockableManager;
import com.pm.aiost.server.ServerManager;
import com.pm.aiost.server.http.HttpServer;
import com.pm.aiost.server.messaging.AiostPluginMessageListener;
import com.pm.aiost.server.world.WorldManager;

// All in one server technology
public class Aiost extends JavaPlugin {

	private static Aiost plugin;

	@Override
	public void onLoad() {
		plugin = this;
		Logger.setLogger(new SpigotLogger());

		Logger.log("Start loading aisot...");

		AiostRegistry.init();

		Logger.log("Loading aisot complete!");
	}

	@Override
	public void onEnable() {
		Logger.log("Initialize Aiost...");

		linkListener();
		Commands.init();
		SpigotConfigManager.init();
		intDatabase();
		if (!SpigotConfig.HAS_BUNGEE)
			HttpServer.start();
		ServerManager.init();
		AiostPluginMessageListener.init(this);
		new EffectGroupBuilder().loadEffectGroups(SpigotConfigManager.getEffectConfig());
		Items.init(); // TODO test if this in onLoad makes custom Items work better!
		EffectTypes.init();
		ParticleBuilder.registerConfig();
		UnlockableManager.init();
		PlayerManager.registerOnlinePlayer();
		WorldManager.init();
		PlayerManager.enableOnlinePlayer();
		AiostScheduler.init();

		Logger.log("Aiost initialized!");
	}

	@Override
	public void onDisable() {
		Logger.log("Disable Aiost...");

		if (!SpigotConfig.HAS_BUNGEE)
			HttpServer.stop();
		PlayerManager.disableOnlinePlayer();
		WorldManager.terminate();
		SpigotConfigManager.terminate();
		AiostRegistry.terminate();

		Logger.log("Aiost disabled!");
	}

	private void linkListener() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new AiostListener(), this);
	}

	private static void intDatabase() {
		Logger.log("Initialize Database...");

		if (DatabaseManager.initConnection(SpigotConfigManager.getAiostConfig()))
			DataAccess.init(new SpigotDatabaseAccess());
		else
			DataAccess.init(new SpigotFileAccess());

		Logger.log("Database initialized!");
	}

	public static Aiost getPlugin() {
		return plugin;
	}

	public static String getUrl() {
		return SpigotConfigManager.getWebUrl();
	}
}
