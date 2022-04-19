package com.pm.aiost.item.custom.items;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.entity.projectile.projectiles.ItemProjectile;
import com.pm.aiost.item.Items;
import com.pm.aiost.item.custom.AiostItem;
import com.pm.aiost.item.custom.Hand;
import com.pm.aiost.item.custom.InteractionResultWrapper;
import com.pm.aiost.item.custom.ItemAnimation;
import com.pm.aiost.misc.utils.nms.NMS;

import net.minecraft.server.v1_15_R1.EntityHuman;

public class ItemGun extends AiostItem {

	public ItemGun(Info info) {
		super(info);
	}

	@Override
	protected InteractionResultWrapper onRightClick(World world, HumanEntity human, Hand hand, ItemStack is) {
		if (is.getAmount() > 1) {
			EntityHuman entityHuman = NMS.getNMS(human);
			ItemProjectile itemProjectile = new ItemProjectile(NMS.getNMS(world), entityHuman,
					NMS.getNMS(Items.get("bullet_1")));
			itemProjectile.a(entityHuman, entityHuman.pitch, entityHuman.yaw, 0.0F, 1.5F, 1.0F);
			if (itemProjectile.world.addEntity(itemProjectile)) {
				if (human.getGameMode() != GameMode.CREATIVE)
					is.setAmount(is.getAmount() - 1);

				world.playSound(human.getLocation(), Sound.BLOCK_ANVIL_BREAK, SoundCategory.NEUTRAL, 0.5F,
						0.4F / (i.nextFloat() * 0.4F + 0.8F));
				return InteractionResultWrapper.SUCCESS;
			}
			return InteractionResultWrapper.PASS;
		}
		return InteractionResultWrapper.FAIL;
	}

	@Override
	protected ItemAnimation getAnimation(ItemStack is) {
		return ItemAnimation.BOW;
	}
}