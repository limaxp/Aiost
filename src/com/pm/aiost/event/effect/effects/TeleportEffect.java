package com.pm.aiost.event.effect.effects;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.effect.blueprints.SimpleEntityEffect;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.player.ServerPlayer;

public class TeleportEffect extends SimpleEntityEffect {

	private double deltaX;
	private double deltaY;
	private double deltaZ;

	public TeleportEffect() {
	}

	public TeleportEffect(byte[] actions, byte condition, double delta) {
		super(actions, condition);
		this.deltaX = delta;
		this.deltaY = delta;
		this.deltaZ = delta;
	}

	public TeleportEffect(byte[] actions, byte condition, double deltaX, double deltaY, double deltaZ) {
		super(actions, condition);
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		this.deltaZ = deltaZ;
	}

	@Override
	public void runEffect(Entity entity) {
		teleport(entity, deltaX, deltaY, deltaZ);
	}

	public static void teleport(Entity entity, double deltaX, double deltaY, double deltaZ) {
		Location loc = entity.getLocation();
		entity.teleport(new Location(loc.getWorld(), loc.getX() + deltaX, loc.getY() + deltaY, loc.getZ() + deltaZ));
		entity.playEffect(EntityEffect.ENTITY_POOF);
	}

	@Override
	public boolean equals(Effect effect) {
		if (!super.equals(effect))
			return false;
		TeleportEffect other = (TeleportEffect) effect;
		if (other.deltaX != deltaX || other.deltaY != deltaY || other.deltaZ != deltaZ)
			return false;
		return true;
	}

	@Override
	public void load(ConfigurationSection effectSection) {
		super.load(effectSection);
		deltaX = effectSection.getDouble("x");
		deltaY = effectSection.getDouble("y");
		deltaZ = effectSection.getDouble("z");
	}

	@Override
	public void load(Effect effect) {
		super.load(effect);
		TeleportEffect other = (TeleportEffect) effect;
		if (other.deltaX != 0)
			deltaX = other.deltaX;
		if (other.deltaY != 0)
			deltaY = other.deltaY;
		if (other.deltaZ != 0)
			deltaZ = other.deltaZ;
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		deltaX = nbt.getDouble("x");
		deltaY = nbt.getDouble("y");
		deltaZ = nbt.getDouble("z");
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		super.save(nbt);
		nbt.setDouble("x", deltaX);
		nbt.setDouble("y", deltaY);
		nbt.setDouble("z", deltaZ);
		return nbt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { EffectActionMenu::new, EffectConditionMenu::getMenu,
						() -> new NumberMenu(BOLD + "Choose delta x"), () -> new NumberMenu(BOLD + "Choose delta y"),
						() -> new NumberMenu(BOLD + "Choose delta z") },
				new Consumer[] { this::setActions, this::setCondition, (deltaX) -> this.deltaX = (Double) deltaX,
						(deltaY) -> this.deltaY = (Double) deltaY, (deltaZ) -> this.deltaZ = (Double) deltaZ });
	}

	@Override
	public EffectType<? extends TeleportEffect> getType() {
		return EffectTypes.TELEPORT;
	}

	@Override
	public void setDefault() {
		super.setDefault();
		deltaX = 1;
		deltaY = 1;
		deltaZ = 1;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "Delta x: " + ChatColor.DARK_GRAY + deltaX);
		list.add(ChatColor.GRAY + "Delta y: " + ChatColor.DARK_GRAY + deltaY);
		list.add(ChatColor.GRAY + "Delta z: " + ChatColor.DARK_GRAY + deltaZ);
	}

	public double getDeltaX() {
		return deltaX;
	}

	public void setDeltaX(double deltaX) {
		this.deltaX = deltaX;
	}

	public double getDeltaY() {
		return deltaY;
	}

	public void setDeltaY(double deltaY) {
		this.deltaY = deltaY;
	}

	public double getDeltaZ() {
		return deltaZ;
	}

	public void setDeltaZ(double deltaZ) {
		this.deltaZ = deltaZ;
	}
}
