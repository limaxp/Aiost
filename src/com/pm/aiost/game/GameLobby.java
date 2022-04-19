package com.pm.aiost.game;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.DARK_RED;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;
import static com.pm.aiost.misc.utils.ChatColor.RED;
import static com.pm.aiost.misc.utils.ChatColor.YELLOW;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.pm.aiost.Aiost;
import com.pm.aiost.event.eventHandler.EventHandler;
import com.pm.aiost.event.eventHandler.handler.LobbyEventHandler;
import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.misc.menu.menus.GameLobbyMenu;
import com.pm.aiost.misc.scoreboard.scoreboards.GameScoreboard;
import com.pm.aiost.player.ServerPlayer;

public class GameLobby implements AutoCloseable, LobbyEventHandler {

	private static final byte DEFAULT_TIME_TO_START = 20; // in sec

	private Game game;
	private int timeToStart;
	private BukkitRunnable scheduler;
	private GameLobbyMenu menu;
	private BossBar bossBar;

	public GameLobby() {
	}

	public GameLobby(Game game) {
		this.game = game;
		timeToStart = DEFAULT_TIME_TO_START;
		init();
	}

	protected void init() {
		menu = new GameLobbyMenu(this);
		game.getScoreboard().createLobbySidebar(game);
		bossBar = Bukkit.createBossBar(DARK_RED + BOLD + game.getType().name, BarColor.RED, BarStyle.SOLID);
		scheduler = startScheduler();
	}

	@Override
	public final void close() {
		stopScheduler();
		game.close();
	}

	private final BukkitRunnable startScheduler() {
		BukkitRunnable scheduler = new BukkitRunnable() {
			@Override
			public void run() {
				tick();
			}
		};
		scheduler.runTaskTimer(Aiost.getPlugin(), 0, 20);
		return scheduler;
	}

	private final void stopScheduler() {
		if (!scheduler.isCancelled())
			scheduler.cancel();
	}

	protected void tick() {
		bossBar.setProgress((double) timeToStart / DEFAULT_TIME_TO_START);

		if (timeToStart < 11) {
			game.getScoreboard().setLobbyTime(RED + timeToStart);
			if (timeToStart < 4)
				game.broadcastSubtitle(RED + timeToStart, 10, 70, 20);
			else if (timeToStart < 7)
				game.broadcastSubtitle(YELLOW + timeToStart, 10, 70, 20);
			else
				game.broadcastSubtitle(GREEN + timeToStart, 10, 70, 20);
		} else if (timeToStart < 31)
			game.getScoreboard().setLobbyTime(YELLOW + timeToStart);
		else
			game.getScoreboard().setLobbyTime(GREEN + timeToStart);

		if (timeToStart == 0)
			tryStartGame();
		else if (timeToStart % 10 == 0)
			game.broadcastActionBar(YELLOW + "Game start in " + timeToStart + " seconds");

		timeToStart--;
	}

	public final void tryStartGame() {
		if (hasEnoughPlayer())
			startGame();
		else
			resetTimer(RED + "Game waiting for more player... " + game.getPlayer().size() + "/" + game.getMinPlayer());
	}

	public final boolean hasEnoughPlayer() {
		return game.getPlayer().size() >= game.getMinPlayer();
	}

	public final boolean hasMaxPlayer() {
		return game.getPlayer().size() >= game.getMaxPlayer();
	}

	public final void resetTimer(String msg) {
		timeToStart = DEFAULT_TIME_TO_START + 1;
		game.broadcastActionBar(msg);
	}

	private final void startGame() {
		stopScheduler();
		game.getScoreboard().removeSidebar();
		game.getRegion().setEventHandler(game, false);
		List<ServerPlayer> playerList = game.getPlayer();
		BossBar gameBossbar = game.getBossBar();
		for (int i = 0; i < playerList.size(); i++) {
			ServerPlayer serverPlayer = playerList.get(i);
			serverPlayer.setEventHandlerSilent(game);
			serverPlayer.resetStats();
			Player player = serverPlayer.player;
			bossBar.removePlayer(player);
			gameBossbar.addPlayer(player);
			player.closeInventory();
		}
		game.start();
	}

	@Override
	public void onPlayerJoin(ServerPlayer serverPlayer) {
		if (serverPlayer.getGameData().game != game)
			game.addPlayer(serverPlayer);
		Player player = serverPlayer.player;
		GameScoreboard scoreboard = game.getScoreboard();
		scoreboard.setLobbyPlayerSize(game.getPlayer().size(), game.getMaxPlayer());
		scoreboard.addPlayer(player);
		bossBar.addPlayer(player);
		game.setPlayerList(serverPlayer);
		getMenu().open(player);

		if (hasMaxPlayer() && timeToStart > 10)
			timeToStart = 10;
	}

	@Override
	public void onPlayerQuit(ServerPlayer serverPlayer, QuitReason reason) {
		Player player = serverPlayer.player;
		bossBar.removePlayer(player);
		game.removePlayerList(serverPlayer);
		if (reason != QuitReason.DISABLE) {
			GameScoreboard scoreboard = game.getScoreboard();
			scoreboard.removePlayer(player);
			if (game.getPlayer().size() == 1)
				close();
			else {
				scoreboard.setLobbyPlayerSize(game.getPlayer().size(), game.getMaxPlayer());
				game.removePlayer(serverPlayer);
			}
		}
	}

	@Override
	public void load(ConfigurationSection section) {
		game = (Game) EventHandler.create(section.getString("game"));
		game.loadBase(section);
		timeToStart = section.getInt("timeToStart");
		init();
	}

	@Override
	public void save(ConfigurationSection section) {
		if (scheduler.isCancelled())
			return;
		section.set("game", game.getEventHandlerName());
		game.save(section);
		section.set("timeToStart", timeToStart);
	}

	@Override
	public final Menu getMenu() {
		return menu;
	}

	@Override
	public String getEventHandlerName() {
		return "GameLobby";
	}

	public final Game getGame() {
		return game;
	}

	public final int getTimeToStart() {
		return timeToStart;
	}

	@Override
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onEntityShootBow(EntityShootBowEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onPlayerDropItem(ServerPlayer serverPlayer, PlayerDropItemEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onEntityPickupItem(EntityPickupItemEvent event) {
		event.setCancelled(true);
	}

	@Override
	public final boolean canModifyWorld(ServerPlayer serverPlayer) {
		return false;
	}

	@Override
	public final boolean hasChanged() {
		return true;
	}
}
