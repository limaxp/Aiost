package com.pm.aiost.item.spell;

import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.pm.aiost.misc.registry.AiostRegistry;
import com.pm.aiost.misc.utils.ChatColor;
import com.pm.aiost.player.ServerPlayer;

public abstract class Spell {

	private static final int DEFAULT_CAST_VALUE = -100;

	private static final String[] MANA_STRINGS = { ChatColor.DARK_GRAY + "৯৯৯৯৯৯৯৯৯৯",
			ChatColor.BLUE + "৯" + ChatColor.DARK_GRAY + "৯৯৯৯৯৯৯৯৯",
			ChatColor.BLUE + "৯৯" + ChatColor.DARK_GRAY + "৯৯৯৯৯৯৯৯",
			ChatColor.BLUE + "৯৯৯" + ChatColor.DARK_GRAY + "৯৯৯৯৯৯৯",
			ChatColor.BLUE + "৯৯৯৯" + ChatColor.DARK_GRAY + "৯৯৯৯৯৯",
			ChatColor.BLUE + "৯৯৯৯৯" + ChatColor.DARK_GRAY + "৯৯৯৯৯",
			ChatColor.BLUE + "৯৯৯৯৯৯" + ChatColor.DARK_GRAY + "৯৯৯৯",
			ChatColor.BLUE + "৯৯৯৯৯৯৯" + ChatColor.DARK_GRAY + "৯৯৯",
			ChatColor.BLUE + "৯৯৯৯৯৯৯৯" + ChatColor.DARK_GRAY + "৯৯",
			ChatColor.BLUE + "৯৯৯৯৯৯৯৯৯" + ChatColor.DARK_GRAY + "৯", ChatColor.BLUE + "৯৯৯৯৯৯৯৯৯৯" };

	private final String name;
	private final String displayName;
	private final int cooldown;
	private final double cost;

	public Spell(String name, int cooldown, double cost) {
		this(name, name, cooldown, cost);
	}

	public Spell(String name, String displayName, int cooldown, double cost) {
		this.name = name.toLowerCase();
		this.displayName = displayName;
		this.cooldown = cooldown;
		this.cost = cost;
	}

	public boolean doCast(LivingEntity entity) {
		return doCast(entity, DEFAULT_CAST_VALUE);
	}

	public boolean doCast(LivingEntity entity, int i) {
		if (entity instanceof Player)
			return doCast(ServerPlayer.getByPlayer((Player) entity), i);
		else
			cast(entity, i);
		return true;
	}

	public boolean doCast(ServerPlayer serverPlayer) {
		return doCast(serverPlayer, DEFAULT_CAST_VALUE);
	}

	public boolean doCast(ServerPlayer serverPlayer, int i) {
		if (i > -6) // so you can click other things still!
			return false;
		if (serverPlayer.checkCooldown(this))
			return false;

		if (serverPlayer.player.getGameMode() == GameMode.CREATIVE) {
			if (cast(serverPlayer, i)) {
				serverPlayer.setCooldown(this, cooldown);
				return true;
			}
		} else {
			if (checkMana(serverPlayer)) {
				if (cast(serverPlayer, i)) {
					serverPlayer.removeMana(cost);
					serverPlayer.setCooldown(this, cooldown);
					showMana(serverPlayer);
					return true;
				}
			}
			showMana(serverPlayer);
		}
		return false;
	}

	public void cast(LivingEntity entity) {
		cast(entity, DEFAULT_CAST_VALUE);
	}

	public abstract void cast(LivingEntity entity, int i);

	public boolean cast(ServerPlayer serverPlayer) {
		return cast(serverPlayer, DEFAULT_CAST_VALUE);
	}

	public boolean cast(ServerPlayer serverPlayer, int i) {
		cast(serverPlayer.player, i);
		return true;
	}

	private boolean checkMana(ServerPlayer serverPlayer) {
		if (serverPlayer.hasMana(cost))
			return true;
		showNotEnoughMana(serverPlayer);
		return false;
	}

	private void showNotEnoughMana(ServerPlayer serverPlayer) {
		serverPlayer.sendActionBar(ChatColor.RED + cost + " mana needed!");
	}

	public static void showMana(ServerPlayer serverPlayer) {
		double manaPerSymbol = serverPlayer.getMaxMana() / 10;
		int amount = (int) Math.round(serverPlayer.getMana() / manaPerSymbol);
		serverPlayer.sendItemBar(MANA_STRINGS[amount]);
	}

	public static void showMana(ServerPlayer serverPlayer, int amount) {
		serverPlayer.sendItemBar(MANA_STRINGS[amount]);
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public int getCooldown() {
		return cooldown;
	}

	public double getCost() {
		return cost;
	}

	public static Spell load(ConfigurationSection section) {
		return AiostRegistry.SPELLS.get(section.getString("spell").toLowerCase());
	}
}