package com.pm.aiost.server.world.chunk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2IntMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.IntArrayList;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.IntList;

import com.pm.aiost.collection.list.UnorderedIdentityArrayList;
import com.pm.aiost.misc.packet.entity.PacketEntity;
import com.pm.aiost.misc.packet.entity.PacketEntityType;
import com.pm.aiost.misc.packet.object.PacketObject;
import com.pm.aiost.misc.packet.object.PacketObjectType;
import com.pm.aiost.server.world.ServerWorld;
import com.pm.aiost.server.world.object.TickingObject;
import com.pm.aiost.server.world.object.tileObject.TileObject;
import com.pm.aiost.server.world.region.IRegion;
import com.pm.aiost.server.world.region.Region;
import com.pm.aiost.server.world.region.WorldRegions;

public class ServerChunk {

	private static final int SECTION_SIZE = 16;

	public final ServerWorld world;
	public final Chunk chunk;
	private final long key;
	protected final ServerChunkSection[] sections;
	protected final Int2ObjectMap<PacketObject> packetObjects;
	protected final List<PacketEntity> packetEntities;
	protected final Int2IntMap blockEffects;
	protected final IntList regions;
	protected final Int2ObjectMap<TileObject> tileObjects;

	public ServerChunk(Chunk chunk) {
		world = ServerWorld.getByWorld(chunk.getWorld());
		this.chunk = chunk;
		key = getKey(chunk);
		sections = new ServerChunkSection[SECTION_SIZE];
		packetObjects = new Int2ObjectOpenHashMap<PacketObject>();
		packetEntities = new UnorderedIdentityArrayList<PacketEntity>();
		blockEffects = new Int2IntOpenHashMap();
		regions = new IntArrayList();
		tileObjects = new Int2ObjectOpenHashMap<TileObject>();
	}

	public ServerChunkSection getSection(int index) {
		return sections[index];
	}

	public ServerChunkSection getSectionFromY(int y) {
		return sections[y >> 4];
	}

	public void addPacketObject(PacketObject packetObject) {
		PacketObject prev = packetObjects
				.putIfAbsent(ServerChunkSection.getKey(packetObject.x, packetObject.y, packetObject.z), packetObject);
		if (prev == null) {
			world.addPacketThing(packetObject);
			packetObject.spawn();
		}
	}

	public void loadPacketObject(PacketObject packetObject) {
		packetObjects.put(ServerChunkSection.getKey(packetObject.x, packetObject.y, packetObject.z), packetObject);
		world.addPacketThing(packetObject);
	}

	@Nullable
	public PacketObject removePacketObject(Block block) {
		PacketObject packetObject = packetObjects.remove(ServerChunkSection.getKey(block));
		world.removePacketThing(packetObject);
		return packetObject;
	}

	@Nullable
	public PacketObject removePacketObject(Location loc) {
		PacketObject packetObject = packetObjects.remove(ServerChunkSection.getKey(loc));
		world.removePacketThing(packetObject);
		return packetObject;
	}

	@Nullable
	public PacketObject removePacketObject(int x, int y, int z) {
		PacketObject packetObject = packetObjects.remove(ServerChunkSection.getKey(x, y, z));
		world.removePacketThing(packetObject);
		return packetObject;
	}

	@Nullable
	public PacketObject getPacketObject(Block block) {
		return packetObjects.get(ServerChunkSection.getKey(block));
	}

	@Nullable
	public PacketObject getPacketObject(Location loc) {
		return packetObjects.get(ServerChunkSection.getKey(loc));
	}

	@Nullable
	public PacketObject getPacketObject(int x, int y, int z) {
		return packetObjects.get(ServerChunkSection.getKey(x, y, z));
	}

	public Collection<PacketObject> getPacketObjects() {
		return packetObjects.values();
	}

	public List<PacketObject> getPacketObjects(PacketObjectType<?> type) {
		List<PacketObject> list = new ArrayList<PacketObject>();
		for (Iterator<PacketObject> iterator = packetObjects.values().iterator(); iterator.hasNext();) {
			PacketObject packetObject = (PacketObject) iterator.next();
			if (packetObject.getPacketObjectType() == type)
				list.add(packetObject);
		}
		return list;
	}

	public boolean hasPacketObject(Block block) {
		return packetObjects.containsKey(ServerChunkSection.getKey(block));
	}

	public boolean hasPacketObject(Location loc) {
		return packetObjects.containsKey(ServerChunkSection.getKey(loc));
	}

	public boolean hasPacketObject(int x, int y, int z) {
		return packetObjects.containsKey(ServerChunkSection.getKey(x, y, z));
	}

	public void addPacketEntity(PacketEntity packetEntity) {
		packetEntities.add(packetEntity);
		world.addPacketThing(packetEntity);
		packetEntity.spawn();
	}

	public void loadPacketEntity(PacketEntity packetEntity) {
		packetEntities.add(packetEntity);
		world.addPacketThing(packetEntity);
	}

	public void removePacketEntity(PacketEntity packetEntity) {
		packetEntities.remove(packetEntity);
		world.removePacketThing(packetEntity);
	}

	public List<PacketEntity> getPacketEntities() {
		return packetEntities;
	}

	public List<PacketEntity> getPacketEntities(PacketEntityType<?> type) {
		List<PacketEntity> list = new ArrayList<PacketEntity>();
		for (PacketEntity packetEntity : packetEntities) {
			if (packetEntity.getPacketEntityType() == type)
				list.add(packetEntity);
		}
		return list;
	}

	public void addTileObject(TileObject tileObject) {
		TileObject prev = tileObjects.putIfAbsent(ServerChunkSection.getKey(tileObject.x, tileObject.y, tileObject.z),
				tileObject);
		if (prev == null && tileObject instanceof TickingObject)
			world.addTickingObject((TickingObject) tileObject);
	}

	@Nullable
	public TileObject removeTileObject(Block block) {
		TileObject tileObject = tileObjects.remove(ServerChunkSection.getKey(block));
		if (tileObject instanceof TickingObject)
			world.removeTickingObject((TickingObject) tileObject);
		return tileObject;
	}

	@Nullable
	public TileObject removeTileObject(Location loc) {
		TileObject tileObject = tileObjects.remove(ServerChunkSection.getKey(loc));
		if (tileObject instanceof TickingObject)
			world.removeTickingObject((TickingObject) tileObject);
		return tileObject;
	}

	@Nullable
	public TileObject removeTileObject(int x, int y, int z) {
		TileObject tileObject = tileObjects.remove(ServerChunkSection.getKey(x, y, z));
		if (tileObject instanceof TickingObject)
			world.removeTickingObject((TickingObject) tileObject);
		return tileObject;
	}

	@Nullable
	public TileObject getTileObject(Block block) {
		return tileObjects.get(ServerChunkSection.getKey(block));
	}

	@Nullable
	public TileObject getTileObject(Location loc) {
		return tileObjects.get(ServerChunkSection.getKey(loc));
	}

	@Nullable
	public TileObject getTileObject(int x, int y, int z) {
		return tileObjects.get(ServerChunkSection.getKey(x, y, z));
	}

	public Collection<TileObject> getTileObjects() {
		return tileObjects.values();
	}

	public boolean hasTileObject(Block block) {
		return tileObjects.containsKey(ServerChunkSection.getKey(block));
	}

	public boolean hasTileObject(Location loc) {
		return tileObjects.containsKey(ServerChunkSection.getKey(loc));
	}

	public boolean hasTileObject(int x, int y, int z) {
		return tileObjects.containsKey(ServerChunkSection.getKey(x, y, z));
	}

	public void setEffect(Block block, int effect) {
		blockEffects.put(ServerChunkSection.getKey(block), effect);
	}

	public void setEffect(Location loc, int effect) {
		blockEffects.put(ServerChunkSection.getKey(loc), effect);
	}

	public void setEffect(int x, int y, int z, int effect) {
		blockEffects.put(ServerChunkSection.getKey(x, y, z), effect);
	}

	public int removeEffect(Block block) {
		return blockEffects.remove(ServerChunkSection.getKey(block));
	}

	public int removeEffect(Location loc) {
		return blockEffects.remove(ServerChunkSection.getKey(loc));
	}

	public int removeEffect(int x, int y, int z) {
		return blockEffects.remove(ServerChunkSection.getKey(x, y, z));
	}

	public int getEffect(Block block) {
		return blockEffects.get(ServerChunkSection.getKey(block));
	}

	public int getEffect(Location loc) {
		return blockEffects.get(ServerChunkSection.getKey(loc));
	}

	public int getEffect(int x, int y, int z) {
		return blockEffects.get(ServerChunkSection.getKey(x, y, z));
	}

	public boolean hasEffect(Block block) {
		return blockEffects.get(ServerChunkSection.getKey(block)) != 0;
	}

	public boolean hasEffect(Location loc) {
		return blockEffects.get(ServerChunkSection.getKey(loc)) != 0;
	}

	public boolean hasEffect(int x, int y, int z) {
		return blockEffects.get(ServerChunkSection.getKey(x, y, z)) != 0;
	}

	public void addRegion(int region) {
		regions.add(region);
	}

	public void removeRegion(int region) {
		regions.rem(region);
	}

	public IRegion getRegion(Location loc) {
		WorldRegions worldRegions = world.getRegions();
		int length = regions.size();
		for (int i = 0; i < length; i++) {
			Region region = worldRegions.get(world, regions.getInt(i));
			if (region.contains(loc))
				return region;
		}
		return world;
	}

	public IRegion getRegion(int x, int y, int z) {
		WorldRegions worldRegions = world.getRegions();
		int length = regions.size();
		for (int i = 0; i < length; i++) {
			Region region = worldRegions.get(world, regions.getInt(i));
			if (region.contains(x, y, z))
				return region;
		}
		return world;
	}

	public long getKey() {
		return key;
	}

	public static long getKey(Chunk chunk) {
		return getKey(chunk.getX(), chunk.getZ());
	}

	public static long getKey(int x, int z) {
		return x & 0xFFFFFFFFL | (z & 0xFFFFFFFFL) << 32;
	}
}
