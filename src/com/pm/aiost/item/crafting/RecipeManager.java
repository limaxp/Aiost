package com.pm.aiost.item.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R4.CraftRegistry;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R4.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import com.pm.aiost.misc.SpigotConfigManager;
import com.pm.aiost.misc.nms.NMS;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeHolder;

public class RecipeManager {

	private static List<RecipeHolder<?>> recipes;

	static {
		initRecipes();
	}

	public static void init() {
		RecipeLoader.loadRecipeConfig(SpigotConfigManager.getRecipeConfig());
		RecipeLoader.loadConfig(SpigotConfigManager.getRecipeFolder());
		updateRecipes();
	}

	private static void initRecipes() {
		List<RecipeHolder<?>> recipes = new ArrayList<RecipeHolder<?>>();
		RecipeManager.recipes = recipes;
		for (RecipeHolder<?> recipe : getCraftingManager().getRecipes())
			recipes.add(recipe);
	}

	public static void updateRecipes() {
		net.minecraft.world.item.crafting.RecipeManager craftingManager = getCraftingManager();
		craftingManager.clearRecipes();
		for (RecipeHolder<?> recipe : recipes)
			craftingManager.addRecipe(recipe);
	}

	public static boolean addRecipe(String key, net.minecraft.world.item.crafting.Recipe<?> recipe) {
		return recipes
				.add(new RecipeHolder<net.minecraft.world.item.crafting.Recipe<?>>(new ResourceLocation(key), recipe));
	}

	public static void removeRecipe(net.minecraft.world.item.crafting.Recipe<?> recipe) {
		removeRecipes((recipe_) -> recipe == recipe_);
	}

	public static void removeRecipes(Material material) {
		removeRecipes(CraftMagicNumbers.getItem(material));
	}

	public static void removeRecipes(Item item) {
		removeRecipes((recipe) -> item == recipe.getResultItem(CraftRegistry.getMinecraftRegistry()).getItem());
	}

	public static void removeRecipes(ItemStack itemStack) {
		removeRecipes(CraftItemStack.asNMSCopy(itemStack));
	}

	public static void removeRecipes(net.minecraft.world.item.ItemStack itemStack) {
		removeRecipes((recipe) -> itemStack.equals(recipe.getResultItem(CraftRegistry.getMinecraftRegistry())));
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
			Item result = recipe.getResultItem(CraftRegistry.getMinecraftRegistry()).getItem();
			for (Item item : items) {
				if (item == result)
					return true;
			}
			return false;
		});
	}

	public static void removeRecipes(ItemStack... itemStacks) {
		int length = itemStacks.length;
		net.minecraft.world.item.ItemStack[] items = new net.minecraft.world.item.ItemStack[length];
		for (int i = 0; i < length; i++)
			items[i] = CraftItemStack.asNMSCopy(itemStacks[i]);
		removeRecipes(items);
	}

	public static void removeRecipes(net.minecraft.world.item.ItemStack... itemStacks) {
		removeRecipes((recipe) -> {
			net.minecraft.world.item.ItemStack result = recipe.getResultItem(CraftRegistry.getMinecraftRegistry());
			for (net.minecraft.world.item.ItemStack itemStack : itemStacks) {
				if (itemStack.equals(result))
					return true;
			}
			return false;
		});
	}

	public static void removeRecipes(Predicate<net.minecraft.world.item.crafting.Recipe<?>> predicate) {
		int length = recipes.size();
		for (int i = length - 1; i >= 0; i--) {
			RecipeHolder<?> recipe = recipes.get(i);
			if (predicate.test(recipe.value()))
				recipes.remove(recipe);
		}
	}

	public static net.minecraft.world.item.crafting.RecipeManager getCraftingManager() {
		return NMS.getMinecraftServer().getRecipeManager();
	}
}
