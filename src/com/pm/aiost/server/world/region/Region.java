package com.pm.aiost.server.world.region;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.pm.aiost.collection.list.UnorderedIdentityArrayList;
import com.pm.aiost.event.eventHandler.EventHandler;
import com.pm.aiost.event.eventHandler.EventHandlerLoader;
import com.pm.aiost.misc.utils.boundingBox.WorldBB;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.ServerWorld;

public class Region extends WorldBB implements IRegion {

	private ServerWorld serverWorld;
	private int id;
	private String name;
	private EventHandler eventHandler = EventHandler.NULL;
	private final List<Player> player = new UnorderedIdentityArrayList<Player>();
	private final List<ServerPlayer> serverPlayer = new UnorderedIdentityArrayList<ServerPlayer>();

	public Region() {
	}

	public Region(String name, Location first, Location second) {
		super(first, second);
		this.name = name;
	}

	public Region(String name, int x1, int y1, int z1, int x2, int y2, int z2) {
		super(x1, y1, z1, x2, y2, z2);
		this.name = name;
	}

	public void registerWorld() {
		int firstChunkX = xMin >> 4;
		int lastChunkX = xMax >> 4;
		int firstChunkZ = zMin >> 4;
		int lastChunkZ = zMax >> 4;

		for (int chunkX = firstChunkX; chunkX <= lastChunkX; chunkX++)
			for (int chunkZ = firstChunkZ; chunkZ <= lastChunkZ; chunkZ++)
				serverWorld.getChunk(chunkX, chunkZ).addRegion(id);
	}

	public void unregisterWorld() {
		int firstChunkX = xMin >> 4;
		int lastChunkX = xMax >> 4;
		int firstChunkZ = zMin >> 4;
		int lastChunkZ = zMax >> 4;

		for (int chunkX = firstChunkX; chunkX <= lastChunkX; chunkX++)
			for (int chunkZ = firstChunkZ; chunkZ <= lastChunkZ; chunkZ++)
				serverWorld.getChunk(chunkX, chunkZ).removeRegion(id);
	}

	@Override
	public void registerPlayer(ServerPlayer serverPlayer) {
		this.serverPlayer.add(serverPlayer);
		this.player.add(serverPlayer.player);
	}

	@Override
	public void unregisterPlayer(ServerPlayer serverPlayer) {
		this.serverPlayer.remove(serverPlayer);
		this.player.remove(serverPlayer.player);
	}

	@Override
	public List<ServerPlayer> getServerPlayer() {
		return serverPlayer;
	}

	@Override
	public List<Player> getPlayer() {
		return player;
	}

	@Override
	public int getPlayerSize() {
		return player.size();
	}

	@Override
	public void setEventHandler(EventHandler handler, boolean isDefault) {
		setEventHandler(handler);
	}

	public void setEventHandler(EventHandler handler) {
		this.eventHandler = handler;
		serverWorld.updateRegion(this);
	}

	@Override
	public EventHandler getEventHandler() {
		return eventHandler;
	}

	@Override
	public byte getRegionType() {
		return RegionType.REGION;
	}

	@Override
	public void delete(boolean save) {
		serverWorld.removeRegion(this);
	}

	@Override
	public void delete(boolean save, Consumer<World> unloadedCallback) {
		serverWorld.removeRegion(this);
		unloadedCallback.accept(serverWorld.world);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Location getMarkerLocation(String name) {
		return serverWorld.getMarkerLocation(name);
	}

	@Override
	public Location[] getMarkerLocations(String name) {
		return serverWorld.getMarkerLocations(name);
	}

	@Override
	public Location getSpawnLocation() {
		return getCenter(serverWorld.world);
	}

	@Override
	public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> clazz) {
		return serverWorld.world.getEntitiesByClass(clazz);
	}

	@Override
	public Collection<Entity> getEntitiesByClasses(Class<?>... clazz) {
		return serverWorld.world.getEntitiesByClasses(clazz);
	}

	public void load(ConfigurationSection section) {
		super.load(section);
		name = section.getString("name");
		eventHandler = EventHandlerLoader.load(section.getConfigurationSection("eventHandler"));
	}

	public void save(ConfigurationSection section) {
		super.save(section);
		section.set("name", name);
		EventHandlerLoader.save(section.createSection("eventHandler"), eventHandler);
	}

	void setServerWorld(ServerWorld serverWorld) {
		this.serverWorld = serverWorld;
	}

	public ServerWorld getServerWorld() {
		return serverWorld;
	}

	void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
