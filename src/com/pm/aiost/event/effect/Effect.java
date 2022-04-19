package com.pm.aiost.event.effect;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.player.PlayerFishEvent;

import com.pm.aiost.event.effect.effects.EmptyEffect;
import com.pm.aiost.event.eventHandler.EventHandler;
import com.pm.aiost.event.events.PlayerEquipItemEvent;
import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.NoMenuRequest.SimpleNoMenuRequest;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.misc.utils.nbt.custom.NBTCompound;
import com.pm.aiost.misc.utils.nbt.custom.NBTCompoundWrapper;
import com.pm.aiost.player.ServerPlayer;

import net.minecraft.server.v1_15_R1.NBTTagCompound;

public abstract class Effect implements EventHandler {

	public static final Effect EMPTY = new EmptyEffect();

	protected static final byte[] DEFAULT_ACTIONS = new byte[] { EffectAction.CLICK };

	private byte[] actions;
	private byte condition;

	public Effect() {
	}

	public Effect(byte action) {
		this(new byte[] { action }, EffectCondition.NONE);
	}

	public Effect(byte[] actions) {
		this(actions, EffectCondition.NONE);
	}

	public Effect(byte action, byte condition) {
		this(new byte[] { action }, condition);
	}

	public Effect(byte[] actions, byte condition) {
		this.actions = actions;
		this.condition = condition;
	}

	public abstract EffectType<? extends Effect> getType();

	public void setActions(byte[] actions) {
		this.actions = actions;
	}

	protected void setActions(Object actions) {
		this.actions = (byte[]) actions;
	}

	public byte[] getActions() {
		return actions;
	}

	public void setCondition(byte condition) {
		this.condition = condition;
	}

	protected void setCondition(Object condition) {
		this.condition = (byte) condition;
	}

	public byte getCondition() {
		return condition;
	}

	public String getName() {
		return getType().name;
	}

	public boolean isSimilar(Effect effect) {
		if (getClass() != effect.getClass())
			return false;
		if (!equals(actions, effect.actions))
			return false;
		return true;
	}

	public boolean equals(Effect effect) {
		if (getClass() != effect.getClass())
			return false;
		if (!equals(effect.actions, actions))
			return false;
		if (effect.condition != condition)
			return false;
		return false;
	}

	@Override
	public void load(ConfigurationSection section) {
		actions = EffectBuilder.readActions(section);
		condition = EffectBuilder.readCondition(section);
	}

	public void load(Effect effect) {
		if (effect.actions != null)
			actions = effect.actions;
		if (effect.condition != 0)
			condition = effect.condition;
	}

	public void load(INBTTagCompound nbt) {
		actions = nbt.getByteArray("actions");
		condition = nbt.getByte("condition");
	}

	public INBTTagCompound save(INBTTagCompound nbt) {
		nbt.setByteArray("actions", actions);
		nbt.setByte("condition", condition);
		return nbt;
	}

	public static Effect loadConfiguration(ConfigurationSection section) {
		ConfigurationSection effectSection = section.getConfigurationSection("effect");
		if (effectSection != null) {
			ConfigurationSection namedSection = effectSection
					.getConfigurationSection(effectSection.getKeys(false).iterator().next());
			return namedSection != null ? EffectBuilder.createEffect(namedSection) : EMPTY;
		}
		return EMPTY;
	}

	public static Effect loadNBT(INBTTagCompound nbt) {
		return EffectBuilder.createEffect(nbt);
	}

	public static Effect loadNBT(NBTCompound nbt) {
		return EffectBuilder.createEffect((INBTTagCompound) nbt);
	}

	public static Effect loadNBT(NBTTagCompound nbt) {
		return EffectBuilder.createEffect(nbt);
	}

	public static INBTTagCompound saveNBT(Effect effect, INBTTagCompound nbt) {
		nbt.setString("effectId", effect.getName());
		effect.save(nbt);
		return nbt;
	}

	public static INBTTagCompound saveNBT(Effect effect, NBTCompound nbt) {
		nbt.setString("effectId", effect.getName());
		effect.save(nbt);
		return nbt;
	}

	public static NBTTagCompound saveNBT(Effect effect, NBTTagCompound nbt) {
		nbt.setString("effectId", effect.getName());
		effect.save(new NBTCompoundWrapper(nbt));
		return nbt;
	}

	public MenuRequest getMenuRequest(ServerPlayer serverPlayer) {
		return getMenuRequest(serverPlayer, MenuRequest.EMPTY_CONSUMER);
	}

	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Menu requestMenu) {
		return getMenuRequest(serverPlayer, requestMenu::open);
	}

	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer) {
		return getMenuRequest(serverPlayer, requestConsumer, requestConsumer);
	}

	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Menu requestMenu, Menu resultMenu) {
		return getMenuRequest(serverPlayer, requestMenu::open, resultMenu::open);
	}

	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleNoMenuRequest(requestConsumer, targetConsumer);
	}

	@Override
	public String toString() {
		String s = getName() + "[Actions(";
		s += actionsToString();
		if (condition != 0)
			s += "), Condition: " + EffectCondition.getName(condition);
		else
			s += ")";
		return s + "]";
	}

	private String actionsToString() {
		String s = EffectAction.getName(actions[0]);
		int length = actions.length;
		if (length > 1) {
			for (int i = 1; i < length; i++)
				s += ", " + EffectAction.getName(actions[i]);
		}
		return s;
	}

	public static boolean containsSimilar(Iterable<Effect> effects, Effect effect) {
		for (Effect listEffect : effects) {
			if (listEffect.isSimilar(effect))
				return true;
		}
		return false;
	}

	public static Effect getFirstSimilar(Iterable<Effect> effects, Effect effect) {
		for (Effect listEffect : effects) {
			if (listEffect.isSimilar(effect))
				return listEffect;
		}
		return null;
	}

	public static List<Effect> getSimilar(Iterable<Effect> effects, Effect effect) {
		List<Effect> list = new ArrayList<Effect>();
		for (Effect listEffect : effects) {
			if (listEffect.isSimilar(effect))
				list.add(listEffect);
		}
		return list;
	}

	public static boolean equals(byte[] actions, Object actions2) {
		if (actions2 instanceof EffectAction[])
			return equals(actions, (EffectAction[]) actions2);
		return false;
	}

	public static boolean equals(byte[] actions, byte[] actions2) {
		int length = actions.length;
		if (length != actions2.length)
			return false;
		for (int i = 0; i < length; i++) {
			if (actions[i] != actions2[i])
				return false;
		}
		return true;
	}

	@Override
	public String getEventHandlerName() {
		return "Effect";
	}

	public void setDefault() {
		actions = DEFAULT_ACTIONS;
	}

	public List<String> createDescription() {
		List<String> list = new ArrayList<String>();
		createDescription(list);
		return list;
	}

	public void createDescription(List<String> list) {
		list.add(ChatColor.GRAY + "Type: " + ChatColor.DARK_GRAY + getType().displayName);
		list.add(ChatColor.GRAY + "Actions: " + ChatColor.DARK_GRAY + actionsToString());
		list.add(ChatColor.GRAY + "Condition: " + ChatColor.DARK_GRAY + EffectCondition.getName(condition));
	}

	public void onTick(ServerPlayer serverPlayer) {
		onTick(serverPlayer.player);
	}

	public void onTick(Entity entity) {
	}

	public void onPlayerUnequipItem(PlayerEquipItemEvent event) {
	}

	public void onItemMerge(ItemMergeEvent event) {
	}

	public void onPlayerFishingRodLaunch(ServerPlayer serverPlayer, PlayerFishEvent event) {
	}
}
