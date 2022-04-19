package com.pm.aiost.event.effect.effects;

import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.event.effect.EffectHelper;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.player.ServerPlayer;

public class ThrowTNTEffect extends LaunchTNTEffect {

	@Override
	public void onPlayerInteract(ServerPlayer serverPlayer, PlayerInteractEvent event) {
		ItemStack is;
		if (event.getHand() == EquipmentSlot.HAND)
			is = serverPlayer.player.getInventory().getItemInMainHand();
		else
			is = serverPlayer.player.getInventory().getItemInOffHand();
		LaunchTNTEffect.launchTNT(event.getPlayer(), velocityMultiplier, damage, knockback, fuseTicks, effect);
		EffectHelper.decrementItemStack(serverPlayer, is, event.getHand(), event);
	}

	@Override
	public void onPlayerFish(ServerPlayer serverPlayer, PlayerFishEvent event) {
		ItemStack is = serverPlayer.lastRightClickedIS;
		LaunchTNTEffect.launchTNT(event.getPlayer(), velocityMultiplier, damage, knockback, fuseTicks, effect);
		EffectHelper.decrementItemStack(serverPlayer, is, serverPlayer.lastRightClickedEquipmentSlot, event);
	}

	@Override
	public void onPlayerProjectileLaunch(ServerPlayer serverPlayer, ProjectileLaunchEvent event) {
		ItemStack is = serverPlayer.lastRightClickedIS;
		LaunchTNTEffect.launchTNT(serverPlayer.player, velocityMultiplier, damage, knockback, fuseTicks, effect);
		EffectHelper.decrementItemStack(serverPlayer, is, serverPlayer.lastRightClickedEquipmentSlot, event);
	}

	@Override
	public EffectType<? extends ThrowTNTEffect> getType() {
		return EffectTypes.THROW_TNT;
	}
}
