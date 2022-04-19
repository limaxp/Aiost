package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.util.Arrays;

import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.misc.menu.anvilMenu.AnvilMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.misc.utils.worldEdit.Schematic;
import com.pm.aiost.misc.utils.worldEdit.SchematicFileLoader;
import com.pm.aiost.player.ServerPlayer;

public class SchematicMenu extends SingleInventoryMenu {
	
	// TODO make schematics work!

	private static final ItemStack NO_SAVED_SCHEMATIC_ITEM = MetaHelper.setMeta(Material.BARRIER,
			RED + BOLD + "No schematic saved!", Arrays.asList(GRAY + "You have to save a schematic first"));

	private Block block1;
	private Block block2;
	private Schematic savedSchematic;

	public SchematicMenu(Menu backLink) {
		super(BOLD + "Schematic editor", 3, true);
		set(MetaHelper.setMeta(Material.GOLD_BLOCK, GRAY + BOLD + "Copy", Arrays.asList(GRAY + "Click to copy region")),

				MetaHelper.setMeta(Material.EMERALD_BLOCK, GRAY + BOLD + "Paste",
						Arrays.asList(GRAY + "Click to paste region")),

				null, null, null,

				MetaHelper.setMeta(Material.PAPER, GRAY + BOLD + "Save",
						Arrays.asList(GRAY + "Click to save region as schematic")),

				MetaHelper.setMeta(Material.WRITABLE_BOOK, GRAY + BOLD + "Load",
						Arrays.asList(GRAY + "Click to load schematic")));
		setBackLink(backLink);
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		if (event.getCurrentItem() != null) {
			switch (event.getSlot()) {

			case 10:
				savedSchematic = new Schematic(block1, block2);
				serverPlayer.closeInventory();
				break;

			case 11:
				if (savedSchematic == null) {
					displayInSlot(event, NO_SAVED_SCHEMATIC_ITEM, 30);
					return;
				}
				savedSchematic.spawn(serverPlayer.player.getLocation());
				serverPlayer.closeInventory();
				break;

			case 12:
				saveSchematicMenu().open(serverPlayer);
				break;

			case 13:
				loadSchematicMenu().open(serverPlayer);
				break;

			default:
				break;
			}
		}
	}

	private AnvilMenu saveSchematicMenu() {
		AnvilMenu menu = new AnvilMenu(BOLD + "Choose name", MetaHelper.setMeta(Material.PAPER, "name")) {
			@Override
			public void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
				event.setCancelled(true);
				if (event.getSlot() == 2) {
					saveSchematic(event.getCurrentItem().getItemMeta().getDisplayName());
					SchematicMenu.this.open(serverPlayer);
				}
			}
		};
		menu.setBackLink(this);
		return menu;
	}

	private void saveSchematic(String name) {
		Schematic schematic = new Schematic(block1, block2);
		SchematicFileLoader.save("testName", schematic);
	}

	private AnvilMenu loadSchematicMenu() {
		AnvilMenu menu = new AnvilMenu(BOLD + "Choose name", MetaHelper.setMeta(Material.PAPER, "name")) {
			@Override
			public void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
				event.setCancelled(true);
				if (event.getSlot() == 2) {
					loadSchematic(event.getCurrentItem().getItemMeta().getDisplayName(),
							serverPlayer.player.getLocation());
					SchematicMenu.this.open(serverPlayer);
				}
			}
		};
		menu.setBackLink(this);
		return menu;
	}

	private void loadSchematic(String name, Location loc) {
		Schematic schematic = SchematicFileLoader.load(name);
		schematic.spawn(loc);
	}

	@Nullable
	public Schematic getSchematic() {
		return savedSchematic;
	}

	public void setBlock1(Block block) {
		this.block1 = block;
	}

	public Block getBlock1() {
		return block1;
	}

	public void setBlock2(Block block) {
		this.block2 = block;
	}

	public Block getBlock2() {
		return block2;
	}
}
