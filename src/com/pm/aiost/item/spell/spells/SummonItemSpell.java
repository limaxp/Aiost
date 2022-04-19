package com.pm.aiost.item.spell.spells;

import java.util.HashMap;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.item.spell.Spell;
import com.pm.aiost.player.ServerPlayer;

public class SummonItemSpell extends Spell {

	private final ItemStack item;

	public SummonItemSpell(String name, int cooldown, double cost, ItemStack item) {
		this(name, name, cooldown, cost, item);
	}

	public SummonItemSpell(String name, String displayName, int cooldown, double cost, ItemStack item) {
		super(name, displayName, cooldown, cost);
		this.item = item;
	}

	@Override
	public void cast(LivingEntity entity, int i) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean cast(ServerPlayer serverPlayer, int i) {
		HashMap<Integer, ItemStack> couldnotStore = serverPlayer.player.getInventory().addItem(item);
		if (couldnotStore.size() > 0)
			return false;
		// TODO: remove after some time
		// TODO: make it inpossible to get the item 2 times
		return true;
	}

	public ItemStack getItem() {
		return item;
	}
}