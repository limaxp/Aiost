package com.pm.aiost.event.effect.effects;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerMoveEvent;

import com.pm.aiost.event.effect.EffectAction;
import com.pm.aiost.event.effect.EffectCondition;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.blueprints.SingletonEffect;
import com.pm.aiost.misc.packet.entity.entities.ChatHologram;
import com.pm.aiost.misc.packet.object.objects.Hologram;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.player.handler.ChatHologramHandler;

public class ChatHologramEffect extends SingletonEffect {

	private static final byte[] ACTIONS = new byte[] { EffectAction.MOVE };
	public static final ChatHologramEffect INSTANCE = new ChatHologramEffect();

	protected ChatHologramEffect() {
		super(ACTIONS, EffectCondition.UNIQUE);
	}

	@Override
	public void onPlayerMove(ServerPlayer serverPlayer, PlayerMoveEvent event) {
		Location loc = serverPlayer.player.getLocation();
		double x = loc.getX();
		double y = loc.getY() + ChatHologramHandler.Y_OFFSET;
		double z = loc.getZ();
		float yaw = loc.getYaw();
		float pitch = loc.getPitch();
		double yAdd = 0;
		for (ChatHologram chatHologram : serverPlayer.getChatHolograms())
			chatHologram.teleport(x, y + (yAdd += chatHologram.lineSize() * Hologram.ABS), z, yaw, pitch, false);
	}

	@Override
	public EffectType<? extends ChatHologramEffect> getType() {
		return null;
	}
}
