package com.pm.aiost.event.effect.effects;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.effect.blueprints.SimplePlayerEffect;
import com.pm.aiost.player.ServerPlayer;

public class SpawnPointEffect extends SimplePlayerEffect {

	public SpawnPointEffect() {
	}

	public SpawnPointEffect(byte[] actions, byte condition) {
		super(actions, condition);
	}

	@Override
	public void runEffect(ServerPlayer serverPlayer) {
		serverPlayer.player.setBedSpawnLocation(serverPlayer.player.getLocation().getBlock().getLocation());
	}

	@Override
	public EffectType<? extends Effect> getType() {
		return EffectTypes.SPAWNPOINT;
	}
}