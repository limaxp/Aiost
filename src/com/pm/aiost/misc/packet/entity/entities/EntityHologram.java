package com.pm.aiost.misc.packet.entity.entities;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.misc.nms.NBT;
import com.pm.aiost.misc.nms.NBT.NBTType;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.misc.packet.entity.PacketEntity;
import com.pm.aiost.misc.packet.entity.PacketEntityType;
import com.pm.aiost.misc.packet.entity.PacketEntityTypes;
import com.pm.aiost.misc.packet.object.objects.Hologram;
import com.pm.aiost.world.ServerWorld;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;

public class EntityHologram extends PacketEntity {

	protected String[] text;
	protected Packet<?>[] spawnPackets;

	public EntityHologram(ServerWorld world) {
		super(world);
	}

	public EntityHologram(ServerWorld world, String text) {
		super(world);
		this.text = new String[] { text };
		id = super.generateId();
	}

	public EntityHologram(ServerWorld world, String[] text) {
		super(world);
		this.text = text;
		id = generateIds(text.length);
	}

	public EntityHologram(ServerWorld world, Collection<String> text) {
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
		PacketSender.sendNearby(world.world, x, y, z, PACKET_OBJECT_VISIBILE_RANGE, spawnPackets = createSpawnPackets());
	}

	@Override
	public void show(Player player) {
		PacketSender.send(player, spawnPackets);
	}

	@Override
	public void spawn(Player player) {
		PacketSender.send(player, createSpawnPackets());
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
	public Packet<?> createSpawnPacket() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Packet<?> createRemovePacket() {
		int length = text.length;
		int[] ids = new int[length];
		ids[0] = id;
		for (int i = 1; i < length; i++)
			ids[i] = id + i;
		return PacketFactory.packetEntityDestroy(ids);
	}

	public Packet<?>[] createSpawnPackets() {
		int length = text.length;
		Packet<?>[] spawnPackets = new Packet[length * 2];
		spawnPackets[0] = createSpawnPacket(0);
		spawnPackets[1] = createMetaDataPacket(0, text[0]);
		int index = 2;
		for (int i = 1; i < length; i++, index += 2) {
			spawnPackets[index] = createSpawnPacket(i);
			spawnPackets[index + 1] = createMetaDataPacket(i, text[i]);
		}
		return spawnPackets;
	}

	public Packet<?> createSpawnPacket(int index) {
		return PacketFactory.packetEntitySpawn(id + index, UUID.randomUUID(), x, y - (Hologram.ABS * index), z, 0, 0,
				AiostEntityTypes.ARMOR_STAND);
	}

	public Packet<?> createMetaDataPacket(int index, String text) {
		return PacketFactory.packetEntityMetadata(id + index, Hologram.createDataWatcher(text));
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
	public PacketEntityType<?> getPacketEntityType() {
		return PacketEntityTypes.ENTITY_HOLOGRAM;
	}
}
