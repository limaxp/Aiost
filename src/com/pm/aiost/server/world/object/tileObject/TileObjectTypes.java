package com.pm.aiost.server.world.object.tileObject;

import org.bukkit.Location;

import com.pm.aiost.misc.registry.AiostRegistry;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.misc.utils.nbt.custom.NBTCompoundWrapper;
import com.pm.aiost.server.world.ServerWorld;
import com.pm.aiost.server.world.chunk.ServerChunk;
import com.pm.aiost.server.world.object.tileObject.TileObjectType.TileObjectConstructor;
import com.pm.aiost.server.world.object.tileObject.tileObjects.BlinkingBlock;
import com.pm.aiost.server.world.object.tileObject.tileObjects.MovingBlock;
import com.pm.aiost.server.world.object.tileObject.tileObjects.ProximityFallingBlock;
import com.pm.aiost.server.world.object.tileObject.tileObjects.SlidingBlock;
import com.pm.aiost.server.world.object.tileObject.tileObjects.ProximityFallingBlock.ConstantProximityFallingBlock;

import net.minecraft.server.v1_15_R1.NBTTagCompound;

public class TileObjectTypes {

	public static final TileObjectType<BlinkingBlock> BLINKING_BLOCK = a(0, "blinking_block", BlinkingBlock::new);

	public static final TileObjectType<MovingBlock> MOVING_BLOCK = a(1, "moving_block", MovingBlock::new);

	public static final TileObjectType<SlidingBlock> SLIDING_BLOCK = a(2, "sliding_block", SlidingBlock::new);

	public static final TileObjectType<ProximityFallingBlock> PROXIMITY_FALLING_BLOCK = a(3, "proximity_falling_block",
			ProximityFallingBlock::new);

	public static final TileObjectType<ConstantProximityFallingBlock> CONSTANT_PROXIMITY_FALLING_BLOCK = a(4,
			"constant_proximity_falling_block", ConstantProximityFallingBlock::new);

	public static <T extends TileObject> TileObjectType<T> a(int id, String name,
			TileObjectConstructor<T> constructor) {
		TileObjectType<T> type = new TileObjectType<T>(id, name, constructor);
		AiostRegistry.TILE_OBJECTS.register(id, name, type);
		return type;
	}

	public static TileObject spawn(int id, ServerChunk chunk, NBTTagCompound nbt) {
		return spawn(AiostRegistry.TILE_OBJECTS.get(id).constructor, chunk, nbt);
	}

	public static <T extends TileObject> T spawn(TileObjectType<T> type, ServerChunk chunk, NBTTagCompound nbt) {
		return spawn(type.constructor, chunk, nbt);
	}

	public static <T extends TileObject> T spawn(TileObjectConstructor<T> constructor, ServerChunk chunk,
			NBTTagCompound nbt) {
		return spawn(constructor, chunk, new NBTCompoundWrapper(nbt));
	}

	public static TileObject spawn(int id, ServerChunk chunk, INBTTagCompound nbt) {
		return spawn(AiostRegistry.TILE_OBJECTS.get(id).constructor, chunk, nbt);
	}

	public static <T extends TileObject> T spawn(TileObjectType<T> type, ServerChunk chunk, INBTTagCompound nbt) {
		return spawn(type.constructor, chunk, nbt);
	}

	public static <T extends TileObject> T spawn(TileObjectConstructor<T> constructor, ServerChunk chunk,
			INBTTagCompound nbt) {
		T t = constructor.get(chunk.world);
		t.load(nbt);
		chunk.addTileObject(t);
		return t;
	}

	public static TileObject spawn(int id, Location loc, NBTTagCompound nbt) {
		return spawn(AiostRegistry.TILE_OBJECTS.get(id).constructor, loc, nbt);
	}

	public static <T extends TileObject> T spawn(TileObjectType<T> type, Location loc, NBTTagCompound nbt) {
		return spawn(type.constructor, loc, nbt);
	}

	public static <T extends TileObject> T spawn(TileObjectConstructor<T> constructor, Location loc,
			NBTTagCompound nbt) {
		return spawn(constructor, loc, new NBTCompoundWrapper(nbt));
	}

	public static TileObject spawn(int id, Location loc, INBTTagCompound nbt) {
		return spawn(AiostRegistry.TILE_OBJECTS.get(id).constructor, loc, nbt);
	}

	public static <T extends TileObject> T spawn(TileObjectType<T> type, Location loc, INBTTagCompound nbt) {
		return spawn(type.constructor, loc, nbt);
	}

	public static <T extends TileObject> T spawn(TileObjectConstructor<T> constructor, Location loc,
			INBTTagCompound nbt) {
		ServerWorld world = ServerWorld.getByWorld(loc.getWorld());
		T t = constructor.get(world);
		t.load(nbt);
		t.setPositionRotation(loc);
		world.addTileObject(t);
		return t;
	}

	public static TileObject spawn(int id, Location loc) {
		return spawn(AiostRegistry.TILE_OBJECTS.get(id).constructor, loc);
	}

	public static <T extends TileObject> T spawn(TileObjectType<T> type, Location loc) {
		return spawn(type.constructor, loc);
	}

	public static <T extends TileObject> T spawn(TileObjectConstructor<T> constructor, Location loc) {
		ServerWorld world = ServerWorld.getByWorld(loc.getWorld());
		T t = constructor.get(world);
		t.setPositionRotation(loc);
		world.addTileObject(t);
		return t;
	}

	public static void spawn(TileObject tileObject, Location loc) {
		tileObject.setPositionRotation(loc);
		tileObject.getWorld().addTileObject(tileObject);
	}

	public static void spawn(TileObject tileObject, int x, int y, int z) {
		tileObject.setPositionRotation(x, y, z);
		tileObject.getWorld().addTileObject(tileObject);
	}
}
