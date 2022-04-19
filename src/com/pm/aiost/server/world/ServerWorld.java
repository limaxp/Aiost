package com.pm.aiost.server.world;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.IntArrayList;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.IntList;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;

import com.pm.aiost.collection.list.IdentityArrayList;
import com.pm.aiost.collection.list.UnorderedIdentityArrayList;
import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.eventHandler.EventHandler;
import com.pm.aiost.event.eventHandler.EventHandlerLoader;
import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.misc.packet.PacketThing;
import com.pm.aiost.misc.packet.entity.PacketEntity;
import com.pm.aiost.misc.packet.entity.PacketEntityType;
import com.pm.aiost.misc.packet.object.PacketObject;
import com.pm.aiost.misc.packet.object.PacketObjectType;
import com.pm.aiost.misc.utils.LocationHelper;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.chunk.ChunkLoader;
import com.pm.aiost.server.world.chunk.ServerChunk;
import com.pm.aiost.server.world.creation.WorldBuilder;
import com.pm.aiost.server.world.effects.WorldEffects;
import com.pm.aiost.server.world.marker.MarkerLoader;
import com.pm.aiost.server.world.object.TickingObject;
import com.pm.aiost.server.world.object.tileObject.TileObject;
import com.pm.aiost.server.world.region.IRegion;
import com.pm.aiost.server.world.region.Region;
import com.pm.aiost.server.world.region.WorldRegions;

public class ServerWorld implements AutoCloseable, IRegion {

	// chunks are saved as map where key = x und y combined and value ServerChunk

	public final World world;
	private Long2ObjectMap<ServerChunk> chunks;
	private final RegionFileCache regionFileCache;
	private Int2ObjectMap<PacketThing> packetThings;
	private List<TickingObject> tickingObjects;
	private final WorldEffects worldEffects;
	private final WorldRegions worldRegions;
	private final MarkerLoader markerLoader;
	private List<ServerPlayer> serverPlayer;
	private EventHandler eventHandler;
	private final EventHandlerLoader eventHandlerLoader;
	private final Map<Object, Menu> menus;

	public ServerWorld(World world) {
		this.world = world;
		chunks = new Long2ObjectOpenHashMap<ServerChunk>();
		packetThings = new Int2ObjectOpenHashMap<PacketThing>();
		tickingObjects = new UnorderedIdentityArrayList<TickingObject>();

		File aiostFile = new File(Bukkit.getWorldContainer(), world.getName() + File.separator + "aiost");
		if (!aiostFile.exists())
			aiostFile.mkdir();
		regionFileCache = new RegionFileCache(new File(aiostFile, "region"));
		eventHandlerLoader = new EventHandlerLoader(new File(aiostFile, "eventHandler.yml"));
		worldEffects = new WorldEffects(new File(aiostFile, "effects"));
		worldRegions = new WorldRegions(new File(aiostFile, "regions"));
		markerLoader = new MarkerLoader(new File(aiostFile, "marker.yml"), world);
		serverPlayer = new UnorderedIdentityArrayList<ServerPlayer>();
		menus = new IdentityHashMap<Object, Menu>();
	}

	public void init() {
		eventHandler = eventHandlerLoader.load();
	}

	@Override
	public void close() throws IOException {
		if (world.isAutoSave())
			save();
		else if (eventHandlerLoader.doesForceSave())
			eventHandlerLoader.save(eventHandler);

		packetThings = null;
		tickingObjects = null;
		serverPlayer = null;
		chunks = null;
		eventHandler = null;
		regionFileCache.close();
	}

	public void save() {
		eventHandlerLoader.save(eventHandler);
		markerLoader.saveFile();
		saveChunks();
	}

	public void saveChunks() {
		Iterator<ServerChunk> iterator = chunks.values().iterator();
		while (iterator.hasNext())
			saveChunk(iterator.next());
	}

	public void saveChunk(ServerChunk serverChunk) {
		regionFileCache.saveChunk(serverChunk.chunk, ChunkLoader.saveChunk(serverChunk));
	}

	public void loadChunk(Chunk chunk) {
		ServerChunk serverChunk = new ServerChunk(chunk);
		ChunkLoader.loadChunk(serverChunk, regionFileCache.loadChunk(chunk));
		chunks.put(serverChunk.getKey(), serverChunk);
	}

	public void unloadChunk(Chunk chunk, boolean shouldSave) {
		ServerChunk serverChunk = chunks.remove(ServerChunk.getKey(chunk));
		if (shouldSave)
			saveChunk(serverChunk);

		List<PacketEntity> packetEntities = serverChunk.getPacketEntities();
		for (int i = packetEntities.size() - 1; i >= 0; i--)
			removePacketThing(packetEntities.get(i));

		Collection<PacketObject> packetObjects = serverChunk.getPacketObjects();
		Iterator<PacketObject> iterator = packetObjects.iterator();
		while (iterator.hasNext())
			removePacketThing(iterator.next());
	}

	@Nullable
	public ServerChunk getChunk(int x, int z) {
		return chunks.get(ServerChunk.getKey(x, z));
	}

	@Nullable
	public ServerChunk getChunk(Chunk chunk) {
		return chunks.get(ServerChunk.getKey(chunk));
	}

	@Nullable
	public ServerChunk getChunk(long key) {
		return chunks.get(key);
	}

	public void update() {
		int size = tickingObjects.size();
		for (int i = 0; i < size; i++)
			tickingObjects.get(i).tick();
	}

	public void addPacketThing(PacketThing packetThing) {
		packetThings.put(packetThing.getId(), packetThing);
		if (packetThing instanceof TickingObject)
			tickingObjects.add((TickingObject) packetThing);
	}

	public void removePacketThing(PacketThing packetThing) {
		packetThings.remove(packetThing.getId());
		if (packetThing instanceof TickingObject)
			tickingObjects.remove((TickingObject) packetThing);
	}

	@Nullable
	public PacketThing getPacketThing(int id) {
		return packetThings.get(id);
	}

	public Collection<PacketThing> getPacketThings() {
		return packetThings.values();
	}

	public void addTickingObject(TickingObject tickingObject) {
		tickingObjects.add(tickingObject);
	}

	public void removeTickingObject(TickingObject tickingObject) {
		tickingObjects.remove(tickingObject);
	}

	public void addPacketObject(PacketObject packetObject) {
		getChunk(packetObject.x >> 4, packetObject.z >> 4).addPacketObject(packetObject);
	}

	@Nullable
	public PacketObject removePacketObject(Block block) {
		return getChunk(block.getChunk()).removePacketObject(block);
	}

	@Nullable
	public PacketObject removePacketObject(Location loc) {
		return getChunk(loc.getChunk()).removePacketObject(loc);
	}

	@Nullable
	public PacketObject removePacketObject(int x, int y, int z) {
		return getChunk(x >> 4, z >> 4).removePacketObject(x, y, z);
	}

	@Nullable
	public PacketObject getPacketObject(Block block) {
		return getChunk(block.getChunk()).getPacketObject(block);
	}

	@Nullable
	public PacketObject getPacketObject(Location loc) {
		return getChunk(loc.getChunk()).getPacketObject(loc);
	}

	@Nullable
	public PacketObject getPacketObject(int x, int y, int z) {
		return getChunk(x >> 4, z >> 4).getPacketObject(x, y, z);
	}

	public boolean hasPacketObject(Block block) {
		return getChunk(block.getChunk()).hasPacketObject(block);
	}

	public boolean hasPacketObject(Location loc) {
		return getChunk(loc.getChunk()).hasPacketObject(loc);
	}

	public boolean hasPacketObject(int x, int y, int z) {
		return getChunk(x >> 4, z >> 4).hasPacketObject(x, y, z);
	}

	public Collection<PacketObject> getPacketObjects(Chunk chunk) {
		return getChunk(chunk).getPacketObjects();
	}

	public Collection<PacketObject> getPacketObjects(Chunk chunk, PacketObjectType<?> type) {
		return getChunk(chunk).getPacketObjects(type);
	}

	public List<PacketObject> getPacketObjects(Block block, int range) {
		return getPacketObjects(block.getX(), block.getY(), block.getZ(), range);
	}

	public List<PacketObject> getPacketObjects(Location loc, int range) {
		return getPacketObjects(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), range);
	}

	public List<PacketObject> getPacketObjects(int x, int y, int z, int range) {
		List<PacketObject> list = new ArrayList<PacketObject>();
		for (int deltaX = -range; deltaX <= range; deltaX++) {
			for (int deltaY = -range; deltaY <= range; deltaY++) {
				for (int deltaZ = -range; deltaZ <= range; deltaZ++) {
					PacketObject packetObject = getPacketObject(x + deltaX, y + deltaY, z + deltaZ);
					if (packetObject != null)
						list.add(packetObject);
				}
			}
		}
		return list;
	}

	public List<PacketObject> getPacketObjects(Block block, int range, PacketObjectType<?> type) {
		return getPacketObjects(block.getX(), block.getY(), block.getZ(), range, type);
	}

	public List<PacketObject> getPacketObjects(Location loc, int range, PacketObjectType<?> type) {
		return getPacketObjects(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), range, type);
	}

	public List<PacketObject> getPacketObjects(int x, int y, int z, int range, PacketObjectType<?> type) {
		List<PacketObject> list = new ArrayList<PacketObject>();
		for (int deltaX = -range; deltaX <= range; deltaX++) {
			for (int deltaY = -range; deltaY <= range; deltaY++) {
				for (int deltaZ = -range; deltaZ <= range; deltaZ++) {
					PacketObject packetObject = getPacketObject(x + deltaX, y + deltaY, z + deltaZ);
					if (packetObject != null && packetObject.getPacketObjectType() == type)
						list.add(packetObject);
				}
			}
		}
		return list;
	}

	public void removePacketObjects(Block block, int range, PacketObjectType<?> type) {
		removePacketObjects(block.getX(), block.getY(), block.getZ(), range, type);
	}

	public void removePacketObjects(Location loc, int range, PacketObjectType<?> type) {
		removePacketObjects(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), range, type);
	}

	public void removePacketObjects(int x, int y, int z, int range, PacketObjectType<?> type) {
		for (int deltaX = -range; deltaX <= range; deltaX++) {
			for (int deltaY = -range; deltaY <= range; deltaY++) {
				for (int deltaZ = -range; deltaZ <= range; deltaZ++) {
					PacketObject packetObject = getPacketObject(x + deltaX, y + deltaY, z + deltaZ);
					if (packetObject != null && packetObject.getPacketObjectType() == type)
						removePacketObject(x + deltaX, y + deltaY, z + deltaZ);
				}
			}
		}
	}

	public void addPacketEntity(PacketEntity packetEntity) {
		getChunk((int) packetEntity.x >> 4, (int) packetEntity.z >> 4).addPacketEntity(packetEntity);
	}

	public void removePacketEntity(PacketEntity packetEntity) {
		getChunk((int) packetEntity.x >> 4, (int) packetEntity.z >> 4).removePacketEntity(packetEntity);
	}

	@Nullable
	public PacketEntity getPacketEntity(int id) {
		return (PacketEntity) packetThings.get(id);
	}

	public List<PacketEntity> getPacketEntities(Chunk chunk) {
		return getChunk(chunk).getPacketEntities();
	}

	public List<PacketEntity> getPacketEntities(Chunk chunk, PacketEntityType<?> type) {
		return getChunk(chunk).getPacketEntities(type);
	}

	public List<PacketEntity> getPacketEntities(Location loc, int range) {
		return getPacketEntities(loc.getX(), loc.getY(), loc.getZ(), range);
	}

	public List<PacketEntity> getPacketEntities(double x, double y, double z, int range) {
		List<PacketEntity> list = new ArrayList<PacketEntity>();
		for (ServerChunk chunk : getChunks((int) x, (int) z, range))
			for (PacketEntity packetEntity : chunk.getPacketEntities())
				if (LocationHelper.distance(x, y, z, packetEntity.x, packetEntity.y, packetEntity.z) <= range)
					list.add(packetEntity);
		return list;
	}

	public List<PacketEntity> getPacketEntities(Location loc, int range, PacketEntityType<?> type) {
		return getPacketEntities(loc.getX(), loc.getY(), loc.getZ(), range, type);
	}

	public List<PacketEntity> getPacketEntities(double x, double y, double z, int range, PacketEntityType<?> type) {
		List<PacketEntity> list = new ArrayList<PacketEntity>();
		for (ServerChunk chunk : getChunks((int) x, (int) z, range)) {
			for (PacketEntity packetEntity : chunk.getPacketEntities()) {
				if (packetEntity.getPacketEntityType() == type
						&& LocationHelper.distance(x, y, z, packetEntity.x, packetEntity.y, packetEntity.z) <= range)
					list.add(packetEntity);
			}
		}
		return list;
	}

	public void removePacketEntities(Location loc, int range, PacketEntityType<?> type) {
		removePacketEntities(loc.getX(), loc.getY(), loc.getZ(), range, type);
	}

	public void removePacketEntities(double x, double y, double z, int range, PacketEntityType<?> type) {
		List<PacketEntity> packetEntities = getPacketEntities(x, y, z, range, type);
		for (PacketEntity packetEntity : packetEntities)
			packetEntity.remove();
	}

	/**
	 * Max range: 16!
	 * 
	 * @param x
	 * @param z
	 * @param range
	 * @return
	 */
	public List<ServerChunk> getChunks(Location loc, int range) {
		return getChunks(loc.getBlockX(), loc.getBlockZ(), range);
	}

	/**
	 * Max range: 16!
	 * 
	 * @param x
	 * @param z
	 * @param range
	 * @return
	 */
	public List<ServerChunk> getChunks(int x, int z, int range) {
		if (range > 16)
			range = 16;
		List<ServerChunk> chunks = new IdentityArrayList<ServerChunk>();
		int chunkX = x >> 4;
		int chunkZ = z >> 4;
		int moduloX = Math.abs(x) % 16;
		int moduloZ = Math.abs(z) % 16;

		chunks.add(getChunk(chunkX, chunkZ));
		if (moduloX - range < 0) {
			chunks.add(getChunk(chunkX - 1, chunkZ));
			if (moduloZ - range < 0)
				chunks.add(getChunk(chunkX - 1, chunkZ - 1));
			if (moduloZ + range > 16)
				chunks.add(getChunk(chunkX - 1, chunkZ + 1));
		}
		if (moduloX + range > 16) {
			chunks.add(getChunk(chunkX + 1, chunkZ));
			if (moduloZ - range < 0)
				chunks.add(getChunk(chunkX + 1, chunkZ - 1));
			if (moduloZ + range > 16)
				chunks.add(getChunk(chunkX + 1, chunkZ + 1));
		}
		if (moduloZ - range < 0)
			chunks.add(getChunk(chunkX, chunkZ - 1));
		if (moduloZ + range > 16)
			chunks.add(getChunk(chunkX, chunkZ + 1));
		return chunks;
	}

	@Nullable
	public void addTileObject(TileObject packetObject) {
		getChunk(packetObject.x >> 4, packetObject.z >> 4).addTileObject(packetObject);
	}

	@Nullable
	public TileObject removeTileObject(Block block) {
		return getChunk(block.getChunk()).removeTileObject(block);
	}

	@Nullable
	public TileObject removeTileObject(Location loc) {
		return getChunk(loc.getChunk()).removeTileObject(loc);
	}

	@Nullable
	public TileObject removeTileObject(int x, int y, int z) {
		return getChunk(x >> 4, z >> 4).removeTileObject(x, y, z);
	}

	@Nullable
	public TileObject getTileObject(Block block) {
		return getChunk(block.getChunk()).getTileObject(block);
	}

	@Nullable
	public TileObject getTileObject(Location loc) {
		return getChunk(loc.getChunk()).getTileObject(loc);
	}

	@Nullable
	public TileObject getTileObject(int x, int y, int z) {
		return getChunk(x >> 4, z >> 4).getTileObject(x, y, z);
	}

	public boolean hasTileObject(Block block) {
		return getChunk(block.getChunk()).hasTileObject(block);
	}

	public boolean hasTileObject(Location loc) {
		return getChunk(loc.getChunk()).hasTileObject(loc);
	}

	public boolean hasTileObject(int x, int y, int z) {
		return getChunk(x >> 4, z >> 4).hasTileObject(x, y, z);
	}

	public void setEffect(Block block, int effect) {
		getChunk(block.getChunk()).setEffect(block, effect);
	}

	public void setEffect(Location loc, int effect) {
		getChunk(loc.getChunk()).setEffect(loc, effect);
	}

	public void setEffect(int x, int y, int z, int effect) {
		getChunk(x >> 4, z >> 4).setEffect(x, y, z, effect);
	}

	public int removeEffect(Block block) {
		return getChunk(block.getChunk()).removeEffect(block);
	}

	public int removeEffect(Location loc) {
		return getChunk(loc.getChunk()).removeEffect(loc);
	}

	public int removeEffect(int x, int y, int z) {
		return getChunk(x >> 4, z >> 4).removeEffect(x, y, z);
	}

	public Effect[] getEffect(int id) {
		return worldEffects.get(id);
	}

	public int getEffect(Block block) {
		return getChunk(block.getChunk()).getEffect(block);
	}

	public int getEffect(Location loc) {
		return getChunk(loc.getChunk()).getEffect(loc);
	}

	public int getEffect(int x, int y, int z) {
		return getChunk(x >> 4, z >> 4).getEffect(x, y, z);
	}

	public boolean hasEffect(Block block) {
		return getChunk(block.getChunk()).hasEffect(block);
	}

	public boolean hasEffect(Location loc) {
		return getChunk(loc.getChunk()).hasEffect(loc);
	}

	public boolean hasEffect(int x, int y, int z) {
		return getChunk(x >> 4, z >> 4).hasEffect(x, y, z);
	}

	public List<Effect[]> getEffects(Block block, int range) {
		return getEffects(block.getX(), block.getY(), block.getZ(), range);
	}

	public List<Effect[]> getEffects(Location loc, int range) {
		return getEffects(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), range);
	}

	public List<Effect[]> getEffects(int x, int y, int z, int range) {
		List<Effect[]> list = new ArrayList<Effect[]>();
		for (int deltaX = -range; deltaX <= range; deltaX++) {
			for (int deltaY = -range; deltaY <= range; deltaY++) {
				for (int deltaZ = -range; deltaZ <= range; deltaZ++) {
					int effectId = getEffect(x + deltaX, y + deltaY, z + deltaZ);
					if (effectId != -1)
						list.add(worldEffects.get(effectId));
				}
			}
		}
		return list;
	}

	public IntList getEffectIds(Block block, int range) {
		return getEffectIds(block.getX(), block.getY(), block.getZ(), range);
	}

	public IntList getEffectIds(Location loc, int range) {
		return getEffectIds(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), range);
	}

	public IntList getEffectIds(int x, int y, int z, int range) {
		IntList list = new IntArrayList();
		for (int deltaX = -range; deltaX <= range; deltaX++) {
			for (int deltaY = -range; deltaY <= range; deltaY++) {
				for (int deltaZ = -range; deltaZ <= range; deltaZ++) {
					int effectId = getEffect(x + deltaX, y + deltaY, z + deltaZ);
					if (effectId != -1)
						list.add(effectId);
				}
			}
		}
		return list;
	}

	public void removeEffects(Block block, int range, EffectType<?> type) {
		removeEffects(block.getX(), block.getY(), block.getZ(), range, type);
	}

	public void removeEffects(Location loc, int range, EffectType<?> type) {
		removeEffects(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), range, type);
	}

	public void removeEffects(int x, int y, int z, int range, EffectType<?> type) {
		for (int deltaX = -range; deltaX <= range; deltaX++) {
			for (int deltaY = -range; deltaY <= range; deltaY++) {
				for (int deltaZ = -range; deltaZ <= range; deltaZ++) {
					int effectId = getEffect(x + deltaX, y + deltaY, z + deltaZ);
					if (effectId == 0)
						continue;
					Effect[] effects = getEffect(effectId);
					if (effects.length == 1 && effects[0].getType() == type)
						removeEffect(x + deltaX, y + deltaY, z + deltaZ);
				}
			}
		}
	}

	public void removeEffects(Block block, int range, EffectType<?>... types) {
		removeEffects(block.getX(), block.getY(), block.getZ(), range, types);
	}

	public void removeEffects(Location loc, int range, EffectType<?>... types) {
		removeEffects(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), range, types);
	}

	public void removeEffects(int x, int y, int z, int range, EffectType<?>... types) {
		for (int deltaX = -range; deltaX <= range; deltaX++) {
			for (int deltaY = -range; deltaY <= range; deltaY++) {
				for (int deltaZ = -range; deltaZ <= range; deltaZ++) {
					int effectId = getEffect(x + deltaX, y + deltaY, z + deltaZ);
					if (effectId == 0)
						continue;
					Effect[] effects = getEffect(effectId);
					if (effects.length == types.length) {
						boolean correct = true;
						for (int i = 0; i < effects.length; i++) {
							if (effects[i].getType() != types[i]) {
								correct = false;
								break;
							}
						}
						if (correct)
							removeEffect(x + deltaX, y + deltaY, z + deltaZ);
					}
				}
			}
		}
	}

	@Override
	public void setEventHandler(EventHandler handler, boolean isDefault) {
		this.eventHandler = handler;
		eventHandlerLoader.setHasChanged();
		eventHandlerLoader.setDefaultEventHandler(isDefault);
	}

	@Override
	public EventHandler getEventHandler() {
		return eventHandler;
	}

	@Override
	public void delete(boolean save) {
		WorldBuilder.delete(world, save);
	}

	@Override
	public void delete(boolean save, Consumer<World> unloadedCallback) {
		WorldBuilder.delete(world, save, unloadedCallback);
	}

	public void unload(boolean save) {
		WorldBuilder.unload(world, save);
	}

	public void addRegion(Region region) {
		worldRegions.add(this, region);
	}

	@Nullable
	public Region removeRegion(Location loc) {
		IRegion region = getRegion(loc);
		if (region.isRegion()) {
			Region region1 = (Region) region;
			removeRegion(region1);
			return region1;
		}
		return null;
	}

	public void updateRegion(Region region) {
		worldRegions.update(region);
	}

	public void removeRegion(Region region) {
		worldRegions.remove(region);
	}

	public IRegion getRegion(Location loc) {
		return getChunk(loc.getChunk()).getRegion(loc);
	}

	public IRegion getRegion(int x, int y, int z) {
		return getChunk(x >> 4, z >> 4).getRegion(x, y, z);
	}

	public WorldRegions getRegions() {
		return worldRegions;
	}

	public int addMarker(String name, Location loc) {
		return markerLoader.add(name, loc);
	}

	public int addMarker(String name, int x, int y, int z) {
		return markerLoader.add(name, x, y, z);
	}

	public void removeMarker(String name) {
		markerLoader.remove(name);
	}

	public void removeMarker(String name, Location loc) {
		markerLoader.remove(name, loc);
	}

	public void removeMarker(String name, int x, int y, int z) {
		markerLoader.remove(name, x, y, z);
	}

	public void removeMarker(String name, int index) {
		markerLoader.remove(name, index);
	}

	@Override
	public Location getMarkerLocation(String name) {
		return markerLoader.get(name);
	}

	@Override
	public Location[] getMarkerLocations(String name) {
		return markerLoader.getAll(name);
	}

	@Override
	public Location getSpawnLocation() {
		return world.getSpawnLocation();
	}

	public boolean hasMarker(String name) {
		return markerLoader.has(name);
	}

	public boolean hasMarker(String name, int amount) {
		return markerLoader.has(name, amount);
	}

	public boolean hasMinMarker(String name, int amount) {
		return markerLoader.hasMin(name, amount);
	}

	public WorldEffects getWorldEffects() {
		return worldEffects;
	}

	public void setForceSaveEventHandler(boolean forceSave) {
		eventHandlerLoader.setForceSave(forceSave);
	}

	public boolean doesForceSaveEventHandler() {
		return eventHandlerLoader.doesForceSave();
	}

	public boolean hasDefaultEventHandler() {
		return eventHandlerLoader.hasDefaultEventHandler();
	}

	public void addMenu(Object identifier, Menu menu) {
		menus.put(identifier, menu);
	}

	@Nullable
	public Menu removeMenu(Object identifier) {
		return menus.remove(identifier);
	}

	@Nullable
	public Menu replaceMenu(Object identifier, Menu menu) {
		return menus.replace(identifier, menu);
	}

	@Nullable
	public Menu getMenu(Object identifier) {
		return menus.get(identifier);
	}

	public Collection<Menu> getMenus() {
		return menus.values();
	}

	public Menu getOrCreateMenu(Object identifier, Supplier<Menu> supplier) {
		Menu menu = menus.get(identifier);
		if (menu == null)
			menus.put(identifier, menu = supplier.get());
		return menu;
	}

	public Menu getOrCreateMenu(Object identifier, Function<ServerPlayer, Menu> supplier, ServerPlayer serverPlayer) {
		Menu menu = menus.get(identifier);
		if (menu == null)
			menus.put(identifier, menu = supplier.apply(serverPlayer));
		return menu;
	}

	public <T> Menu getOrCreateMenu(Object identifier, Function<T, Menu> supplier, T t) {
		Menu menu = menus.get(identifier);
		if (menu == null)
			menus.put(identifier, menu = supplier.apply(t));
		return menu;
	}

	@Override
	public String getName() {
		return world.getName();
	}

	@Override
	public void registerPlayer(ServerPlayer serverPlayer) {
		this.serverPlayer.add(serverPlayer);
	}

	@Override
	public void unregisterPlayer(ServerPlayer serverPlayer) {
		this.serverPlayer.remove(serverPlayer);
	}

	@Override
	public List<ServerPlayer> getServerPlayer() {
		return serverPlayer;
	}

	@Override
	public List<Player> getPlayer() {
		return world.getPlayers();
	}

	@Override
	public int getPlayerSize() {
		return world.getPlayers().size();
	}

	@Override
	public byte getRegionType() {
		return RegionType.WORLD;
	}

	@Override
	public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> clazz) {
		return world.getEntitiesByClass(clazz);
	}

	@Override
	public Collection<Entity> getEntitiesByClasses(Class<?>... clazz) {
		return world.getEntitiesByClasses(clazz);
	}

	public <T> boolean setGameRule(GameRule<T> gameRule, T value) {
		return world.setGameRule(gameRule, value);
	}

	public <T> T getGameRuleValue(GameRule<T> gameRule) {
		return world.getGameRuleValue(gameRule);
	}

	public void removeBlock(Block block) {
		removeEffect(block);
		removeTileObject(block);
	}

	public void removeBlock(Location loc) {
		removeEffect(loc);
		removeTileObject(loc);
	}

	public void removeBlock(int x, int y, int z) {
		removeEffect(x, y, z);
		removeTileObject(x, y, z);
	}

	public void onPistonExpand(BlockPistonExtendEvent event) {
		// TODO scanning all blocks and then canceling seems bad!
		// Effect Blocks and Tile Objects should be moved!

//		for (Block block : event.getBlocks()) {
//			if (getEffect(block.getLocation()) != 0) {
//				event.setCancelled(true);
//				break;
//			}
//			if (getTileObject(block.getLocation()) != null) {
//				event.setCancelled(true);
//				break;
//			}
//		}
	}

	public void onInventoryCreative(ServerPlayer serverPlayer, InventoryCreativeEvent event) {
		// TODO: make this only detect world block middle mouse clicks
		// nothing inside the event seems to help!

//		if (event.getSlotType() == SlotType.QUICKBAR && event.getCurrentItem().getType() == Material.AIR
//				&& event.getCurrentItem().getAmount() == 0) {
//			System.out.println("SCAN");
//			Block targetBlock = serverPlayer.player.getTargetBlock((Set<Material>) null, 5);
//			if (targetBlock != null) {
//				int effectId = getEffect(targetBlock.getLocation());
//				if (effectId != 0)
//					event.setCursor(NBTHelper.setWorldEffect(event.getCursor(),
//							worldEffects.addTemp(new PlaceEffectBlockEffect(effectId))));
//			}
//		}
	}

	public static ServerWorld getByWorld(World world) {
		return WorldManager.getWorld(world);
	}

	public static List<ServerWorld> getWorlds() {
		return WorldManager.getWorlds();
	}
}
