package com.pm.aiost.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class ItemHelper {

	public static ItemStack createWaterBottle() {
		ItemStack bottle = new ItemStack(Material.POTION, 1);
		ItemMeta meta = bottle.getItemMeta();
		PotionMeta pmeta = (PotionMeta) meta;
		pmeta.setBasePotionData(new PotionData(PotionType.WATER));
		bottle.setItemMeta(meta);
		return bottle;
	}
}
