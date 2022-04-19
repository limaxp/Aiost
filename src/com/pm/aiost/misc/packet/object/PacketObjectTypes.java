package com.pm.aiost.misc.packet.object;

import org.bukkit.Location;

import com.pm.aiost.misc.packet.object.PacketObjectType.PacketObjectConstructor;
import com.pm.aiost.misc.packet.object.objects.Furniture;
import com.pm.aiost.misc.packet.object.objects.Hologram;
import com.pm.aiost.misc.packet.object.objects.Marker;
import com.pm.aiost.misc.packet.object.objects.SimpleText;
import com.pm.aiost.misc.packet.object.objects.TextDisplay;
import com.pm.aiost.misc.registry.AiostRegistry;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.misc.utils.nbt.custom.NBTCompoundWrapper;
import com.pm.aiost.server.world.ServerWorld;
import com.pm.aiost.server.world.chunk.ServerChunk;

import net.minecraft.server.v1_15_R1.NBTTagCompound;

public class PacketObjectTypes {

	public static final PacketObjectType<Furniture> FURNITURE = a(0, "furniture", Furniture::new);

	public static final PacketObjectType<SimpleText> SIMPLE_TEXT = a(1, "simple_text", SimpleText::new);

	public static final PacketObjectType<Marker> MARKER = a(2, "marker", Marker::new);

	public static final PacketObjectType<Hologram> HOLOGRAM = a(3, "hologram", Hologram::new);

	public static final PacketObjectType<TextDisplay> TEXT_DISPLAY = a(4, "text_display", TextDisplay::new);

	public static <T extends PacketObject> PacketObjectType<T> a(int id, String name,
			PacketObjectConstructor<T> constructor) {
		PacketObjectType<T> type = new PacketObjectType<T>(id, name, constructor);
		AiostRegistry.PACKET_OBJECTS.register(id, name, type);
		return type;
	}

	public static PacketObject spawn(int id, ServerChunk chunk, NBTTagCompound nbt) {
		return spawn(AiostRegistry.PACKET_OBJECTS.get(id).constructor, chunk, nbt);
	}

	public static <T extends PacketObject> T spawn(PacketObjectType<T> type, ServerChunk chunk, NBTTagCompound nbt) {
		return spawn(type.constructor, chunk, nbt);
	}

	public static <T extends PacketObject> T spawn(PacketObjectConstructor<T> constructor, ServerChunk chunk,
			NBTTagCompound nbt) {
		return spawn(constructor, chunk, new NBTCompoundWrapper(nbt));
	}

	public static PacketObject spawn(int id, ServerChunk chunk, INBTTagCompound nbt) {
		return spawn(AiostRegistry.PACKET_OBJECTS.get(id).constructor, chunk, nbt);
	}

	public static <T extends PacketObject> T spawn(PacketObjectType<T> type, ServerChunk chunk, INBTTagCompound nbt) {
		return spawn(type.constructor, chunk, nbt);
	}

	public static <T extends PacketObject> T spawn(PacketObjectConstructor<T> constructor, ServerChunk chunk,
			INBTTagCompound nbt) {
		T t = constructor.get(chunk.world);
		t.load(nbt);
		chunk.loadPacketObject(t);
		return t;
	}

	public static PacketObject spawn(int id, Location loc, NBTTagCompound nbt) {
		return spawn(AiostRegistry.PACKET_OBJECTS.get(id).constructor, loc, nbt);
	}

	public static <T extends PacketObject> T spawn(PacketObjectType<T> type, Location loc, NBTTagCompound nbt) {
		return spawn(type.constructor, loc, nbt);
	}

	public static <T extends PacketObject> T spawn(PacketObjectConstructor<T> constructor, Location loc,
			NBTTagCompound nbt) {
		return spawn(constructor, loc, new NBTCompoundWrapper(nbt));
	}

	public static PacketObject spawn(int id, Location loc, INBTTagCompound nbt) {
		return spawn(AiostRegistry.PACKET_OBJECTS.get(id).constructor, loc, nbt);
	}

	public static <T extends PacketObject> T spawn(PacketObjectType<T> type, Location loc, INBTTagCompound nbt) {
		return spawn(type.constructor, loc, nbt);
	}

	public static <T extends PacketObject> T spawn(PacketObjectConstructor<T> constructor, Location loc,
			INBTTagCompound nbt) {
		ServerWorld world = ServerWorld.getByWorld(loc.getWorld());
		T t = constructor.get(world);
		t.load(nbt);
		t.setPositionRotation(loc);
		world.addPacketObject(t);
		return t;
	}

	public static PacketObject spawn(int id, Location loc) {
		return spawn(AiostRegistry.PACKET_OBJECTS.get(id).constructor, loc);
	}

	public static <T extends PacketObject> T spawn(PacketObjectType<T> type, Location loc) {
		return spawn(type.constructor, loc);
	}

	public static <T extends PacketObject> T spawn(PacketObjectConstructor<T> constructor, Location loc) {
		ServerWorld world = ServerWorld.getByWorld(loc.getWorld());
		T t = constructor.get(world);
		t.setPositionRotation(loc);
		world.addPacketObject(t);
		return t;
	}

	public static void spawn(PacketObject packetObject, Location loc) {
		packetObject.setPositionRotation(loc);
		packetObject.world.addPacketObject(packetObject);
	}

	public static void spawn(PacketObject packetObject, int x, int y, int z) {
		packetObject.setPositionRotation(x, y, z, 0.0F, 0.0F);
		packetObject.world.addPacketObject(packetObject);
	}

	public static void spawn(PacketObject packetObject, int x, int y, int z, float yaw) {
		packetObject.setPositionRotation(x, y, z, yaw, 0.0F);
		packetObject.world.addPacketObject(packetObject);
	}

	public static void spawn(PacketObject packetObject, int x, int y, int z, float yaw, float pitch) {
		packetObject.setPositionRotation(x, y, z, yaw, pitch);
		packetObject.world.addPacketObject(packetObject);
	}
}
