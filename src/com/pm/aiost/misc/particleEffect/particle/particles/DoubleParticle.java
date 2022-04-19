package com.pm.aiost.misc.particleEffect.particle.particles;

import java.util.List;
import java.util.function.Consumer;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.menu.menus.request.DoubleParticleMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.particleEffect.particle.ParticleBuilder;
import com.pm.aiost.misc.particleEffect.particle.ParticleType;
import com.pm.aiost.misc.particleEffect.particle.ParticleTypes;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.misc.utils.nbt.custom.NBTCompound;
import com.pm.aiost.player.ServerPlayer;

public class DoubleParticle implements IParticle {

	protected IParticle particle1;
	protected IParticle particle2;

	public DoubleParticle() {
	}

	public DoubleParticle(IParticle particle1, IParticle particle2) {
		this.particle1 = particle1;
		this.particle2 = particle2;
	}

	@Override
	public void spawn(World world, double x, double y, double z, float yaw, float pitch) {
		particle1.spawn(world, x, y, z);
		particle2.spawn(world, x, y, z);
	}

	@Override
	public void spawn(double x, double y, double z, float yaw, float pitch, Iterable<Player> player) {
		particle1.spawn(x, y, z, player);
		particle2.spawn(x, y, z, player);
	}

	@Override
	public void load(ConfigurationSection section) {
		if (!section.contains("particle1") && !section.contains("particle2"))
			return;

		particle1 = ParticleBuilder.create(section.getConfigurationSection("particle1"));
		particle2 = ParticleBuilder.create(section.getConfigurationSection("particle2"));
	}

	@Override
	public void load(INBTTagCompound nbt) {
		particle1 = ParticleBuilder.create(nbt.getCompound("particle1"));
		particle2 = ParticleBuilder.create(nbt.getCompound("particle2"));
	}

	@Override
	public void save(INBTTagCompound nbt) {
		INBTTagCompound particleNbt;
		ParticleBuilder.save(particle1, particleNbt = new NBTCompound());
		nbt.set("particle1", particleNbt);
		ParticleBuilder.save(particle2, particleNbt = new NBTCompound());
		nbt.set("particle2", particleNbt);
	}

	@Override
	public ParticleType<? extends DoubleParticle> getType() {
		return ParticleTypes.DOUBLE_PARTICLE;
	}

	public void setParticle1(IParticle particle) {
		this.particle1 = particle;
	}

	public IParticle getParticle1() {
		return particle1;
	}

	public void setParticle2(IParticle particle) {
		this.particle2 = particle;
	}

	public IParticle getParticle2() {
		return particle2;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DoubleParticle == false)
			return false;
		if (!particle1.equals(((DoubleParticle) obj).particle1))
			return false;
		if (!particle2.equals(((DoubleParticle) obj).particle2))
			return false;
		return true;
	}

	@Override
	public MenuRequest getMenuRequest(Consumer<ServerPlayer> requestConsumer, Consumer<ServerPlayer> targetConsumer) {
		return new SingleMenuRequest(DoubleParticleMenu::new) {

			@Override
			public void onResult(ServerPlayer serverPlayer, Object obj) {
				IParticle[] arr = (IParticle[]) obj;
				DoubleParticle.this.particle1 = arr[0];
				DoubleParticle.this.particle2 = arr[1];
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

	@Override
	public void setDefault() {
		particle1 = IParticle.DEFAULT;
		particle2 = IParticle.DEFAULT;
	}

	@Override
	public void createDescription(List<String> list) {
		list.add(ChatColor.GRAY + "particle 1:");
		particle1.createDescription(list);
		list.add(null);
		list.add(ChatColor.GRAY + "particle 2:");
		particle2.createDescription(list);
		list.add(null);
	}
}
