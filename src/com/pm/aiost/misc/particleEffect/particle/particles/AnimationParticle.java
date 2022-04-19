package com.pm.aiost.misc.particleEffect.particle.particles;

import java.util.List;
import java.util.function.Consumer;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.misc.menu.menus.request.AnimationParticleMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.NoMenuRequest.SimpleNoMenuRequest;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.particleEffect.particle.ParticleBuilder;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.misc.utils.nbt.custom.NBTCompound;
import com.pm.aiost.player.ServerPlayer;

public abstract class AnimationParticle implements IParticle {

	protected IParticle particle;

	public AnimationParticle() {
	}

	public AnimationParticle(IParticle particle) {
		this.particle = particle;
	}

	@Override
	public AnimationParticle init() {
		return this;
	}

	@Override
	public void load(ConfigurationSection section) {
		particle = ParticleBuilder.createSubtype(section);
	}

	@Override
	public void load(INBTTagCompound nbt) {
		particle = ParticleBuilder.create(nbt.getCompound("particle"));
	}

	@Override
	public void save(INBTTagCompound nbt) {
		INBTTagCompound particleNbt;
		ParticleBuilder.save(particle, particleNbt = new NBTCompound());
		nbt.set("particle", particleNbt);
	}

	public void setParticle(IParticle particle) {
		this.particle = particle;
	}

	public IParticle getParticle() {
		return particle;
	}

	@Override
	public boolean equals(Object obj) {
		return particle.equals(((AnimationParticle) obj).particle);
	}

	@Override
	public MenuRequest getMenuRequest(Consumer<ServerPlayer> requestConsumer, Consumer<ServerPlayer> targetConsumer) {
		return new SingleMenuRequest(new AnimationParticleMenu(this)) {

			@Override
			public void onResult(ServerPlayer serverPlayer, Object obj) {
			}

			@Override
			public void openRequest(ServerPlayer serverPlayer) {
				requestConsumer.accept(serverPlayer);
			}

			@Override
			public void openTarget(ServerPlayer serverPlayer) {
				targetConsumer.accept(serverPlayer);
			}
		};
	}

	public MenuRequest getAnimationMenuRequest(Menu requestMenu) {
		return getAnimationMenuRequest(requestMenu::open);
	}

	public MenuRequest getAnimationMenuRequest(Consumer<ServerPlayer> requestConsumer) {
		return getAnimationMenuRequest(requestConsumer, requestConsumer);
	}

	public MenuRequest getAnimationMenuRequest(Menu requestMenu, Menu resultMenu) {
		return getAnimationMenuRequest(requestMenu::open, resultMenu::open);
	}

	public MenuRequest getAnimationMenuRequest(Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleNoMenuRequest(requestConsumer, targetConsumer);
	}

	@Override
	public void setDefault() {
		particle = IParticle.DEFAULT;
	}

	@Override
	public void createDescription(List<String> list) {
		particle.createDescription(list);
		list.add(ChatColor.GRAY + "animation: " + ChatColor.DARK_GRAY + getType().displayName);
	}
}
