package com.pm.aiost.event.eventHandler.handler;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.pm.aiost.event.eventHandler.EventHandler;
import com.pm.aiost.event.events.PacketThingAttackEvent;
import com.pm.aiost.misc.scoreboard.scoreboards.PlayerRegionScoreboard;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.region.IRegion;

public class PlayerRegionEventHandler implements EventHandler {

	protected long ownerId;
	protected String ownerName;
	protected IRegion region;
	protected PlayerRegionScoreboard scoreboard;

	public PlayerRegionEventHandler() {
	}

	public PlayerRegionEventHandler(ServerPlayer serverPlayer, IRegion region) {
		this(serverPlayer.getDatabaseID(), serverPlayer.player.getName(), region);
	}

	public PlayerRegionEventHandler(long ownerId, String ownerName, IRegion region) {
		this.ownerId = ownerId;
		this.ownerName = ownerName;
		this.region = region;
		this.scoreboard = createScoreboard();
	}

	@Override
	public void onPlayerJoin(ServerPlayer serverPlayer) {
		scoreboard.setPlayerSize(region.getPlayerSize());
		scoreboard.addPlayer(serverPlayer.player);
		if (serverPlayer.getDatabaseID() == ownerId)
			scoreboard.getBuilderTeam().addEntry(serverPlayer.name);
		else
			scoreboard.getGuestTeam().addEntry(serverPlayer.name);
	}

	@Override
	public void onPlayerQuit(ServerPlayer serverPlayer, QuitReason reason) {
		if (reason != QuitReason.DISABLE) {
			Player player = serverPlayer.player;
			scoreboard.removePlayer(player);
			String name = player.getName();
			scoreboard.getEntryTeam(name).removeEntry(name);
			scoreboard.setPlayerSize(region.getPlayerSize());
		}
	}

	@Override
	public void onPlayerDamage(ServerPlayer serverPlayer, EntityDamageEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onPlayerDamageByEntity(ServerPlayer serverPlayer, EntityDamageByEntityEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onBlockPlace(ServerPlayer serverPlayer, BlockPlaceEvent event) {
		if (!canModifyWorld(serverPlayer))
			event.setCancelled(true);
	}

	@Override
	public void onBlockBreak(ServerPlayer serverPlayer, BlockBreakEvent event) {
		if (!canModifyWorld(serverPlayer))
			event.setCancelled(true);
	}

	@Override
	public void onPacketThingAttack(PacketThingAttackEvent event) {
		if (!canModifyWorld(event.getServerPlayer()))
			event.setCancelled(true);
	}

	@Override
	public void onEntityDamageByPlayer(ServerPlayer serverPlayer, EntityDamageByEntityEvent event) {
		if (!canModifyWorld(serverPlayer))
			event.setCancelled(true);
	}

	@Override
	public void onPlayerInteractEntity(ServerPlayer serverPlayer, PlayerInteractEntityEvent event) {
		if (!canModifyWorld(serverPlayer))
			event.setCancelled(true);
	}

	@Override
	public void onPlayerInteractAtEntity(ServerPlayer serverPlayer, PlayerInteractAtEntityEvent event) {
		if (!canModifyWorld(serverPlayer))
			event.setCancelled(true);
	}

	@Override
	public void onPlayerDropItem(ServerPlayer serverPlayer, PlayerDropItemEvent event) {
		if (!canModifyWorld(serverPlayer))
			event.setCancelled(true);
	}

	@Override
	public void onPlayerPickupItem(ServerPlayer serverPlayer, EntityPickupItemEvent event) {
		if (!canModifyWorld(serverPlayer))
			event.setCancelled(true);
	}

	@Override
	public boolean canModifyWorld(ServerPlayer serverPlayer) {
		return scoreboard.getEntryTeam(serverPlayer.name).equals(scoreboard.getBuilderTeam()) ? true
				: serverPlayer.getRank().isAdmin();
	}

	public boolean isOwner(ServerPlayer serverPlayer) {
		return serverPlayer.getDatabaseID() == ownerId;
	}

	protected PlayerRegionScoreboard createScoreboard() {
		return new PlayerRegionScoreboard(ownerName, region.getName());
	}

	@Override
	public void load(ConfigurationSection section) {
		ownerId = section.getLong("ownerId");
		ownerName = section.getString("ownerName");
		region = IRegion.load(section);
		scoreboard = createScoreboard();
	}

	@Override
	public void save(ConfigurationSection section) {
		section.set("ownerId", ownerId);
		section.set("ownerName", ownerName);
		IRegion.save(section, region);
	}

	@Override
	public String getEventHandlerName() {
		return "PlayerRegion";
	}

	public long getOwnerId() {
		return ownerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public IRegion getRegion() {
		return region;
	}
}
