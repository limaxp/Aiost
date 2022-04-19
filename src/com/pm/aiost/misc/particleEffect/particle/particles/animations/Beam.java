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

public class Beam extends AnimationParticle {

	protected static final int DEFAULT_RANGE = 10;

	protected double range;
	protected float yaw;
	protected float pitch;
	protected double[] coordinates;

	public Beam() {
	}

	public Beam(IParticle particle) {
		this(particle, DEFAULT_RANGE, 0, 0);
	}

	public Beam(IParticle particle, double range) {
		this(particle, range, 0, 0);
	}

	public Beam(IParticle particle, double range, float yaw, float pitch) {
		super(particle);
		this.range = range;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	@Override
	public Beam init() {
		coordinates = Geometric.beam(range, Math.toRadians(yaw), Math.toRadians(pitch));
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
	public void load(ConfigurationSection particleSection) {
		super.load(particleSection);
		range = particleSection.getDouble("range", DEFAULT_RANGE);
		yaw = (float) particleSection.getDouble("yaw", 0);
		pitch = (float) particleSection.getDouble("pitch", 0);
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		range = nbt.getDouble("range");
		yaw = nbt.getFloat("yaw");
		pitch = nbt.getFloat("pitch");
	}

	@Override
	public void save(INBTTagCompound nbt) {
		super.save(nbt);
		nbt.setDouble("range", range);
		nbt.setFloat("yaw", yaw);
		nbt.setFloat("pitch", pitch);
	}

	@Override
	public ParticleType<? extends Beam> getType() {
		return ParticleTypes.BEAM;
	}

	public void setRange(double range) {
		this.range = range;
	}

	public double getRange() {
		return range;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getYaw() {
		return yaw;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getPitch() {
		return pitch;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Ball))
			return false;
		Beam other = (Beam) obj;
		if (range != other.range)
			return false;
		if (yaw != other.yaw)
			return false;
		if (pitch != other.pitch)
			return false;
		return super.equals(obj);
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getAnimationMenuRequest(Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { () -> new NumberMenu(BOLD + "Choose range"), () -> new NumberMenu(BOLD + "Choose yaw"),
						() -> new NumberMenu(BOLD + "Choose pitch") },

				new Consumer[] { (range) -> this.range = (Double) range,
						(yaw) -> this.yaw = ((Double) yaw).floatValue(),
						(pitch) -> this.pitch = ((Double) pitch).floatValue() });
	}

	@Override
	public void setDefault() {
		super.setDefault();
		range = DEFAULT_RANGE;
		yaw = 0;
		pitch = 0;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "range: " + ChatColor.DARK_GRAY + range);
		list.add(ChatColor.GRAY + "yaw: " + ChatColor.DARK_GRAY + yaw);
		list.add(ChatColor.GRAY + "pitch: " + ChatColor.DARK_GRAY + pitch);
	}
}