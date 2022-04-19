package com.pm.aiost.game.games.tntThrow;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.DARK_RED;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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

public class TntThrow extends Game {

	private static final int RESTOCK_TIME = 5;

	private static final ItemStack THROWABLE_TNT = MetaHelper.setMeta(new ItemStack(Material.TNT), "Throwable TNT",
			Arrays.asList());

	@Override
	public GameType<? extends TntThrow> getType() {
		return GameTypes.TNT_THROW;
	}

	@Override
	protected TntThrowScoreboard createScoreboard() {
		return new TntThrowScoreboard();
	}

	@Override
	public TntThrowScoreboard getScoreboard() {
		return (TntThrowScoreboard) super.getScoreboard();
	}

	@Override
	protected BossBar createBossbar() {
		return Bukkit.createBossBar(DARK_RED + BOLD + "TntThrow", BarColor.RED, BarStyle.SOLID);
	}

	@Override
	protected void createTeams(List<GameTeam> list) {
		GameTeam.twoTeams(this, list);
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
		if (shouldRestock())
			restockPlayer();
	}

	@Override
	protected WinCondition createWinCondition() {
		return WinCondition.LAST_TEAM;
	}

	@Override
	protected void defeat(ServerPlayer serverPlayer) {
		super.defeat(serverPlayer);
		getScoreboard().setPlayerSize(getPlayer().size());
	}

	protected boolean shouldRestock() {
		return getTime() % RESTOCK_TIME == 0;
	}

	private final void restockPlayer() {
		List<ServerPlayer> player = getPlayer();
		for (int i = 0; i < player.size(); i++) {
			Inventory inv = player.get(i).player.getInventory();
			if (!inv.containsAtLeast(THROWABLE_TNT, 3))
				inv.addItem(THROWABLE_TNT);
		}
	}

	@Override
	public void onPlayerChangeBlockPosition(ServerPlayer serverPlayer, PlayerMoveEvent event) {
		if (event.getTo().getBlock().isLiquid())
			defeat(serverPlayer);
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
		System.out.println(event.getCause());
		if (event.getCause() == DamageCause.BLOCK_EXPLOSION)
			defeat(serverPlayer);
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
	public void onBlockDamage(ServerPlayer serverPlayer, BlockDamageEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onPacketThingAttack(PacketThingAttackEvent event) {
		event.setCancelled(true);
	}

	@Override
	protected void loadData(ConfigurationSection section) {

	}

	@Override
	protected void saveData(ConfigurationSection section) {

	}

	@Override
	protected GameKit[] createKits() {
		ItemStack throwableTntStack = THROWABLE_TNT.clone();
		throwableTntStack.setAmount(3);
		return new GameKit[] { new GameKit("Thrower", Material.TNT, 0, new ItemStack[] { throwableTntStack }),

				new GameKit("Archer", Material.BOW, 500, new ItemStack[] { new ItemStack(Material.BOW) }) };
	}
}