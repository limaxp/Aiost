package com.pm.aiost.event.eventHandler.handler;

import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.misc.menu.menus.PlayerWorldControlMenu;
import com.pm.aiost.misc.scoreboard.scoreboards.PlayerWorldScoreboard;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.ServerWorld;
import com.pm.aiost.server.world.creation.WorldLoader;
import com.pm.aiost.server.world.region.IRegion.RegionType;

public class PlayerWorldEventHandler extends PlayerRegionEventHandler {

	protected UUID uuid;
	protected String name;
	protected PlayerWorldControlMenu menu;

	public PlayerWorldEventHandler() {
	}

	public PlayerWorldEventHandler(UUID uuid, String name, ServerPlayer serverPlayer, ServerWorld world) {
		this(uuid, name, serverPlayer.getDatabaseID(), serverPlayer.player.getName(), world);
	}

	public PlayerWorldEventHandler(UUID uuid, String name, long ownerId, String ownerName, ServerWorld world) {
		super(ownerId, ownerName, world);
		this.uuid = uuid;
		this.name = name;
		menu = new PlayerWorldControlMenu(this);
	}

	protected void close() {
		menu = null;
		deleteRegion();
	}

	protected void deleteRegion() {
		if (region.getRegionType() == RegionType.WORLD)
			region.delete(true, (world) -> WorldLoader.savePlayerWorld(uuid, world));
	}

	@Override
	public void onPlayerQuit(ServerPlayer serverPlayer, QuitReason reason) {
		if (reason != QuitReason.DISABLE) {
			Player player = serverPlayer.player;
			scoreboard.removePlayer(player);
			if (isOwner(serverPlayer)) {
				close();
				return;
			}

			String name = player.getName();
			scoreboard.getEntryTeam(name).removeEntry(name);
			scoreboard.setPlayerSize(region.getPlayerSize());
		}
	}

	@Override
	protected PlayerWorldScoreboard createScoreboard() {
		return new PlayerWorldScoreboard(ownerName, name);
	}

	@Override
	public void load(ConfigurationSection section) {
		uuid = UUID.fromString(section.getString("uuid"));
		name = section.getString("name");
		super.load(section);
		menu = new PlayerWorldControlMenu(this);
	}

	@Override
	public void save(ConfigurationSection section) {
		super.save(section);
		section.set("uuid", uuid.toString());
		section.set("name", name);
	}

	@Override
	public Menu getMenu() {
		return menu;
	}

	@Override
	public boolean markerVisible() {
		return true;
	}

	@Override
	public String getEventHandlerName() {
		return "PlayerWorld";
	}

	public UUID getUniqueID() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	public boolean isReleased() {
		return false;
	}

	public ServerWorld getRegion() {
		return (ServerWorld) region;
	}
}