package com.pm.aiost;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.pm.aiost.effect.EffectTypes;
import com.pm.aiost.effect.group.EffectGroupBuilder;
import com.pm.aiost.item.Items;
import com.pm.aiost.misc.SpigotConfig;
import com.pm.aiost.misc.SpigotConfigManager;
import com.pm.aiost.misc.SpigotLogger;
import com.pm.aiost.misc.command.Commands;
import com.pm.aiost.misc.dataAccess.DataAccess;
import com.pm.aiost.misc.dataAccess.SpigotDatabaseAccess;
import com.pm.aiost.misc.dataAccess.SpigotFileAccess;
import com.pm.aiost.misc.database.DatabaseManager;
import com.pm.aiost.misc.event.AiostListener;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenuCustomAnimationHandler;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenuHandler;
import com.pm.aiost.misc.menu.menus.GameJoinMenu;
import com.pm.aiost.misc.particle.EntityParticleManager;
import com.pm.aiost.misc.particle.ParticleBuilder;
import com.pm.aiost.misc.registry.AiostRegistry;
import com.pm.aiost.misc.scoreboard.scoreboards.LobbyScoreboard;
import com.pm.aiost.misc.server.ServerManager;
import com.pm.aiost.misc.server.messaging.AiostPluginMessageListener;
import com.pm.aiost.misc.server.messaging.ServerDataRequester;
import com.pm.aiost.misc.utils.scheduler.AiostScheduler;
import com.pm.aiost.player.PlayerManager;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.player.handler.TPSOptimizer;
import com.pm.aiost.player.unlockable.UnlockableManager;
import com.pm.aiost.server.http.HttpServer;
import com.pm.aiost.world.WorldManager;

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

		getServer().getPluginManager().registerEvents(new AiostListener(), this);
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
		startScheduler();

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

	private static void intDatabase() {
		Logger.log("Initialize Database...");

		if (DatabaseManager.initConnection(SpigotConfigManager.getAiostConfig()))
			DataAccess.init(new SpigotDatabaseAccess());
		else
			DataAccess.init(new SpigotFileAccess());

		Logger.log("Database initialized!");
	}

	private static void startScheduler() {
		// 0.25 sec
		new BukkitRunnable() {
			@Override
			public void run() {
				for (ServerPlayer serverPlayer : ServerPlayer.getOnlinePlayer())
					serverPlayer.spawnParticles(); // TODO: Check visibility an render only to self!
				EntityParticleManager.render();
				WorldManager.updateWorlds();
			}
		}.runTaskTimer(Aiost.getPlugin(), 0, 5);

		// 0.5 sec
		new BukkitRunnable() {
			@Override
			public void run() {
				InventoryMenuHandler.animateMenusSchedulerTick();
				InventoryMenuCustomAnimationHandler.animateMenusSchedulerTick();
			}
		}.runTaskTimer(Aiost.getPlugin(), 0, 10);

		// 1 sec
		new BukkitRunnable() {
			@Override
			public void run() {
				AiostScheduler.update();
				for (ServerPlayer serverPlayer : ServerPlayer.getOnlinePlayer())
					serverPlayer.update();
				TPSOptimizer.update();
			}
		}.runTaskTimer(Aiost.getPlugin(), 0, 20);

		// 5 sec
		new BukkitRunnable() {
			@Override
			public void run() {
				if (SpigotConfig.HAS_BUNGEE)
					ServerDataRequester.requestData();
				else
					GameJoinMenu.updateMenus();
				LobbyScoreboard.updatePlayerSize();
			}
		}.runTaskTimer(Aiost.getPlugin(), 0, 100);
	}

	public static Aiost getPlugin() {
		return plugin;
	}

	public static String getUrl() {
		return SpigotConfigManager.getWebUrl();
	}
}
