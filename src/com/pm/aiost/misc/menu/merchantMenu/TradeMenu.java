package com.pm.aiost.misc.menu.merchantMenu;

import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.player.ServerPlayer;

import net.minecraft.server.v1_15_R1.MerchantRecipe;
import net.minecraft.server.v1_15_R1.MerchantRecipeList;

public class TradeMenu implements Menu {

	private String name;
	private MerchantRecipeList recipeList;

	public TradeMenu(String name) {
		this.name = name;
		recipeList = new MerchantRecipeList();
	}

	public void addRecipe(MerchantRecipe recipe) {
		recipeList.add(recipe);
	}

	public void addRecipes(MerchantRecipe recipe) {
		recipeList.add(recipe);
	}

	public void addRecipe(ItemStack in, ItemStack result, int i, int j, int f) {
		recipeList.add(new MerchantRecipe(CraftItemStack.asNMSCopy(in), CraftItemStack.asNMSCopy(result), i, j, f));
	}

	public void addRecipe(ItemStack inOne, ItemStack inTwo, ItemStack result, int i, int j, int f) {
		recipeList.add(new MerchantRecipe(CraftItemStack.asNMSCopy(inOne), CraftItemStack.asNMSCopy(inTwo),
				CraftItemStack.asNMSCopy(result), i, j, f));
	}

	@Override
	public void open(Player player) {
//		((CraftPlayer) player).getHandle().openTrade(i, recipeList, j, k, flag);
	}

	@Override
	public void open(HumanEntity player) {

	}

	@Override
	public void open(ServerPlayer serverPlayer) {

	}

	public String getName() {
		return name;
	}
}
