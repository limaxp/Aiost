package com.pm.aiost.item.nms.items;

import org.bukkit.craftbukkit.v1_15_R1.event.CraftEventFactory;
import org.bukkit.event.entity.EntityShootBowEvent;

import net.minecraft.server.v1_15_R1.EnchantmentManager;
import net.minecraft.server.v1_15_R1.Enchantments;
import net.minecraft.server.v1_15_R1.EntityArrow;
import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.ItemArrow;
import net.minecraft.server.v1_15_R1.ItemBow;
import net.minecraft.server.v1_15_R1.ItemStack;
import net.minecraft.server.v1_15_R1.Items;
import net.minecraft.server.v1_15_R1.SoundCategory;
import net.minecraft.server.v1_15_R1.SoundEffects;
import net.minecraft.server.v1_15_R1.StatisticList;
import net.minecraft.server.v1_15_R1.ToolMaterial;
import net.minecraft.server.v1_15_R1.World;

public class ItemMaterialBow extends ItemBow {

	private final ToolMaterial toolMaterial;
	protected final float damage;
	protected final float speedMultiplier;

	public ItemMaterialBow(ToolMaterial toolMaterial, float damage, float speedMultiplier, Info info) {
		super(info);
		this.toolMaterial = toolMaterial;
		this.damage = damage;
		this.speedMultiplier = speedMultiplier + 3.0F + toolMaterial.b() * 0.1F;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void a(ItemStack itemstack, World world, EntityLiving entityliving, int i) {
//		super.a(itemstack, world, entityliving, i);
		if (entityliving instanceof EntityHuman) {
			EntityHuman entityhuman = (EntityHuman) entityliving;
			boolean flag = !(!entityhuman.abilities.canInstantlyBuild
					&& EnchantmentManager.getEnchantmentLevel(Enchantments.ARROW_INFINITE, itemstack) <= 0);
			ItemStack itemstack1 = entityhuman.f(itemstack);

			if (!itemstack1.isEmpty() || flag) {
				if (itemstack1.isEmpty()) {
					itemstack1 = new ItemStack(Items.ARROW);
				}

				int j = f_(itemstack) - i;
				float f = a(j);

				if (f >= 0.1D) {
					boolean flag1 = (flag && itemstack1.getItem() == Items.ARROW);

					if (!world.isClientSide) {
						ItemArrow itemarrow = (ItemArrow) ((itemstack1.getItem() instanceof ItemArrow)
								? itemstack1.getItem()
								: Items.ARROW);
						EntityArrow entityarrow = itemarrow.a(world, itemstack1, entityhuman);

						entityarrow.a(entityhuman, entityhuman.pitch, entityhuman.yaw, 0.0F, f * speedMultiplier, 1.0F);
						if (f == 1.0F) {
							entityarrow.setCritical(true);
						}

						int k = EnchantmentManager.getEnchantmentLevel(Enchantments.ARROW_DAMAGE, itemstack);

						if (k > 0) {
							entityarrow.setDamage(entityarrow.getDamage() + k * 0.5D + 0.5D + damage);
						}

						int l = EnchantmentManager.getEnchantmentLevel(Enchantments.ARROW_KNOCKBACK, itemstack);

						if (l > 0) {
							entityarrow.setKnockbackStrength(l);
						}

						if (EnchantmentManager.getEnchantmentLevel(Enchantments.ARROW_FIRE, itemstack) > 0) {
							entityarrow.setOnFire(100);
						}

						EntityShootBowEvent event = CraftEventFactory.callEntityShootBowEvent(entityhuman, itemstack,
								entityarrow, f);
						if (event.isCancelled()) {
							event.getProjectile().remove();

							return;
						}

						itemstack.damage(1, entityhuman,
								entityhuman1 -> entityhuman1.broadcastItemBreak(entityhuman1.getRaisedHand()));

						if (flag1 || (entityhuman.abilities.canInstantlyBuild
								&& (itemstack1.getItem() == Items.SPECTRAL_ARROW
										|| itemstack1.getItem() == Items.TIPPED_ARROW))) {
							entityarrow.fromPlayer = EntityArrow.PickupStatus.CREATIVE_ONLY;
						}

						if (event.getProjectile() == entityarrow.getBukkitEntity() && !world.addEntity(entityarrow)) {
							if (entityhuman instanceof EntityPlayer) {
								((EntityPlayer) entityhuman).getBukkitEntity().updateInventory();
							}

							return;
						}
					}

					world.playSound(null, entityhuman.locX(), entityhuman.locY(), entityhuman.locZ(),
							SoundEffects.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F,
							1.0F / (ItemBow.i.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
					if (!flag1 && !entityhuman.abilities.canInstantlyBuild) {
						itemstack1.subtract(1);
						if (itemstack1.isEmpty()) {
							entityhuman.inventory.f(itemstack1);
						}
					}

					entityhuman.b(StatisticList.ITEM_USED.b(this));
				}
			}
		}
	}

	public ToolMaterial getToolMaterial() {
		return toolMaterial;
	}
}
