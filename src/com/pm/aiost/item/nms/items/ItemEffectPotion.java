package com.pm.aiost.item.nms.items;

import org.bukkit.entity.Player;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.player.ServerPlayer;

import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.ItemPotion;
import net.minecraft.server.v1_15_R1.ItemStack;
import net.minecraft.server.v1_15_R1.World;

public class ItemEffectPotion extends ItemPotion {

	private Effect[] effects;
	private int time;

	public ItemEffectPotion(Effect effect, int time, Info info) {
		this(new Effect[] { effect }, time, info);
	}

	public ItemEffectPotion(Effect[] effects, int time, Info info) {
		super(info);
		this.effects = effects;
		this.time = time;
	}

	@Override
	public ItemStack a(ItemStack itemstack, World world, EntityLiving entityliving) {
		if (entityliving instanceof EntityHuman) {
			EntityHuman entityHuman = (EntityHuman) entityliving;
			ServerPlayer.getByPlayer((Player) entityHuman.getBukkitEntity()).addEffect(time, effects);
		}
		return itemstack;
	}

	public Effect[] getEffects() {
		return effects;
	}

	public int getTime() {
		return time;
	}
}
