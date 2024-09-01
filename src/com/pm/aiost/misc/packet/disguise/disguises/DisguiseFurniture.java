package com.pm.aiost.misc.packet.disguise.disguises;

import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.item.ItemLoader;
import com.pm.aiost.item.Items;
import com.pm.aiost.item.custom.Slot;
import com.pm.aiost.misc.nms.NMS;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.disguise.Disguise;
import com.pm.aiost.misc.packet.object.objects.Furniture;

public class DisguiseFurniture implements Disguise {

	protected int furnitureID;
	protected @Nullable ItemStack is;

	public DisguiseFurniture() {
	}

	public DisguiseFurniture(int furnitureID) {
		this.furnitureID = furnitureID;
	}

	public DisguiseFurniture(ItemStack is) {
		setItemStack(is);
	}

	@Override
	public void addPackets(LivingEntity entity, List<Object> packets) {
		Location loc = entity.getLocation();
		packets.add(PacketFactory.packetEntitySpawn(entity.getEntityId(), entity.getUniqueId(), loc.getX(),
				loc.getY() - 1.188, loc.getZ(), loc.getYaw(), loc.getPitch(), AiostEntityTypes.ARMOR_STAND));
		addDataPackets(entity, packets);
	}

	@Override
	public void addDataPackets(LivingEntity entity, List<Object> packets) {
		int id = entity.getEntityId();
		packets.add(PacketFactory.packetEntityMetadata(id, Furniture.DATA_WATCHER));
		if (is != null)
			packets.add(PacketFactory.packetEntityEquipment(id, Slot.HEAD.nmsSlot, NMS.to(is)));
		else
			packets.add(PacketFactory.packetEntityEquipment(id, Slot.HEAD.nmsSlot,
					NMS.to(Furniture.FURNITURES.get(furnitureID))));
	}

	@Override
	public void load(ConfigurationSection section) {
		if (section.contains("item"))
			setItemStack(ItemLoader.loadItem(section.get("item")));
		else
			this.furnitureID = section.getInt("furnitureId");
	}

	public int getType() {
		return furnitureID;
	}

	protected void setItemStack(@Nullable ItemStack is) {
		int index = Furniture.INDEX_MAP.getOrDefault((int) Items.getEffectID(is), -1);
		if (index != -1)
			this.furnitureID = index;
		else
			this.is = is;
	}

	public ItemStack getItemStack() {
		if (is != null)
			return is;
		return Furniture.FURNITURES.get(furnitureID).clone();
	}

	public ItemStack getItemStackDirect() {
		if (is != null)
			return is;
		return Furniture.FURNITURES.get(furnitureID);
	}
}
