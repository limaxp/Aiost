package com.pm.aiost.event.effect.effects;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.entity.LivingEntity;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.effect.blueprints.SimpleLivingEntityEffect;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.player.ServerPlayer;

public class KillEffect extends SimpleLivingEntityEffect {

	public KillEffect() {
	}

	public KillEffect(byte[] actions, byte condition) {
		super(actions, condition);
	}

	@Override
	public void runEffect(LivingEntity entity) {
		kill(entity);
	}

	public void kill(LivingEntity entity) {
		entity.setHealth(0);
	}

	@Override
	public boolean equals(Effect effect) {
		if (!super.equals(effect))
			return false;
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { EffectActionMenu::new, EffectConditionMenu::getMenu },
				new Consumer[] { this::setActions, this::setCondition });
	}

	@Override
	public EffectType<? extends KillEffect> getType() {
		return EffectTypes.KILL;
	}
}