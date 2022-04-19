package com.pm.aiost.misc.particleEffect.particle.particles.animations;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.particleEffect.particle.ParticleType;
import com.pm.aiost.misc.particleEffect.particle.ParticleTypes;
import com.pm.aiost.misc.particleEffect.particle.particles.AnimationParticle;
import com.pm.aiost.misc.utils.ChatColor;
import com.pm.aiost.misc.utils.Geometric;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.player.ServerPlayer;

public class Ring extends AnimationParticle {

	protected static final double DEFAULT_RADIUS = 0.5;
	protected static final int DEFAULT_SIZE = 18;

	protected double radius;
	protected int size;
	protected double[] coordinates;

	public Ring() {
	}

	public Ring(IParticle particle, double radius) {
		this(particle, radius, DEFAULT_SIZE);
	}

	public Ring(IParticle particle, double radius, int size) {
		super(particle);
		this.radius = radius;
		this.size = size;
	}

	@Override
	public Ring init() {
		coordinates = Geometric.horizontalRing(radius, size);
		return this;
	}

	@Override
	public void spawn(World world, double x, double y, double z, float yaw, float pitch) {
		for (int i = 0; i < coordinates.length; i += 2)
			particle.spawn(world, x + coordinates[i], y, z + coordinates[i + 1]);
	}

	@Override
	public void spawn(double x, double y, double z, float yaw, float pitch, Iterable<Player> player) {
		for (int i = 0; i < coordinates.length; i += 2)
			particle.spawn(x + coordinates[i], y, z + coordinates[i + 1], player);
	}

	@Override
	public void load(ConfigurationSection section) {
		super.load(section);
		radius = section.getDouble("radius", DEFAULT_RADIUS);
		size = section.getInt("size", DEFAULT_SIZE);
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		radius = nbt.getDouble("radius");
		size = nbt.getInt("size");
	}

	@Override
	public void save(INBTTagCompound nbt) {
		super.save(nbt);
		nbt.setDouble("radius", radius);
		nbt.setInt("size", size);
	}

	@Override
	public ParticleType<? extends Ring> getType() {
		return ParticleTypes.RING;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getRadius() {
		return radius;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Ring))
			return false;
		Ring other = (Ring) obj;
		if (radius != other.radius)
			return false;
		if (size != other.size)
			return false;
		return super.equals(obj);
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getAnimationMenuRequest(Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { () -> new NumberMenu(BOLD + "Choose radius"),
						() -> new NumberMenu(BOLD + "Choose size") },

				new Consumer[] { (radius) -> this.radius = (Double) radius,
						(size) -> this.size = ((Double) size).intValue() });
	}

	@Override
	public void setDefault() {
		super.setDefault();
		radius = DEFAULT_RADIUS;
		size = DEFAULT_SIZE;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "radius: " + ChatColor.DARK_GRAY + radius);
		list.add(ChatColor.GRAY + "size: " + ChatColor.DARK_GRAY + size);
	}
}
