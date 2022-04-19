package com.pm.aiost.event.effect.effects;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.pm.aiost.event.effect.EffectAction;
import com.pm.aiost.event.effect.EffectCondition;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.blueprints.SingletonEffect;
import com.pm.aiost.player.ServerPlayer;

public class ThirstEffect extends SingletonEffect {

	public static final ThirstEffect INSTANCE = new ThirstEffect();

	protected ThirstEffect() {
		super(EffectAction.TICK, EffectCondition.UNIQUE);
	}

	@Override
	public void onTick(ServerPlayer serverPlayer) {
		updateThirst(serverPlayer);
	}

	public static void updateThirst(ServerPlayer serverPlayer) {
		Player player = serverPlayer.player;
		if (serverPlayer.getThirst() > 0) {
			if (player.isSprinting())
				serverPlayer.removeThirst(0.04F);
			else if (player.isSneaking())
				serverPlayer.removeThirst(0.01F);
			else
				serverPlayer.removeThirst(0.02F);
		} else
			thirstDamage(player);
	}

	private static void thirstDamage(Player player) {
		if (player.getHealth() > 0.2) {
			player.damage(0.2);
			player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 10, 1));
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10, 1));
		} else {
			player.setHealth(0.0);
		}
	}

	@Override
	public EffectType<? extends SingletonEffect> getType() {
		return null;
	}
}
