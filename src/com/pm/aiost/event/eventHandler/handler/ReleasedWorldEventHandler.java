package com.pm.aiost.event.eventHandler.handler;

import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

import com.pm.aiost.game.GameType;
import com.pm.aiost.game.GameTypes;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.ServerWorld;
import com.pm.aiost.server.world.region.IRegion.RegionType;

public class ReleasedWorldEventHandler extends PlayerWorldEventHandler {

	protected GameType<?> gameType;

	public ReleasedWorldEventHandler() {
	}

	public ReleasedWorldEventHandler(UUID uuid, String name, ServerPlayer serverPlayer, ServerWorld world,
			GameType<?> gameType) {
		this(uuid, name, serverPlayer.getDatabaseID(), serverPlayer.player.getName(), world, gameType);
	}

	public ReleasedWorldEventHandler(UUID uuid, String name, long ownerId, String ownerName, ServerWorld world,
			GameType<?> gameType) {
		super(uuid, name, ownerId, ownerName, world);
		this.gameType = gameType;
	}

	@Override
	protected void deleteRegion() {
		if (region.getRegionType() == RegionType.WORLD)
			region.delete(true);
	}

	@Override
	public void load(ConfigurationSection section) {
		super.load(section);
		gameType = GameTypes.get(section.getInt("gameType"));
	}

	@Override
	public void save(ConfigurationSection section) {
		super.save(section);
		section.set("gameType", gameType.getId());
	}

	@Override
	public String getEventHandlerName() {
		return "ReleasedWorld";
	}

	public GameType<?> getGameType() {
		return gameType;
	}

	@Override
	public boolean isReleased() {
		return true;
	}
}
