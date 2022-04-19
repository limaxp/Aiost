package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;
import static com.pm.aiost.misc.utils.ChatColor.PURPLE;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.pm.aiost.misc.menu.anvilMenu.AnvilMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.menus.request.enumeration.EnumerationMenus;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.other.WordFilter;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.EnvironmentHelper;
import com.pm.aiost.server.world.creation.WorldLoader;
import com.pm.aiost.server.world.type.AiostWorldType;
import com.pm.aiost.server.world.type.AiostWorldTypes;

public class CreateWorldMenu extends SingleInventoryMenu {

	private static final ItemStack CHOOSE_ENVIRONMENT = MetaHelper.setMeta(Material.GRASS_BLOCK, GRAY + BOLD + "Normal",
			Arrays.asList(GRAY + "Click to change the environment of your world"));

	private static final ItemStack CHOOSE_TYPE = MetaHelper.setMeta(Material.STONE, GRAY + BOLD + "Default",
			Arrays.asList(GRAY + "Click to change the type of your world"));

	private static final ItemStack CHOOSE_NAME = MetaHelper.setMeta(Material.NAME_TAG, PURPLE + BOLD + "Change name",
			Arrays.asList(GRAY + "Click to change the name of your world"));

	public static final ItemStack RESET_ITEM = MetaHelper.setMeta(Material.LAVA_BUCKET, RED + BOLD + "Reset settings",
			Arrays.asList(GRAY + "Click to reset all settings"));

	private static final ItemStack ACCEPT_ITEM = MetaHelper.setMeta(Material.NETHER_STAR, GOLD + BOLD + "Accept world",
			Arrays.asList(GRAY + "Click to create your world with choosen settings"));

	private static final ItemStack NO_NAME_ITEM = MetaHelper.setMeta(Material.BARRIER,
			RED + BOLD + "Name cannot be empty!", Arrays.asList(GRAY + "You have to choose a name first"));

	private static final ItemStack FORBIDDEN_NAME_ITEM = MetaHelper.setMeta(Material.BARRIER,
			RED + BOLD + "Forbidden name!", Arrays.asList(GRAY + "The given name is not allowed"));

	private static final ItemStack GENERATE_STRUCTURES_YES = MetaHelper.setMeta(Material.LIME_DYE,
			GREEN + BOLD + "Generate structures", Arrays.asList(GRAY + "Click to turn " + RED + BOLD + "off", "",
					GRAY + "Toggles world structure generation", "", GRAY + "Status: " + GREEN + BOLD + "on"));

	private static final ItemStack GENERATE_STRUCTURES_NO = MetaHelper.setMeta(Material.GRAY_DYE,
			RED + BOLD + "Generate structures", Arrays.asList(GRAY + "Click to turn " + GREEN + BOLD + "on", "",
					GRAY + "Toggles world structure generation", "", GRAY + "Status: " + RED + BOLD + "off"));

	private static final Environment DEFAULT_ENVIRONMENT = Environment.NORMAL;
	private static final AiostWorldType<WorldType> DEFAULT_TYPE = AiostWorldTypes.DEFAULT;
	private static final int ENVIRONMENT_SLOT = 10;
	private static final int TYPE_SLOT = 11;
	private static final int NAME_SLOT = 12;
	private static final int GENERATE_STRUCTURES_SLOT = 13;

	private Environment environment;
	private AiostWorldType<?> type;
	private String name;
	private boolean generateStructures;

	public CreateWorldMenu() {
		super(BOLD + "Create World", 3, true);
		environment = DEFAULT_ENVIRONMENT;
		type = DEFAULT_TYPE;
		generateStructures = true;
		initMenu();
	}

	protected void initMenu() {
		set(CHOOSE_ENVIRONMENT.clone(), CHOOSE_TYPE.clone(), CHOOSE_NAME.clone(), GENERATE_STRUCTURES_YES, null,
				RESET_ITEM, ACCEPT_ITEM);
		setBackLink(WorldMenu.getMenu());
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		if (event.getCurrentItem() != null) {
			switch (event.getSlot()) {

			case ENVIRONMENT_SLOT:
				serverPlayer.doMenuRequest(new SingleMenuRequest(EnumerationMenus.ENVIRONMENT_MENU) {

					@Override
					protected void openRequest(ServerPlayer serverPlayer) {
						CreateWorldMenu.this.open(serverPlayer);
					}

					@Override
					protected void onResult(ServerPlayer serverPlayer, Object obj) {
						setEnvironment((Environment) obj);
					}
				});
				break;

			case TYPE_SLOT:
				serverPlayer.doMenuRequest(new SingleMenuRequest(EnumerationMenus.WORLD_TYPE_MENU) {

					@Override
					protected void openRequest(ServerPlayer serverPlayer) {
						CreateWorldMenu.this.open(serverPlayer);
					}

					@Override
					protected void onResult(ServerPlayer serverPlayer, Object obj) {
						setType((AiostWorldType<?>) obj);
					}
				});
				break;

			case NAME_SLOT:
				renameWorldMenu().open(serverPlayer);
				break;

			case 13:
				toggleGenerateStructures();
				break;

			case 15:
				reset();
				break;

			case 16:
				accept(serverPlayer, event);
				break;

			default:
				break;
			}
		}
	}

	private AnvilMenu renameWorldMenu() {
		AnvilMenu menu = new AnvilMenu(BOLD + "Choose world name",
				MetaHelper.setMeta(Material.PAPER, name == null ? "name" : name)) {
			@Override
			public void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
				event.setCancelled(true);
				if (event.getSlot() == 2) {
					setName(event.getCurrentItem().getItemMeta().getDisplayName());
					CreateWorldMenu.this.open(serverPlayer);
				}
			}
		};
		menu.setBackLink(this);
		return menu;
	}

	private void reset() {
		environment = DEFAULT_ENVIRONMENT;
		type = DEFAULT_TYPE;
		name = null;
		generateStructures = true;
		Inventory inv = getInventory();
		inv.setItem(ENVIRONMENT_SLOT, CHOOSE_ENVIRONMENT.clone());
		inv.setItem(TYPE_SLOT, CHOOSE_TYPE.clone());
		inv.setItem(NAME_SLOT, CHOOSE_NAME.clone());
		inv.setItem(GENERATE_STRUCTURES_SLOT, GENERATE_STRUCTURES_YES);
	}

	private void accept(ServerPlayer serverPlayer, InventoryClickEvent event) {
		if (name == null)
			displayInSlot(event, NO_NAME_ITEM, ACCEPT_ITEM, 30);
		else if (WordFilter.containsBlocked(name))
			displayInSlot(event, FORBIDDEN_NAME_ITEM, ACCEPT_ITEM, 30);
		else {
			serverPlayer.closeInventory();
			WorldLoader.createPlayerWorld(serverPlayer, name, environment, type, generateStructures);
		}
	}

	private void toggleGenerateStructures() {
		if (generateStructures) {
			generateStructures = false;
			getInventory().setItem(GENERATE_STRUCTURES_SLOT, GENERATE_STRUCTURES_NO);
		} else {
			generateStructures = true;
			getInventory().setItem(GENERATE_STRUCTURES_SLOT, GENERATE_STRUCTURES_YES);
		}
	}

	public void setName(String name) {
		this.name = name;
		ItemStack is = getInventory().getItem(NAME_SLOT);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(GRAY + BOLD + name);
		is.setItemMeta(im);
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
		ItemStack inventoryItem = getInventory().getItem(ENVIRONMENT_SLOT);
		inventoryItem.setType(EnvironmentHelper.getMaterial(environment));
		ItemMeta im = inventoryItem.getItemMeta();
		im.setDisplayName(GRAY + BOLD + EnvironmentHelper.getDisplayName(environment));
		inventoryItem.setItemMeta(im);
	}

	public void setType(AiostWorldType<?> type) {
		this.type = type;
		ItemStack inventoryItem = getInventory().getItem(TYPE_SLOT);
		ItemMeta im = inventoryItem.getItemMeta();
		im.setDisplayName(GRAY + BOLD + type.name);
		inventoryItem.setItemMeta(im);
	}
}
