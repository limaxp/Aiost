package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.AQUA;
import static com.pm.aiost.misc.utils.ChatColor.BLUE;
import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.pm.aiost.game.Game;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.menus.DatabaseGameMenu.GameData;
import com.pm.aiost.misc.menu.menus.request.TextMenu;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class GameStartMenu extends SingleInventoryMenu {

	public static final ItemStack RESET_ITEM = MetaHelper.setMeta(Material.LAVA_BUCKET, RED + BOLD + "Reset settings",
			Arrays.asList(GRAY + "Click to reset settings to default"));

	private final GameData data;
	private int minPlayer;
	private int maxPlayer;
	private String password;

	public GameStartMenu(GameData data) {
		super(BOLD + "Host " + data.name, 3, true);
		this.data = data;
		minPlayer = data.gameType.minPlayer;
		maxPlayer = data.gameType.maxPlayer;
		set(MetaHelper.setMeta(Material.SKELETON_SKULL, AQUA + BOLD + "Min player: " + minPlayer,
				Arrays.asList(GRAY + "Click to change minimum player")),

				MetaHelper.setMeta(Material.PLAYER_HEAD, BLUE + BOLD + "Max player: " + maxPlayer,
						Arrays.asList(GRAY + "Click to change maximum player")),

				MetaHelper.setMeta(Material.WRITABLE_BOOK, GOLD + BOLD + "Password: None",
						Arrays.asList(GRAY + "Left click to enter a password",
								GRAY + "Right click to remove password")),

				null,

				null,

				RESET_ITEM,

				MetaHelper.setMeta(Material.NETHER_STAR, GREEN + BOLD + "Start game",
						Arrays.asList(GRAY + "Click to start game", null, GRAY + "Min player: " + minPlayer,
								GRAY + "Max player: " + maxPlayer, GRAY + "Password: None")));
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		if (event.getCurrentItem() != null) {
			switch (event.getSlot()) {

			case 10:
				serverPlayer.doMenuRequest(
						new SingleMenuRequest(TextMenu.createInteger(BOLD + "Set min player", minPlayer)) {

							@Override
							public void openRequest(ServerPlayer serverPlayer) {
								serverPlayer.openInventory(event.getInventory());
							}

							@Override
							public void onResult(ServerPlayer serverPlayer, Object obj) {
								setMinPlayer((int) obj);
							}
						});
				break;

			case 11:
				serverPlayer.doMenuRequest(
						new SingleMenuRequest(TextMenu.createInteger(BOLD + "Set max player", maxPlayer)) {

							@Override
							public void openRequest(ServerPlayer serverPlayer) {
								serverPlayer.openInventory(event.getInventory());
							}

							@Override
							public void onResult(ServerPlayer serverPlayer, Object obj) {
								setMaxPlayer(maxPlayer = (int) obj);
							}
						});
				break;

			case 12:
				ClickType clickType = event.getClick();
				if (clickType == ClickType.LEFT || clickType == ClickType.SHIFT_LEFT) {
					serverPlayer.doMenuRequest(new SingleMenuRequest(
							TextMenu.create(BOLD + "Set password", password != null ? password : "password")) {

						@Override
						public void openRequest(ServerPlayer serverPlayer) {
							serverPlayer.openInventory(event.getInventory());
						}

						@Override
						public void onResult(ServerPlayer serverPlayer, Object obj) {
							setPassword((String) obj);
						}
					});
				} else if (clickType == ClickType.RIGHT || clickType == ClickType.SHIFT_RIGHT) {
					setPassword(null);
				}
				break;

			case 15:
				reset();
				break;

			case 16:
				startGame(serverPlayer);
				break;

			default:
				break;
			}
		}
	}

	private void reset() {
		setMinPlayer(data.gameType.minPlayer);
		setMaxPlayer(maxPlayer = data.gameType.maxPlayer);
		setPassword(null);
	}

	private void startGame(ServerPlayer serverPlayer) {
		Game.host(serverPlayer, data, minPlayer, maxPlayer, password);
	}

	private void setMinPlayer(int minPlayer) {
		if (minPlayer < data.gameType.minPlayer)
			minPlayer = data.gameType.minPlayer;
		if (minPlayer > data.gameType.maxPlayer)
			minPlayer = data.gameType.maxPlayer;
		this.minPlayer = minPlayer;
		if (maxPlayer < minPlayer)
			setMaxPlayer(minPlayer);
		writeItem(0, AQUA, "Min player: " + minPlayer);
	}

	private void setMaxPlayer(int maxPlayer) {
		if (maxPlayer > data.gameType.maxPlayer)
			maxPlayer = data.gameType.maxPlayer;
		if (maxPlayer < data.gameType.minPlayer)
			maxPlayer = data.gameType.minPlayer;
		this.maxPlayer = maxPlayer;
		if (minPlayer > maxPlayer)
			setMinPlayer(maxPlayer);
		writeItem(1, BLUE, "Max player: " + maxPlayer);
	}

	private void setPassword(String password) {
		this.password = password;
		if (password == null)
			password = "none";
		writeItem(2, GOLD, "Password: " + password);
	}

	private void writeItem(int index, String color, String text) {
		Inventory inv = getInventory();
		ItemStack is = inv.getItem(10 + index);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(color + BOLD + text);
		is.setItemMeta(im);

		ItemStack acceptIS = inv.getItem(16);
		ItemMeta acceptIM = acceptIS.getItemMeta();
		List<String> lore = acceptIM.getLore();
		lore.set(2 + index, GRAY + text);
		acceptIM.setLore(lore);
		acceptIS.setItemMeta(acceptIM);
	}
}
