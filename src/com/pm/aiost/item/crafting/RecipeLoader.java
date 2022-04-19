package com.pm.aiost.item.crafting;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import com.pm.aiost.item.ItemLoader;
import com.pm.aiost.item.Items;
import com.pm.aiost.misc.SpigotConfigManager;
import com.pm.aiost.misc.log.Logger;

import net.minecraft.server.v1_15_R1.FurnaceRecipe;
import net.minecraft.server.v1_15_R1.IRecipe;
import net.minecraft.server.v1_15_R1.ItemStack;
import net.minecraft.server.v1_15_R1.MerchantRecipe;
import net.minecraft.server.v1_15_R1.MinecraftKey;
import net.minecraft.server.v1_15_R1.NonNullList;
import net.minecraft.server.v1_15_R1.RecipeBlasting;
import net.minecraft.server.v1_15_R1.RecipeCampfire;
import net.minecraft.server.v1_15_R1.RecipeCooking;
import net.minecraft.server.v1_15_R1.RecipeItemStack;
import net.minecraft.server.v1_15_R1.RecipeSmoking;
import net.minecraft.server.v1_15_R1.RecipeStonecutting;
import net.minecraft.server.v1_15_R1.ShapedRecipes;
import net.minecraft.server.v1_15_R1.ShapelessRecipes;

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

	public static IRecipe<?> loadRecipe(ConfigurationSection section) {
		if (!section.contains("item")) {
			Logger.warn("RecipeLoader: No item for recipe '" + section.getName() + "' defined!");
			return null;
		}
		ItemStack is = ItemLoader.loadNMSItem(section.get("item"));
		if (section.contains("amount")) {
			is = is.cloneItemStack();
			is.setCount(section.getInt("amount"));
		}
		return loadRecipe(section, is);
	}

	public static IRecipe<?> loadRecipe(ConfigurationSection section, ItemStack is) {
		String typeString = section.getString("type");
		if (typeString == null || typeString.isEmpty()) {
			Logger.warn("RecipeLoader: No type for recipe '" + section.getName() + "' defined!");
			return null;
		}
		IRecipe<?> recipe = loadRecipeFromType(typeString, section, is);
		if (recipe == null) {
			Logger.warn(
					"RecipeLoader: Type '" + typeString + "' for recipe '" + section.getName() + "' does not exist!");
			return null;
		}
		RecipeManager.addRecipe(recipe);
		return recipe;
	}

	private static IRecipe<?> loadRecipeFromType(String type, ConfigurationSection section, ItemStack is) {
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

	public static ShapedRecipes loadShapedRecipe(ConfigurationSection section, ItemStack is) {
		List<String> shape = section.getStringList("shape");
		if (shape == null || shape.isEmpty()) {
			Logger.warn("RecipeLoader: No shape for shaped recipe '" + section.getName() + "' defined!");
			return null;
		}
		Map<Character, RecipeItemStack> ingredients = loadShapedIngredients(section);
		int width = shape.get(0).length();
		int length = shape.size();
		NonNullList<RecipeItemStack> data = NonNullList.a(length * width, RecipeItemStack.a);
		for (int i = 0; i < length; i++) {
			String row = shape.get(i);
			for (int j = 0; j < row.length(); j++)
				data.set(i * width + j, ingredients.getOrDefault(row.charAt(j), RecipeItemStack.a));
		}
		return new CustomShapedRecipes(loadKey(section), loadGroup(section), width, length, data, is);
	}

	public static ShapelessRecipes loadShapelessRecipe(ConfigurationSection section, ItemStack is) {
		List<RecipeItemStack> ingredients = loadShapelessIngredients(section);
		int length = ingredients.size();
		NonNullList<RecipeItemStack> data = NonNullList.a(length, RecipeItemStack.a);
		for (int i = 0; i < length; i++)
			data.set(i, ingredients.get(i));
		return new CustomShapelessRecipes(loadKey(section), loadGroup(section), is, data);
	}

	public static <T extends RecipeCooking> RecipeCooking loadCookingRecipe(ConfigurationSection section, ItemStack is,
			RecipeCookingConstructor<T> constructor) {
		if (!section.contains("experience")) {
			Logger.warn("RecipeLoader: No experience for cooking recipe '" + section.getName() + "' defined!");
			return null;
		}
		if (!section.contains("cookingTime")) {
			Logger.warn("RecipeLoader: No cookingTime for cooking recipe '" + section.getName() + "' defined!");
			return null;
		}
		return constructor.create(loadKey(section), loadGroup(section), loadIngredient(section), is,
				(float) section.getDouble("experience"), section.getInt("cookingTime"));
	}

	public static RecipeStonecutting loadStonecuttingRecipe(ConfigurationSection section, ItemStack is) {
		// TODO: implement this!
		return null;
	}

	public static MerchantRecipe loadMerchantRecipe(ConfigurationSection section, ItemStack is) {
		// TODO: implement this!
		return null;
	}

	private static MinecraftKey loadKey(ConfigurationSection section) {
		return new MinecraftKey(section.getName().replace(" ", "_").toLowerCase());
	}

	private static String loadGroup(ConfigurationSection section) {
		String group = section.getString("group");
		if (group == null)
			return "";
		return group;
	}

	private static Map<Character, RecipeItemStack> loadShapedIngredients(ConfigurationSection section) {
		Map<Character, RecipeItemStack> map = new HashMap<Character, RecipeItemStack>();
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

	private static List<RecipeItemStack> loadShapelessIngredients(ConfigurationSection section) {
		List<RecipeItemStack> list = new ArrayList<RecipeItemStack>();
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

	private static RecipeItemStack loadIngredient(ConfigurationSection section) {
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

	public static RecipeItemStack createRecipeItemStack(List<Material> materials, boolean requireNotEmpty) {
		return buildRecipeItemStack(
				new RecipeItemStack(
						materials.stream()
								.map(mat -> new RecipeItemStack.StackProvider(
										CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(mat))))),
				requireNotEmpty);
	}

	public static RecipeItemStack createExactRecipeItemStack(List<ItemStack> items, boolean requireNotEmpty) {
		RecipeItemStack stack = new RecipeItemStack(items.stream().map(mat -> new RecipeItemStack.StackProvider(mat)));
		stack.exact = true;
		return buildRecipeItemStack(stack, requireNotEmpty);
	}

	private static RecipeItemStack buildRecipeItemStack(RecipeItemStack stack, boolean requireNotEmpty) {
		stack.buildChoices();
		if (requireNotEmpty && stack.choices.length == 0) {
			throw new IllegalArgumentException("Recipe requires at least one non-air choice!");
		}
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
	public static interface RecipeCookingConstructor<T extends RecipeCooking> {

		public T create(MinecraftKey key, String group, RecipeItemStack source, ItemStack result, float experience,
				int cookingTime);
	}

	public static class CustomShapedRecipes extends ShapedRecipes {

		public CustomShapedRecipes(MinecraftKey minecraftkey, String s, int i, int j,
				NonNullList<RecipeItemStack> nonnulllist, ItemStack itemstack) {
			super(minecraftkey, s, i, j, nonnulllist, itemstack);
		}

		@Override
		public ShapedRecipe toBukkitRecipe() {
			try {
				return super.toBukkitRecipe();
			} catch (IllegalArgumentException e) {
				return null;
			}
		}
	}

	public static class CustomShapelessRecipes extends ShapelessRecipes {

		public CustomShapelessRecipes(MinecraftKey minecraftkey, String s, ItemStack itemstack,
				NonNullList<RecipeItemStack> nonnulllist) {
			super(minecraftkey, s, itemstack, nonnulllist);
		}

		@Override
		public ShapelessRecipe toBukkitRecipe() {
			try {
				return super.toBukkitRecipe();
			} catch (IllegalArgumentException e) {
				return null;
			}
		}
	}

	public static class CustomFurnaceRecipes extends FurnaceRecipe {

		public CustomFurnaceRecipes(MinecraftKey minecraftkey, String s, RecipeItemStack recipeitemstack,
				ItemStack itemstack, float f, int i) {
			super(minecraftkey, s, recipeitemstack, itemstack, f, i);
		}

		@Override
		public Recipe toBukkitRecipe() {
			try {
				return super.toBukkitRecipe();
			} catch (IllegalArgumentException e) {
				return null;
			}
		}
	}

	public static class CustomRecipeBlasting extends RecipeBlasting {

		public CustomRecipeBlasting(MinecraftKey minecraftkey, String s, RecipeItemStack recipeitemstack,
				ItemStack itemstack, float f, int i) {
			super(minecraftkey, s, recipeitemstack, itemstack, f, i);
		}

		@Override
		public Recipe toBukkitRecipe() {
			try {
				return super.toBukkitRecipe();
			} catch (IllegalArgumentException e) {
				return null;
			}
		}
	}

	public static class CustomRecipeCampfire extends RecipeCampfire {

		public CustomRecipeCampfire(MinecraftKey minecraftkey, String s, RecipeItemStack recipeitemstack,
				ItemStack itemstack, float f, int i) {
			super(minecraftkey, s, recipeitemstack, itemstack, f, i);
		}

		@Override
		public Recipe toBukkitRecipe() {
			try {
				return super.toBukkitRecipe();
			} catch (IllegalArgumentException e) {
				return null;
			}
		}
	}

	public static class CustomRecipeSmoking extends RecipeSmoking {

		public CustomRecipeSmoking(MinecraftKey minecraftkey, String s, RecipeItemStack recipeitemstack,
				ItemStack itemstack, float f, int i) {
			super(minecraftkey, s, recipeitemstack, itemstack, f, i);
		}

		@Override
		public Recipe toBukkitRecipe() {
			try {
				return super.toBukkitRecipe();
			} catch (IllegalArgumentException e) {
				return null;
			}
		}
	}
}
