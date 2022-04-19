package com.pm.aiost.event.effect.effects;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.effect.blueprints.SimplePlayerEffect;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.menus.request.enumeration.EnumerationMenus;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.player.ServerPlayer;

public class PotionEffect extends SimplePlayerEffect {

	private PotionEffectType type;
	private int duration;
	private int amplifier;

	public PotionEffect() {
	}

	public PotionEffect(byte[] actions, byte condition, String type, int duration, int amplifier) {
		this(actions, condition, PotionEffectType.getByName(type.toUpperCase()), duration, amplifier);
	}

	public PotionEffect(byte[] actions, byte condition, PotionEffectType type, int duration, int amplifier) {
		super(actions, condition);
		this.type = type;
		this.duration = duration;
		this.amplifier = amplifier;
	}

	@Override
	public void runEffect(ServerPlayer serverPlayer) {
		potionEffect(serverPlayer.player, type, duration, amplifier);
	}

	public static void potionEffect(LivingEntity entity, PotionEffectType type, int duration, int amplifier) {
		if (entity.hasPotionEffect(type)) {
			org.bukkit.potion.PotionEffect currentPotionEffect = entity.getPotionEffect(type);
			if (currentPotionEffect.getAmplifier() <= amplifier && currentPotionEffect.getDuration() <= duration)
				entity.removePotionEffect(type);
			else
				return;
		}
		entity.addPotionEffect(new org.bukkit.potion.PotionEffect(type, duration, amplifier));
	}

	@Override
	public boolean equals(Effect effect) {
		if (!super.equals(effect))
			return false;
		PotionEffect potionEffect = (PotionEffect) effect;
		if (potionEffect.type != type)
			return false;
		if (potionEffect.duration != duration)
			return false;
		if (potionEffect.amplifier != amplifier)
			return false;
		return true;
	}

	@Override
	public void load(ConfigurationSection effectSection) {
		super.load(effectSection);
		type = PotionEffectType.getByName(effectSection.getString("type").toUpperCase());
		duration = effectSection.getInt("duration");
		amplifier = effectSection.getInt("amplifier");
	}

	@Override
	public void load(Effect effect) {
		super.load(effect);
		PotionEffect potionEffect = (PotionEffect) effect;
		if (potionEffect.type != null)
			type = potionEffect.type;
		if (potionEffect.duration != 0)
			duration = potionEffect.duration;
		if (potionEffect.amplifier != 0)
			amplifier = potionEffect.amplifier;
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		type = PotionEffectType.getById(nbt.getInt("type"));
		duration = nbt.getInt("duration");
		amplifier = nbt.getInt("amplifier");
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		super.save(nbt);
		nbt.setInt("type", type.getId());
		nbt.setInt("duration", duration);
		nbt.setInt("amplifier", amplifier);
		return nbt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { EffectActionMenu::new, EffectConditionMenu::getMenu,
						() -> EnumerationMenus.POTION_EFFECT_TYPE_MENU, () -> new NumberMenu(BOLD + "Choose duration"),
						() -> new NumberMenu(BOLD + "Choose amplifier") },
				new Consumer[] { this::setActions, this::setCondition, (type) -> this.type = (PotionEffectType) type,
						(duration) -> this.duration = ((Double) duration).intValue(),
						(amplifier) -> this.amplifier = ((Double) amplifier).intValue() });
	}

	public void setPotionType(PotionEffectType type) {
		this.type = type;
	}

	public PotionEffectType getPotionType() {
		return type;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getDuration() {
		return duration;
	}

	public void setAmplifier(int amplifier) {
		this.amplifier = amplifier;
	}

	public int getAmplifier() {
		return amplifier;
	}

	@Override
	public EffectType<? extends PotionEffect> getType() {
		return EffectTypes.POTION_EFFECT;
	}

	@Override
	public void setDefault() {
		super.setDefault();
		type = PotionEffectType.ABSORPTION;
		duration = 20;
		amplifier = 1;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "Potion effect: " + ChatColor.DARK_GRAY + type.getName());
		list.add(ChatColor.GRAY + "Duration: " + ChatColor.DARK_GRAY + duration);
		list.add(ChatColor.GRAY + "Amplifier: " + ChatColor.DARK_GRAY + amplifier);
	}
}
