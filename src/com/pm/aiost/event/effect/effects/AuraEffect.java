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
import com.pm.aiost.misc.menu.menus.request.creation.CreationMenus;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.particleEffect.particle.ParticleBuilder;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.misc.utils.nbt.custom.NBTCompound;
import com.pm.aiost.player.ServerPlayer;

public class AuraEffect extends Effect {

	protected static final Effect DEFAULT_EFFECT = new DamageEffect(new byte[] { EffectAction.TICK },
			EffectCondition.NONE, 1);
	protected static final double DEFAULT_RANGE = 5.0;

	protected IParticle particle;
	protected Effect effect;
	protected double range;

	public AuraEffect() {
		super(EffectAction.TICK, EffectCondition.UNIQUE);
	}

	public AuraEffect(IParticle particle, Effect effect) {
		this(particle, effect, DEFAULT_RANGE);
	}

	public AuraEffect(IParticle particle, Effect effect, double range) {
		super(EffectAction.TICK, EffectCondition.UNIQUE);
		this.particle = particle;
		this.effect = effect;
		this.range = range;
	}

	@Override
	public void onTick(ServerPlayer serverPlayer) {
		aura(serverPlayer.player, particle, effect, range);
	}

	public static void aura(Player player, IParticle particle, Effect effect, double range) {
		Location loc = player.getLocation();
		particle.spawn(loc);
		player.getWorld().getNearbyEntities(loc, range, range, range, (e) -> {
			if (e instanceof LivingEntity && e != player)
				effect.onTick(e);
			return false;
		});
	}

	@Override
	public boolean equals(Effect effect) {
		if (getClass() != effect.getClass())
			return false;
		AuraEffect auraEffect = (AuraEffect) effect;
		if (auraEffect.particle != particle)
			return false;
		if (auraEffect.effect.equals(this.effect))
			return false;
		if (auraEffect.range != range)
			return false;
		return true;
	}

	@Override
	public void load(ConfigurationSection section) {
		super.load(section);
		particle = ParticleBuilder.create(section);
		effect = Effect.loadConfiguration(section);
		range = section.getDouble("range");
	}

	@Override
	public void load(Effect effect) {
		super.load(effect);
		AuraEffect auraEffect = (AuraEffect) effect;
		if (auraEffect.particle != null)
			particle = auraEffect.particle;
		if (auraEffect.effect != null)
			this.effect = auraEffect.effect;
		if (auraEffect.range != 0)
			range = auraEffect.range;
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		particle = ParticleBuilder.create(nbt);
		effect = Effect.loadNBT(nbt.getCompound("effect"));
		range = nbt.getDouble("range");
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		super.save(nbt);
		ParticleBuilder.save(particle, nbt);
		nbt.set("effect", Effect.saveNBT(effect, new NBTCompound()));
		nbt.setDouble("range", range);
		return nbt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { ChooseParticleMenu::getMenu, () -> CreationMenus.getEffectMenu(serverPlayer),
						() -> new NumberMenu(BOLD + "Choose range") },
				new Consumer[] { (particle) -> this.particle = (IParticle) particle,
						(effect) -> this.effect = (Effect) effect, (range) -> this.range = (Double) range });
	}

	@Override
	public EffectType<? extends AuraEffect> getType() {
		return EffectTypes.AURA;
	}

	@Override
	public void setDefault() {
		super.setDefault();
		particle = IParticle.DEFAULT;
		effect = DEFAULT_EFFECT;
		range = DEFAULT_RANGE;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "Range: " + ChatColor.DARK_GRAY + range);
		list.add(null);
		list.add(null);
		list.add(ChatColor.GRAY + "Particle:");
		particle.createDescription(list);
		list.add(null);
		list.add(ChatColor.GRAY + "Effect:");
		effect.createDescription(list);
	}

	public IParticle getParticle() {
		return particle;
	}

	public void setParticle(IParticle particle) {
		this.particle = particle;
	}

	public Effect getEffect() {
		return effect;
	}

	public void setEffect(Effect effect) {
		this.effect = effect;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}
}
