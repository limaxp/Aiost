package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.menus.request.SearchBlockMenu;
import com.pm.aiost.misc.menu.menus.request.enumeration.EnumerationMenus;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.misc.utils.worldEdit.Brush;
import com.pm.aiost.misc.utils.worldEdit.Brush.BrushMode;
import com.pm.aiost.misc.utils.worldEdit.BrushData;
import com.pm.aiost.player.ServerPlayer;

public class WorldEditorMenu extends SingleInventoryMenu {

	private Block block1;
	private Block block2;
	private Brush brush = Brush.CUBE;
	private BrushData data = new BrushData();

	public WorldEditorMenu() {
		super(BOLD + "World editor", 3, true);
		set(MetaHelper.setMeta(Material.STONE, GRAY + BOLD + "Type",
				Arrays.asList(GRAY + "Left click to choose type", GRAY + "Right click to search type",
						GRAY + "Shift click to set to air")),

				MetaHelper.setMeta(Material.BARRIER, GRAY + BOLD + "Type 2",
						Arrays.asList(GRAY + "Left click to choose type", GRAY + "Right click to search type",
								GRAY + "Shift click to set to air")),

				MetaHelper.setMeta(Material.ARROW, GRAY + BOLD + "Brush",
						Arrays.asList(GRAY + "Click to choose brush")),

				MetaHelper.setMeta(Material.GUNPOWDER, GRAY + BOLD + "Mode",
						Arrays.asList(GRAY + "Click to choose mode")),

				null, null,

				MetaHelper.setMeta(Material.NETHER_STAR, GRAY + BOLD + "Accept",
						Arrays.asList(GRAY + "Click to accept settings")));

		addBorderItem(9, MetaHelper.setMeta(Material.PAPER, GRAY + BOLD + "Schematic",
				Arrays.asList(GRAY + "Click to open schematic menu")));
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		if (event.getCurrentItem() != null) {
			ClickType click = event.getClick();
			switch (event.getSlot()) {

			case 10:
				if (click == ClickType.SHIFT_LEFT || click == ClickType.SHIFT_RIGHT)
					setType(Material.AIR);
				else if (click == ClickType.LEFT)
					serverPlayer.doMenuRequest(new SingleMenuRequest(EnumerationMenus.BLOCK_MENU) {

						@Override
						public void openRequest(ServerPlayer serverPlayer) {
							WorldEditorMenu.this.open(serverPlayer);
						}

						@Override
						public void onResult(ServerPlayer serverPlayer, Object obj) {
							setType((Material) obj);
						}
					});
				else if (click == ClickType.RIGHT)
					serverPlayer.doMenuRequest(this,
							new SingleMenuRequest(() -> new SearchBlockMenu(BOLD + "Search block")) {

								@Override
								public void openRequest(ServerPlayer serverPlayer) {
									WorldEditorMenu.this.open(serverPlayer);
								}

								@Override
								public void onResult(ServerPlayer serverPlayer, Object obj) {
									setType(((ItemStack) obj).getType());
								}
							});
				break;

			case 11:
				if (click == ClickType.SHIFT_LEFT || click == ClickType.SHIFT_RIGHT)
					setType2(Material.AIR);
				else if (click == ClickType.LEFT)
					serverPlayer.doMenuRequest(new SingleMenuRequest(EnumerationMenus.BLOCK_MENU) {

						@Override
						public void openRequest(ServerPlayer serverPlayer) {
							WorldEditorMenu.this.open(serverPlayer);
						}

						@Override
						public void onResult(ServerPlayer serverPlayer, Object obj) {
							setType2((Material) obj);
						}
					});
				else if (click == ClickType.RIGHT)
					serverPlayer.doMenuRequest(this,
							new SingleMenuRequest(() -> new SearchBlockMenu(BOLD + "Search block")) {

								@Override
								public void openRequest(ServerPlayer serverPlayer) {
									WorldEditorMenu.this.open(serverPlayer);
								}

								@Override
								public void onResult(ServerPlayer serverPlayer, Object obj) {
									setType2(((ItemStack) obj).getType());
								}
							});
				break;

			case 12:
				serverPlayer.doMenuRequest(new SingleMenuRequest(EnumerationMenus.BRUSH_MENU) {

					@Override
					public void openRequest(ServerPlayer serverPlayer) {
						WorldEditorMenu.this.open(serverPlayer);
					}

					@Override
					public void onResult(ServerPlayer serverPlayer, Object obj) {
						setBrush((Brush) obj);
					}
				});
				break;

			case 13:
				serverPlayer.doMenuRequest(new SingleMenuRequest(EnumerationMenus.BRUSH_MODE_MENU) {

					@Override
					public void openRequest(ServerPlayer serverPlayer) {
						WorldEditorMenu.this.open(serverPlayer);
					}

					@Override
					public void onResult(ServerPlayer serverPlayer, Object obj) {
						setBrushMode((BrushMode) obj);
					}
				});
				break;

			case 16:
				data.set(block1, block2);
				brush.run(data, false);
				serverPlayer.closeInventory();
				break;

			case 18:
				SchematicMenu menu = (SchematicMenu) serverPlayer.getOrCreateMenu(this, () -> new SchematicMenu(this));
				menu.setBlock1(block1);
				menu.setBlock2(block2);
				menu.open(serverPlayer);
				break;

			default:
				break;
			}
		}
	}

	public void setData(BrushData data) {
		this.data = data;
		setType(data.type);
		setType2(data.type2);
	}

	public BrushData getData() {
		return data;
	}

	public void setType(Material type) {
		data.type = type;
		if (type != Material.AIR)
			getInventory().getItem(10).setType(type);
		else
			getInventory().getItem(10).setType(Material.BARRIER);
	}

	public void setType2(Material type) {
		data.type2 = type;
		if (type != Material.AIR)
			getInventory().getItem(11).setType(type);
		else
			getInventory().getItem(11).setType(Material.BARRIER);
	}

	public Material getType() {
		return data.type;
	}

	public Material getType2() {
		return data.type2;
	}

	public void setBrush(Brush brush) {
		this.brush = brush;
	}

	public Brush getBrush() {
		return brush;
	}

	public void setBrushMode(BrushMode brushMode) {
		data.mode = brushMode;
	}

	public BrushMode getBrushMode() {
		return data.mode;
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
