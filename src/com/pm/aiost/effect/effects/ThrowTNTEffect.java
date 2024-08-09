package com.pm.aiost.effect.effects;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.effect.Effect;
import com.pm.aiost.effect.EffectHelper;
import com.pm.aiost.effect.EffectType;
import com.pm.aiost.effect.EffectTypes;
import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.misc.event.eventHandler.handler.ProjectileEventHandler;
import com.pm.aiost.misc.utils.ProjectileHelper;
import com.pm.aiost.player.ServerPlayer;

public class ThrowTNTEffect extends LaunchTNTEffect {

	@Override
	public void onPlayerInteract(ServerPlayer serverPlayer, PlayerInteractEvent event) {
		ItemStack is;
		if (event.getHand() == EquipmentSlot.HAND)
			is = serverPlayer.player.getInventory().getItemInMainHand();
		else
			is = serverPlayer.player.getInventory().getItemInOffHand();
		throwTNT(event.getPlayer(), velocityMultiplier, damage, knockback, fuseTicks, effect);
		EffectHelper.decrementItemStack(serverPlayer, is, event.getHand(), event);
	}

	@Override
	public void onPlayerFish(ServerPlayer serverPlayer, PlayerFishEvent event) {
		ItemStack is = serverPlayer.lastRightClickedIS;
		throwTNT(event.getPlayer(), velocityMultiplier, damage, knockback, fuseTicks, effect);
		EffectHelper.decrementItemStack(serverPlayer, is, serverPlayer.lastRightClickedEquipmentSlot, event);
	}

	@Override
	public void onPlayerProjectileLaunch(ServerPlayer serverPlayer, ProjectileLaunchEvent event) {
		ItemStack is = serverPlayer.lastRightClickedIS;
		throwTNT(serverPlayer.player, velocityMultiplier, damage, knockback, fuseTicks, effect);
		EffectHelper.decrementItemStack(serverPlayer, is, serverPlayer.lastRightClickedEquipmentSlot, event);
	}

	public static void throwTNT(LivingEntity entity, float velocityMultiplier, float damage, float knockback,
			int fuseTicks, Effect effect) {
		ProjectileEventHandler handler = new ProjectileEventHandler(entity);
		if (ProjectileHelper.launchProjectile(entity, AiostEntityTypes.TNT_PROJECTILE, 0.5F, velocityMultiplier * 0.4F,
				0.95F, handler, false) != null) {
			handler.setDamage(damage);
			handler.setKnockback(knockback);
			handler.setEffect(effect);
			handler.setDuration(fuseTicks);
			handler.setExplode(true);
		}
	}

	@Override
	public EffectType<? extends ThrowTNTEffect> getType() {
		return EffectTypes.THROW_TNT;
	}
}
