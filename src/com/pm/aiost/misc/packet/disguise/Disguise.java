package com.pm.aiost.misc.packet.disguise;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.packet.PacketFactory;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public interface Disguise {

	public void addPackets(Player player, List<Object> packets);

	public default void removePackets(Player player, List<Object> packets) {
	}

	public void load(ConfigurationSection section);

	public static void addPlayerStatePackets(net.minecraft.world.entity.player.Player entityPlayer,
			List<Object> packets) {
		int id = entityPlayer.getId();
		packets.add(PacketFactory.packetEntityMetadata(id, entityPlayer.getEntityData().getNonDefaultValues()));
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			ItemStack equimentItem = entityPlayer.getItemBySlot(slot);
			if (equimentItem.getItem() != Items.AIR)
				packets.add(PacketFactory.packetEntityEquipment(id, slot, equimentItem));
		}
	}
}
