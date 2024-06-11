package com.pm.aiost.misc.packet.entity.entities;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.misc.nms.NMS;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.entity.PacketEntity;
import com.pm.aiost.misc.packet.entity.PacketEntityType;
import com.pm.aiost.misc.packet.entity.PacketEntityTypes;
import com.pm.aiost.server.world.ServerWorld;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;

public class PacketEntityFallingBlock extends PacketEntity {

	protected int blockId;

	public PacketEntityFallingBlock(ServerWorld world) {
		super(world);
	}

	public PacketEntityFallingBlock(ServerWorld world, Block block) {
		super(world);
		setBlock(block);
	}

	public PacketEntityFallingBlock(ServerWorld world, BlockData block) {
		super(world);
		setBlockData(block);
	}

	public PacketEntityFallingBlock(ServerWorld world, Material material) {
		super(world);
		setMaterial(material);
	}

	public PacketEntityFallingBlock(ServerWorld world, int blockId) {
		super(world);
		setBlockId(blockId);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		setBlockId(nbt.getInt("blockId"));
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		super.save(nbt);
		nbt.putInt("blockId", blockId);
		return nbt;
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return PacketFactory.packetEntitySpawn(id, uuid, x, y, z, yaw, pitch, AiostEntityTypes.FALLING_BLOCK, blockId);
	}

	@Override
	public String getName() {
		return NMS.getByBlockId(blockId).getBlock().getName().getString();
	}

	public void setBlock(Block block) {
		setBlockData(block.getBlockData());
	}

	public void setBlockData(BlockData block) {
		this.blockId = NMS.getBlockId(NMS.to(block));
	}

	public BlockData getBlockData() {
		return NMS.from(NMS.getByBlockId(blockId));
	}

	public void setMaterial(Material material) {
		this.blockId = NMS.getBlockId(NMS.getBlock(material).defaultBlockState());
	}

	public Material getMaterial() {
		return getBlockData().getMaterial();
	}

	public void setBlockId(int blockId) {
		this.blockId = blockId;
	}

	public int getBlockId() {
		return blockId;
	}

	@Override
	public PacketEntityType<?> getPacketEntityType() {
		return PacketEntityTypes.ENTITY_FALLING_BLOCK;
	}
}
