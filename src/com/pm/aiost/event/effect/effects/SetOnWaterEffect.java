package com.pm.aiost.event.effect.effects;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.entity.ProjectileHitEvent;

import com.pm.aiost.Aiost;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.effect.blueprints.SimpleBlockEffect;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.player.ServerPlayer;

public class SetOnWaterEffect extends SimpleBlockEffect {

	public SetOnWaterEffect() {
	}

	public SetOnWaterEffect(byte[] actions, byte condition) {
		super(actions, condition);
	}

	@Override
	public void runEffect(Block block) {
		setOnWater(block);
	}

	@Override
	public void onProjectileHit(ProjectileHitEvent event) {
		Block block = event.getHitBlock() == null ? event.getHitEntity().getLocation().getBlock()
				: event.getHitBlock().getRelative(BlockFace.UP);
		runEffect(block);
	}

	public static void setOnWater(Block block) {
		if (block.getType() == Material.AIR || block.getType() == Material.FIRE) {
			block.setType(Material.WATER);
			Bukkit.getScheduler().runTaskLater(Aiost.getPlugin(), () -> {
				if (block.getType() == Material.WATER)
					block.setType(Material.AIR);
			}, 10);
		}
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
	public EffectType<? extends SetOnWaterEffect> getType() {
		return EffectTypes.SET_ON_WATER;
	}
}