package com.pm.aiost.misc.packet.object.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.misc.nms.NBT;
import com.pm.aiost.misc.nms.NBT.NBTType;
import com.pm.aiost.misc.nms.NMS;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.misc.packet.object.PacketObject;
import com.pm.aiost.misc.packet.object.PacketObjectType;
import com.pm.aiost.misc.packet.object.PacketObjectTypes;
import com.pm.aiost.server.world.ServerWorld;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData.DataValue;

public class Hologram extends PacketObject {

	public static final double ABS = 0.3D;
	public static final EntityDataAccessor<Byte> FLAG_WATCHER = new EntityDataAccessor<Byte>(0,
			EntityDataSerializers.BYTE);
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static final EntityDataAccessor<Optional<Component>> NAME_WATCHER = new EntityDataAccessor(2,
			EntityDataSerializers.COMPONENT);
	public static final EntityDataAccessor<Boolean> NAME_VISIBLE_WATCHER = new EntityDataAccessor<>(3,
			EntityDataSerializers.BOOLEAN);

	protected String[] text;
	protected Object[] spawnPackets;

	public Hologram(ServerWorld world) {
		super(world);
	}

	public Hologram(ServerWorld world, String text) {
		super(world);
		this.text = new String[] { text };
		id = super.generateId();
	}

	public Hologram(ServerWorld world, String[] text) {
		super(world);
		this.text = text;
		id = generateIds(text.length);
	}

	public Hologram(ServerWorld world, Collection<String> text) {
		super(world);
		int size = text.size();
		this.text = text.toArray(new String[size]);
		id = generateIds(size);
	}

	@Override
	protected int generateId() {
		return -1;
	}

	@Override
	public void spawn() {
		PacketSender.sendNear_(world.world, x, y, z, PACKET_OBJECT_VISIBILE_RANGE, spawnPackets = createSpawnPackets());
	}

	@Override
	public void show(Player player) {
		PacketSender.send_(player, spawnPackets);
	}

	@Override
	public void spawn(Player player) {
		PacketSender.send_(player, createSpawnPackets());
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		ListTag list = nbt.getList("text", NBTType.STRING);
		int size = list.size();
		id = generateIds(size);
		text = new String[size];
		for (int i = 0; i < size; i++)
			text[i] = list.get(i).getAsString();
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		super.save(nbt);
		ListTag list = new ListTag();
		nbt.put("text", list);
		for (int i = 0; i < text.length; i++)
			list.add(NBT.createNBTTagString(text[i]));
		return nbt;
	}

	@Override
	public Object createSpawnPacket() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object createRemovePacket() {
		int length = text.length;
		int[] ids = new int[length];
		ids[0] = id;
		for (int i = 1; i < length; i++)
			ids[i] = id + i;
		return PacketFactory.packetEntityDestroy(ids);
	}

	public Object[] createSpawnPackets() {
		int length = text.length;
		Object[] spawnPackets = new Object[length * 2];
		spawnPackets[0] = createSpawnPacket(0);
		spawnPackets[1] = createMetaDataPacket(0, text[0]);
		int index = 2;
		for (int i = 1; i < length; i++, index += 2) {
			spawnPackets[index] = createSpawnPacket(i);
			spawnPackets[index + 1] = createMetaDataPacket(i, text[i]);
		}
		return spawnPackets;
	}

	public Object createSpawnPacket(int index) {
		return PacketFactory.packetEntitySpawn(id + index, UUID.randomUUID(), x + 0.5, y - (ABS * index), z + 0.5, 0, 0,
				AiostEntityTypes.ARMOR_STAND);
	}

	public Object createMetaDataPacket(int index, String text) {
		return PacketFactory.packetEntityMetadata(id + index, createDataWatcher(text));
	}

	public static List<DataValue<?>> createDataWatcher(String text) {
		List<DataValue<?>> dataWatcher = new ArrayList<DataValue<?>>();
		dataWatcher.add(DataValue.create(FLAG_WATCHER, (byte) 0x20));
		dataWatcher.add(DataValue.create(NAME_WATCHER, Optional.ofNullable(NMS.createChatComponent(text))));
		dataWatcher.add(DataValue.create(NAME_VISIBLE_WATCHER, true));
		return dataWatcher;
	}

	public String getText() {
		return text[0];
	}

	public String getText(int index) {
		return text[index];
	}

	public String[] getTexts() {
		return text;
	}

	public int lineSize() {
		return text.length;
	}

	@Override
	public PacketObjectType<?> getPacketObjectType() {
		return PacketObjectTypes.HOLOGRAM;
	}
}
