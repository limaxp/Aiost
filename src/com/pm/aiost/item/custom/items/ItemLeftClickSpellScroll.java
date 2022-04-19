package com.pm.aiost.item.custom.items;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.item.spell.Spell;

public class ItemLeftClickSpellScroll extends ItemLeftClickSpell {

	public ItemLeftClickSpellScroll(Spell spell, Info info) {
		super(spell, info);
	}

	@Override
	protected boolean onLeftClickBlock(BlockData blockData, Location loc, HumanEntity entity) {
		if (spell.doCast(entity)) {
			// TODO: get ItemStack somehow!
//			ItemSpellScroll.reduceItemAmount(is);
		}
		return true;
	}

	@Override
	protected boolean onLeftClickEntity(ItemStack is, LivingEntity entity1, LivingEntity entity2) {
		if (spell.doCast(entity2))
			ItemSpellScroll.reduceItemAmount(is);
		return false;
	}
}
