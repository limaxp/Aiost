package com.pm.aiost.item.crafting;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import com.pm.aiost.item.ItemLoader;
import com.pm.aiost.item.Items;
import com.pm.aiost.misc.SpigotConfigManager;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.nms.NMS;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.item.crafting.StonecutterRecipe;

public class RecipeLoader {

	static void loadRecipeConfig(ConfigurationSection section) {
		Set<String> recipeNames = section.getKeys(false);
		for (String recipeName : recipeNames)
			if (recipeName.charAt(0) == '!')
				switchCommand(recipeName, section.get(recipeName));
			else
				loadRecipe(section.getConfigurationSection(recipeName));
	}

	public static void loadConfig(File file) {
		if (!file.exists())
			return;
		if (file.isFile()) {
			loadRecipe(SpigotConfigManager.loadConfig(file));
			return;
		}
		loadConfigs(file);
	}

	private static void loadConfigs(File dir) {
		for (File listFile : dir.listFiles()) {
			if (listFile.isFile())
				loadRecipe(SpigotConfigManager.loadConfig(listFile));
			else if (listFile.isDirectory())
				loadConfigs(listFile);
		}
	}

	public static void loadRecipes(ConfigurationSection section) {
		Set<String> recipeNames = section.getKeys(false);
		for (String recipeName : recipeNames)
			loadRecipe(section.getConfigurationSection(recipeName));
	}

	public static void loadRecipes(ConfigurationSection section, ItemStack is) {
		Set<String> recipeNames = section.getKeys(false);
		for (String recipeName : recipeNames)
			loadRecipe(section.getConfigurationSection(recipeName), is);
	}

	public static Recipe<?> loadRecipe(ConfigurationSection section) {
		try {
			if (!section.contains("item")) {
				Logger.warn("RecipeLoader: No item for recipe '" + section.getName() + "' defined!");
				return null;
			}
			ItemStack is = ItemLoader.loadNMSItem(section.get("item"));
			if (section.contains("amount")) {
				is = is.copy();
				is.setCount(section.getInt("amount"));
			}
			return loadRecipe(section, is);
		} catch (Exception e) {
			Logger.warn("RecipeLoader: " + e.getClass().getName() + " loading " + section.getName());
			return null;
		}
	}

	public static Recipe<?> loadRecipe(ConfigurationSection section, ItemStack is) {
		try {
			String name = section.getName();
			String typeString = section.getString("type");
			if (typeString == null || typeString.isEmpty()) {
				Logger.warn("RecipeLoader: No type for recipe '" + name + "' defined!");
				return null;
			}
			Recipe<?> recipe = loadRecipeFromType(typeString, section, is);
			if (recipe == null) {
				Logger.warn("RecipeLoader: Type '" + typeString + "' for recipe '" + name + "' does not exist!");
				return null;
			}
			RecipeManager.addRecipe(name, recipe);
			return recipe;
		} catch (Exception e) {
			Logger.warn("RecipeLoader: " + e.getClass().getName() + " loading " + section.getName());
			return null;
		}
	}

	private static Recipe<?> loadRecipeFromType(String type, ConfigurationSection section, ItemStack is) {
		switch (type.toLowerCase()) {
		case "shaped":
			return loadShapedRecipe(section, is);

		case "shapeless":
			return loadShapelessRecipe(section, is);

		case "furnace":
			return loadCookingRecipe(section, is, CustomFurnaceRecipes::new);

		case "blasting":
			return loadCookingRecipe(section, is, CustomRecipeBlasting::new);

		case "campfire":
			return loadCookingRecipe(section, is, CustomRecipeCampfire::new);

		case "merchant":
//			return loadMerchantRecipe(section, is);
			return null;

		case "smoking":
			return loadCookingRecipe(section, is, CustomRecipeSmoking::new);

		case "stonecutting":
			return loadStonecuttingRecipe(section, is);

		default:
			return null;
		}
	}

	public static net.minecraft.world.item.crafting.ShapedRecipe loadShapedRecipe(ConfigurationSection section,
			ItemStack is) {
		List<String> shape = section.getStringList("shape");
		if (shape == null || shape.isEmpty()) {
			Logger.warn("RecipeLoader: No shape for shaped recipe '" + section.getName() + "' defined!");
			return null;
		}
		Map<Character, Ingredient> ingredients = loadShapedIngredients(section);
		int width = shape.get(0).length();
		int height = shape.size();
		NonNullList<Ingredient> data = NonNullList.<Ingredient>withSize(width * height, Ingredient.EMPTY);
		for (int i = 0; i < height; i++) {
			String row = shape.get(i);
			for (int j = 0; j < row.length(); j++)
				data.set(i * width + j, ingredients.getOrDefault(row.charAt(j), Ingredient.EMPTY));
		}
		ShapedRecipePattern pattern = new ShapedRecipePattern(width, height, data, Optional.empty());
		return new CustomShapedRecipes(loadKey(section), loadCraftingCategory(section), pattern, is, true);
	}

	public static net.minecraft.world.item.crafting.ShapelessRecipe loadShapelessRecipe(ConfigurationSection section,
			ItemStack is) {
		List<Ingredient> ingredients = loadShapelessIngredients(section);
		int length = ingredients.size();
		NonNullList<Ingredient> data = NonNullList.<Ingredient>withSize(length, Ingredient.EMPTY);
		for (int i = 0; i < length; i++)
			data.set(i, ingredients.get(i));
		return new CustomShapelessRecipes(loadKey(section), loadCraftingCategory(section), is, data);
	}

	public static <T extends AbstractCookingRecipe> AbstractCookingRecipe loadCookingRecipe(
			ConfigurationSection section, ItemStack is, RecipeCookingConstructor<T> constructor) {
		if (!section.contains("experience")) {
			Logger.warn("RecipeLoader: No experience for cooking recipe '" + section.getName() + "' defined!");
			return null;
		}
		if (!section.contains("cookingTime")) {
			Logger.warn("RecipeLoader: No cookingTime for cooking recipe '" + section.getName() + "' defined!");
			return null;
		}
		return constructor.create(loadKey(section), loadCookingCategory(section), loadIngredient(section), is,
				(float) section.getDouble("experience"), section.getInt("cookingTime"));
	}

	public static StonecutterRecipe loadStonecuttingRecipe(ConfigurationSection section, ItemStack is) {
		// TODO: implement this!
		return null;
	}

	public static MerchantRecipe loadMerchantRecipe(ConfigurationSection section, ItemStack is) {
		// TODO: implement this!
		return null;
	}

	private static String loadKey(ConfigurationSection section) {
		return section.getName().replace(" ", "_").toLowerCase();
	}

	private static CraftingBookCategory loadCraftingCategory(ConfigurationSection section) {
		CraftingBookCategory group = CraftingBookCategory.valueOf(section.getString("group"));
		if (group == null)
			return CraftingBookCategory.MISC;
		return group;
	}

	private static CookingBookCategory loadCookingCategory(ConfigurationSection section) {
		CookingBookCategory group = CookingBookCategory.valueOf(section.getString("group"));
		if (group == null)
			return CookingBookCategory.MISC;
		return group;
	}

	private static Map<Character, Ingredient> loadShapedIngredients(ConfigurationSection section) {
		Map<Character, Ingredient> map = new HashMap<Character, Ingredient>();
		if (section.contains("ingredients")) {
			ConfigurationSection ingredientsSection = section.getConfigurationSection("ingredients");
			for (String ingredient : ingredientsSection.getKeys(false))
				map.put(ingredient.charAt(0),
						createRecipeItemStack(Arrays.asList(loadMaterials(ingredientsSection.get(ingredient))), false));
		}
		if (section.contains("exact_ingredients")) {
			ConfigurationSection exactIngredientsSection = section.getConfigurationSection("exact_ingredients");
			for (String ingredient : exactIngredientsSection.getKeys(false))
				map.put(ingredient.charAt(0), createExactRecipeItemStack(
						Arrays.asList(loadItems(exactIngredientsSection.get(ingredient))), false));
		}
		if (section.contains("group_ingredients")) {
//			ConfigurationSection groupIngredientsSection = recipeSection.getConfigurationSection("group_ingredients");
//			for (String ingredient : groupIngredientsSection.getKeys(false)) {
			// TODO: implement a way to use groups in Recipes
			// in future versions there is a way to get all Tag values!
			// or find some way in nms
			// after update change all stone and wood recipes!
//			}
		}
		return map;
	}

	private static List<Ingredient> loadShapelessIngredients(ConfigurationSection section) {
		List<Ingredient> list = new ArrayList<Ingredient>();
		if (section.contains("ingredients")) {
			ConfigurationSection ingredientsSection = section.getConfigurationSection("ingredients");
			for (String ingredient : ingredientsSection.getKeys(false))
				list.add(createRecipeItemStack(Arrays.asList(loadMaterials(ingredientsSection.get(ingredient))), true));
		}
		if (section.contains("exact_ingredients")) {
			ConfigurationSection exactIngredientsSection = section.getConfigurationSection("exact_ingredients");
			for (String ingredient : exactIngredientsSection.getKeys(false))
				list.add(createExactRecipeItemStack(Arrays.asList(loadItems(exactIngredientsSection.get(ingredient))),
						true));
		}
		return list;
	}

	private static Ingredient loadIngredient(ConfigurationSection section) {
		if (section.contains("ingredient"))
			return createRecipeItemStack(Arrays.asList(loadMaterials(section.get("ingredient"))), true);
		if (section.contains("exact_ingredient"))
			return createExactRecipeItemStack(Arrays.asList(loadItems(section.get("exact_ingredient"))), true);
		return null;
	}

	@SuppressWarnings("unchecked")
	private static Material[] loadMaterials(Object ingredientObject) {
		if (ingredientObject instanceof String)
			return new Material[] { Material.valueOf(((String) ingredientObject).toUpperCase()) };
		else if (ingredientObject instanceof List)
			return loadMaterials((List<String>) ingredientObject);
		return null;
	}

	private static Material[] loadMaterials(List<String> materialNames) {
		int size = materialNames.size();
		Material[] materials = new Material[size];
		for (int i = 0; i < size; i++)
			materials[i] = Material.valueOf(materialNames.get(i).toUpperCase());
		return materials;
	}

	@SuppressWarnings("unchecked")
	private static ItemStack[] loadItems(Object ingredientObject) {
		if (ingredientObject instanceof String)
			return new ItemStack[] { ItemLoader.loadNMSItem((String) ingredientObject) };
		else if (ingredientObject instanceof List)
			return loadItems((List<String>) ingredientObject);
		else if (ingredientObject instanceof ConfigurationSection)
			return loadItems((ConfigurationSection) ingredientObject);
		return null;
	}

	private static ItemStack[] loadItems(List<String> itemNames) {
		int size = itemNames.size();
		ItemStack[] items = new ItemStack[size];
		for (int i = 0; i < size; i++)
			items[i] = ItemLoader.loadNMSItem(itemNames.get(i));
		return items;
	}

	private static ItemStack[] loadItems(ConfigurationSection section) {
		Set<String> itemNames = section.getKeys(false);
		if (itemNames.size() == 1)
			return new ItemStack[] { ItemLoader.loadNMSItem(section.get(itemNames.iterator().next())) };
		else
			return ItemLoader.loadNMSItems(section);
	}

	public static Ingredient createRecipeItemStack(List<Material> materials, boolean requireNotEmpty) {
		return new Ingredient(materials.stream()
				.map(mat -> new Ingredient.ItemValue(NMS.getNMS(new org.bukkit.inventory.ItemStack(mat)))));
	}

	public static Ingredient createExactRecipeItemStack(List<ItemStack> items, boolean requireNotEmpty) {
		Ingredient stack = new Ingredient(items.stream().map(mat -> new Ingredient.ItemValue(mat)));
		stack.exact = true;
		return stack;
	}

	private static void switchCommand(String recipeName, Object commandObject) {
		switch (recipeName) {
		case "!Remove":
			removeCommand(commandObject);
			break;

		default:
			break;
		}
	}

	private static void removeCommand(Object commandObject) {
		if (commandObject instanceof String) {
			String materialName = (String) commandObject;
			Material material = Material.valueOf((materialName).toUpperCase());
			if (material != null)
				RecipeManager.removeRecipes(material);
			else
				RecipeManager.removeRecipes(Items.get(materialName));
		}
		if (commandObject instanceof List) {
			@SuppressWarnings("unchecked")
			List<String> materialNames = (List<String>) commandObject;
			int size = materialNames.size();
			List<Material> materials = new ArrayList<Material>();
			List<ItemStack> itemStacks = new ArrayList<ItemStack>();
			for (int i = 0; i < size; i++) {
				String materialName = materialNames.get(i);
				Material material = Material.valueOf((materialName).toUpperCase());
				if (material != null)
					materials.add(material);
				else
					itemStacks.add(CraftItemStack.asNMSCopy(Items.get(materialName)));
			}

			if (materialNames.size() > 0)
				RecipeManager.removeRecipes(materials.toArray(new Material[materials.size()]));
			if (itemStacks.size() > 0)
				RecipeManager.removeRecipes(itemStacks.toArray(new ItemStack[itemStacks.size()]));
		} else if (commandObject instanceof ConfigurationSection) {
			ConfigurationSection commandSection = (ConfigurationSection) commandObject;
			Set<String> itemNames = commandSection.getKeys(false);
			if (itemNames.size() == 1)
				RecipeManager.removeRecipes(ItemLoader.loadNMSItem(commandSection.get(itemNames.iterator().next())));
			else
				RecipeManager.removeRecipes(ItemLoader.loadNMSItems(commandSection));
		}
	}

	@FunctionalInterface
	public static interface RecipeCookingConstructor<T extends AbstractCookingRecipe> {

		public T create(String s, CookingBookCategory category, Ingredient source, ItemStack result, float experience,
				int cookingTime);
	}

	public static class CustomShapedRecipes extends net.minecraft.world.item.crafting.ShapedRecipe {

		public CustomShapedRecipes(String s, CraftingBookCategory category, ShapedRecipePattern pattern,
				ItemStack result, boolean flag) {
			super(s, category, pattern, result, flag);
		}

		@Override
		public ShapedRecipe toBukkitRecipe(NamespacedKey key) {
			try {
				return super.toBukkitRecipe(key);
			} catch (IllegalArgumentException e) {
				return null;
			}
		}
	}

	public static class CustomShapelessRecipes extends net.minecraft.world.item.crafting.ShapelessRecipe {

		public CustomShapelessRecipes(String s, CraftingBookCategory category, ItemStack result,
				NonNullList<Ingredient> ingredients) {
			super(s, category, result, ingredients);
		}

		@Override
		public ShapelessRecipe toBukkitRecipe(NamespacedKey key) {
			try {
				return super.toBukkitRecipe(key);
			} catch (IllegalArgumentException e) {
				return null;
			}
		}
	}

	public static class CustomFurnaceRecipes extends SmeltingRecipe {

		public CustomFurnaceRecipes(String s, CookingBookCategory category, Ingredient ingredient, ItemStack result,
				float experience, int cookingTime) {
			super(s, category, ingredient, result, experience, cookingTime);
		}

		@Override
		public org.bukkit.inventory.Recipe toBukkitRecipe(NamespacedKey key) {
			try {
				return super.toBukkitRecipe(key);
			} catch (IllegalArgumentException e) {
				return null;
			}
		}
	}

	public static class CustomRecipeBlasting extends BlastingRecipe {

		public CustomRecipeBlasting(String s, CookingBookCategory category, Ingredient ingredient, ItemStack result,
				float experience, int cookingTime) {
			super(s, category, ingredient, result, experience, cookingTime);
		}

		@Override
		public org.bukkit.inventory.Recipe toBukkitRecipe(NamespacedKey key) {
			try {
				return super.toBukkitRecipe(key);
			} catch (IllegalArgumentException e) {
				return null;
			}
		}
	}

	public static class CustomRecipeCampfire extends CampfireCookingRecipe {

		public CustomRecipeCampfire(String s, CookingBookCategory category, Ingredient ingredient, ItemStack result,
				float experience, int cookingTime) {
			super(s, category, ingredient, result, experience, cookingTime);
		}

		@Override
		public org.bukkit.inventory.Recipe toBukkitRecipe(NamespacedKey key) {
			try {
				return super.toBukkitRecipe(key);
			} catch (IllegalArgumentException e) {
				return null;
			}
		}
	}

	public static class CustomRecipeSmoking extends SmokingRecipe {

		public CustomRecipeSmoking(String s, CookingBookCategory category, Ingredient recipeitemstack,
				ItemStack itemstack, float experience, int cookingTime) {
			super(s, category, recipeitemstack, itemstack, experience, cookingTime);
		}

		@Override
		public org.bukkit.inventory.Recipe toBukkitRecipe(NamespacedKey key) {
			try {
				return super.toBukkitRecipe(key);
			} catch (IllegalArgumentException e) {
				return null;
			}
		}
	}
}
