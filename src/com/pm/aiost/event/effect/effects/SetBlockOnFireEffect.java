package com.pm.aiost.event.effect.effects;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.entity.ProjectileHitEvent;

import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.effect.blueprints.SimpleBlockEffect;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.player.ServerPlayer;

public class SetBlockOnFireEffect extends SimpleBlockEffect {

	public SetBlockOnFireEffect() {
	}

	public SetBlockOnFireEffect(byte[] actions, byte condition) {
		super(actions, condition);
	}

	@Override
	public void runEffect(Block block) {
		setOnFire(block);
	}

	@Override
	public void onProjectileHit(ProjectileHitEvent event) {
		Block block = event.getHitBlock() == null ? event.getHitEntity().getLocation().getBlock()
				: event.getHitBlock().getRelative(BlockFace.UP);
		runEffect(block);
	}

	public static void setOnFire(Block block) {
		if (block.getType() == Material.AIR)
			block.setType(Material.FIRE);
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
	public EffectType<? extends SetBlockOnFireEffect> getType() {
		return EffectTypes.SET_BLOCK_ON_FIRE;
	}
}