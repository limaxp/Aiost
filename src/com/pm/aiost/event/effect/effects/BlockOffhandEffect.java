package com.pm.aiost.event.effect.effects;

import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.event.effect.EffectAction;
import com.pm.aiost.event.effect.EffectCondition;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.effect.blueprints.SingletonEffect;
import com.pm.aiost.event.events.PlayerEquipItemEvent;
import com.pm.aiost.item.Items;
import com.pm.aiost.player.ServerPlayer;

public class BlockOffhandEffect extends SingletonEffect {

	private static final byte[] ACTIONS = { EffectAction.EQUIP, EffectAction.UNEQUIP };
	private static ItemStack blockingItemStack;

	public static final BlockOffhandEffect INSTANCE = new BlockOffhandEffect();

	public static void init() {
		blockingItemStack = Items.get("stop_symbol");
	}

	protected BlockOffhandEffect() {
		super(ACTIONS, EffectCondition.SELF);
	}

	@Override
	public void onPlayerEquipItem(PlayerEquipItemEvent event) {
		if (!checkOffHandSlot(event))
			return;
		blockOffhand(event.getServerPlayer());
	}

	@Override
	public void onPlayerUnequipItem(PlayerEquipItemEvent event) {
		freeOffhand(event.getServerPlayer());
	}

	public static void blockOffhand(ServerPlayer serverPlayer) {
		Player player = serverPlayer.player;
		ItemStack offHand = player.getInventory().getItemInOffHand();
		serverPlayer.setItemInOffHand(blockingItemStack);
		if (offHand.getAmount() > 0)
			player.getWorld().dropItem(player.getLocation(), offHand);
	}

	public static void freeOffhand(ServerPlayer serverPlayer) {
		serverPlayer.setItemInOffHand(null);
	}

	public static boolean checkOffHandSlot(PlayerEquipItemEvent event) {
		if (event.getSlot() == EquipmentSlot.OFF_HAND) {
			Player player = event.getServerPlayer().player;
			player.getWorld().dropItem(player.getLocation(), event.getItemStack());
			player.setItemOnCursor(null);
			event.setCancelled(true);
			return false;
		}
		return true;
	}

	@Override
	public EffectType<? extends BlockOffhandEffect> getType() {
		return EffectTypes.BLOCK_OFF_HAND;
	}

	public static BlockOffhandEffect getInstance() {
		return INSTANCE;
	}
}
