package com.pm.aiost.event.effect.blueprints;

import java.util.function.Consumer;

import org.bukkit.configuration.ConfigurationSection;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.NoMenuRequest.SimpleNoMenuRequest;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.player.ServerPlayer;

public abstract class SingletonEffect extends Effect {

	protected SingletonEffect() {
	}

	protected SingletonEffect(byte action) {
		super(action);
	}

	protected SingletonEffect(byte[] actions) {
		super(actions);
	}

	protected SingletonEffect(byte action, byte condition) {
		super(action, condition);
	}

	protected SingletonEffect(byte[] actions, byte condition) {
		super(actions, condition);
	}

	@Override
	public boolean equals(Effect effect) {
		if (getClass() != effect.getClass())
			return false;
		return true;
	}

	@Override
	public void load(ConfigurationSection section) {
	}

	@Override
	public void load(Effect effect) {
	}

	@Override
	public void load(INBTTagCompound nbt) {
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		return nbt;
	}

	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleNoMenuRequest(requestConsumer, targetConsumer);
	}

	@Override
	public void setDefault() {
	}
}
