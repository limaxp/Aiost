package com.pm.aiost.item.nms.items;

import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRiptideEvent;

import com.google.common.collect.Multimap;

import net.minecraft.server.v1_15_R1.AttributeModifier;
import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.EnchantmentManager;
import net.minecraft.server.v1_15_R1.EntityArrow;
import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.EntityThrownTrident;
import net.minecraft.server.v1_15_R1.EnumAnimation;
import net.minecraft.server.v1_15_R1.EnumHand;
import net.minecraft.server.v1_15_R1.EnumItemSlot;
import net.minecraft.server.v1_15_R1.EnumMoveType;
import net.minecraft.server.v1_15_R1.GenericAttributes;
import net.minecraft.server.v1_15_R1.IBlockData;
import net.minecraft.server.v1_15_R1.InteractionResultWrapper;
import net.minecraft.server.v1_15_R1.Item;
import net.minecraft.server.v1_15_R1.ItemStack;
import net.minecraft.server.v1_15_R1.MathHelper;
import net.minecraft.server.v1_15_R1.SoundCategory;
import net.minecraft.server.v1_15_R1.SoundEffect;
import net.minecraft.server.v1_15_R1.SoundEffects;
import net.minecraft.server.v1_15_R1.StatisticList;
import net.minecraft.server.v1_15_R1.Vec3D;
import net.minecraft.server.v1_15_R1.World;

public class ItemArmorstandProjectile extends Item {

	public ItemArmorstandProjectile(Item.Info item_info) {
		super(item_info);
	}

	@Override
	public boolean a(IBlockData iblockdata, World world, BlockPosition blockposition, EntityHuman entityhuman) {
		return !entityhuman.isCreative();
	}

	@Override
	public EnumAnimation e_(ItemStack itemstack) {
		return EnumAnimation.SPEAR;
	}

	@Override
	public int f_(ItemStack itemstack) {
		return 72000;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void a(ItemStack itemstack, World world, EntityLiving entityliving, int i) {
		if (entityliving instanceof EntityHuman) {
			EntityHuman entityhuman = (EntityHuman) entityliving;
			int j = f_(itemstack) - i;

			if (j >= 10) {
				int k = EnchantmentManager.g(itemstack);

				if (k <= 0 || entityhuman.isInWaterOrRain()) {
					if (!world.isClientSide) {

						if (k == 0) {
							EntityThrownTrident entitythrowntrident = new EntityThrownTrident(world, entityhuman,
									itemstack);

							entitythrowntrident.a(entityhuman, entityhuman.pitch, entityhuman.yaw, 0.0F,
									2.5F + k * 0.5F, 1.0F);
							if (entityhuman.abilities.canInstantlyBuild) {
								entitythrowntrident.fromPlayer = EntityArrow.PickupStatus.CREATIVE_ONLY;
							}

							if (!world.addEntity(entitythrowntrident)) {
								if (entityhuman instanceof EntityPlayer) {
									((EntityPlayer) entityhuman).getBukkitEntity().updateInventory();
								}

								return;
							}
							itemstack.damage(1, entityhuman,
									entityhuman1 -> entityhuman1.broadcastItemBreak(entityhuman1.getRaisedHand()));

							entitythrowntrident.trident = itemstack.cloneItemStack();

							world.playSound(null, entitythrowntrident, SoundEffects.ITEM_TRIDENT_THROW,
									SoundCategory.PLAYERS, 1.0F, 1.0F);
							if (!entityhuman.abilities.canInstantlyBuild) {
								entityhuman.inventory.f(itemstack);
							}
						} else {

							itemstack.damage(1, entityhuman,
									entityhuman1 -> entityhuman1.broadcastItemBreak(entityhuman1.getRaisedHand()));
						}
					}

					entityhuman.b(StatisticList.ITEM_USED.b(this));
					if (k > 0) {
						SoundEffect soundeffect;
						PlayerRiptideEvent event = new PlayerRiptideEvent((Player) entityhuman.getBukkitEntity(),
								CraftItemStack.asCraftMirror(itemstack));
						event.getPlayer().getServer().getPluginManager().callEvent(event);

						float f = entityhuman.yaw;
						float f1 = entityhuman.pitch;
						float f2 = -MathHelper.sin(f * 0.017453292F) * MathHelper.cos(f1 * 0.017453292F);
						float f3 = -MathHelper.sin(f1 * 0.017453292F);
						float f4 = MathHelper.cos(f * 0.017453292F) * MathHelper.cos(f1 * 0.017453292F);
						float f5 = MathHelper.c(f2 * f2 + f3 * f3 + f4 * f4);
						float f6 = 3.0F * (1.0F + k) / 4.0F;

						f2 *= f6 / f5;
						f3 *= f6 / f5;
						f4 *= f6 / f5;
						entityhuman.h(f2, f3, f4);
						entityhuman.r(20);
						if (entityhuman.onGround) {
							@SuppressWarnings("unused")
							float f7 = 1.1999999F;

							entityhuman.move(EnumMoveType.SELF, new Vec3D(0.0D, 1.1999999284744263D, 0.0D));
						}

						if (k >= 3) {
							soundeffect = SoundEffects.ITEM_TRIDENT_RIPTIDE_3;
						} else if (k == 2) {
							soundeffect = SoundEffects.ITEM_TRIDENT_RIPTIDE_2;
						} else {
							soundeffect = SoundEffects.ITEM_TRIDENT_RIPTIDE_1;
						}

						world.playSound(null, entityhuman, soundeffect, SoundCategory.PLAYERS, 1.0F, 1.0F);
					}
				}
			}
		}
	}

	@Override
	public InteractionResultWrapper<ItemStack> a(World world, EntityHuman entityhuman, EnumHand enumhand) {
		ItemStack itemstack = entityhuman.b(enumhand);

		if (itemstack.getDamage() >= itemstack.h() - 1)
			return InteractionResultWrapper.fail(itemstack);
		if (EnchantmentManager.g(itemstack) > 0 && !entityhuman.isInWaterOrRain()) {
			return InteractionResultWrapper.fail(itemstack);
		}
		entityhuman.c(enumhand);
		return InteractionResultWrapper.consume(itemstack);
	}

	@Override
	public boolean a(ItemStack itemstack, EntityLiving entityliving, EntityLiving entityliving1) {
		itemstack.damage(1, entityliving1, entityliving2 -> entityliving2.broadcastItemBreak(EnumItemSlot.MAINHAND));

		return true;
	}

	@Override
	public boolean a(ItemStack itemstack, World world, IBlockData iblockdata, BlockPosition blockposition,
			EntityLiving entityliving) {
		if (iblockdata.f(world, blockposition) != 0.0D) {
			itemstack.damage(2, entityliving, entityliving1 -> entityliving1.broadcastItemBreak(EnumItemSlot.MAINHAND));
		}

		return true;
	}

	@Override
	public Multimap<String, AttributeModifier> a(EnumItemSlot enumitemslot) {
		Multimap<String, AttributeModifier> multimap = super.a(enumitemslot);

		if (enumitemslot == EnumItemSlot.MAINHAND) {
			multimap.put(GenericAttributes.ATTACK_DAMAGE.getName(),
					new AttributeModifier(g, "Tool modifier", 8.0D, AttributeModifier.Operation.ADDITION));
			multimap.put(GenericAttributes.ATTACK_SPEED.getName(), new AttributeModifier(h, "Tool modifier",
					-2.9000000953674316D, AttributeModifier.Operation.ADDITION));
		}

		return multimap;
	}

	@Override
	public int c() {
		return 1;
	}
}
