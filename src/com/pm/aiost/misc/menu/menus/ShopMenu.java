package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import com.pm.aiost.item.ItemHelper;
import com.pm.aiost.item.Items;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.merchantMenu.MerchantMenu;
import com.pm.aiost.misc.utils.ChatColor;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

@SuppressWarnings("deprecation")
public class ShopMenu extends SingleInventoryMenu {

	private static final ItemStack BUY_LIVE_ITEM = MetaHelper.setMeta(Material.PLAYER_HEAD, BOLD + "Buy Live",
			Arrays.asList(GRAY + "Click to buy a live"));

	private static final ItemStack CANNOT_BUY_LIVES_ITEM = MetaHelper.setMeta(Material.BARRIER,
			RED + BOLD + "Cannot buy lives!", Arrays.asList(GRAY + "This game does not allow buying lives"));

	private static final ItemStack NOT_ENOUGH_MONEY_FOR_LIVE_ITEM = MetaHelper.setMeta(Material.BARRIER,
			RED + BOLD + "Not enough money!", Arrays.asList(GRAY + "You need 200 to buy a live"));

	private final Merchant[] menus;
	private final InventoryMenu liveMenu;

	public ShopMenu(boolean canBuyLives) {
		super(BOLD + "Shop", 4, true);
		set(MetaHelper.setMeta(Material.DIAMOND_SWORD, BOLD + "Sword shop",
				Arrays.asList(GRAY + "Click to view sword shop")),

				MetaHelper.set(Items.getClone("diamond_dagger"), BOLD + "Dagger shop",
						Arrays.asList(GRAY + "Click to view dagger shop")),

				MetaHelper.setMeta(Material.DIAMOND_AXE, BOLD + "Axe shop",
						Arrays.asList(GRAY + "Click to view axe shop")),

				MetaHelper.set(Items.getClone("diamond_waraxe"), BOLD + "Waraxe shop",
						Arrays.asList(GRAY + "Click to view waraxé shop")),

				MetaHelper.set(Items.getClone("diamond_bihander"), BOLD + "Bihander shop",
						Arrays.asList(GRAY + "Click to view bihander shop")),

				MetaHelper.set(Items.getClone("diamond_hammer"), BOLD + "Hammer shop",
						Arrays.asList(GRAY + "Click to view hammer shop")),

				MetaHelper.set(Items.getClone("diamond_spear"), BOLD + "Spear shop",
						Arrays.asList(GRAY + "Click to view spear shop")),

				MetaHelper.setMeta(Material.BOW, BOLD + "Bow shop", Arrays.asList(GRAY + "Click to view bow shop")),

				MetaHelper.set(Items.getClone("flame"), BOLD + "Spell shop",
						Arrays.asList(GRAY + "Click to view spell shop")),

				MetaHelper.setMeta(Material.DIAMOND_HELMET, BOLD + "Armor shop",
						Arrays.asList(GRAY + "Click to view armor shop")),

				MetaHelper.setMeta(Material.COOKED_BEEF, BOLD + "Food shop",
						Arrays.asList(GRAY + "Click to view food shop")),

				MetaHelper.setMeta(Material.POTION, BOLD + "Potion shop",
						Arrays.asList(GRAY + "Click to view ppotion shop")),

				MetaHelper.setMeta(Material.ENDER_PEARL, BOLD + "Miscellaneous shop",
						Arrays.asList(GRAY + "Click to view miscellaneous shop")),

				BUY_LIVE_ITEM);

		menus = createSubMenus();
		if (canBuyLives)
			liveMenu = createLiveMenu();
		else
			liveMenu = null;
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			int index = InventoryMenu.parseBorderedIndex(event.getView().getTitle(), event.getSlot());
			if (index == 13) {
				if (liveMenu == null) {
					displayInSlot(serverPlayer.player, CANNOT_BUY_LIVES_ITEM, BUY_LIVE_ITEM, event.getSlot(), 30);
					return;
				}
				if (!serverPlayer.player.getInventory().containsAtLeast(new ItemStack(Material.GOLD_NUGGET), 200)) {
					displayInSlot(serverPlayer.player, NOT_ENOUGH_MONEY_FOR_LIVE_ITEM, BUY_LIVE_ITEM, event.getSlot(),
							30);
					return;
				}
				liveMenu.open(serverPlayer);
				return;
			}
			serverPlayer.player.openMerchant(menus[index], true);
		}
	}

	private YesNoMenu createLiveMenu() {
		YesNoMenu menu = new YesNoMenu(BOLD + "Buy 1 live?", Arrays.asList(GRAY + "Gives you +1 respawn!"));
		menu.setBackLink(this);
		menu.setYesCallback((serverPlayer, event) -> {
			serverPlayer.getGameData().addLive();
			ShopMenu.this.open(serverPlayer);
		});
		return menu;
	}

	private static Merchant[] createSubMenus() {
		Merchant[] menus = new Merchant[13];
		menus[0] = createSwordMenu();
		menus[1] = createDaggerMenu();
		menus[2] = createAxeMenu();
		menus[3] = createWaraxeMenu();
		menus[4] = createBihanderMenu();
		menus[5] = createHammerMenu();
		menus[6] = createSpearMenu();
		menus[7] = createBowMenu();
		menus[8] = createSpellMenu();
		menus[9] = createArmorMenu();
		menus[10] = createFoodMenu();
		menus[11] = createPotionMenu();
		menus[12] = createMiscMenu();
		return menus;
	}

	private static Merchant createSwordMenu() {
		List<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();
		recipes.add(MerchantMenu.createRecipe(Material.WOODEN_SWORD, new ItemStack(Material.GOLD_NUGGET, 4)));
		recipes.add(MerchantMenu.createRecipe(Material.STONE_SWORD, new ItemStack(Material.GOLD_NUGGET, 10)));
		recipes.add(MerchantMenu.createRecipe(Items.get("copper_sword"), new ItemStack(Material.GOLD_NUGGET, 15)));
		recipes.add(MerchantMenu.createRecipe(Items.get("tin_sword"), new ItemStack(Material.GOLD_NUGGET, 18)));
		recipes.add(MerchantMenu.createRecipe(Material.GOLDEN_SWORD, new ItemStack(Material.GOLD_NUGGET, 20)));
		recipes.add(MerchantMenu.createRecipe(Items.get("bronze_sword"), new ItemStack(Material.GOLD_NUGGET, 20)));
		recipes.add(MerchantMenu.createRecipe(Material.IRON_SWORD, new ItemStack(Material.GOLD_NUGGET, 25)));
		recipes.add(MerchantMenu.createRecipe(Items.get("emerald_sword"), new ItemStack(Material.GOLD_NUGGET, 40)));
		recipes.add(MerchantMenu.createRecipe(Items.get("ruby_sword"), new ItemStack(Material.GOLD_NUGGET, 50)));
		recipes.add(MerchantMenu.createRecipe(Material.DIAMOND_SWORD, new ItemStack(Material.GOLD_NUGGET, 64)));
		recipes.add(MerchantMenu.createRecipe(Items.get("black_diamond_sword"), new ItemStack(Material.GOLD_NUGGET, 64),
				new ItemStack(Material.GOLD_NUGGET, 10)));
		recipes.add(MerchantMenu.createRecipe(Items.get("obsidian_crystal_sword"),
				new ItemStack(Material.GOLD_NUGGET, 64), new ItemStack(Material.GOLD_NUGGET, 20)));
		recipes.add(MerchantMenu.createRecipe(Items.get("lava_crystal_sword"), new ItemStack(Material.GOLD_NUGGET, 64),
				new ItemStack(Material.GOLD_NUGGET, 30)));
		recipes.add(MerchantMenu.createRecipe(Items.get("quartz_crystal_sword"),
				new ItemStack(Material.GOLD_NUGGET, 64), new ItemStack(Material.GOLD_NUGGET, 40)));
		return MerchantMenu.createMerchant(ChatColor.BOLD + "Sword shop", recipes);
	}

	private static Merchant createDaggerMenu() {
		List<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();
		recipes.add(MerchantMenu.createRecipe(Items.get("wooden_dagger"), new ItemStack(Material.GOLD_NUGGET, 2)));
		recipes.add(MerchantMenu.createRecipe(Items.get("stone_dagger"), new ItemStack(Material.GOLD_NUGGET, 5)));
		recipes.add(MerchantMenu.createRecipe(Items.get("copper_dagger"), new ItemStack(Material.GOLD_NUGGET, 8)));
		recipes.add(MerchantMenu.createRecipe(Items.get("tin_dagger"), new ItemStack(Material.GOLD_NUGGET, 10)));
		recipes.add(MerchantMenu.createRecipe(Items.get("golden_dagger"), new ItemStack(Material.GOLD_NUGGET, 12)));
		recipes.add(MerchantMenu.createRecipe(Items.get("bronze_dagger"), new ItemStack(Material.GOLD_NUGGET, 12)));
		recipes.add(MerchantMenu.createRecipe(Items.get("iron_dagger"), new ItemStack(Material.GOLD_NUGGET, 15)));
		recipes.add(MerchantMenu.createRecipe(Items.get("emerald_dagger"), new ItemStack(Material.GOLD_NUGGET, 25)));
		recipes.add(MerchantMenu.createRecipe(Items.get("ruby_dagger"), new ItemStack(Material.GOLD_NUGGET, 35)));
		recipes.add(MerchantMenu.createRecipe(Items.get("diamond_dagger"), new ItemStack(Material.GOLD_NUGGET, 45)));
		recipes.add(
				MerchantMenu.createRecipe(Items.get("black_diamond_dagger"), new ItemStack(Material.GOLD_NUGGET, 55)));
		recipes.add(MerchantMenu.createRecipe(Items.get("obsidian_crystal_dagger"),
				new ItemStack(Material.GOLD_NUGGET, 64)));
		recipes.add(MerchantMenu.createRecipe(Items.get("lava_crystal_dagger"), new ItemStack(Material.GOLD_NUGGET, 64),
				new ItemStack(Material.GOLD_NUGGET, 10)));
		recipes.add(MerchantMenu.createRecipe(Items.get("quartz_crystal_dagger"),
				new ItemStack(Material.GOLD_NUGGET, 64), new ItemStack(Material.GOLD_NUGGET, 20)));
		return MerchantMenu.createMerchant(ChatColor.BOLD + "Dagger shop", recipes);
	}

	private static Merchant createAxeMenu() {
		List<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();
		recipes.add(MerchantMenu.createRecipe(Material.WOODEN_AXE, new ItemStack(Material.GOLD_NUGGET, 6)));
		recipes.add(MerchantMenu.createRecipe(Material.STONE_AXE, new ItemStack(Material.GOLD_NUGGET, 15)));
		recipes.add(MerchantMenu.createRecipe(Items.get("copper_axe"), new ItemStack(Material.GOLD_NUGGET, 20)));
		recipes.add(MerchantMenu.createRecipe(Items.get("tin_axe"), new ItemStack(Material.GOLD_NUGGET, 25)));
		recipes.add(MerchantMenu.createRecipe(Material.GOLDEN_AXE, new ItemStack(Material.GOLD_NUGGET, 30)));
		recipes.add(MerchantMenu.createRecipe(Items.get("bronze_axe"), new ItemStack(Material.GOLD_NUGGET, 30)));
		recipes.add(MerchantMenu.createRecipe(Material.IRON_AXE, new ItemStack(Material.GOLD_NUGGET, 40)));
		recipes.add(MerchantMenu.createRecipe(Items.get("emerald_axe"), new ItemStack(Material.GOLD_NUGGET, 50)));
		recipes.add(MerchantMenu.createRecipe(Items.get("ruby_axe"), new ItemStack(Material.GOLD_NUGGET, 60)));
		recipes.add(MerchantMenu.createRecipe(Material.DIAMOND_AXE, new ItemStack(Material.GOLD_NUGGET, 64),
				new ItemStack(Material.GOLD_NUGGET, 10)));
		recipes.add(MerchantMenu.createRecipe(Items.get("black_diamond_axe"), new ItemStack(Material.GOLD_NUGGET, 64),
				new ItemStack(Material.GOLD_NUGGET, 20)));
		recipes.add(MerchantMenu.createRecipe(Items.get("obsidian_crystal_axe"),
				new ItemStack(Material.GOLD_NUGGET, 64), new ItemStack(Material.GOLD_NUGGET, 30)));
		recipes.add(MerchantMenu.createRecipe(Items.get("lava_crystal_axe"), new ItemStack(Material.GOLD_NUGGET, 64),
				new ItemStack(Material.GOLD_NUGGET, 40)));
		recipes.add(MerchantMenu.createRecipe(Items.get("quartz_crystal_axe"), new ItemStack(Material.GOLD_NUGGET, 64),
				new ItemStack(Material.GOLD_NUGGET, 50)));
		return MerchantMenu.createMerchant(ChatColor.BOLD + "Axe shop", recipes);
	}

	private static Merchant createWaraxeMenu() {
		List<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();
		recipes.add(MerchantMenu.createRecipe(Items.get("wooden_waraxe"), new ItemStack(Material.GOLD_NUGGET, 10)));
		recipes.add(MerchantMenu.createRecipe(Items.get("stone_waraxe"), new ItemStack(Material.GOLD_NUGGET, 20)));
		recipes.add(MerchantMenu.createRecipe(Items.get("copper_waraxe"), new ItemStack(Material.GOLD_NUGGET, 25)));
		recipes.add(MerchantMenu.createRecipe(Items.get("tin_waraxe"), new ItemStack(Material.GOLD_NUGGET, 28)));
		recipes.add(MerchantMenu.createRecipe(Items.get("golden_waraxe"), new ItemStack(Material.GOLD_NUGGET, 35)));
		recipes.add(MerchantMenu.createRecipe(Items.get("bronze_waraxe"), new ItemStack(Material.GOLD_NUGGET, 35)));
		recipes.add(MerchantMenu.createRecipe(Items.get("iron_waraxe"), new ItemStack(Material.GOLD_NUGGET, 45)));
		recipes.add(MerchantMenu.createRecipe(Items.get("emerald_waraxe"), new ItemStack(Material.GOLD_NUGGET, 50)));
		recipes.add(MerchantMenu.createRecipe(Items.get("ruby_waraxe"), new ItemStack(Material.GOLD_NUGGET, 64)));
		recipes.add(MerchantMenu.createRecipe(Items.get("diamond_waraxe"), new ItemStack(Material.GOLD_NUGGET, 64),
				new ItemStack(Material.GOLD_NUGGET, 15)));
		recipes.add(MerchantMenu.createRecipe(Items.get("black_diamond_waraxe"),
				new ItemStack(Material.GOLD_NUGGET, 64), new ItemStack(Material.GOLD_NUGGET, 25)));
		recipes.add(MerchantMenu.createRecipe(Items.get("obsidian_crystal_waraxe"),
				new ItemStack(Material.GOLD_NUGGET, 64), new ItemStack(Material.GOLD_NUGGET, 35)));
		recipes.add(MerchantMenu.createRecipe(Items.get("lava_crystal_waraxe"), new ItemStack(Material.GOLD_NUGGET, 64),
				new ItemStack(Material.GOLD_NUGGET, 45)));
		recipes.add(MerchantMenu.createRecipe(Items.get("quartz_crystal_waraxe"),
				new ItemStack(Material.GOLD_NUGGET, 64), new ItemStack(Material.GOLD_NUGGET, 56)));
		return MerchantMenu.createMerchant(ChatColor.BOLD + "Waraxe shop", recipes);
	}

	private static Merchant createBihanderMenu() {
		List<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();
		recipes.add(MerchantMenu.createRecipe(Items.get("wooden_bihander"), new ItemStack(Material.GOLD_NUGGET, 15)));
		recipes.add(MerchantMenu.createRecipe(Items.get("stone_bihander"), new ItemStack(Material.GOLD_NUGGET, 25)));
		recipes.add(MerchantMenu.createRecipe(Items.get("copper_bihander"), new ItemStack(Material.GOLD_NUGGET, 28)));
		recipes.add(MerchantMenu.createRecipe(Items.get("tin_bihander"), new ItemStack(Material.GOLD_NUGGET, 30)));
		recipes.add(MerchantMenu.createRecipe(Items.get("golden_bihander"), new ItemStack(Material.GOLD_NUGGET, 40)));
		recipes.add(MerchantMenu.createRecipe(Items.get("bronze_bihander"), new ItemStack(Material.GOLD_NUGGET, 40)));
		recipes.add(MerchantMenu.createRecipe(Items.get("iron_bihander"), new ItemStack(Material.GOLD_NUGGET, 50)));
		recipes.add(MerchantMenu.createRecipe(Items.get("emerald_bihander"), new ItemStack(Material.GOLD_NUGGET, 64)));
		recipes.add(MerchantMenu.createRecipe(Items.get("ruby_bihander"), new ItemStack(Material.GOLD_NUGGET, 64),
				new ItemStack(Material.GOLD_NUGGET, 10)));
		recipes.add(MerchantMenu.createRecipe(Items.get("diamond_bihander"), new ItemStack(Material.GOLD_NUGGET, 64),
				new ItemStack(Material.GOLD_NUGGET, 20)));
		recipes.add(MerchantMenu.createRecipe(Items.get("black_diamond_bihander"),
				new ItemStack(Material.GOLD_NUGGET, 64), new ItemStack(Material.GOLD_NUGGET, 30)));
		recipes.add(MerchantMenu.createRecipe(Items.get("obsidian_crystal_bihander"),
				new ItemStack(Material.GOLD_NUGGET, 64), new ItemStack(Material.GOLD_NUGGET, 40)));
		recipes.add(MerchantMenu.createRecipe(Items.get("lava_crystal_bihander"),
				new ItemStack(Material.GOLD_NUGGET, 64), new ItemStack(Material.GOLD_NUGGET, 50)));
		recipes.add(MerchantMenu.createRecipe(Items.get("quartz_crystal_bihander"),
				new ItemStack(Material.GOLD_NUGGET, 64), new ItemStack(Material.GOLD_NUGGET, 64)));
		return MerchantMenu.createMerchant(ChatColor.BOLD + "Bihander shop", recipes);
	}

	private static Merchant createHammerMenu() {
		List<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();
		recipes.add(MerchantMenu.createRecipe(Items.get("wooden_hammer"), new ItemStack(Material.GOLD_NUGGET, 4)));
		recipes.add(MerchantMenu.createRecipe(Items.get("stone_hammer"), new ItemStack(Material.GOLD_NUGGET, 10)));
		recipes.add(MerchantMenu.createRecipe(Items.get("copper_hammer"), new ItemStack(Material.GOLD_NUGGET, 15)));
		recipes.add(MerchantMenu.createRecipe(Items.get("tin_hammer"), new ItemStack(Material.GOLD_NUGGET, 18)));
		recipes.add(MerchantMenu.createRecipe(Items.get("golden_hammer"), new ItemStack(Material.GOLD_NUGGET, 20)));
		recipes.add(MerchantMenu.createRecipe(Items.get("bronze_hammer"), new ItemStack(Material.GOLD_NUGGET, 20)));
		recipes.add(MerchantMenu.createRecipe(Items.get("iron_hammer"), new ItemStack(Material.GOLD_NUGGET, 25)));
		recipes.add(MerchantMenu.createRecipe(Items.get("emerald_hammer"), new ItemStack(Material.GOLD_NUGGET, 40)));
		recipes.add(MerchantMenu.createRecipe(Items.get("ruby_hammer"), new ItemStack(Material.GOLD_NUGGET, 50)));
		recipes.add(MerchantMenu.createRecipe(Items.get("diamond_hammer"), new ItemStack(Material.GOLD_NUGGET, 64)));
		recipes.add(MerchantMenu.createRecipe(Items.get("black_diamond_hammer"),
				new ItemStack(Material.GOLD_NUGGET, 64), new ItemStack(Material.GOLD_NUGGET, 10)));
		recipes.add(MerchantMenu.createRecipe(Items.get("obsidian_crystal_hammer"),
				new ItemStack(Material.GOLD_NUGGET, 64), new ItemStack(Material.GOLD_NUGGET, 20)));
		recipes.add(MerchantMenu.createRecipe(Items.get("lava_crystal_hammer"), new ItemStack(Material.GOLD_NUGGET, 64),
				new ItemStack(Material.GOLD_NUGGET, 30)));
		recipes.add(MerchantMenu.createRecipe(Items.get("quartz_crystal_hammer"),
				new ItemStack(Material.GOLD_NUGGET, 64), new ItemStack(Material.GOLD_NUGGET, 40)));
		return MerchantMenu.createMerchant(ChatColor.BOLD + "Hammer shop", recipes);
	}

	private static Merchant createSpearMenu() {
		List<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();
		recipes.add(MerchantMenu.createRecipe(Items.get("wooden_spear"), new ItemStack(Material.GOLD_NUGGET, 6)));
		recipes.add(MerchantMenu.createRecipe(Items.get("stone_spear"), new ItemStack(Material.GOLD_NUGGET, 15)));
		recipes.add(MerchantMenu.createRecipe(Items.get("copper_spear"), new ItemStack(Material.GOLD_NUGGET, 20)));
		recipes.add(MerchantMenu.createRecipe(Items.get("tin_spear"), new ItemStack(Material.GOLD_NUGGET, 25)));
		recipes.add(MerchantMenu.createRecipe(Items.get("golden_spear"), new ItemStack(Material.GOLD_NUGGET, 30)));
		recipes.add(MerchantMenu.createRecipe(Items.get("bronze_spear"), new ItemStack(Material.GOLD_NUGGET, 30)));
		recipes.add(MerchantMenu.createRecipe(Items.get("iron_spear"), new ItemStack(Material.GOLD_NUGGET, 40)));
		recipes.add(MerchantMenu.createRecipe(Items.get("emerald_spear"), new ItemStack(Material.GOLD_NUGGET, 50)));
		recipes.add(MerchantMenu.createRecipe(Items.get("ruby_spear"), new ItemStack(Material.GOLD_NUGGET, 60)));
		recipes.add(MerchantMenu.createRecipe(Items.get("diamond_spear"), new ItemStack(Material.GOLD_NUGGET, 64),
				new ItemStack(Material.GOLD_NUGGET, 10)));
		recipes.add(MerchantMenu.createRecipe(Items.get("black_diamond_spear"), new ItemStack(Material.GOLD_NUGGET, 64),
				new ItemStack(Material.GOLD_NUGGET, 20)));
		recipes.add(MerchantMenu.createRecipe(Items.get("obsidian_crystal_spear"),
				new ItemStack(Material.GOLD_NUGGET, 64), new ItemStack(Material.GOLD_NUGGET, 30)));
		recipes.add(MerchantMenu.createRecipe(Items.get("lava_crystal_spear"), new ItemStack(Material.GOLD_NUGGET, 64),
				new ItemStack(Material.GOLD_NUGGET, 40)));
		recipes.add(MerchantMenu.createRecipe(Items.get("quartz_crystal_spear"),
				new ItemStack(Material.GOLD_NUGGET, 64), new ItemStack(Material.GOLD_NUGGET, 50)));
		return MerchantMenu.createMerchant(ChatColor.BOLD + "Spear shop", recipes);
	}

	private static Merchant createBowMenu() {
		List<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();
		recipes.add(MerchantMenu.createRecipe(Material.ARROW, new ItemStack(Material.GOLD_NUGGET, 1)));
		recipes.add(MerchantMenu.createRecipe(Material.SPECTRAL_ARROW, new ItemStack(Material.GOLD_NUGGET, 2)));
		recipes.add(MerchantMenu.createRecipe(Material.BOW, new ItemStack(Material.GOLD_NUGGET, 20)));
		recipes.add(MerchantMenu.createRecipe(Items.get("copper_bow"), new ItemStack(Material.GOLD_NUGGET, 30)));
		recipes.add(MerchantMenu.createRecipe(Items.get("tin_bow"), new ItemStack(Material.GOLD_NUGGET, 35)));
		recipes.add(MerchantMenu.createRecipe(Items.get("golden_bow"), new ItemStack(Material.GOLD_NUGGET, 40)));
		recipes.add(MerchantMenu.createRecipe(Items.get("bronze_bow"), new ItemStack(Material.GOLD_NUGGET, 40)));
		recipes.add(MerchantMenu.createRecipe(Items.get("iron_bow"), new ItemStack(Material.GOLD_NUGGET, 50)));
		recipes.add(MerchantMenu.createRecipe(Items.get("emerald_bow"), new ItemStack(Material.GOLD_NUGGET, 64)));
		recipes.add(MerchantMenu.createRecipe(Items.get("ruby_bow"), new ItemStack(Material.GOLD_NUGGET, 64),
				new ItemStack(Material.GOLD_NUGGET, 10)));
		recipes.add(MerchantMenu.createRecipe(Items.get("diamond_bow"), new ItemStack(Material.GOLD_NUGGET, 64),
				new ItemStack(Material.GOLD_NUGGET, 20)));
		recipes.add(MerchantMenu.createRecipe(Items.get("black_diamond_bow"), new ItemStack(Material.GOLD_NUGGET, 64),
				new ItemStack(Material.GOLD_NUGGET, 30)));
		recipes.add(MerchantMenu.createRecipe(Items.get("obsidian_crystal_bow"),
				new ItemStack(Material.GOLD_NUGGET, 40), new ItemStack(Material.GOLD_NUGGET, 64)));
		recipes.add(MerchantMenu.createRecipe(Items.get("lava_crystal_bow"), new ItemStack(Material.GOLD_NUGGET, 64),
				new ItemStack(Material.GOLD_NUGGET, 50)));
		recipes.add(MerchantMenu.createRecipe(Items.get("quartz_crystal_bow"), new ItemStack(Material.GOLD_NUGGET, 64),
				new ItemStack(Material.GOLD_NUGGET, 64)));
		return MerchantMenu.createMerchant(ChatColor.BOLD + "Bow shop", recipes);
	}

	private static Merchant createSpellMenu() {
		List<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();
		recipes.add(MerchantMenu.createRecipe(Items.get("flame"), new ItemStack(Material.GOLD_NUGGET, 40)));
		recipes.add(MerchantMenu.createRecipe(Items.get("water_beam"), new ItemStack(Material.GOLD_NUGGET, 40)));
		recipes.add(MerchantMenu.createRecipe(Items.get("fire_ball"), new ItemStack(Material.GOLD_NUGGET, 64),
				new ItemStack(Material.GOLD_NUGGET, 20)));
		recipes.add(MerchantMenu.createRecipe(Items.get("fountain"), new ItemStack(Material.GOLD_NUGGET, 64),
				new ItemStack(Material.GOLD_NUGGET, 20)));
		recipes.add(MerchantMenu.createRecipe(Items.get("fire_aura"), new ItemStack(Material.GOLD_NUGGET, 64),
				new ItemStack(Material.GOLD_NUGGET, 64)));
		recipes.add(MerchantMenu.createRecipe(Items.get("water_aura"), new ItemStack(Material.GOLD_NUGGET, 64),
				new ItemStack(Material.GOLD_NUGGET, 64)));
		recipes.add(MerchantMenu.createRecipe(Items.get("summon_zombie"), new ItemStack(Material.GOLD_NUGGET, 64)));
		recipes.add(MerchantMenu.createRecipe(Items.get("summon_skeleton"), new ItemStack(Material.GOLD_NUGGET, 64)));
		return MerchantMenu.createMerchant(ChatColor.BOLD + "Spell shop", recipes);
	}

	private static Merchant createArmorMenu() {
		List<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();
		recipes.add(MerchantMenu.createRecipe(Material.SHIELD, new ItemStack(Material.GOLD_NUGGET, 40)));

		recipes.add(MerchantMenu.createRecipe(Material.LEATHER_HELMET, new ItemStack(Material.GOLD_NUGGET, 10)));
		recipes.add(MerchantMenu.createRecipe(Material.LEATHER_CHESTPLATE, new ItemStack(Material.GOLD_NUGGET, 20)));
		recipes.add(MerchantMenu.createRecipe(Material.LEATHER_LEGGINGS, new ItemStack(Material.GOLD_NUGGET, 15)));
		recipes.add(MerchantMenu.createRecipe(Material.LEATHER_BOOTS, new ItemStack(Material.GOLD_NUGGET, 10)));

		recipes.add(MerchantMenu.createRecipe(Material.CHAINMAIL_HELMET, new ItemStack(Material.GOLD_NUGGET, 20)));
		recipes.add(MerchantMenu.createRecipe(Material.CHAINMAIL_CHESTPLATE, new ItemStack(Material.GOLD_NUGGET, 40)));
		recipes.add(MerchantMenu.createRecipe(Material.CHAINMAIL_LEGGINGS, new ItemStack(Material.GOLD_NUGGET, 30)));
		recipes.add(MerchantMenu.createRecipe(Material.CHAINMAIL_BOOTS, new ItemStack(Material.GOLD_NUGGET, 20)));

		recipes.add(MerchantMenu.createRecipe(Material.IRON_HELMET, new ItemStack(Material.GOLD_NUGGET, 40)));
		recipes.add(MerchantMenu.createRecipe(Material.IRON_CHESTPLATE, new ItemStack(Material.GOLD_NUGGET, 60)));
		recipes.add(MerchantMenu.createRecipe(Material.IRON_LEGGINGS, new ItemStack(Material.GOLD_NUGGET, 50)));
		recipes.add(MerchantMenu.createRecipe(Material.IRON_BOOTS, new ItemStack(Material.GOLD_NUGGET, 40)));

		recipes.add(MerchantMenu.createRecipe(Material.GOLDEN_HELMET, new ItemStack(Material.GOLD_NUGGET, 50)));
		recipes.add(MerchantMenu.createRecipe(Material.GOLDEN_CHESTPLATE, new ItemStack(Material.GOLD_NUGGET, 64)));
		recipes.add(MerchantMenu.createRecipe(Material.GOLDEN_LEGGINGS, new ItemStack(Material.GOLD_NUGGET, 60)));
		recipes.add(MerchantMenu.createRecipe(Material.GOLDEN_BOOTS, new ItemStack(Material.GOLD_NUGGET, 50)));

		recipes.add(MerchantMenu.createRecipe(Material.DIAMOND_HELMET, new ItemStack(Material.GOLD_NUGGET, 64)));
		recipes.add(MerchantMenu.createRecipe(Material.DIAMOND_CHESTPLATE, new ItemStack(Material.GOLD_NUGGET, 64),
				new ItemStack(Material.GOLD_NUGGET, 30)));
		recipes.add(MerchantMenu.createRecipe(Material.DIAMOND_LEGGINGS, new ItemStack(Material.GOLD_NUGGET, 64),
				new ItemStack(Material.GOLD_NUGGET, 15)));
		recipes.add(MerchantMenu.createRecipe(Material.DIAMOND_BOOTS, new ItemStack(Material.GOLD_NUGGET, 64)));
		return MerchantMenu.createMerchant(ChatColor.BOLD + "Armor shop", recipes);
	}

	private static Merchant createFoodMenu() {
		List<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();
		recipes.add(MerchantMenu.createRecipe(ItemHelper.createWaterBottle(), new ItemStack(Material.GOLD_NUGGET, 2)));
		recipes.add(MerchantMenu.createRecipe(Items.get("mana_potion"), new ItemStack(Material.GOLD_NUGGET, 5)));
		recipes.add(MerchantMenu.createRecipe(Material.COOKIE, new ItemStack(Material.GOLD_NUGGET, 1)));
		recipes.add(MerchantMenu.createRecipe(Material.CARROT, new ItemStack(Material.GOLD_NUGGET, 1)));
		recipes.add(MerchantMenu.createRecipe(Material.MELON_SLICE, new ItemStack(Material.GOLD_NUGGET, 1)));
		recipes.add(MerchantMenu.createRecipe(Material.APPLE, new ItemStack(Material.GOLD_NUGGET, 1)));
		recipes.add(MerchantMenu.createRecipe(Material.BAKED_POTATO, new ItemStack(Material.GOLD_NUGGET, 2)));
		recipes.add(MerchantMenu.createRecipe(Material.BREAD, new ItemStack(Material.GOLD_NUGGET, 2)));
		recipes.add(MerchantMenu.createRecipe(Material.COOKED_COD, new ItemStack(Material.GOLD_NUGGET, 2)));
		recipes.add(MerchantMenu.createRecipe(Material.COOKED_RABBIT, new ItemStack(Material.GOLD_NUGGET, 2)));
		recipes.add(MerchantMenu.createRecipe(Material.COOKED_SALMON, new ItemStack(Material.GOLD_NUGGET, 3)));
		recipes.add(MerchantMenu.createRecipe(Material.COOKED_CHICKEN, new ItemStack(Material.GOLD_NUGGET, 3)));
		recipes.add(MerchantMenu.createRecipe(Material.COOKED_MUTTON, new ItemStack(Material.GOLD_NUGGET, 3)));
		recipes.add(MerchantMenu.createRecipe(Material.PUMPKIN_PIE, new ItemStack(Material.GOLD_NUGGET, 4)));
		recipes.add(MerchantMenu.createRecipe(Material.COOKED_PORKCHOP, new ItemStack(Material.GOLD_NUGGET, 4)));
		recipes.add(MerchantMenu.createRecipe(Material.COOKED_BEEF, new ItemStack(Material.GOLD_NUGGET, 4)));
		recipes.add(MerchantMenu.createRecipe(Material.HONEY_BOTTLE, new ItemStack(Material.GOLD_NUGGET, 8)));
		recipes.add(MerchantMenu.createRecipe(Material.GOLDEN_APPLE, new ItemStack(Material.GOLD_NUGGET, 64)));
		return MerchantMenu.createMerchant(ChatColor.BOLD + "Food shop", recipes);
	}

	private static Merchant createPotionMenu() {
		List<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();
		recipes.add(MerchantMenu.createRecipe(new Potion(PotionType.REGEN, 1, true).toItemStack(1),
				new ItemStack(Material.GOLD_NUGGET, 10)));
		recipes.add(MerchantMenu.createRecipe(new Potion(PotionType.REGEN, 2, true).toItemStack(1),
				new ItemStack(Material.GOLD_NUGGET, 20)));
		recipes.add(MerchantMenu.createRecipe(new Potion(PotionType.INSTANT_HEAL, 1, true).toItemStack(1),
				new ItemStack(Material.GOLD_NUGGET, 15)));
		recipes.add(MerchantMenu.createRecipe(new Potion(PotionType.SPEED, 1, true).toItemStack(1),
				new ItemStack(Material.GOLD_NUGGET, 30)));
		recipes.add(MerchantMenu.createRecipe(new Potion(PotionType.JUMP, 1, true).toItemStack(1),
				new ItemStack(Material.GOLD_NUGGET, 30)));
		recipes.add(MerchantMenu.createRecipe(new Potion(PotionType.FIRE_RESISTANCE, 1, true).toItemStack(1),
				new ItemStack(Material.GOLD_NUGGET, 30)));
		return MerchantMenu.createMerchant(ChatColor.BOLD + "Potion shop", recipes);
	}

	private static Merchant createMiscMenu() {
		List<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();
		recipes.add(MerchantMenu.createRecipe(Material.TORCH, new ItemStack(Material.GOLD_NUGGET, 5)));
		recipes.add(MerchantMenu.createRecipe(Material.LAPIS_LAZULI, new ItemStack(Material.GOLD_NUGGET, 5)));
		recipes.add(MerchantMenu.createRecipe(Material.ENDER_PEARL, new ItemStack(Material.GOLD_NUGGET, 30)));
		recipes.add(MerchantMenu.createRecipe(Material.TNT, new ItemStack(Material.GOLD_NUGGET, 40)));
		recipes.add(MerchantMenu.createRecipe(Material.FLINT_AND_STEEL, new ItemStack(Material.GOLD_NUGGET, 64),
				new ItemStack(Material.GOLD_NUGGET, 64)));
		return MerchantMenu.createMerchant(ChatColor.BOLD + "Miscellaneous shop", recipes);
	}
}
