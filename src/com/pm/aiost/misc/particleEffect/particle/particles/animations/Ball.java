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

public class Ball extends AnimationParticle {

	protected static final int DEFAULT_RADIUS = 1;
	protected static final int DEFAULT_UP_SIZE = 14;
	protected static final int DEFAULT_SIDE_SIZE = 20;

	protected double radius;
	protected int upSize;
	protected int sideSize;
	protected double[] coordinates;

	public Ball() {
	}

	public Ball(IParticle particle) {
		this(particle, DEFAULT_RADIUS, DEFAULT_UP_SIZE, DEFAULT_SIDE_SIZE);
	}

	public Ball(IParticle particle, double radius) {
		this(particle, radius, DEFAULT_UP_SIZE, DEFAULT_SIDE_SIZE);
	}

	public Ball(IParticle particle, double radius, int size) {
		this(particle, radius, size, size);
	}

	public Ball(IParticle particle, double radius, int upSize, int sideSize) {
		super(particle);
		this.radius = radius;
		this.upSize = upSize;
		this.sideSize = sideSize;
	}

	@Override
	public Ball init() {
		coordinates = Geometric.sphere(radius, upSize, sideSize);
		return this;
	}

	@Override
	public void spawn(World world, double x, double y, double z, float yaw, float pitch) {
		for (int i = 0; i < coordinates.length; i += 3)
			particle.spawn(world, x + coordinates[i], y + coordinates[i + 1], z + coordinates[i + 2]);
	}

	@Override
	public void spawn(double x, double y, double z, float yaw, float pitch, Iterable<Player> player) {
		for (int i = 0; i < coordinates.length; i += 3)
			particle.spawn(x + coordinates[i], y + coordinates[i + 1], z + coordinates[i + 2], player);
	}

	@Override
	public void load(ConfigurationSection section) {
		super.load(section);
		radius = section.getDouble("radius", DEFAULT_RADIUS);
		upSize = section.getInt("upSize", DEFAULT_UP_SIZE);
		sideSize = section.getInt("sideSize", DEFAULT_SIDE_SIZE);
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		radius = nbt.getDouble("radius");
		upSize = nbt.getInt("upSize");
		sideSize = nbt.getInt("sideSize");
	}

	@Override
	public void save(INBTTagCompound nbt) {
		super.save(nbt);
		nbt.setDouble("radius", radius);
		nbt.setInt("upSize", upSize);
		nbt.setInt("sideSize", sideSize);
	}

	@Override
	public ParticleType<? extends Ball> getType() {
		return ParticleTypes.BALL;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getRadius() {
		return radius;
	}

	public void setUpSize(int upSize) {
		this.upSize = upSize;
	}

	public int getUpSize() {
		return upSize;
	}

	public void setSideSize(int sideSize) {
		this.sideSize = sideSize;
	}

	public int getSideSize() {
		return sideSize;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Ball))
			return false;
		Ball other = (Ball) obj;
		if (radius != other.radius)
			return false;
		if (upSize != other.upSize)
			return false;
		if (sideSize != other.sideSize)
			return false;
		return super.equals(obj);
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getAnimationMenuRequest(Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { () -> new NumberMenu(BOLD + "Choose radius"),
						() -> new NumberMenu(BOLD + "Choose up size"),
						() -> new NumberMenu(BOLD + "Choose side size") },

				new Consumer[] { (radius) -> this.radius = (Double) radius,
						(upSize) -> this.upSize = ((Double) upSize).intValue(),
						(sideSize) -> this.sideSize = ((Double) sideSize).intValue() });
	}

	@Override
	public void setDefault() {
		super.setDefault();
		radius = DEFAULT_RADIUS;
		upSize = DEFAULT_UP_SIZE;
		sideSize = DEFAULT_SIDE_SIZE;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "radius: " + ChatColor.DARK_GRAY + radius);
		list.add(ChatColor.GRAY + "up size: " + ChatColor.DARK_GRAY + upSize);
		list.add(ChatColor.GRAY + "side size: " + ChatColor.DARK_GRAY + sideSize);
	}
}
