package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;

import java.util.Arrays;
import java.util.List;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.game.Game;
import com.pm.aiost.game.GameTeam;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.other.TeamData;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class GameTeamMenu extends SingleInventoryMenu {

	private Game game;

	public GameTeamMenu(Game game) {
		super(BOLD + "Team menu", 5, true);
		this.game = game;
		buildMenu();
	}

	private void buildMenu() {
		List<GameTeam> teams = game.getTeams();
		int size = teams.size();
		ItemStack[] itemStacks = new ItemStack[size];
		List<String> lore = Arrays.asList(GRAY + "Click to change team");
		for (int i = 0; i < size; i++) {
			TeamData team = teams.get(i);
			itemStacks[i] = MetaHelper.setMeta(team.getItem(), team.getColor() + BOLD + team.getName() + " team", lore);
		}
		set(itemStacks);
		setBackLink(ServerPlayer::openEventHandlerMenu);
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		if (event.getCurrentItem() != null) {
			game.changeTeam(serverPlayer, convertSlotToIndex(event.getSlot()));
			serverPlayer.player.closeInventory();
		}
	}
}
