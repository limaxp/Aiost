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
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.player.ServerPlayer;

public class Helix extends AnimationParticle {

	protected static final int DEFAULT_RADIUS = 1;
	protected static final int DEFAULT_HEIGHT = 2;
	protected static final int DEFAULT_STEP_AMOUNT = 18;
	protected static final double DEFAULT_PHI = Math.PI * 2;

	private double radius;
	private double height;
	private int size;
	private double phi;

	public Helix() {
	}

	public Helix(IParticle particle, double radius) {
		this(particle, radius, radius, DEFAULT_STEP_AMOUNT, DEFAULT_PHI);
	}

	public Helix(IParticle particle, double radius, double height) {
		this(particle, radius, height, DEFAULT_STEP_AMOUNT, DEFAULT_PHI);
	}

	public Helix(IParticle particle, double radius, int size) {
		this(particle, radius, radius, size, DEFAULT_PHI);
	}

	public Helix(IParticle particle, double radius, double height, int size) {
		this(particle, radius, height, size, DEFAULT_PHI);
	}

	public Helix(IParticle particle, double radius, int size, double phi) {
		this(particle, radius, radius, size, phi);
	}

	public Helix(IParticle particle, double radius, double height, int size, double phi) {
		this.radius = radius;
		this.height = height;
		this.size = size;
		this.phi = phi;
	}

	@Override
	public Helix init() {
		return this;
	}

	@Override
	public void spawn(World world, double x, double y, double z, float yaw, float pitch) {
		for (double y1 = 0; y1 <= height; y1 += Math.PI / size) {
			double x1 = radius * Math.cos(phi * y1);
			double z1 = radius * Math.sin(phi * y1);
			particle.spawn(world, x + x1, y + y1, z + z1);
		}
	}

	@Override
	public void spawn(double x, double y, double z, float yaw, float pitch, Iterable<Player> player) {
		for (float y1 = 0; y1 <= height; y1 += Math.PI / size) {
			double x1 = radius * Math.cos(phi * y1);
			double z1 = radius * Math.sin(phi * y1);
			particle.spawn(x + x1, y + y1, z + z1, player);
		}
	}

	@Override
	public void load(ConfigurationSection particleSection) {
		super.load(particleSection);
		radius = particleSection.getDouble("radius", DEFAULT_RADIUS);
		height = particleSection.getDouble("height", DEFAULT_HEIGHT);
		size = particleSection.getInt("size", DEFAULT_STEP_AMOUNT);
		phi = particleSection.getDouble("phi", DEFAULT_PHI);
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		radius = nbt.getDouble("radius");
		height = nbt.getDouble("height");
		size = nbt.getInt("size");
		phi = nbt.getDouble("phi");
	}

	@Override
	public void save(INBTTagCompound nbt) {
		super.save(nbt);
		nbt.setDouble("radius", radius);
		nbt.setDouble("height", height);
		nbt.setInt("size", size);
		nbt.setDouble("phi", phi);
	}

	@Override
	public ParticleType<? extends Helix> getType() {
		return ParticleTypes.HELIX;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getRadius() {
		return radius;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getHeight() {
		return height;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}

	public void setPhi(double phi) {
		this.phi = phi;
	}

	public double getPhi() {
		return phi;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Helix == false)
			return false;
		Helix other = (Helix) obj;
		if (radius != other.radius)
			return false;
		if (height != other.height)
			return false;
		if (size != other.size)
			return false;
		if (phi != other.phi)
			return false;
		return super.equals(obj);
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getAnimationMenuRequest(Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { () -> new NumberMenu(BOLD + "Choose radius"),
						() -> new NumberMenu(BOLD + "Choose height"), () -> new NumberMenu(BOLD + "Choose size"),
						() -> new NumberMenu(BOLD + "Choose phi") },

				new Consumer[] { (radius) -> setRadius((Double) radius), (height) -> setHeight((Double) height),
						(additor) -> size = ((Double) additor).intValue(), (phi) -> setPhi((Double) phi) });
	}

	@Override
	public void setDefault() {
		super.setDefault();
		radius = DEFAULT_RADIUS;
		height = DEFAULT_HEIGHT;
		size = DEFAULT_STEP_AMOUNT;
		phi = DEFAULT_PHI;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "radius: " + ChatColor.DARK_GRAY + radius);
		list.add(ChatColor.GRAY + "height: " + ChatColor.DARK_GRAY + height);
		list.add(ChatColor.GRAY + "size: " + ChatColor.DARK_GRAY + size);
		list.add(ChatColor.GRAY + "phi: " + ChatColor.DARK_GRAY + phi);
	}
}
