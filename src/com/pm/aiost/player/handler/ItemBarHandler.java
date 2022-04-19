package com.pm.aiost.player.handler;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.pm.aiost.event.effect.effects.ItemBarEffect;
import com.pm.aiost.item.Items;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class ItemBarHandler {

	public static void send(ServerPlayer serverPlayer, String msg) {
		Player player = serverPlayer.player;
		PlayerInventory inv = player.getInventory();
		ItemStack mainHandItem = inv.getItemInMainHand();
		int slot = inv.getHeldItemSlot() + 36;
		PacketSender.send(player, PacketFactory.packetSetSlot(0, slot,
				prepareItemStack(mainHandItem, msg + switchItemBarMessageSuffix(serverPlayer))));

		if (serverPlayer.getItemBarItem() == null) {
			serverPlayer.addEffect(ItemBarEffect.INSTANCE);
			serverPlayer.setItemBarItem(mainHandItem, slot);
		}
	}

	private static String switchItemBarMessageSuffix(ServerPlayer serverPlayer) {
		if (serverPlayer.isItemBarSuffixAdded()) {
			serverPlayer.setItemBarSuffixAdded(false);
			return "";
		}
		serverPlayer.setItemBarSuffixAdded(true);
		return "§5";
	}

	private static ItemStack prepareItemStack(ItemStack is, String msg) {
		ItemStack clone;
		if (is.getAmount() == 0)
			clone = Items.get("invisible_item").clone();
		else
			clone = is.clone();
		return MetaHelper.set(clone, msg);
	}

	public static void clear(ServerPlayer serverPlayer) {
		PacketSender.send(serverPlayer.player,
				PacketFactory.packetSetSlot(0, serverPlayer.getItemBarSlot(), serverPlayer.getItemBarItem()));
		clearSilent(serverPlayer);
	}

	public static void clearSilent(ServerPlayer serverPlayer) {
		serverPlayer.removeEffect(ItemBarEffect.INSTANCE);
		serverPlayer.setItemBarItem(null, 0);
	}

	public static void clearCreative(ServerPlayer serverPlayer, InventoryClickEvent event) {
		// TODO: This does not work fully!
		if (event.getSlot() + 36 != serverPlayer.getItemBarSlot())
			PacketSender.send(serverPlayer.player,
					PacketFactory.packetSetSlot(0, serverPlayer.getItemBarSlot(), serverPlayer.getItemBarItem()));
		else
			event.setCancelled(true);
		clearSilent(serverPlayer);
	}
}
