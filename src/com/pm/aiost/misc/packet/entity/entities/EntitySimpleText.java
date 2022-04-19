package com.pm.aiost.misc.packet.entity.entities;

import java.util.UUID;

import org.bukkit.entity.Player;

import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.misc.packet.entity.PacketEntity;
import com.pm.aiost.misc.packet.entity.PacketEntityType;
import com.pm.aiost.misc.packet.entity.PacketEntityTypes;
import com.pm.aiost.misc.packet.object.objects.Furniture;
import com.pm.aiost.misc.packet.object.objects.Hologram;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.server.world.ServerWorld;

public class EntitySimpleText extends PacketEntity {

	protected String text;

	public EntitySimpleText(ServerWorld world) {
		super(world);
	}

	public EntitySimpleText(ServerWorld world, String text) {
		super(world);
		this.text = text;
	}

	@Override
	public void spawn() {
		PacketSender.sendNear_(world.world, x, y, z, PACKET_OBJECT_VISIBILE_RANGE, createSpawnPacket(),
				createMetaDataPacket());
	}

	@Override
	public void spawn(Player player) {
		PacketSender.send_(player, createSpawnPacket(), createMetaDataPacket());
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		text = nbt.getString("text");
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		super.save(nbt);
		nbt.setString("text", text);
		return nbt;
	}

	@Override
	public Object createSpawnPacket() {
		return PacketFactory.packetEntityLivingSpawn(id, UUID.randomUUID(), Furniture.ARMOR_STAND_ID, x + 0.5, y,
				z + 0.5, 0, 0);
	}

	public Object createMetaDataPacket() {
		return PacketFactory.packetEntityMetadata(id, Hologram.createDataWatcher(text), true);
	}

	public String getText() {
		return text;
	}

	@Override
	public PacketEntityType<?> getPacketEntityType() {
		return PacketEntityTypes.ENTITY_SIMPLE_TEXT;
	}
}