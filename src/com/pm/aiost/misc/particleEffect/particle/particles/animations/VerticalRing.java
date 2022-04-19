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
import com.pm.aiost.misc.utils.ChatColor;
import com.pm.aiost.misc.utils.Geometric;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.player.ServerPlayer;

public class VerticalRing extends Ring {

	protected float yaw;

	public VerticalRing() {
	}

	public VerticalRing(IParticle particle, double radius) {
		this(particle, radius, DEFAULT_SIZE, 0);
	}

	public VerticalRing(IParticle particle, double radius, int size) {
		this(particle, radius, size, 0);
	}

	public VerticalRing(IParticle particle, double radius, int size, float yaw) {
		super(particle, radius, size);
		this.yaw = yaw;
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
	public VerticalRing init() {
		coordinates = Geometric.verticalRing(radius, size, Math.toRadians(yaw));
		return this;
	}

	@Override
	public void load(ConfigurationSection section) {
		super.load(section);
		yaw = (float) section.getDouble("yaw", 0);
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		yaw = nbt.getFloat("yaw");
	}

	@Override
	public void save(INBTTagCompound nbt) {
		super.save(nbt);
		nbt.setFloat("yaw", yaw);
	}

	@Override
	public ParticleType<? extends VerticalRing> getType() {
		return ParticleTypes.VERTICAL_RING;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getYaw() {
		return yaw;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof VerticalRing))
			return false;
		VerticalRing other = (VerticalRing) obj;
		if (yaw != other.yaw)
			return false;
		return super.equals(obj);
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getAnimationMenuRequest(Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { () -> new NumberMenu(BOLD + "Choose radius"),
						() -> new NumberMenu(BOLD + "Choose size"), () -> new NumberMenu(BOLD + "Choose yaw") },

				new Consumer[] { (radius) -> this.radius = (Double) radius,
						(size) -> this.size = ((Double) size).intValue(),
						(yaw) -> this.yaw = ((Double) yaw).floatValue() });
	}

	@Override
	public void setDefault() {
		super.setDefault();
		yaw = 0;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "yaw: " + ChatColor.DARK_GRAY + yaw);
	}
}