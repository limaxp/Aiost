package com.pm.aiost.server;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import com.pm.aiost.Aiost;
import com.pm.aiost.event.eventHandler.EventHandler;
import com.pm.aiost.event.eventHandler.EventHandlerManager;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.ServerWorld;
import com.pm.aiost.server.world.creation.WorldBuilder;

public class ServerManager {

	public static final ServerType DEFAULT_SERVER_TYPE = ServerType.SURVIVAL;

	private static Server server;

	public static void init() {
		ServerManager.server = new Server(Bukkit.getServer());
		initServerType(DEFAULT_SERVER_TYPE);
	}

	private static EventHandler initServerType(ServerType type) {
		EventHandler eventHandler = ServerTypeEventHandler.get(type);
		server.setType(type);
		EventHandlerManager.init(eventHandler);
		return eventHandler;
	}

	public static void setServerType(ServerType type) {
		EventHandler eventHandler = initServerType(type);
		for (ServerWorld serverWorld : ServerWorld.getWorlds()) {
			if (serverWorld.hasDefaultEventHandler()) {
				serverWorld.setEventHandler(eventHandler, true);

				for (ServerPlayer serverPlayer : serverWorld.getServerPlayer()) {
					EventHandler playerEventHandler = serverPlayer.getEventHandler();
					if (playerEventHandler != eventHandler) {
						EventHandler regionEventHandler = serverPlayer.getRegion().getEventHandler();
						if (playerEventHandler != regionEventHandler)
							serverPlayer.setEventHandler(regionEventHandler);
					}
				}
			}
		}
	}

	public static void reload() {
		BukkitRunnable reloadTask = new BukkitRunnable() {
			@Override
			public void run() {
				if (WorldBuilder.hasUnloadingWorlds()) {
					Logger.log("ServerManager: Server has unloading worlds! Will reload soon...");
					return;
				}
				Logger.log("ServerManager: Server is reloading...");
				Bukkit.reload();
				cancel();
			}
		};
		reloadTask.runTaskTimer(Aiost.getPlugin(), 0, 10);
	}

	public static void restart() {
		Bukkit.shutdown();
		int ram = server.getType().ram;
		try {
			Runtime.getRuntime().exec("cmd.exe /c cd \"" + Bukkit.getWorldContainer().getAbsolutePath()
					+ "\" & start java -Xms" + ram + "M -Xmx" + ram
					+ "M -XX:+AlwaysPreTouch -XX:+DisableExplicitGC -XX:+UseG1GC -XX:+UnlockExperimentalVMOptions -XX:MaxGCPauseMillis=45 -XX:TargetSurvivorRatio=90 -XX:G1NewSizePercent=50 -XX:G1MaxNewSizePercent=80 -XX:InitiatingHeapOccupancyPercent=10 -XX:G1MixedGCLiveThresholdPercent=50 -XX:+AggressiveOpts -jar spigot-1.15.2.jar");
		} catch (IOException e) {
			Logger.err("ServerManager: Error! Could not restart server!", e);
		}
	}

	public static Server getServer() {
		return server;
	}

	public static String getIp() {
		return Bukkit.getServer().getIp();
	}

	public static int getPort() {
		return Bukkit.getServer().getPort();
	}
}