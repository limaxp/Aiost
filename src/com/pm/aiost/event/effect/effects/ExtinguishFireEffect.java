package com.pm.aiost.event.effect.effects;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.effect.blueprints.SimpleEffect;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.player.ServerPlayer;

public class ExtinguishFireEffect extends SimpleEffect {

	public ExtinguishFireEffect() {
	}

	public ExtinguishFireEffect(byte[] actions, byte condition) {
		super(actions, condition);
	}

	@Override
	public void runEffect(Entity entity) {
		extinguishFire(entity);
	}

	@Override
	public void runEffect(Block block) {
		extinguishFire(block);
	}

	public static void extinguishFire(Entity entity) {
		entity.setFireTicks(0);
		extinguishFire(entity.getLocation().getBlock());
	}

	public static void extinguishFire(Block block) {
		if (block.getType() == Material.FIRE)
			block.setType(Material.AIR);
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
	public EffectType<? extends ExtinguishFireEffect> getType() {
		return EffectTypes.EXTINGUISH_FIRE;
	}
}