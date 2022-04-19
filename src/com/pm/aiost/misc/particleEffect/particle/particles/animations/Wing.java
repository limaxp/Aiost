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

public class Wing extends AnimationParticle {

	protected static final int DEFAULT_RADIUS = 1;
	protected static final int DEFAULT_SIZE = 24;

	protected double radius;
	protected int size;
	protected double[] coordinates;

	public Wing() {
	}

	public Wing(IParticle particle, double radius) {
		this(particle, radius, DEFAULT_SIZE);
	}

	public Wing(IParticle particle, double radius, int size) {
		super(particle);
		this.radius = radius;
		this.size = size;
	}

	@Override
	public Wing init() {
		coordinates = Geometric.wings(radius, size);
		return this;
	}

	@Override
	public void spawn(World world, double x, double y, double z, float yaw, float pitch) {
		double yawRad = Math.toRadians(yaw);
		for (int i = 0; i < coordinates.length; i += 3)
			particle.spawn(world, x + (coordinates[i] * Math.cos(yawRad)), y + coordinates[i + 1] + 1,
					z + (coordinates[i + 2] * Math.sin(yawRad)));
	}

	@Override
	public void spawn(double x, double y, double z, float yaw, float pitch, Iterable<Player> player) {
		double yawRad = Math.toRadians(yaw);
		for (int i = 0; i < coordinates.length; i += 3)
			particle.spawn(x + (coordinates[i] * Math.cos(yawRad)), y + coordinates[i + 1] + 1,
					z + (coordinates[i + 2] * Math.sin(yawRad)), player);
	}

	@Override
	public void load(ConfigurationSection particleSection) {
		super.load(particleSection);
		radius = particleSection.getDouble("radius", DEFAULT_RADIUS);
		size = particleSection.getInt("size", DEFAULT_SIZE);
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
	public ParticleType<? extends Wing> getType() {
		return ParticleTypes.WING;
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
		if (!(obj instanceof Wing))
			return false;
		Wing other = (Wing) obj;
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

				new Consumer[] { (radius) -> setRadius((Double) radius),
						(additor) -> size = ((Double) additor).intValue() });
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
