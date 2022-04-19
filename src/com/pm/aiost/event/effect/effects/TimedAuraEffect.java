package com.pm.aiost.event.effect.effects;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.misc.menu.menus.request.ChooseParticleMenu;
import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.menus.request.creation.CreationMenus;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.player.ServerPlayer;

public class TimedAuraEffect extends AuraEffect {

	public static final int DEFAULT_DURATION = 1;

	protected int duration;

	public TimedAuraEffect() {
	}

	public TimedAuraEffect(IParticle particle, Effect effect) {
		super(particle, effect);
		this.duration = DEFAULT_DURATION;
	}

	public TimedAuraEffect(IParticle particle, Effect effect, int duration, double range) {
		super(particle, effect, range);
		this.duration = duration;
	}

	@Override
	public void onTick(ServerPlayer serverPlayer) {
		aura(serverPlayer.player, particle, effect, range, duration);
	}

	public static void aura(Player player, IParticle particle, Effect effect, double range, int duration) {
		Location loc = player.getLocation();
		particle.spawn(loc);
		player.getWorld().getNearbyEntities(loc, range, range, range, (e) -> {
			if (e instanceof Player && e != player)
				ServerPlayer.getByPlayer((Player) e).addEffect(duration, effect);
			return false;
		});
	}

	@Override
	public boolean equals(Effect effect) {
		if (!super.equals(effect))
			return false;
		if (((TimedAuraEffect) effect).duration != duration)
			return false;
		return true;
	}

	@Override
	public void load(ConfigurationSection section) {
		super.load(section);
		duration = section.getInt("duration");
	}

	@Override
	public void load(Effect effect) {
		super.load(effect);
		TimedAuraEffect auraEffect = (TimedAuraEffect) effect;
		if (auraEffect.duration != duration)
			duration = auraEffect.duration;
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		duration = nbt.getInt("duration");
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		super.save(nbt);
		nbt.setInt("duration", duration);
		return nbt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { ChooseParticleMenu::getMenu, () -> CreationMenus.getEffectMenu(serverPlayer),
						() -> new NumberMenu(BOLD + "Choose range"), () -> new NumberMenu(BOLD + "Choose duration") },
				new Consumer[] { (particle) -> this.particle = (IParticle) particle,
						(effect) -> this.effect = (Effect) effect, (range) -> this.range = (Double) range,
						(duration) -> this.duration = ((Double) duration).intValue() });
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	@Override
	public EffectType<? extends AuraEffect> getType() {
		return EffectTypes.TIMED_AURA;
	}

	@Override
	public void setDefault() {
		super.setDefault();
		duration = DEFAULT_DURATION;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.set(7, ChatColor.GRAY + "Duration: " + ChatColor.DARK_GRAY + duration);
	}
}
