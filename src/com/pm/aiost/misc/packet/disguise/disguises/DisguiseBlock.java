package com.pm.aiost.misc.packet.disguise.disguises;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.dataWatcher.AiostDataWatcherObject;
import com.pm.aiost.entity.dataWatcher.AiostDataWatcherRegistry;
import com.pm.aiost.entity.dataWatcher.EmptyDataWatcher;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.disguise.Disguise;
import com.pm.aiost.misc.utils.nms.NMS;

public class DisguiseBlock implements Disguise {

	public static final EmptyDataWatcher DATA_WATCHER;

	static {
		DATA_WATCHER = new EmptyDataWatcher();
		DATA_WATCHER.register(new AiostDataWatcherObject<>(5, AiostDataWatcherRegistry.BOOLEAN), true);
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
	public void addPackets(Player player, List<Object> packets) {
		Location loc = player.getLocation();
		int id = player.getEntityId();
		packets.add(PacketFactory.packetEntitySpawn(id, player.getUniqueId(), loc.getX(), loc.getY(), loc.getZ(),
				loc.getYaw(), loc.getPitch(), AiostEntityTypes.FALLING_BLOCK, blockId, NMS.EMTPY_VEC_3D));
		packets.add(PacketFactory.packetEntityMetadata(id, DATA_WATCHER, true));
	}

	@Override
	public void load(ConfigurationSection section) {
		String materialString = section.getString("material");
		if (materialString != null)
			setMaterial(Material.valueOf(materialString.toUpperCase()));
		else
			setBlockId(section.getInt("blockId"));
	}

	public void setBlock(Block block) {
		setBlockData(block.getBlockData());
	}

	public void setBlockData(BlockData block) {
		this.blockId = NMS.getCombinedId(NMS.getNMS(block));
	}

	public BlockData getBlockData() {
		return NMS.getBukkit(NMS.getByCombinedId(blockId));
	}

	public void setMaterial(Material material) {
		this.blockId = NMS.getCombinedId(NMS.getBlock(material));
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
}
