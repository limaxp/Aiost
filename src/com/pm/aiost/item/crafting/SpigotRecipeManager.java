package com.pm.aiost.item.crafting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import com.pm.aiost.misc.SpigotConfigManager;

public class SpigotRecipeManager {

	private static List<Recipe> recipes;

	static {
		initRecipes();
	}

	@SuppressWarnings("unused")
	private static void init() {
		SpigotRecipeLoader.loadRecipeConfig(SpigotConfigManager.getRecipeConfig());
		updateRecipes();
	}

	private static void initRecipes() {
		List<Recipe> recipes = new ArrayList<Recipe>();
		Iterator<Recipe> iterator = Bukkit.getServer().recipeIterator();
		while (iterator.hasNext())
			recipes.add(iterator.next());
		SpigotRecipeManager.recipes = recipes;
	}

	public static void updateRecipes() {
		Server server = Bukkit.getServer();
		server.clearRecipes();
		for (Recipe recipe : recipes)
			server.addRecipe(recipe);
	}

	public static boolean addRecipe(Recipe recipe) {
		return recipes.add(recipe);
	}

	public static void addRecipes(Recipe... recipes) {
		for (Recipe recipe : recipes)
			SpigotRecipeManager.recipes.add(recipe);
	}

	public static void removeRecipe(Recipe recipe) {
		removeRecipes((recipe_) -> recipe == recipe_);
	}

	public static void removeRecipes(Material material) {
		removeRecipes((recipe) -> material == recipe.getResult().getType());
	}

	public static void removeRecipes(ItemStack itemStack) {
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
		removeRecipes((recipe) -> {
			Material result = recipe.getResult().getType();
			for (Material material : materials) {
				if (material == result)
					return true;
			}
			return false;
		});
	}

	public static void removeRecipes(ItemStack... itemStacks) {
		removeRecipes((recipe) -> {
			ItemStack result = recipe.getResult();
			for (ItemStack itemStack : itemStacks) {
				if (itemStack.equals(result))
					return true;
			}
			return false;
		});
	}

	public static void removeRecipes(Predicate<Recipe> predicate) {
		int length = recipes.size();
		for (int i = length - 1; i >= 0; i--) {
			Recipe recipe = recipes.get(i);
			if (predicate.test(recipe)) {
				recipes.remove(recipe);
			}
		}
	}
}
