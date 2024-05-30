package com.pm.aiost.misc.packet.object.objects;

import java.util.UUID;

import org.bukkit.entity.Player;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.misc.packet.object.PacketObject;
import com.pm.aiost.misc.packet.object.PacketObjectType;
import com.pm.aiost.misc.packet.object.PacketObjectTypes;
import com.pm.aiost.server.world.ServerWorld;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;

public class SimpleText extends PacketObject {

	protected String text;

	public SimpleText(ServerWorld world) {
		super(world);
	}

	public SimpleText(ServerWorld world, String text) {
		super(world);
		this.text = text;
	}

	@Override
	public void spawn() {
		PacketSender.sendNearby(world.world, x, y, z, PACKET_OBJECT_VISIBILE_RANGE, createSpawnPacket(),
				createMetaDataPacket());
	}

	@Override
	public void spawn(Player player) {
		PacketSender.send(player, createSpawnPacket(), createMetaDataPacket());
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		text = nbt.getString("text");
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		super.save(nbt);
		nbt.putString("text", text);
		return nbt;
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return PacketFactory.packetEntitySpawn(id, UUID.randomUUID(), x + 0.5, y, z + 0.5, 0, 0,
				AiostEntityTypes.ARMOR_STAND);
	}

	public Packet<?> createMetaDataPacket() {
		return PacketFactory.packetEntityMetadata(id, Hologram.createDataWatcher(text));
	}

	public String getText() {
		return text;
	}

	@Override
	public PacketObjectType<?> getPacketObjectType() {
		return PacketObjectTypes.SIMPLE_TEXT;
	}
}