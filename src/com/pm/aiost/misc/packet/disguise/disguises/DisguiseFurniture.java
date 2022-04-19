package com.pm.aiost.misc.packet.disguise.disguises;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.item.ItemLoader;
import com.pm.aiost.item.Items;
import com.pm.aiost.item.custom.Slot;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.disguise.Disguise;
import com.pm.aiost.misc.packet.object.objects.Furniture;

public class DisguiseFurniture implements Disguise {

	// TODO: DisguiseFurnitures can surely be made better! Also change
	// PacketEncoder afterwards!

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
	public void addPackets(Player player, List<Object> packets) {
		Location loc = player.getLocation();
		int id = player.getEntityId();

		packets.add(PacketFactory.packetEntityLivingSpawn(id, player.getUniqueId(), Furniture.ARMOR_STAND_ID,
				loc.getX(), loc.getY() - 1.188, loc.getZ(), loc.getYaw(), loc.getPitch()));
		packets.add(PacketFactory.packetEntityMetadata(id, Furniture.DATA_WATCHER, true));
		if (is != null)
			packets.add(PacketFactory.packetEntityEquipment(id, Slot.HEAD.nmsSlot, is));
		else
			packets.add(
					PacketFactory.packetEntityEquipment(id, Slot.HEAD.nmsSlot, Furniture.FURNITURES.get(furnitureID)));
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

	public @Nonnull ItemStack getItemStack() {
		if (is != null)
			return is;
		return Furniture.FURNITURES.get(furnitureID).clone();
	}

	public @Nonnull ItemStack getItemStackDirect() {
		if (is != null)
			return is;
		return Furniture.FURNITURES.get(furnitureID);
	}
}
