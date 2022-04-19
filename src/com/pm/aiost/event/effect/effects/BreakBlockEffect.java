package com.pm.aiost.event.effect.effects;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.block.Block;

import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.effect.blueprints.SimpleBlockEffect;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.utils.worldEdit.WorldEdit;
import com.pm.aiost.player.ServerPlayer;

public class BreakBlockEffect extends SimpleBlockEffect {

	public BreakBlockEffect() {
	}

	public BreakBlockEffect(byte[] actions, byte condition) {
		super(actions, condition);
	}

	@Override
	public void runEffect(Block block) {
		WorldEdit.breakBlock(block);
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
	public EffectType<? extends BreakBlockEffect> getType() {
		return EffectTypes.BREAK_BLOCK;
	}
}
