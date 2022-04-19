package com.pm.aiost.misc.particleEffect.particle.particles;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.menu.menus.request.BooleanMenu;
import com.pm.aiost.misc.menu.menus.request.DataParticleMenu;
import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.menus.request.enumeration.EnumerationMenus;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.misc.particleEffect.particle.ParticleType;
import com.pm.aiost.misc.particleEffect.particle.ParticleTypes;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.player.ServerPlayer;

public class LocationParticle<T> extends DataParticle<T> {

	protected double x;
	protected double y;
	protected double z;

	public LocationParticle() {
	}

	public LocationParticle(org.bukkit.Particle particle, int count, float offsetX, float offsetY, float offsetZ,
			float extra, boolean longDistance, T data, double x, double y, double z) {
		super(particle, count, offsetX, offsetY, offsetZ, extra, longDistance, data);
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public LocationParticle(org.bukkit.Particle particle, int count, float offset, float extra, boolean longDistance,
			T data, double x, double y, double z) {
		super(particle, count, offset, extra, longDistance, data);
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public LocationParticle(org.bukkit.Particle particle, int count, float offsetX, float offsetY, float offsetZ,
			float extra, boolean longDistance, T data, Location loc) {
		super(particle, count, offsetX, offsetY, offsetZ, extra, longDistance, data);
		this.x = loc.getX();
		this.y = loc.getY();
		this.z = loc.getZ();
	}

	public LocationParticle(org.bukkit.Particle particle, int count, float offset, float extra, boolean longDistance,
			T data, Location loc) {
		super(particle, count, offset, extra, longDistance, data);
		this.x = loc.getX();
		this.y = loc.getY();
		this.z = loc.getZ();
	}

	@Override
	public void spawn(World world, double x, double y, double z, float yaw, float pitch) {
		world.spawnParticle(particle, x + this.x, y + this.y, z + this.z, count, offsetX, offsetY, offsetZ, extra, data,
				longDistance);
	}

	@Override
	public void spawn(double x, double y, double z, float yaw, float pitch, Iterable<Player> player) {
		PacketSender.send(player, PacketFactory.packetParticles(NMS.getNMS(particle, data), longDistance, x + this.x,
				y + this.y, z + this.z, offsetX, offsetY, offsetZ, extra, count));
	}

	@Override
	public void load(ConfigurationSection section) {
		super.load(section);
		loadLocation(section);
	}

	protected void loadLocation(ConfigurationSection section) {
		if (section.contains("xyz"))
			x = y = z = section.getDouble("xyz");
		else {
			x = section.getDouble("x");
			y = section.getDouble("y");
			z = section.getDouble("z");
		}
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		x = nbt.getDouble("x");
		y = nbt.getDouble("y");
		z = nbt.getDouble("z");
	}

	@Override
	public void save(INBTTagCompound nbt) {
		super.save(nbt);
		nbt.setDouble("x", x);
		nbt.setDouble("y", y);
		nbt.setDouble("z", z);
	}

	@Override
	public ParticleType<? extends LocationParticle<?>> getType() {
		return ParticleTypes.LOCATION_PARTICLE;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LocationParticle == false)
			return false;
		LocationParticle<?> other = (LocationParticle<?>) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return super.equals(obj);
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(Consumer<ServerPlayer> requestConsumer, Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { () -> EnumerationMenus.PARTICLE_MENU, () -> new NumberMenu(BOLD + "Choose count"),
						() -> new NumberMenu(BOLD + "Choose offset x"), () -> new NumberMenu(BOLD + "Choose offset y"),
						() -> new NumberMenu(BOLD + "Choose offset z"), () -> new NumberMenu(BOLD + "Choose extra"),
						() -> BooleanMenu.create(BOLD + "Use long distance?"), DataParticleMenu::getMenu,
						() -> new NumberMenu(BOLD + "Choose location x"),
						() -> new NumberMenu(BOLD + "Choose location y"),
						() -> new NumberMenu(BOLD + "Choose location z") },

				new Consumer[] { (particle) -> this.particle = (org.bukkit.Particle) particle,
						(count) -> this.count = ((Double) count).intValue(),
						(offsetX) -> this.offsetX = ((Double) offsetX).floatValue(),
						(offsetY) -> this.offsetY = ((Double) offsetY).floatValue(),
						(offsetZ) -> this.offsetZ = ((Double) offsetZ).floatValue(),
						(extra) -> this.extra = ((Double) extra).floatValue(),
						(longDistance) -> this.longDistance = (Boolean) longDistance, (data) -> this.data = (T) data,
						(x) -> this.x = (Double) x, (y) -> this.y = (Double) y, (z) -> this.z = (Double) z });
	}

	@Override
	public void setDefault() {
		super.setDefault();
		x = 0;
		y = 0;
		z = 0;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "x: " + ChatColor.DARK_GRAY + x);
		list.add(ChatColor.GRAY + "y: " + ChatColor.DARK_GRAY + y);
		list.add(ChatColor.GRAY + "z: " + ChatColor.DARK_GRAY + z);
	}
}
