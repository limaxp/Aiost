package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.event.effect.effects.WorldBrushEffect;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.menus.request.SearchBlockMenu;
import com.pm.aiost.misc.menu.menus.request.TextMenu;
import com.pm.aiost.misc.menu.menus.request.XYZMenu;
import com.pm.aiost.misc.menu.menus.request.enumeration.EnumerationMenus;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.misc.utils.worldEdit.Brush;
import com.pm.aiost.misc.utils.worldEdit.Brush.BrushMode;
import com.pm.aiost.player.ServerPlayer;

public class WorldBrushMenu extends SingleInventoryMenu {

	private WorldBrushEffect effect;

	public WorldBrushMenu() {
		super(BOLD + "World brush", 3, true);
		set(MetaHelper.setMeta(Material.STONE, GRAY + BOLD + "Type",
				Arrays.asList(GRAY + "Left click to choose type", GRAY + "Right click to search type",
						GRAY + "Shift click to set to air")),

				MetaHelper.setMeta(Material.STONE, GRAY + BOLD + "Type 2",
						Arrays.asList(GRAY + "Left click to choose type", GRAY + "Right click to search type",
								GRAY + "Shift click to set to air")),

				MetaHelper.setMeta(Material.ARROW, GRAY + BOLD + "Brush",
						Arrays.asList(GRAY + "Click to choose brush")),

				MetaHelper.setMeta(Material.GUNPOWDER, GRAY + BOLD + "Mode",
						Arrays.asList(GRAY + "Click to choose mode")),

				MetaHelper.setMeta(Material.LIME_DYE, GRAY + BOLD + "Radius", Arrays
						.asList(GRAY + "Left click to set radius equaly", GRAY + "Right click to set exact radius")));
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ClickType click = event.getClick();
		switch (event.getSlot()) {

		case 10:
			if (click == ClickType.SHIFT_LEFT || click == ClickType.SHIFT_RIGHT)
				setType(Material.AIR);
			else if (click == ClickType.LEFT)
				serverPlayer.doMenuRequest(new SingleMenuRequest(EnumerationMenus.BLOCK_MENU) {

					@Override
					public void openRequest(ServerPlayer serverPlayer) {
						WorldBrushMenu.this.open(serverPlayer);
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
								WorldBrushMenu.this.open(serverPlayer);
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
						WorldBrushMenu.this.open(serverPlayer);
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
								WorldBrushMenu.this.open(serverPlayer);
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
					WorldBrushMenu.this.open(serverPlayer);
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
					WorldBrushMenu.this.open(serverPlayer);
				}

				@Override
				public void onResult(ServerPlayer serverPlayer, Object obj) {
					setBrushMode((BrushMode) obj);
				}
			});
			break;

		case 14:
			if (click == ClickType.LEFT || click == ClickType.SHIFT_LEFT)
				serverPlayer.doMenuRequest(new SingleMenuRequest(
						() -> TextMenu.createNumber(BOLD + "Choose radius", effect.getData().xRadius)) {

					@Override
					public void openRequest(ServerPlayer serverPlayer) {
						WorldBrushMenu.this.open(serverPlayer);
					}

					@Override
					public void onResult(ServerPlayer serverPlayer, Object obj) {
						setRadius(((Double) obj).intValue());
					}
				});
			else if (click == ClickType.RIGHT || click == ClickType.SHIFT_RIGHT)
				serverPlayer.doMenuRequest(new SingleMenuRequest(() -> new XYZMenu(BOLD + "Choose radius",
						effect.getData().xRadius, effect.getData().yRadius, effect.getData().zRadius)) {

					@Override
					public void openRequest(ServerPlayer serverPlayer) {
						WorldBrushMenu.this.open(serverPlayer);
					}

					@Override
					public void onResult(ServerPlayer serverPlayer, Object obj) {
						double[] value = (double[]) obj;
						setRadius((int) value[0], (int) value[1], (int) value[2]);
					}
				});
			break;

		default:
			break;
		}
	}

	public void setEffect(WorldBrushEffect effect) {
		this.effect = effect;
		setType(effect.getData().type);
		setType2(effect.getData().type2);
	}

	public WorldBrushEffect getEffect() {
		return effect;
	}

	public void setType(Material type) {
		effect.getData().type = type;
		if (type != Material.AIR)
			getInventory().getItem(10).setType(type);
		else
			getInventory().getItem(10).setType(Material.BARRIER);
	}

	public void setType2(Material type) {
		effect.getData().type2 = type;
		if (type != Material.AIR)
			getInventory().getItem(11).setType(type);
		else
			getInventory().getItem(11).setType(Material.BARRIER);
	}

	public void setBrush(Brush brush) {
		effect.setBrush(brush);
	}

	public void setBrushMode(BrushMode mode) {
		effect.getData().mode = mode;
	}

	public void setRadius(int radius) {
		effect.getData().setRadius(radius);
	}

	public void setRadius(int rx, int ry, int rz) {
		effect.getData().setRadius(rx, ry, rz);
	}
}
