package com.pm.aiost.event.eventHandler.handler;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.pm.aiost.Aiost;
import com.pm.aiost.event.eventHandler.EventHandler;
import com.pm.aiost.misc.utils.ChatColor;
import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.player.ServerPlayer;

public class DuelEventHandler implements LobbyEventHandler {

	public enum State {
		INIT, STARTING, FIGHTING, CANCEL, DONE;
	}

	protected ServerPlayer serverPlayer1;
	protected ServerPlayer serverPlayer2;
	protected BossBar bossBar1;
	protected BossBar bossBar2;
	private BukkitRunnable scheduler;
	private int time;
	private State state = State.INIT;
	protected int bet;

	public DuelEventHandler() {
	}

	public DuelEventHandler(ServerPlayer serverPlayer1, ServerPlayer serverPlayer2) {
		registerPlayer(serverPlayer1, serverPlayer2);
		startScheduler();
	}

	public DuelEventHandler(ServerPlayer serverPlayer1, ServerPlayer serverPlayer2, int bet) {
		this(serverPlayer1, serverPlayer2);
		this.bet = bet;
	}

	protected void registerPlayer(ServerPlayer serverPlayer1, ServerPlayer serverPlayer2) {
		this.serverPlayer1 = serverPlayer1;
		this.serverPlayer2 = serverPlayer2;
		bossBar1 = createBossBar(serverPlayer1);
		bossBar2 = createBossBar(serverPlayer2);
	}

	protected BossBar createBossBar(ServerPlayer serverPlayer) {
		BossBar bossBar = Bukkit.createBossBar(ChatColor.RED + ChatColor.BOLD + serverPlayer.name, BarColor.RED,
				BarStyle.SOLID);
		bossBar.setProgress(serverPlayer.player.getHealth() / serverPlayer.player.getMaxHealth());
		return bossBar;
	}

	protected final void startScheduler() {
		state = State.STARTING;
		scheduler = new BukkitRunnable() {
			@Override
			public void run() {
				if (time == 10) {
					broadcastTitle(ChatColor.RED + "Fight!");
					start();
					cancel();
				} else {
					broadcastTitle(ChatColor.RED + (10 - time));
					time++;
				}
			}
		};
		scheduler.runTaskTimer(Aiost.getPlugin(), 0, 20);
	}

	protected final void stopScheduler() {
		if (scheduler != null && !scheduler.isCancelled())
			scheduler.cancel();
	}

	protected void start() {
		state = State.FIGHTING;
		time = NMS.getMinecraftServerTick();
	}

	@Override
	public void onPlayerJoin(ServerPlayer serverPlayer) {
		if (serverPlayer == serverPlayer1)
			bossBar2.addPlayer(serverPlayer.player);
		else if (serverPlayer == serverPlayer2)
			bossBar1.addPlayer(serverPlayer.player);
		else {
			bossBar1.addPlayer(serverPlayer.player);
			bossBar2.addPlayer(serverPlayer.player);
		}
	}

	@Override
	public void onPlayerQuit(ServerPlayer serverPlayer, QuitReason reason) {
		if (serverPlayer == serverPlayer1)
			serverPlayer2.resetEventHandler();
		else if (serverPlayer == serverPlayer2)
			serverPlayer1.resetEventHandler();
		else {
			bossBar1.removePlayer(serverPlayer.player);
			bossBar2.removePlayer(serverPlayer.player);
			return;
		}
		bossBar1.removePlayer(serverPlayer2.player);
		bossBar2.removePlayer(serverPlayer1.player);
		if (state == State.STARTING)
			stopScheduler();
		state = State.CANCEL;
	}

	@Override
	public void onPlayerDeath(ServerPlayer serverPlayer, PlayerDeathEvent event) {
		ServerPlayer winner, loser;
		if (serverPlayer == serverPlayer1) {
			winner = serverPlayer2;
			loser = serverPlayer1;
		} else if (serverPlayer == serverPlayer2) {
			winner = serverPlayer1;
			loser = serverPlayer2;
		} else
			return;
		bossBar1.removePlayer(serverPlayer2.player);
		bossBar2.removePlayer(serverPlayer1.player);
		serverPlayer1.resetEventHandler();
		serverPlayer2.resetEventHandler();
		broadcastTitle(winner.name + " has won duel!");
		if (bet > 0) {
			loser.removeCredits(bet); // TODO: database sending!
			winner.addCredits(bet);
		}
		state = State.DONE;
	}

	@Override
	public void onPlayerDamage(ServerPlayer serverPlayer, EntityDamageEvent event) {
		if (state == State.FIGHTING) {
			if (serverPlayer == serverPlayer1 || serverPlayer == serverPlayer2)
				return;
		}
		event.setCancelled(true);
	}

	@Override
	public void onPlayerDamageByEntity(ServerPlayer serverPlayer, EntityDamageByEntityEvent event) {
		if (state == State.FIGHTING) {
			Entity damager = event.getDamager();
			if (damager instanceof Player) {
				if (checkDamageEntities(serverPlayer, (Player) damager, event.getFinalDamage()))
					return;
			} else if (damager instanceof Projectile) {
				Projectile projectileDamager = (Projectile) damager;
				if (projectileDamager.getShooter() instanceof Player) {
					if (checkDamageEntities(serverPlayer, (Player) projectileDamager.getShooter(),
							event.getFinalDamage()))
						return;
				}
			}
		}
		event.setCancelled(true);
		return;
	}

	protected final boolean checkDamageEntities(ServerPlayer damaged, Player damager, double damage) {
		if (serverPlayer1 == damaged) {
			if (damager == serverPlayer2.player) {
				updateBossbar(bossBar1, damaged.player, damage);
				return true;
			}
		} else if (serverPlayer2 == damaged) {
			if (damager == serverPlayer1.player) {
				updateBossbar(bossBar2, damaged.player, damage);
				return true;
			}
		}
		return false;
	}

	protected void updateBossbar(BossBar bossBar, Player damaged, double damage) {
		if (damaged.getHealth() - damage > 0)
			bossBar.setProgress((damaged.getHealth() - damage) / damaged.getMaxHealth());
	}

	@Override
	public void load(ConfigurationSection section) {
		registerPlayer(loadPlayer(section.getString("serverPlayer1")), loadPlayer(section.getString("serverPlayer´2")));
		time = section.getInt("time");
		state = State.values()[section.getInt("state")];
		bet = section.getInt("bet");
		if (state == State.STARTING)
			startScheduler();
	}

	private final ServerPlayer loadPlayer(String uuid) {
		Player player = Bukkit.getPlayer(UUID.fromString(uuid));
		if (player != null)
			return ServerPlayer.getByPlayer(player);
		return null;
	}

	@Override
	public void save(ConfigurationSection section) {
		section.set("serverPlayer1", serverPlayer1.player.getUniqueId());
		section.set("serverPlayer2", serverPlayer2.player.getUniqueId());
		section.set("time", time);
		section.set("state", state.ordinal());
		section.set("bet", bet);
	}

	@Override
	public boolean canModifyWorld(ServerPlayer serverPlayer) {
		return false;
	}

	@Override
	public boolean allowsChange(EventHandler eventHandler) {
		return false;
	}

	@Override
	public String getEventHandlerName() {
		return "Duel";
	}

	public void broadcastTitle(String text) {
		serverPlayer1.sendTitle(text);
		serverPlayer2.sendTitle(text);
	}

	public ServerPlayer getPlayer1() {
		return serverPlayer1;
	}

	public ServerPlayer getPlayer2() {
		return serverPlayer2;
	}

	public int getTime() {
		if (state == State.STARTING)
			return 0;
		return NMS.getMinecraftServerTick() - time;
	}

	public State getState() {
		return state;
	}

	public int getBet() {
		return bet;
	}
}
