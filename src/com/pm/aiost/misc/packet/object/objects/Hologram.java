package com.pm.aiost.misc.packet.object.objects;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.pm.aiost.entity.dataWatcher.AiostDataWatcherObject;
import com.pm.aiost.entity.dataWatcher.AiostDataWatcherRegistry;
import com.pm.aiost.entity.dataWatcher.EmptyDataWatcher;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.misc.packet.object.PacketObject;
import com.pm.aiost.misc.packet.object.PacketObjectType;
import com.pm.aiost.misc.packet.object.PacketObjectTypes;
import com.pm.aiost.misc.utils.nbt.NBTHelper;
import com.pm.aiost.misc.utils.nbt.NBTType;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagList;
import com.pm.aiost.misc.utils.nbt.custom.NBTList;
import com.pm.aiost.misc.utils.nbt.custom.NBTListWrapper;
import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.server.world.ServerWorld;

public class Hologram extends PacketObject {

	public static final double ABS = 0.3D;
	public static final AiostDataWatcherObject<Byte> FLAG_WATCHER = new AiostDataWatcherObject<>(0,
			AiostDataWatcherRegistry.BYTE);
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static final AiostDataWatcherObject<Optional<?>> NAME_WATCHER = new AiostDataWatcherObject(2,
			AiostDataWatcherRegistry.OPTIONAL_I_CHAT_BASE_COMPONENT);
	public static final AiostDataWatcherObject<Boolean> NAME_VISIBLE_WATCHER = new AiostDataWatcherObject<>(3,
			AiostDataWatcherRegistry.BOOLEAN);

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
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		INBTTagList list = new NBTListWrapper(nbt.getList("text", NBTType.STRING));
		int size = list.size();
		id = generateIds(size);
		text = new String[size];
		for (int i = 0; i < size; i++)
			text[i] = list.get(i).asString();
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		super.save(nbt);
		INBTTagList list = new NBTList();
		nbt.set("text", list);
		for (int i = 0; i < text.length; i++)
			list.add(NBTHelper.createNBTTagString(text[i]));
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
		return PacketFactory.packetEntityLivingSpawn(id + index, UUID.randomUUID(), Furniture.ARMOR_STAND_ID, x + 0.5,
				y - (ABS * index), z + 0.5, 0, 0);
	}

	public Object createMetaDataPacket(int index, String text) {
		return PacketFactory.packetEntityMetadata(id + index, createDataWatcher(text), true);
	}

	public static EmptyDataWatcher createDataWatcher(String text) {
		EmptyDataWatcher dataWatcher = new EmptyDataWatcher();
		dataWatcher.register(FLAG_WATCHER, (byte) 0x20);
		dataWatcher.register(NAME_WATCHER, Optional.ofNullable(NMS.createChatMessage(text)));
		dataWatcher.register(NAME_VISIBLE_WATCHER, true);
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
