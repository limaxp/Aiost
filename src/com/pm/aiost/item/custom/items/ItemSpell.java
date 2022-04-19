package com.pm.aiost.item.custom.items;

import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.item.custom.AiostItem;
import com.pm.aiost.item.custom.Hand;
import com.pm.aiost.item.custom.InteractionResultWrapper;
import com.pm.aiost.item.custom.ItemAnimation;
import com.pm.aiost.item.spell.Spell;

public class ItemSpell extends AiostItem {

	protected final Spell spell;

	public ItemSpell(Spell spell, Info info) {
		super(info);
		this.spell = spell;
	}

	@Override
	protected InteractionResultWrapper onRightClick(World world, HumanEntity entity, Hand hand, ItemStack is) {
		holdRightClick(entity, hand);
		return InteractionResultWrapper.CONSUME;
	}

	@Override
	protected void onRightClickRelease(ItemStack is, World world, LivingEntity entity, int i) {
		spell.doCast(entity, i);
	}

	@Override
	protected ItemAnimation getAnimation(ItemStack is) {
		return ItemAnimation.BOW;
	}

	public Spell getSpell() {
		return spell;
	}
}