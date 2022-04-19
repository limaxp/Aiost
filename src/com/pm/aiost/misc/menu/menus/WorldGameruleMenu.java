package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GREEN;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.MenuHelper;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.LazyInventoryMenu;
import com.pm.aiost.misc.utils.StringUtils;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.ServerWorld;

@SuppressWarnings("unchecked")
public class WorldGameruleMenu extends LazyInventoryMenu {

	private static final GameRule<Boolean>[] GAME_RULES;

	static {
		List<GameRule<?>> booleanRules = new ArrayList<GameRule<?>>();
		GameRule<?>[] rules = GameRule.values();
		int length = rules.length;
		for (int i = 0; i < length; i++) {
			GameRule<?> rule = rules[i];
			if (rule.getType() == Boolean.class)
				booleanRules.add(rule);
		}
		GAME_RULES = booleanRules.toArray(new GameRule[booleanRules.size()]);
	}

	private static final ItemStack[] ITEMS = initItems();

	private static ItemStack[] initItems() {
		String activatedString = GRAY + "Click to turn " + RED + BOLD + "off";
		String deactivatedString = GRAY + "Click to turn " + GREEN + BOLD + "on";
		String offString = GRAY + "Status: " + RED + BOLD + "off";
		String onString = GRAY + "Status: " + GREEN + BOLD + "on";
		List<String> onDescription = Arrays.asList(activatedString, null, null, onString);
		List<String> offDescription = Arrays.asList(deactivatedString, null, null, offString);

		return MenuHelper.buildItemStacks(GAME_RULES.length, 2, (target, i, j) -> {
			String displayName = StringUtils.fillSpaceBeforeUpperCase(GAME_RULES[i].getName());
			target[j] = MetaHelper.setMeta(Material.GRAY_DYE, RED + BOLD + displayName, offDescription);
			target[j + 1] = MetaHelper.setMeta(Material.LIME_DYE, GREEN + BOLD + displayName, onDescription);
		});
	}

	private final ServerWorld serverWorld;

	public WorldGameruleMenu(ServerWorld serverWorld) {
		super(BOLD + "Gamerules", GAME_RULES.length, true);
		this.serverWorld = serverWorld;
		setBackLink(WorldSettingMenu.getMenu());
	}

	@Override
	public void buildInventory(Inventory inv, int index) {
		set(inv, 0, index * InventoryMenu.MAX_ITEMS_WITH_BORDER, GAME_RULES.length, (settingIndex) -> {
			if (serverWorld.getGameRuleValue(GAME_RULES[settingIndex]))
				return ITEMS[settingIndex * 2 + 1];
			else
				return ITEMS[settingIndex * 2];
		});
		addBorderItem(8, MetaHelper.setMeta(Material.COMPARATOR, RED + BOLD + "More gamerules",
				Arrays.asList(GRAY + "Click to open gamerule menu")));
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			int slot = event.getSlot();
			switch (is.getType()) {
			case LIME_DYE:
				int gameruleIndex = InventoryMenu.parseBorderedIndex(event.getView().getTitle(), slot);
				serverWorld.setGameRule(GAME_RULES[gameruleIndex], false);
				event.getInventory().setItem(slot, ITEMS[gameruleIndex * 2]);
				break;

			case GRAY_DYE:
				int gameruleIndex2 = InventoryMenu.parseBorderedIndex(event.getView().getTitle(), slot);
				serverWorld.setGameRule(GAME_RULES[gameruleIndex2], true);
				event.getInventory().setItem(slot, ITEMS[gameruleIndex2 * 2 + 1]);
				break;

			case COMPARATOR:
				serverWorld.getOrCreateMenu(WorldSpecialGameruleMenu.class,
						() -> new WorldSpecialGameruleMenu(serverWorld, this)).open(serverPlayer);
				break;

			default:
				break;
			}
		}
	}
}
