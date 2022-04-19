package com.pm.aiost.misc.particleEffect.particle.particles.animations;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.configuration.ConfigurationSection;

import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.particleEffect.particle.ParticleType;
import com.pm.aiost.misc.particleEffect.particle.ParticleTypes;
import com.pm.aiost.misc.utils.ChatColor;
import com.pm.aiost.misc.utils.Geometric;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.player.ServerPlayer;

public class Sphere extends Ball {

	protected double height;

	public Sphere() {
	}

	public Sphere(IParticle particle, double radius) {
		this(particle, radius, radius, DEFAULT_UP_SIZE, DEFAULT_SIDE_SIZE);
	}

	public Sphere(IParticle particle, double radius, double height) {
		this(particle, radius, height, DEFAULT_UP_SIZE, DEFAULT_SIDE_SIZE);
	}

	public Sphere(IParticle particle, double radius, double height, int size) {
		this(particle, radius, height, size, size);
	}

	public Sphere(IParticle particle, double radius, double height, int upSize, int sideSize) {
		super(particle, radius, upSize, sideSize);
		this.height = height;
	}

	@Override
	public Sphere init() {
		coordinates = Geometric.sphere(radius, height, upSize, sideSize);
		return this;
	}

	@Override
	public void load(ConfigurationSection section) {
		super.load(section);
		height = section.getDouble("height", DEFAULT_RADIUS);
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		height = nbt.getDouble("height");
	}

	@Override
	public void save(INBTTagCompound nbt) {
		super.save(nbt);
		nbt.setDouble("height", height);
	}

	@Override
	public ParticleType<? extends Sphere> getType() {
		return ParticleTypes.SPHERE;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getHeight() {
		return height;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Sphere))
			return false;
		if (height != ((Sphere) obj).height)
			return false;
		return super.equals(obj);
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getAnimationMenuRequest(Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { () -> new NumberMenu(BOLD + "Choose radius"),
						() -> new NumberMenu(BOLD + "Choose height"), () -> new NumberMenu(BOLD + "Choose up size"),
						() -> new NumberMenu(BOLD + "Choose side size") },

				new Consumer[] { (radius) -> radius = (Double) radius, (height) -> height = (Double) height,
						(upSize) -> this.upSize = ((Double) upSize).intValue(),
						(sideSize) -> this.sideSize = ((Double) sideSize).intValue() });
	}

	@Override
	public void setDefault() {
		super.setDefault();
		height = DEFAULT_RADIUS;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "height: " + ChatColor.DARK_GRAY + height);
	}
}
