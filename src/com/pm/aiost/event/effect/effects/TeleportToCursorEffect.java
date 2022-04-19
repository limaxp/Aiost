package com.pm.aiost.event.effect.effects;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;

import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.effect.blueprints.SimplePlayerEffect;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.player.ServerPlayer;

public class TeleportToCursorEffect extends SimplePlayerEffect {

	private static final int RANGE = 200;

	public TeleportToCursorEffect() {
	}

	public TeleportToCursorEffect(byte[] actions, byte condition) {
		super(actions, condition);
	}

	@Override
	public void runEffect(ServerPlayer serverPlayer) {
		teleportToCursor(serverPlayer.player);
	}

	public static void teleportToCursor(HumanEntity player) {
		Location loc = player.getLocation();
		Location target = player.getTargetBlock((Set<Material>) null, RANGE).getLocation().clone().add(0, 1, 0);
		target.setYaw(loc.getYaw());
		target.setPitch(loc.getPitch());
		player.teleport(target);
		player.playEffect(EntityEffect.ENTITY_POOF);
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
	public EffectType<? extends TeleportToCursorEffect> getType() {
		return EffectTypes.TELEPORT_TO_CURSOR;
	}
}
