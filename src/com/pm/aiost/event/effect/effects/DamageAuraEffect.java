package com.pm.aiost.event.effect.effects;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectAction;
import com.pm.aiost.event.effect.EffectCondition;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.misc.menu.menus.request.ChooseParticleMenu;
import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.particleEffect.particle.ParticleBuilder;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.player.ServerPlayer;

public class DamageAuraEffect extends Effect {

	public static final double DEFAULT_DAMAGE = 1.0;

	protected IParticle particle;
	protected double damage;
	protected double range;

	public DamageAuraEffect() {
		super(EffectAction.TICK, EffectCondition.UNIQUE);
	}

	public DamageAuraEffect(IParticle particle) {
		this(particle, DEFAULT_DAMAGE, AuraEffect.DEFAULT_RANGE);
	}

	public DamageAuraEffect(IParticle particle, double damage, double range) {
		super(EffectAction.TICK, EffectCondition.UNIQUE);
		this.particle = particle;
		this.damage = damage;
		this.range = range;
	}

	@Override
	public void onTick(ServerPlayer serverPlayer) {
		aura(serverPlayer.player, particle, damage, range);
	}

	public static void aura(Player player, IParticle particle, double damage, double range) {
		Location loc = player.getLocation();
		particle.spawn(loc);
		player.getWorld().getNearbyEntities(loc, range, range, range, (e) -> {
			if (e instanceof LivingEntity && e != player)
				((LivingEntity) e).damage(damage, player);
			return false;
		});
	}

	@Override
	public boolean equals(Effect effect) {
		if (getClass() != effect.getClass())
			return false;
		DamageAuraEffect auraEffect = (DamageAuraEffect) effect;
		if (auraEffect.particle != particle)
			return false;
		if (auraEffect.damage != damage)
			return false;
		if (auraEffect.range != range)
			return false;
		return true;
	}

	@Override
	public void load(ConfigurationSection section) {
		super.load(section);
		particle = ParticleBuilder.create(section);
		damage = section.getDouble("damage");
		range = section.getDouble("range");
	}

	@Override
	public void load(Effect effect) {
		super.load(effect);
		DamageAuraEffect auraEffect = (DamageAuraEffect) effect;
		if (auraEffect.particle != null) {
			particle = auraEffect.particle;
			damage = auraEffect.damage;
			range = auraEffect.range;
		}
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		particle = ParticleBuilder.create(nbt);
		damage = nbt.getDouble("damage");
		range = nbt.getDouble("range");
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		super.save(nbt);
		ParticleBuilder.save(particle, nbt);
		nbt.setDouble("damage", damage);
		nbt.setDouble("range", range);
		return nbt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { ChooseParticleMenu::getMenu, () -> new NumberMenu(BOLD + "Choose damage"),
						() -> new NumberMenu(BOLD + "Choose range") },
				new Consumer[] { (particle) -> this.particle = (IParticle) particle,
						(damage) -> this.damage = (Double) damage, (range) -> this.range = (Double) range });
	}

	@Override
	public EffectType<? extends DamageAuraEffect> getType() {
		return EffectTypes.DAMAGE_AURA;
	}

	@Override
	public void setDefault() {
		super.setDefault();
		particle = IParticle.DEFAULT;
		damage = DEFAULT_DAMAGE;
		range = AuraEffect.DEFAULT_RANGE;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "Damage: " + ChatColor.DARK_GRAY + damage);
		list.add(ChatColor.GRAY + "Range: " + ChatColor.DARK_GRAY + range);
		list.add(null);
		list.add(ChatColor.GRAY + "Particle:");
		particle.createDescription(list);
	}

	public IParticle getParticle() {
		return particle;
	}

	public void setParticle(IParticle particle) {
		this.particle = particle;
	}

	public double getDamage() {
		return damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}
}
