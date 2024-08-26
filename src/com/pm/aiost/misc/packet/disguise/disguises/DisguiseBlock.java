package com.pm.aiost.misc.packet.disguise.disguises;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.misc.nms.NMS;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.disguise.Disguise;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData.DataValue;

public class DisguiseBlock implements Disguise {

	public static final List<DataValue<?>> DATA_WATCHER;

	static {
		DATA_WATCHER = Arrays
				.asList(DataValue.create(new EntityDataAccessor<Boolean>(5, EntityDataSerializers.BOOLEAN), true));
	}

	protected int blockId;

	public DisguiseBlock() {
	}

	public DisguiseBlock(Block block) {
		setBlock(block);
	}

	public DisguiseBlock(BlockData block) {
		setBlockData(block);
	}

	public DisguiseBlock(Material material) {
		setMaterial(material);
	}

	public DisguiseBlock(int blockId) {
		setBlockId(blockId);
	}

	@Override
	public void addPackets(LivingEntity entity, List<Object> packets) {
		Location loc = entity.getLocation();
		int id = entity.getEntityId();
		packets.add(PacketFactory.packetEntitySpawn(id, entity.getUniqueId(), loc.getX(), loc.getY(), loc.getZ(),
				loc.getYaw(), loc.getPitch(), AiostEntityTypes.FALLING_BLOCK, blockId));
		packets.add(PacketFactory.packetEntityMetadata(id, DATA_WATCHER));
	}

	@Override
	public void load(ConfigurationSection section) {
		String materialString = section.getString("material");
		if (materialString != null)
			setMaterial(Material.valueOf(materialString.toUpperCase()));
		else
			setBlockId(section.getInt("blockId"));
	}

	protected void setBlock(Block block) {
		setBlockData(block.getBlockData());
	}

	protected void setBlockData(BlockData block) {
		this.blockId = NMS.getBlockId(NMS.to(block));
	}

	public BlockData getBlockData() {
		return NMS.from(NMS.getByBlockId(blockId));
	}

	protected void setMaterial(Material material) {
		this.blockId = NMS.getBlockId(NMS.getBlock(material).defaultBlockState());
	}

	public Material getMaterial() {
		return getBlockData().getMaterial();
	}

	protected void setBlockId(int blockId) {
		this.blockId = blockId;
	}

	public int getBlockId() {
		return blockId;
	}
}
