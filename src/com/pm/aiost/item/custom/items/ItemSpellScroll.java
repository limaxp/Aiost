package com.pm.aiost.item.custom.items;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.item.spell.Spell;

public class ItemSpellScroll extends ItemSpell {

	public ItemSpellScroll(Spell spell, Info info) {
		super(spell, info);
	}

	@Override
	protected void onRightClickRelease(ItemStack is, World world, LivingEntity entity, int i) {
		if (spell.doCast(entity, i))
			reduceItemAmount(is);
	}

	public static void reduceItemAmount(ItemStack is) {
		int amount = is.getAmount();
		if (amount > 1)
			is.setAmount(amount - 1);
		else
			is.setType(Material.AIR);
	}
}
