package com.pm.aiost.misc.menu.merchantMenu;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;

import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.player.ServerPlayer;

public class MerchantMenu implements Menu {

	protected Merchant merchant;

	public MerchantMenu(String name) {
		merchant = createMerchant(name);
	}

	public MerchantMenu(String name, List<MerchantRecipe> recipes) {
		this(name);
		setRecipes(recipes);
	}

	protected Merchant createMerchant(String name) {
		return Bukkit.createMerchant(name);
	}

	public void setRecipe(int index, MerchantRecipe recipe) {
		merchant.setRecipe(index, recipe);
	}

	public void setRecipes(List<MerchantRecipe> recipes) {
		merchant.setRecipes(recipes);
	}

	@Override
	public void open(Player player) {
		player.openMerchant(merchant, true);
	}

	@Override
	public void open(HumanEntity player) {
		player.openMerchant(merchant, true);
	}

	@Override
	public void open(ServerPlayer serverPlayer) {
		serverPlayer.player.openMerchant(merchant, true);
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public static Merchant createMerchant(String name, List<MerchantRecipe> recipes) {
		Merchant merchant = Bukkit.createMerchant(name);
		merchant.setRecipes(recipes);
		return merchant;
	}

	public static MerchantRecipe createRecipe(@Nonnull ItemStack result, @Nonnull List<ItemStack> ingredients) {
		MerchantRecipe recipe = new MerchantRecipe(result, Integer.MAX_VALUE);
		recipe.setIngredients(ingredients);
		return recipe;
	}

	public static MerchantRecipe createRecipe(@Nonnull Material result, @Nonnull List<ItemStack> ingredients) {
		return createRecipe(new ItemStack(result), ingredients);
	}

	public static MerchantRecipe createRecipe(@Nonnull ItemStack result, @Nonnull ItemStack... ingredients) {
		return createRecipe(result, Arrays.asList(ingredients));
	}

	public static MerchantRecipe createRecipe(@Nonnull Material result, @Nonnull ItemStack... ingredients) {
		return createRecipe(new ItemStack(result), Arrays.asList(ingredients));
	}

	public static MerchantRecipe createRecipe(@Nonnull ItemStack result, int maxUses,
			@Nonnull List<ItemStack> ingredients) {
		MerchantRecipe recipe = new MerchantRecipe(result, maxUses);
		recipe.setIngredients(ingredients);
		return recipe;
	}

	public static MerchantRecipe createRecipe(@Nonnull Material result, int maxUses,
			@Nonnull List<ItemStack> ingredients) {
		return createRecipe(new ItemStack(result), maxUses, ingredients);
	}

	public static MerchantRecipe createRecipe(@Nonnull ItemStack result, int maxUses,
			@Nonnull ItemStack... ingredients) {
		return createRecipe(result, maxUses, Arrays.asList(ingredients));
	}

	public static MerchantRecipe createRecipe(@Nonnull Material result, int maxUses,
			@Nonnull ItemStack... ingredients) {
		return createRecipe(new ItemStack(result), maxUses, Arrays.asList(ingredients));
	}
}
