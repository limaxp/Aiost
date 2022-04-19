package com.pm.aiost.item.custom.items;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.item.custom.AiostItem;
import com.pm.aiost.item.custom.ItemAnimation;
import com.pm.aiost.item.spell.Spell;

public class ItemLeftClickSpell extends AiostItem {

	protected final Spell spell;

	public ItemLeftClickSpell(Spell spell, Info info) {
		super(info);
		this.spell = spell;
	}

	@Override
	protected boolean onLeftClickBlock(BlockData blockData, Location loc, HumanEntity entity) {
		spell.doCast(entity);
		return true;
	}

	@Override
	protected boolean onLeftClickEntity(ItemStack is, LivingEntity entity1, LivingEntity entity2) {
		spell.doCast(entity2);
		return false;
	}

	@Override
	protected ItemAnimation getAnimation(ItemStack is) {
		return ItemAnimation.BOW;
	}

	public Spell getSpell() {
		return spell;
	}
}