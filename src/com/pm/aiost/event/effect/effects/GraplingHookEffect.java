package com.pm.aiost.event.effect.effects;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.util.Vector;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.player.ServerPlayer;

public class GraplingHookEffect extends Effect {

	public GraplingHookEffect() {
	}

	public GraplingHookEffect(byte[] actions, byte condition) {
		super(actions, condition);
	}

	@Override
	public void onPlayerFish(ServerPlayer serverPlayer, PlayerFishEvent event) {
		if (event.getState() == State.IN_GROUND)
			graplingHookEffect(event.getPlayer(), event.getHook().getLocation());
	}

	public static void graplingHookEffect(Player player, Location targetLocation) {
		final Vector vector = getVectorForPoints(player.getLocation(), targetLocation);
		player.setVelocity(vector);
	}

	private static Vector getVectorForPoints(Location l1, Location l2) {
		double g = -0.08;
		double d = l2.distance(l1);
		double t = d;
		double vX = (1.0 + 0.7 * t) * (l2.getX() - l1.getX()) / t;
		double vY = (1.0 + 0.3 * t) * (l2.getY() - l1.getY()) / t - 0.5 * g * t;
		double vZ = (1.0 + 0.7 * t) * (l2.getZ() - l1.getZ()) / t;
		return new Vector(vX, vY, vZ);

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
	public EffectType<? extends Effect> getType() {
		return EffectTypes.GRAPLING_HOOK;
	}
}
