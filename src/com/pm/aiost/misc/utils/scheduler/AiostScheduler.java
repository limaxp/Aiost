package com.pm.aiost.misc.utils.scheduler;

import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;

import com.pm.aiost.Aiost;
import com.pm.aiost.collection.list.UnorderedIdentityArrayList;
import com.pm.aiost.misc.SpigotConfig;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenuCustomAnimationHandler;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenuHandler;
import com.pm.aiost.misc.menu.menus.GameJoinMenu;
import com.pm.aiost.misc.particleEffect.EntityParticleManager;
import com.pm.aiost.misc.scoreboard.scoreboards.LobbyScoreboard;
import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.player.handler.TPSOptimizer;
import com.pm.aiost.server.messaging.ServerDataRequester;
import com.pm.aiost.server.world.WorldManager;

public class AiostScheduler {

	private static final List<Runnable> TASKS = new UnorderedIdentityArrayList<>();
	private static final List<Runnable> REMOVED_TASKS = new UnorderedIdentityArrayList<>();
	private static final TimeBuffer TIMED_BUFFER = new TimeBuffer();

	public static void init() {
		scheduler5Tick();
		scheduler10Tick();
		scheduler20Tick();
		scheduler100Tick();
	}

	private static void scheduler5Tick() { // 0.25 sec
		new BukkitRunnable() {
			@Override
			public void run() {
				for (ServerPlayer serverPlayer : ServerPlayer.getOnlinePlayer())
					serverPlayer.spawnParticles(); // TODO: Check visibility an render only to self!
				EntityParticleManager.render();
				WorldManager.updateWorlds();
			}
		}.runTaskTimer(Aiost.getPlugin(), 0, 5);
	}

	private static void scheduler10Tick() { // 0.5 sec
		new BukkitRunnable() {
			@Override
			public void run() {
				InventoryMenuHandler.animateMenusSchedulerTick();
				InventoryMenuCustomAnimationHandler.animateMenusSchedulerTick();
			}
		}.runTaskTimer(Aiost.getPlugin(), 0, 10);
	}

	private static void scheduler20Tick() { // 1 sec
		new BukkitRunnable() {
			@Override
			public void run() {
				update();
				for (ServerPlayer serverPlayer : ServerPlayer.getOnlinePlayer())
					serverPlayer.update();
				TPSOptimizer.update();
			}
		}.runTaskTimer(Aiost.getPlugin(), 0, 20);
	}

	private static void scheduler100Tick() { // 5 sec
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

	private static void update() {
		if (!REMOVED_TASKS.isEmpty()) {
			for (Runnable runnable : REMOVED_TASKS) {
				if (TASKS.remove(runnable))
					Logger.warn("AiostScheduler: Tried to remove task that did not exist!");
			}
		}
		TIMED_BUFFER.update(NMS.getMinecraftServerTick());
		for (int i = 0; i < TASKS.size(); i++)
			TASKS.get(i).run();
	}

	public static void runTask(Runnable task) {
		TASKS.add(task);
	}

	public static void stopTask(Runnable task) {
		REMOVED_TASKS.add(task);
	}

	// TODO TIMED_BUFFER has some bug!
	public static void runTaskLater(int time, Runnable task) {
		TIMED_BUFFER.add(NMS.getMinecraftServerTick() + time, task);
	}

	@FunctionalInterface
	public interface AiostTask extends Runnable {

		public default void start() {
			runTask(this);
		}

		public default void stop() {
			stopTask(this);
		}
	}
}