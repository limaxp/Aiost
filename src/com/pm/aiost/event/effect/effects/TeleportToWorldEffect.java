package com.pm.aiost.event.effect.effects;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.effect.blueprints.SimpleEntityEffect;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.menus.request.TextMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.player.ServerPlayer;

public class TeleportToWorldEffect extends SimpleEntityEffect {

	private String worldName;
	private double x;
	private double y;
	private double z;

	public TeleportToWorldEffect() {
	}

	public TeleportToWorldEffect(byte[] actions, byte condition, World world) {
		this(actions, condition, world.getSpawnLocation());
	}

	public TeleportToWorldEffect(byte[] actions, byte condition, Location loc) {
		this(actions, condition, loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ());
	}

	public TeleportToWorldEffect(byte[] actions, byte condition, String worldName, double x, double y, double z) {
		super(actions, condition);
		this.worldName = worldName;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void runEffect(Entity entity) {
		teleport(entity, worldName, x, y, z);
	}

	public static void teleport(Entity entity, String worldName, double x, double y, double z) {
		World world = Bukkit.getWorld(worldName);
		if (world == null)
			return;
		entity.teleport(new Location(world, x, y, z));
		entity.playEffect(EntityEffect.ENTITY_POOF);
	}

	@Override
	public boolean equals(Effect effect) {
		if (!super.equals(effect))
			return false;
		TeleportToWorldEffect other = (TeleportToWorldEffect) effect;
		if (other.x != x || other.y != y || other.z != z || !other.worldName.equals(worldName))
			return false;
		return true;
	}

	@Override
	public void load(ConfigurationSection effectSection) {
		super.load(effectSection);
		worldName = effectSection.getString("worldName");
		x = effectSection.getDouble("x");
		y = effectSection.getDouble("y");
		z = effectSection.getDouble("z");
	}

	@Override
	public void load(Effect effect) {
		super.load(effect);
		TeleportToWorldEffect other = (TeleportToWorldEffect) effect;
		if (other.worldName != null)
			worldName = other.worldName;
		if (other.x != 0)
			x = other.x;
		if (other.y != 0)
			y = other.y;
		if (other.z != 0)
			z = other.z;
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		worldName = nbt.getString("worldName");
		x = nbt.getDouble("x");
		y = nbt.getDouble("y");
		z = nbt.getDouble("z");
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		super.save(nbt);
		nbt.setString("worldName", worldName);
		nbt.setDouble("x", x);
		nbt.setDouble("y", y);
		nbt.setDouble("z", z);
		return nbt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { EffectActionMenu::new, EffectConditionMenu::getMenu,
						() -> TextMenu.create("Choose world", "name"), () -> new NumberMenu(BOLD + "Choose x"),
						() -> new NumberMenu(BOLD + "Choose y"), () -> new NumberMenu(BOLD + "Choose z") },
				new Consumer[] { this::setActions, this::setCondition,
						(worldName) -> this.worldName = (String) worldName, (x) -> this.x = (Double) x,
						(y) -> this.y = (Double) y, (z) -> this.z = (Double) z });
	}

	@Override
	public EffectType<? extends TeleportToWorldEffect> getType() {
		return EffectTypes.TELEPORT_TO_WORLD;
	}

	@Override
	public void setDefault() {
		super.setDefault();
		worldName = "world";
		x = 1;
		y = 1;
		z = 1;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "world: " + ChatColor.DARK_GRAY + worldName);
		list.add(ChatColor.GRAY + "X: " + ChatColor.DARK_GRAY + x);
		list.add(ChatColor.GRAY + "Y: " + ChatColor.DARK_GRAY + y);
		list.add(ChatColor.GRAY + "Z: " + ChatColor.DARK_GRAY + z);
	}

	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}

	public String getWorldName() {
		return worldName;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}
}
