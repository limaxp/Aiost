package com.pm.aiost.misc.particleEffect.particle.particles;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.menu.menus.request.MultiParticleMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.particleEffect.particle.ParticleBuilder;
import com.pm.aiost.misc.particleEffect.particle.ParticleType;
import com.pm.aiost.misc.particleEffect.particle.ParticleTypes;
import com.pm.aiost.misc.utils.nbt.NBTType;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagList;
import com.pm.aiost.misc.utils.nbt.custom.NBTCompound;
import com.pm.aiost.misc.utils.nbt.custom.NBTList;
import com.pm.aiost.misc.utils.nbt.custom.NBTListWrapper;
import com.pm.aiost.player.ServerPlayer;

public class MultiParticle implements IParticle {

	protected static final IParticle[] DEFAULT = new IParticle[] { IParticle.DEFAULT };

	protected IParticle[] particles;

	public MultiParticle() {
	}

	public MultiParticle(IParticle... particles) {
		this.particles = particles;
	}

	@Override
	public void spawn(World world, double x, double y, double z, float yaw, float pitch) {
		for (IParticle particle : particles)
			particle.spawn(world, x, y, z);
	}

	@Override
	public void spawn(double x, double y, double z, float yaw, float pitch, Iterable<Player> player) {
		for (IParticle particle : particles)
			particle.spawn(x, y, z, player);
	}

	@Override
	public void load(ConfigurationSection section) {
		if (!section.contains("particles"))
			return;

		ConfigurationSection particlesSection = section.getConfigurationSection("particles");
		int i = 0;
		Set<String> particleNames = particlesSection.getKeys(false);
		particles = new IParticle[particleNames.size()];
		for (String particleName : particleNames)
			particles[i++] = ParticleBuilder.create(particlesSection.getConfigurationSection(particleName));
	}

	@Override
	public void load(INBTTagCompound nbt) {
		INBTTagList nbtList = new NBTListWrapper(nbt.getList("particles", NBTType.COMPOUND));
		int size = nbtList.size();
		particles = new IParticle[size];
		for (int i = 0; i < size; i++)
			particles[i] = ParticleBuilder.create(nbtList.getCompound(i));
	}

	@Override
	public void save(INBTTagCompound nbt) {
		int size = particles.length;
		INBTTagList nbtList = new NBTList();
		for (int i = 0; i < size; i++) {
			INBTTagCompound particleNBT = new NBTCompound();
			ParticleBuilder.save(particles[i], particleNBT);
			nbtList.add(particleNBT);
		}
		nbt.set("particles", nbtList);
	}

	@Override
	public ParticleType<? extends MultiParticle> getType() {
		return ParticleTypes.MULTI_PARTICLE;
	}

	public void setParticles(IParticle[] particles) {
		this.particles = particles;
	}

	public IParticle[] getParticles() {
		return particles;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MultiParticle == false)
			return false;
		if (!particles.equals(((MultiParticle) obj).particles))
			return false;
		return true;
	}

	@Override
	public MenuRequest getMenuRequest(Consumer<ServerPlayer> requestConsumer, Consumer<ServerPlayer> targetConsumer) {
		return new SingleMenuRequest(new MultiParticleMenu()) {

			@Override
			public void onResult(ServerPlayer serverPlayer, Object obj) {
				@SuppressWarnings("unchecked")
				List<IParticle> list = (List<IParticle>) obj;
				MultiParticle.this.particles = list.toArray(new IParticle[list.size()]);
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
		particles = DEFAULT;
	}

	@Override
	public void createDescription(List<String> list) {
		for (int i = 0; i < particles.length; i++) {
			list.add(ChatColor.GRAY + "particle " + (i + 1) + ':');
			particles[i].createDescription(list);
			list.add(null);
		}
	}
}
