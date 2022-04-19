package com.pm.aiost.item.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.CampfireRecipe;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.RecipeChoice.ExactChoice;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.SmokingRecipe;
import org.bukkit.inventory.StonecuttingRecipe;

import com.pm.aiost.Aiost;
import com.pm.aiost.item.ItemLoader;
import com.pm.aiost.item.Items;
import com.pm.aiost.misc.log.Logger;

@SuppressWarnings("deprecation")
public class SpigotRecipeLoader {

	static void loadRecipeConfig(ConfigurationSection recipesSection) {
		Set<String> recipeNames = recipesSection.getKeys(false);
		for (String recipeName : recipeNames) {
			if (recipeName.charAt(0) == '!')
				switchCommand(recipeName, recipesSection);
			else
				loadRecipe(recipesSection.getConfigurationSection(recipeName));
		}
	}

	public static void loadRecipes(ConfigurationSection recipesSection, ItemStack is) {
		Set<String> recipeNames = recipesSection.getKeys(false);
		for (String recipeName : recipeNames) {
			if (recipeName.charAt(0) == '!')
				switchCommand(recipeName, recipesSection);
			else
				loadRecipe(recipesSection.getConfigurationSection(recipeName), is);
		}
	}

	public static void loadRecipe(ConfigurationSection recipeSection) {
		if (!recipeSection.contains("item")) {
			Logger.warn("RecipeLoader: No item for recipe '" + recipeSection.getName() + "' defined!");
			return;
		}
		ItemStack is = ItemLoader.loadItem(recipeSection.get("item"));
		if (recipeSection.contains("amount")) {
			is = is.clone();
			is.setAmount(recipeSection.getInt("amount"));
		}
		loadRecipe(recipeSection, is);
	}

	public static void loadRecipe(ConfigurationSection recipeSection, ItemStack is) {
		String typeString = recipeSection.getString("type");
		if (typeString == null || typeString.isEmpty()) {
			Logger.warn("RecipeLoader: No type for recipe '" + recipeSection.getName() + "' defined!");
			return;
		}
		Recipe recipe = loadRecipeFromType(typeString, recipeSection, is);
		if (recipe == null) {
			Logger.warn("RecipeLoader: Type '" + typeString + "' for recipe '" + recipeSection.getName()
					+ "' does not exist!");
			return;
		}
		SpigotRecipeManager.addRecipe(recipe);
	}

	public static Recipe loadRecipeFromType(String type, ConfigurationSection recipeSection, ItemStack is) {
		switch (type.toLowerCase()) {
		case "shaped":
			return loadShapedRecipe(recipeSection, is);

		case "shapeless":
			return loadShapelessRecipe(recipeSection, is);

		case "furnace":
			return loadCookingRecipe(recipeSection, is, FurnaceRecipe::new);

		case "blasting":
			return loadCookingRecipe(recipeSection, is, BlastingRecipe::new);

		case "campfire":
			return loadCookingRecipe(recipeSection, is, CampfireRecipe::new);

		case "merchant":
			return loadMerchantRecipe(recipeSection, is);

		case "smoking":
			return loadCookingRecipe(recipeSection, is, SmokingRecipe::new);

		case "stonecutting":
			return loadStonecuttingRecipe(recipeSection, is);

		default:
			return null;
		}
	}

	public static ShapedRecipe loadShapedRecipe(ConfigurationSection recipeSection, ItemStack is) {
		NamespacedKey key = new NamespacedKey(Aiost.getPlugin(), recipeSection.getName().replace(" ", "_"));
		List<String> shapeStrings = recipeSection.getStringList("shape");
		if (shapeStrings == null || shapeStrings.isEmpty()) {
			Logger.warn("RecipeLoader: No shape for shaped recipe '" + key + "' defined!");
			return null;
		}

		ShapedRecipe recipe = new ShapedRecipe(key, is);
		recipe.shape(shapeStrings.toArray(new String[shapeStrings.size()]));
		loadIngredients(recipeSection, recipe);
		String group = recipeSection.getString("group");
		if (group != null && !group.isEmpty())
			recipe.setGroup(group);
		return recipe;
	}

	public static ShapelessRecipe loadShapelessRecipe(ConfigurationSection recipeSection, ItemStack is) {
		ShapelessRecipe recipe = new ShapelessRecipe(
				new NamespacedKey(Aiost.getPlugin(), recipeSection.getName().replace(" ", "_")), is);
		loadIngredients(recipeSection, recipe::addIngredient);
		String group = recipeSection.getString("group");
		if (group != null && !group.isEmpty())
			recipe.setGroup(group);
		return recipe;
	}

	public static <T extends CookingRecipe<T>> T loadCookingRecipe(ConfigurationSection recipeSection, ItemStack is,
			CookingRecipeConstructor<T> constructor) {
		NamespacedKey key = new NamespacedKey(Aiost.getPlugin(), recipeSection.getName().replace(" ", "_"));
		if (!recipeSection.contains("experience")) {
			Logger.warn("RecipeLoader: No experience for cooking recipe '" + key + "' defined!");
			return null;
		}
		if (!recipeSection.contains("cookingTime")) {
			Logger.warn("RecipeLoader: No cookingTime for cooking recipe '" + key + "' defined!");
			return null;
		}

		T recipe = constructor.create(key, is, Material.AIR, (float) recipeSection.getDouble("experience"),
				recipeSection.getInt("cookingTime"));
		loadIngredients(recipeSection, recipe::setInputChoice);
		String group = recipeSection.getString("group");
		if (group != null && !group.isEmpty())
			recipe.setGroup(group);
		return recipe;
	}

	public static StonecuttingRecipe loadStonecuttingRecipe(ConfigurationSection recipeSection, ItemStack is) {
		// TODO: implement this!
		return null;
	}

	public static MerchantRecipe loadMerchantRecipe(ConfigurationSection recipeSection, ItemStack is) {
		return null;
	}

	@SuppressWarnings("unchecked")
	private static void loadIngredients(ConfigurationSection recipeSection, ShapedRecipe recipe) {
		if (recipeSection.contains("ingredients")) {
			ConfigurationSection ingredientsSection = recipeSection.getConfigurationSection("ingredients");
			for (String ingredient : ingredientsSection.getKeys(false)) {
				Object ingredientObj = ingredientsSection.get(ingredient);
				if (ingredientObj instanceof String)
					recipe.setIngredient(ingredient.charAt(0),
							Material.valueOf(((String) ingredientObj).toUpperCase()));
				else if (ingredientObj instanceof List)
					recipe.setIngredient(ingredient.charAt(0), loadMaterialChoice((List<String>) ingredientObj));

			}
		}
		if (recipeSection.contains("exact_ingredients")) {
			ConfigurationSection exactIngredientsSection = recipeSection.getConfigurationSection("exact_ingredients");
			for (String ingredient : exactIngredientsSection.getKeys(false))
				recipe.setIngredient(ingredient.charAt(0), loadExactChoice(exactIngredientsSection.get(ingredient)));
		}
	}

	@SuppressWarnings("unchecked")
	private static void loadIngredients(ConfigurationSection recipeSection,
			Consumer<RecipeChoice> addRecipeChoiceFunction) {
		if (recipeSection.contains("ingredients")) {
			ConfigurationSection ingredientsSection = recipeSection.getConfigurationSection("ingredients");
			for (String ingredient : ingredientsSection.getKeys(false)) {
				Object ingredientObj = ingredientsSection.get(ingredient);
				if (ingredientObj instanceof String)
					addRecipeChoiceFunction
							.accept(new MaterialChoice(Material.valueOf(((String) ingredientObj).toUpperCase())));
				else if (ingredientObj instanceof List)
					addRecipeChoiceFunction.accept(loadMaterialChoice((List<String>) ingredientObj));
			}
		}
		if (recipeSection.contains("exact_ingredients")) {
			ConfigurationSection exactIngredientsSection = recipeSection.getConfigurationSection("exact_ingredients");
			for (String ingredient : exactIngredientsSection.getKeys(false))
				addRecipeChoiceFunction.accept(loadExactChoice(exactIngredientsSection.get(ingredient)));
		}
	}

	private static MaterialChoice loadMaterialChoice(List<String> materialNames) {
		int size = materialNames.size();
		Material[] materials = new Material[size];
		for (int i = 0; i < size; i++)
			materials[i] = Material.valueOf(materialNames.get(i).toUpperCase());
		return new RecipeChoice.MaterialChoice(materials);
	}

	private static ExactChoice loadExactChoice(Object ingredientObject) {
		if (ingredientObject instanceof String)
			return new ExactChoice(loadItemStack((String) ingredientObject));
		if (ingredientObject instanceof List) {
			@SuppressWarnings("unchecked")
			List<String> materialNames = (List<String>) ingredientObject;
			int size = materialNames.size();
			ItemStack[] items = new ItemStack[size];
			for (int i = 0; i < size; i++)
				items[i] = loadItemStack(materialNames.get(i));
			return new ExactChoice(items);
		} else if (ingredientObject instanceof ConfigurationSection)
			return loadExactChoice((ConfigurationSection) ingredientObject);
		return null;
	}

	private static ItemStack loadItemStack(String materialName) {
		if (materialName.charAt(0) == '!')
			return Items.get(materialName.substring(1));
		return new ItemStack(Material.valueOf(materialName.toUpperCase()));
	}

	private static ExactChoice loadExactChoice(ConfigurationSection ingredientSection) {
		Set<String> itemNames = ingredientSection.getKeys(false);
		if (itemNames.size() == 1)
			return new ExactChoice(
					ItemLoader.loadItem(ingredientSection.getConfigurationSection(itemNames.iterator().next())));
		else
			return new ExactChoice(ItemLoader.loadItems(ingredientSection));
	}

	private static void switchCommand(String recipeName, ConfigurationSection recipesSection) {
		switch (recipeName) {
		case "!Remove":
			removeCommand(recipesSection.get(recipeName));
			break;

		default:
			break;
		}
	}

	private static void removeCommand(Object commandObject) {
		if (commandObject instanceof String) {
			String materialName = (String) commandObject;
			if (materialName.charAt(0) == '!')
				SpigotRecipeManager.removeRecipes(Items.get(materialName.substring(1)));
			else
				SpigotRecipeManager.removeRecipes(Material.valueOf((materialName).toUpperCase()));
		}
		if (commandObject instanceof List) {
			@SuppressWarnings("unchecked")
			List<String> materialNames = (List<String>) commandObject;
			int size = materialNames.size();
			List<Material> materials = new ArrayList<Material>();
			List<ItemStack> itemStacks = new ArrayList<ItemStack>();
			for (int i = 0; i < size; i++) {
				String materialName = materialNames.get(i);
				if (materialName.charAt(0) == '!')
					itemStacks.add(Items.get(materialName.substring(1)));
				else
					materials.add(Material.valueOf(materialNames.get(i).toUpperCase()));
			}

			if (materialNames.size() > 0)
				SpigotRecipeManager.removeRecipes(materials.toArray(new Material[materials.size()]));
			if (itemStacks.size() > 0)
				SpigotRecipeManager.removeRecipes(itemStacks.toArray(new ItemStack[itemStacks.size()]));
		} else if (commandObject instanceof ConfigurationSection) {
			ConfigurationSection commandSection = (ConfigurationSection) commandObject;
			Set<String> itemNames = commandSection.getKeys(false);
			if (itemNames.size() == 1)
				SpigotRecipeManager.removeRecipes(
						ItemLoader.loadItem(commandSection.getConfigurationSection(itemNames.iterator().next())));
			else
				SpigotRecipeManager.removeRecipes(ItemLoader.loadItems(commandSection));
		}
	}

	@FunctionalInterface
	public static interface CookingRecipeConstructor<T extends CookingRecipe<T>> {

		public T create(NamespacedKey key, ItemStack result, Material source, float experience, int cookingTime);
	}
}
