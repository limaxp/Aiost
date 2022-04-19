package com.pm.aiost.misc.packet.disguise;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.packet.PacketFactory;

import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.EnumItemSlot;
import net.minecraft.server.v1_15_R1.Items;

public interface Disguise {

	public void addPackets(Player player, List<Object> packets);

	public default void removePackets(Player player, List<Object> packets) {
	}

	public void load(ConfigurationSection section);

	public static void addPlayerStatePackets(EntityPlayer entityPlayer, List<Object> packets) {
		int id = entityPlayer.getId();
		packets.add(PacketFactory.packetEntityMetadata(id, entityPlayer.getDataWatcher(), true));
		for (EnumItemSlot slot : EnumItemSlot.values()) {
			net.minecraft.server.v1_15_R1.ItemStack equimentItem = entityPlayer.getEquipment(slot);
			if (equimentItem.getItem() != Items.AIR)
				packets.add(PacketFactory.packetEntityEquipment(id, slot, equimentItem));
		}
	}
}
