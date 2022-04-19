package com.pm.aiost.misc.particleEffect.particle;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.NoMenuRequest.SimpleNoMenuRequest;
import com.pm.aiost.misc.particleEffect.particle.particles.Particle;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.player.ServerPlayer;

public interface IParticle {

	public static final Particle DEFAULT = new Particle(org.bukkit.Particle.FLAME, 1, 0, 0, false);

	public default void spawn(World world, double x, double y, double z) {
		spawn(world, x, y, z, 0, 0);
	}

	public default void spawn(Location loc) {
		spawn(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	}

	public void spawn(World world, double x, double y, double z, float yaw, float pitch);

	public default void spawn(double x, double y, double z, Iterable<Player> player) {
		spawn(x, y, z, 0, 0, player);
	}

	public default void spawn(Location loc, Iterable<Player> player) {
		spawn(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch(), player);
	}

	public void spawn(double x, double y, double z, float yaw, float pitch, Iterable<Player> player);

	public void load(ConfigurationSection particleSection);

	public void load(INBTTagCompound nbt);

	public void save(INBTTagCompound nbt);

	public default IParticle init() {
		return this;
	}

	public ParticleType<? extends IParticle> getType();

	public default MenuRequest getMenuRequest(Menu requestMenu) {
		return getMenuRequest(requestMenu::open);
	}

	public default MenuRequest getMenuRequest(Consumer<ServerPlayer> requestConsumer) {
		return getMenuRequest(requestConsumer, requestConsumer);
	}

	public default MenuRequest getMenuRequest(Menu requestMenu, Menu resultMenu) {
		return getMenuRequest(requestMenu::open, resultMenu::open);
	}

	public default MenuRequest getMenuRequest(Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleNoMenuRequest(requestConsumer, targetConsumer);
	}

	public void setDefault();

	public default List<String> createDescription() {
		List<String> list = new ArrayList<String>();
		createDescription(list);
		return list;
	}

	public void createDescription(List<String> list);
}