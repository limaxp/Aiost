package com.pm.aiost.block;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BlockMaterial {

	private static Material[] materials;

	static {
		Material[] values = Material.values();
		List<Material> materials = new ArrayList<Material>();
		int size = values.length;
		for (int i = 0; i < size; i++) {
			Material material = values[i];
			if (material.isBlock() && material.isItem()) {
				ItemStack is = new ItemStack(material);
				ItemMeta im = is.getItemMeta();
				if (im != null)
					materials.add(material);
			}
		}
		BlockMaterial.materials = materials.toArray(new Material[materials.size()]);
	}

	public static boolean isBlock(Material type) {
		if (type.isBlock() && type.isItem()) {
			ItemStack is = new ItemStack(type);
			ItemMeta im = is.getItemMeta();
			if (im != null)
				return true;
		}
		return false;
	}

	public static Material get(int index) {
		return materials[index];
	}

	public static int size() {
		return materials.length;
	}
}
