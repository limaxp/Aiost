package com.pm.aiost.item.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import com.pm.aiost.misc.SpigotConfigManager;
import com.pm.aiost.misc.utils.nms.NMS;

import net.minecraft.server.v1_15_R1.CraftingManager;
import net.minecraft.server.v1_15_R1.IRecipe;
import net.minecraft.server.v1_15_R1.Item;
import net.minecraft.server.v1_15_R1.MinecraftKey;
import net.minecraft.server.v1_15_R1.Recipes;

public class RecipeManager {

	private static List<IRecipe<?>> recipes;

	static {
		initRecipes();
	}

	public static void init() {
		RecipeLoader.loadRecipeConfig(SpigotConfigManager.getRecipeConfig());
		RecipeLoader.loadConfig(SpigotConfigManager.getRecipeFolder());
		updateRecipes();
	}

	private static void initRecipes() {
		List<IRecipe<?>> recipes = new ArrayList<IRecipe<?>>();
		Map<Recipes<?>, Object2ObjectLinkedOpenHashMap<MinecraftKey, IRecipe<?>>> recipeMap = getCraftingManager().recipes;
		for (Object2ObjectLinkedOpenHashMap<MinecraftKey, IRecipe<?>> typeMap : recipeMap.values())
			for (IRecipe<?> recipe : typeMap.values())
				recipes.add(recipe);
		RecipeManager.recipes = recipes;
	}

	public static void updateRecipes() {
		CraftingManager craftingManager = getCraftingManager();
		clearRecipes(craftingManager);
		for (IRecipe<?> recipe : recipes)
			craftingManager.addRecipe(recipe);
	}

	private static void clearRecipes(CraftingManager craftingManager) {
		for (Object2ObjectLinkedOpenHashMap<MinecraftKey, IRecipe<?>> typeMap : craftingManager.recipes.values())
			typeMap.clear();
	}

	public static boolean addRecipe(IRecipe<?> recipe) {
		return recipes.add(recipe);
	}

	public static void addRecipes(IRecipe<?>... recipes) {
		for (IRecipe<?> recipe : recipes)
			RecipeManager.recipes.add(recipe);
	}

	public static void removeRecipe(IRecipe<?> recipe) {
		removeRecipes((recipe_) -> recipe == recipe_);
	}

	public static void removeRecipes(Material material) {
		removeRecipes(CraftMagicNumbers.getItem(material));
	}

	public static void removeRecipes(Item item) {
		removeRecipes((recipe) -> item == recipe.getResult().getItem());
	}

	public static void removeRecipes(ItemStack itemStack) {
		removeRecipes(CraftItemStack.asNMSCopy(itemStack));
	}

	public static void removeRecipes(net.minecraft.server.v1_15_R1.ItemStack itemStack) {
		removeRecipes((recipe) -> itemStack.equals(recipe.getResult()));
	}

	public static void removeRecipes(Recipe... recipes) {
		removeRecipes((recipe) -> {
			for (Recipe recipe_ : recipes) {
				if (recipe == recipe_)
					return true;
			}
			return false;
		});
	}

	public static void removeRecipes(Material... materials) {
		int length = materials.length;
		Item[] items = new Item[length];
		for (int i = 0; i < length; i++)
			items[i] = CraftMagicNumbers.getItem(materials[i]);
		removeRecipes(items);
	}

	public static void removeRecipes(Item... items) {
		removeRecipes((recipe) -> {
			Item result = recipe.getResult().getItem();
			for (Item item : items) {
				if (item == result)
					return true;
			}
			return false;
		});
	}

	public static void removeRecipes(ItemStack... itemStacks) {
		int length = itemStacks.length;
		net.minecraft.server.v1_15_R1.ItemStack[] items = new net.minecraft.server.v1_15_R1.ItemStack[length];
		for (int i = 0; i < length; i++)
			items[i] = CraftItemStack.asNMSCopy(itemStacks[i]);
		removeRecipes(items);
	}

	public static void removeRecipes(net.minecraft.server.v1_15_R1.ItemStack... itemStacks) {
		removeRecipes((recipe) -> {
			net.minecraft.server.v1_15_R1.ItemStack result = recipe.getResult();
			for (net.minecraft.server.v1_15_R1.ItemStack itemStack : itemStacks) {
				if (itemStack.equals(result))
					return true;
			}
			return false;
		});
	}

	public static void removeRecipes(Predicate<IRecipe<?>> predicate) {
		int length = recipes.size();
		for (int i = length - 1; i >= 0; i--) {
			IRecipe<?> recipe = recipes.get(i);
			if (predicate.test(recipe)) {
				recipes.remove(recipe);
			}
		}
	}

	public static CraftingManager getCraftingManager() {
		return NMS.getMinecraftServer().getCraftingManager();
	}
}
