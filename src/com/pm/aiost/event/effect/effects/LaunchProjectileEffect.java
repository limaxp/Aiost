package com.pm.aiost.event.effect.effects;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.effect.blueprints.SimpleLivingEntityEffect;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.menus.request.enumeration.EnumerationMenus;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.other.ProjectileClass;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.player.ServerPlayer;

public class LaunchProjectileEffect extends SimpleLivingEntityEffect {

	protected Class<? extends Projectile> projectileClass;
	protected float velocityMultiplier;

	public LaunchProjectileEffect() {
	}

	public LaunchProjectileEffect(byte[] actions, byte condition, String projectile, float velocityMultiplier) {
		this(actions, condition, ProjectileClass.get(projectile), velocityMultiplier);
	}

	public LaunchProjectileEffect(byte[] actions, byte condition, Class<? extends Projectile> projectileClass,
			float velocityMultiplier) {
		super(actions, condition);
		this.projectileClass = projectileClass;
		this.velocityMultiplier = velocityMultiplier;
	}

	@Override
	public void runEffect(LivingEntity entity) {
		launchProjectile(entity, projectileClass, velocityMultiplier);
	}

	public static Projectile launchProjectile(LivingEntity entity, Class<? extends Projectile> projetileClass,
			float velocityMultiplier) {
		Projectile projectile = entity.launchProjectile(projetileClass);
		projectile.setVelocity(entity.getVelocity().clone().multiply(velocityMultiplier));
		return projectile;
	}

	@Override
	public boolean equals(Effect effect) {
		if (!super.equals(effect))
			return false;
		LaunchProjectileEffect launchProjectile = (LaunchProjectileEffect) effect;
		if (launchProjectile.projectileClass != projectileClass)
			return false;
		if (launchProjectile.velocityMultiplier != velocityMultiplier)
			return false;
		return true;
	}

	@Override
	public void load(ConfigurationSection effectSection) {
		super.load(effectSection);
		projectileClass = ProjectileClass.get(effectSection.getString("projectile"));
		velocityMultiplier = (float) effectSection.getDouble("velocityMultiplier");
	}

	@Override
	public void load(Effect effect) {
		super.load(effect);
		LaunchProjectileEffect launchProjectile = (LaunchProjectileEffect) effect;
		if (launchProjectile.projectileClass != null)
			projectileClass = launchProjectile.projectileClass;
		if (launchProjectile.velocityMultiplier != 0)
			velocityMultiplier = launchProjectile.velocityMultiplier;
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		projectileClass = ProjectileClass.get(nbt.getString("projectile"));
		velocityMultiplier = nbt.getFloat("velocityMultiplier");
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		super.save(nbt);
		nbt.setString("projectile", ProjectileClass.getName(projectileClass));
		nbt.setFloat("velocityMultiplier", velocityMultiplier);
		return nbt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { EffectActionMenu::new, EffectConditionMenu::getMenu,
						() -> EnumerationMenus.PROJECTILE_CLASS_MENU,
						() -> new NumberMenu(BOLD + "Choose velocity multiplier") },
				new Consumer[] { this::setActions, this::setCondition,
						(projectileClass) -> this.projectileClass = (Class<? extends Projectile>) projectileClass,
						(velocityMultiplier) -> this.velocityMultiplier = ((Double) velocityMultiplier).floatValue() });
	}

	@Override
	public EffectType<? extends LaunchProjectileEffect> getType() {
		return EffectTypes.LAUNCH_PROJECTILE;
	}

	@Override
	public void setDefault() {
		super.setDefault();
		projectileClass = Snowball.class;
		velocityMultiplier = 1;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "Projectile: " + ChatColor.DARK_GRAY + projectileClass.getSimpleName());
		list.add(ChatColor.GRAY + "Velocity multiplier: " + ChatColor.DARK_GRAY + velocityMultiplier);
	}

	public void setProjectileClass(Class<? extends Projectile> projectileClass) {
		this.projectileClass = projectileClass;
	}

	public Class<? extends Projectile> getProjectileClass() {
		return projectileClass;
	}

	public void setVelocityMultiplier(float velocityMultiplier) {
		this.velocityMultiplier = velocityMultiplier;
	}

	public float getVelocityMultiplier() {
		return velocityMultiplier;
	}
}