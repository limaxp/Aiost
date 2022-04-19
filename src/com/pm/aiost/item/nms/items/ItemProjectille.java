package com.pm.aiost.item.nms.items;

import com.pm.aiost.entity.projectile.projectiles.ItemProjectile;

import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.EnumHand;
import net.minecraft.server.v1_15_R1.InteractionResultWrapper;
import net.minecraft.server.v1_15_R1.Item;
import net.minecraft.server.v1_15_R1.ItemStack;
import net.minecraft.server.v1_15_R1.SoundCategory;
import net.minecraft.server.v1_15_R1.SoundEffects;
import net.minecraft.server.v1_15_R1.StatisticList;
import net.minecraft.server.v1_15_R1.World;

public class ItemProjectille extends Item {

	public ItemProjectille(Item.Info item_info) {
		super(item_info);
	}

	@SuppressWarnings("deprecation")
	@Override
	public InteractionResultWrapper<ItemStack> a(World world, EntityHuman entityhuman, EnumHand enumhand) {
		ItemStack itemstack = entityhuman.b(enumhand);
		if (!world.isClientSide) {
			// TODO: set damage and knockback!
			ItemProjectile itemProjectile = new ItemProjectile(world, entityhuman, itemstack);
			itemProjectile.a(entityhuman, entityhuman.pitch, entityhuman.yaw, 0.0F, 1.5F, 1.0F);
			if (world.addEntity(itemProjectile)) {
				if (!entityhuman.abilities.canInstantlyBuild) {
					itemstack.subtract(1);
				}

				world.playSound(null, entityhuman.locX(), entityhuman.locY(), entityhuman.locZ(),
						SoundEffects.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F,
						0.4F / (i.nextFloat() * 0.4F + 0.8F));
			} else if (entityhuman instanceof EntityPlayer) {
				((EntityPlayer) entityhuman).getBukkitEntity().updateInventory();
			}
		}

		entityhuman.b(StatisticList.ITEM_USED.b(this));
		return InteractionResultWrapper.success(itemstack);
	}
}
