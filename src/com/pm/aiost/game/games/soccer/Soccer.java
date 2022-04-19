package com.pm.aiost.game.games.soccer;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.DARK_RED;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.custom.Ball;
import com.pm.aiost.event.events.PacketThingAttackEvent;
import com.pm.aiost.game.Game;
import com.pm.aiost.game.GameTeam;
import com.pm.aiost.game.GameType;
import com.pm.aiost.game.GameTypes;
import com.pm.aiost.game.WinCondition;
import com.pm.aiost.misc.scoreboard.scoreboards.GameScoreboard;
import com.pm.aiost.misc.utils.ChatColor;
import com.pm.aiost.player.ServerPlayer;

public class Soccer extends Game {

	protected Ball ball;
	protected int bluePoints;
	protected int redPoints;

	@Override
	public GameType<? extends Soccer> getType() {
		return GameTypes.SOCCER;
	}

	@Override
	protected GameScoreboard createScoreboard() {
		return new SoccerScoreboard();
	}

	@Override
	public SoccerScoreboard getScoreboard() {
		return (SoccerScoreboard) super.getScoreboard();
	}

	@Override
	protected BossBar createBossbar() {
		return Bukkit.createBossBar(DARK_RED + BOLD + "0 : 0", BarColor.RED, BarStyle.SOLID);
	}

	@Override
	protected void createTeams(List<GameTeam> list) {
		GameTeam.twoTeams(this, list);
	}

	@Override
	public void start() {
		super.start();
		ball = (Ball) AiostEntityTypes.spawnEntity(AiostEntityTypes.BALL, getRegion().getSpawnLocation());
	}

	@Override
	protected void tick() {
		int time = getTime();
		getScoreboard().setTime(GREEN + timeToString(time));
		if (time > 300) {
			if (bluePoints != redPoints)
				end();
		}
	}

	@Override
	public void end() {
		super.end();
		ball.die();
		if (bluePoints > redPoints)
			broadcastTitle(ChatColor.BLUE + "Blue team has won!");
		else
			broadcastTitle(ChatColor.RED + "Red team has won!");
	}

	@Override
	protected WinCondition createWinCondition() {
		return WinCondition.NONE;
	}

	@Override
	public void onPlayerPickupItem(ServerPlayer serverPlayer, EntityPickupItemEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onPlayerDropItem(ServerPlayer serverPlayer, PlayerDropItemEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onPlayerDamage(ServerPlayer serverPlayer, EntityDamageEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onBlockBreak(ServerPlayer serverPlayer, BlockBreakEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onBlockPlace(ServerPlayer serverPlayer, BlockPlaceEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onBlockDamage(ServerPlayer serverPlayer, BlockDamageEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onPacketThingAttack(PacketThingAttackEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		event.setCancelled(true);
	}

	@Override
	protected void loadData(ConfigurationSection section) {

	}

	@Override
	protected void saveData(ConfigurationSection section) {

	}
}
