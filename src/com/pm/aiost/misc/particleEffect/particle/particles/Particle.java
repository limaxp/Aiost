package com.pm.aiost.misc.particleEffect.particle.particles;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.menu.menus.request.BooleanMenu;
import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.menus.request.enumeration.EnumerationMenus;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.particleEffect.particle.ParticleType;
import com.pm.aiost.misc.particleEffect.particle.ParticleTypes;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.player.ServerPlayer;

public class Particle implements IParticle {

	protected org.bukkit.Particle particle;
	protected int count;
	protected float offsetX;
	protected float offsetY;
	protected float offsetZ;
	protected float extra;
	protected boolean longDistance;

	public Particle() {
	}

	public Particle(org.bukkit.Particle particle, int count, float offsetX, float offsetY, float offsetZ, float extra,
			@Nullable Boolean longDistance) {
		this.particle = particle;
		this.count = count;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
		this.extra = extra;
		this.longDistance = longDistance;
	}

	public Particle(org.bukkit.Particle particle, int count, float offset, float extra,
			@Nullable Boolean longDistance) {
		this.particle = particle;
		this.count = count;
		this.offsetX = offset;
		this.offsetY = offset;
		this.offsetZ = offset;
		this.extra = extra;
		this.longDistance = longDistance;
	}

	@Override
	public void spawn(World world, double x, double y, double z, float yaw, float pitch) {
		world.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, null, longDistance);
	}

	@Override
	public void spawn(double x, double y, double z, float yaw, float pitch, Iterable<Player> player) {
		PacketSender.send(player, PacketFactory.packetParticles(NMS.getNMS(particle), longDistance, x, y, z, offsetX,
				offsetY, offsetZ, extra, count));
	}

	@Override
	public void load(ConfigurationSection section) {
		particle = org.bukkit.Particle.valueOf(section.getString("particle"));
		count = section.getInt("count", 1);
		loadOffset(section.get("offset"));
		extra = (float) section.getDouble("extra");
		longDistance = section.getBoolean("longDistance");
	}

	protected final void loadOffset(Object obj) {
		if (obj instanceof Number)
			offsetX = offsetY = offsetZ = ((Number) obj).floatValue();
		else if (obj instanceof List) {
			@SuppressWarnings("unchecked")
			List<Number> list = (List<Number>) obj;
			offsetX = list.get(0).floatValue();
			offsetY = list.get(1).floatValue();
			offsetZ = list.get(2).floatValue();
		}
	}

	@Override
	public void load(INBTTagCompound nbt) {
		particle = org.bukkit.Particle.valueOf(nbt.getString("particle"));
		count = nbt.getInt("count");
		offsetX = nbt.getFloat("offsetX");
		offsetY = nbt.getFloat("offsetY");
		offsetZ = nbt.getFloat("offsetZ");
		extra = nbt.getFloat("extra");
		longDistance = nbt.getBoolean("longDistance");
	}

	@Override
	public void save(INBTTagCompound nbt) {
		nbt.setString("particle", particle.name());
		nbt.setInt("count", count);
		nbt.setFloat("offsetX", offsetX);
		nbt.setFloat("offsetY", offsetY);
		nbt.setFloat("offsetZ", offsetZ);
		nbt.setFloat("extra", extra);
		nbt.setBoolean("longDistance", longDistance);
	}

	@Override
	public ParticleType<? extends Particle> getType() {
		return ParticleTypes.PARTICLE;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Particle == false)
			return false;
		Particle other = (Particle) obj;
		if (!this.particle.equals(other.particle))
			return false;
		if (count != other.count)
			return false;
		if (offsetX != other.offsetX)
			return false;
		if (offsetY != other.offsetY)
			return false;
		if (offsetZ != other.offsetZ)
			return false;
		if (extra != other.extra)
			return false;
		if (longDistance != other.longDistance)
			return false;
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(Consumer<ServerPlayer> requestConsumer, Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { () -> EnumerationMenus.PARTICLE_MENU,
						() -> new NumberMenu(ChatColor.BOLD + "Choose count"),
						() -> new NumberMenu(ChatColor.BOLD + "Choose offset x"),
						() -> new NumberMenu(ChatColor.BOLD + "Choose offset y"),
						() -> new NumberMenu(ChatColor.BOLD + "Choose offset z"),
						() -> new NumberMenu(ChatColor.BOLD + "Choose extra"),
						() -> BooleanMenu.create(ChatColor.BOLD + "Use long distance?") },

				new Consumer[] { (particle) -> this.particle = (org.bukkit.Particle) particle,
						(count) -> this.count = ((Double) count).intValue(),
						(offsetX) -> this.offsetX = ((Double) offsetX).floatValue(),
						(offsetY) -> this.offsetY = ((Double) offsetY).floatValue(),
						(offsetZ) -> this.offsetZ = ((Double) offsetZ).floatValue(),
						(extra) -> this.extra = ((Double) extra).floatValue(),
						(longDistance) -> this.longDistance = (Boolean) longDistance });
	}

	@Override
	public void setDefault() {
		particle = org.bukkit.Particle.FLAME;
		count = 1;
		offsetX = 0;
		offsetY = 0;
		offsetZ = 0;
		extra = 0;
		longDistance = false;
	}

	@Override
	public void createDescription(List<String> list) {
		list.add(ChatColor.GRAY + "type: " + ChatColor.DARK_GRAY + getType().displayName);
		list.add(ChatColor.GRAY + "particle: " + ChatColor.DARK_GRAY + particle);
		list.add(ChatColor.GRAY + "count: " + ChatColor.DARK_GRAY + count);
		list.add(ChatColor.GRAY + "offset x: " + ChatColor.DARK_GRAY + offsetX);
		list.add(ChatColor.GRAY + "offset y: " + ChatColor.DARK_GRAY + offsetY);
		list.add(ChatColor.GRAY + "offset z: " + ChatColor.DARK_GRAY + offsetZ);
		list.add(ChatColor.GRAY + "extra: " + ChatColor.DARK_GRAY + extra);
		list.add(ChatColor.GRAY + "long distance: " + ChatColor.DARK_GRAY + longDistance);
	}
}
