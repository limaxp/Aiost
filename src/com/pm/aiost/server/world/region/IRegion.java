package com.pm.aiost.server.world.region;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.pm.aiost.event.eventHandler.EventHandler;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.ServerWorld;

public interface IRegion {

	public static class RegionType {

		public static final byte UNDEFINED = 0;
		public static final byte WORLD = 1;
		public static final byte REGION = 2;
	}

	public void registerPlayer(ServerPlayer serverPlayer);

	public void unregisterPlayer(ServerPlayer serverPlayer);

	public List<ServerPlayer> getServerPlayer();

	public List<Player> getPlayer();

	public int getPlayerSize();

	public void setEventHandler(EventHandler handler, boolean isDefault);

	public EventHandler getEventHandler();

	public byte getRegionType();

	public void delete(boolean save);

	public void delete(boolean save, Consumer<World> unloadedCallback);

	public String getName();

	public Location getMarkerLocation(String name);

	public Location[] getMarkerLocations(String name);

	public Location getSpawnLocation();

	public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> clazz);

	public Collection<Entity> getEntitiesByClasses(Class<?>... clazz);

	public default boolean isWorld() {
		return getRegionType() == RegionType.WORLD;
	}

	public default boolean isRegion() {
		return getRegionType() == RegionType.REGION;
	}

	public static IRegion load(ConfigurationSection section) {
		byte type = (byte) section.getInt("regionType");
		if (type == RegionType.WORLD)
			return ServerWorld.getByWorld(Bukkit.getWorld(section.getString("regionWorldName")));
		else if (type == RegionType.REGION) {
			ServerWorld serverWorld = ServerWorld.getByWorld(Bukkit.getWorld(section.getString("regionWorldName")));
			return serverWorld.getRegions().get(serverWorld, section.getInt("regionId"));
		} else
			return null;
	}

	public static void save(ConfigurationSection section, IRegion region) {
		byte type = region.getRegionType();
		section.set("regionType", region.getRegionType());
		if (type == RegionType.WORLD)
			section.set("regionWorldName", region.getName());
		else if (type == RegionType.REGION) {
			Region actualRegion = (Region) region;
			section.set("regionWorldName", actualRegion.getServerWorld().getName());
			section.set("regionId", actualRegion.getId());
		}
	}
}
