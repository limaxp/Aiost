package com.pm.aiost.game.games.spleef;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.DARK_RED;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.event.events.PacketThingAttackEvent;
import com.pm.aiost.game.Game;
import com.pm.aiost.game.GameKit;
import com.pm.aiost.game.GameTeam;
import com.pm.aiost.game.GameType;
import com.pm.aiost.game.GameTypes;
import com.pm.aiost.game.WinCondition;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class Spleef extends Game {

	private static final ItemStack SNOW_BALL = MetaHelper.setMeta(new ItemStack(Material.SNOWBALL), "Spleef ball",
			Arrays.asList());

	@Override
	public GameType<? extends Spleef> getType() {
		return GameTypes.SPLEEF;
	}

	@Override
	protected SpleefScoreboard createScoreboard() {
		return new SpleefScoreboard();
	}

	@Override
	public SpleefScoreboard getScoreboard() {
		return (SpleefScoreboard) super.getScoreboard();
	}

	@Override
	protected BossBar createBossbar() {
		return Bukkit.createBossBar(DARK_RED + BOLD + "Spleef", BarColor.RED, BarStyle.SOLID);
	}

	@Override
	protected void createTeams(List<GameTeam> list) {
		GameTeam.allTeams(this, list);
	}

	@Override
	public void start() {
		super.start();
		getScoreboard().setPlayerSize(getPlayer().size());
	}

	@Override
	protected void restart() {
		super.restart();
		getScoreboard().setPlayerSize(getPlayer().size());
	}

	@Override
	protected void tick() {
		getScoreboard().setTime(GREEN + timeToString(getTime()));
	}

	@Override
	protected WinCondition createWinCondition() {
		return WinCondition.LAST_PLAYER;
	}

	@Override
	protected void defeat(ServerPlayer serverPlayer) {
		super.defeat(serverPlayer);
		getScoreboard().setPlayerSize(getPlayer().size());
	}

	@Override
	public void onPlayerChangeBlockPosition(ServerPlayer serverPlayer, PlayerMoveEvent event) {
		if (event.getTo().getBlock().isLiquid())
			defeat(serverPlayer);
	}

	@Override
	public void onBlockDamage(ServerPlayer serverPlayer, BlockDamageEvent event) {
		Inventory inv = event.getPlayer().getInventory();
		if (!inv.containsAtLeast(SNOW_BALL, 64))
			inv.addItem(SNOW_BALL);
		event.getBlock().setType(Material.AIR);
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
		if (event.getCause() == DamageCause.VOID)
			defeat(serverPlayer);
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
	public void onPacketThingAttack(PacketThingAttackEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onProjectileHit(ProjectileHitEvent event) {
		Block hitblock = event.getHitBlock();
		if (hitblock != null)
			hitblock.setType(Material.AIR);
	}

	@Override
	protected void loadData(ConfigurationSection section) {

	}

	@Override
	protected void saveData(ConfigurationSection section) {

	}

	@Override
	protected GameKit[] createKits() {
		ItemStack snowballStack = SNOW_BALL.clone();
		snowballStack.setAmount(16);
		return new GameKit[] {
				new GameKit("Snowballer", Material.SNOWBALL, 0, new ItemStack[] { snowballStack, snowballStack }),

				new GameKit("Archer", Material.BOW, 500, new ItemStack[] { new ItemStack(Material.BOW) }) };
	}
}