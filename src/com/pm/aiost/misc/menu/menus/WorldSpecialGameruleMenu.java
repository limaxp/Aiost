package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.DARK_GRAY;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.utils.StringUtils;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.ServerWorld;

@SuppressWarnings("unchecked")
public class WorldSpecialGameruleMenu extends SingleInventoryMenu {

	private static final GameRule<Object>[] GAME_RULES;
	private static final ItemStack[] ITEMS;

	static {
		List<GameRule<?>> gamerules = new ArrayList<GameRule<?>>();
		List<ItemStack> itemstacks = new ArrayList<ItemStack>();
		GameRule<?>[] rules = GameRule.values();
		List<String> lore = Arrays.asList(GRAY + "Click to change this gamerule");
		int length = rules.length;
		for (int i = 0; i < length; i++) {
			GameRule<?> gamerule = rules[i];
			if (gamerule.getType() == Integer.class) {
				gamerules.add(gamerule);
				itemstacks.add(MetaHelper.setMeta(Material.LIME_DYE,
						DARK_GRAY + BOLD + StringUtils.fillSpaceBeforeUpperCase(gamerule.getName()), lore));
			}
		}
		int size = gamerules.size();
		GAME_RULES = gamerules.toArray(new GameRule[size]);
		ITEMS = itemstacks.toArray(new ItemStack[size]);
	}

	private final ServerWorld serverWorld;

	public WorldSpecialGameruleMenu(ServerWorld serverWorld, WorldGameruleMenu menu) {
		super(BOLD + "Gamerules", 3, true);
		this.serverWorld = serverWorld;
		setBackLink(menu);
		set(ITEMS);
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			int slot = event.getSlot();
			GameRule<Object> gamerule = GAME_RULES[InventoryMenu.parseBorderedIndex(event.getView().getTitle(), slot)];
			Class<?> type = gamerule.getType();
			if (type == Integer.class) {
				serverPlayer.doMenuRequest(gamerule, () -> new SingleMenuRequest(
						new NumberMenu(event.getCurrentItem().getItemMeta().getDisplayName())) {

					@Override
					public void onResult(ServerPlayer serverPlayer, Object obj) {
						serverWorld.setGameRule(gamerule, ((Double) obj).intValue());
					}

					@Override
					public void openRequest(ServerPlayer serverPlayer) {
						WorldSpecialGameruleMenu.this.open(serverPlayer);
					}
				});

			}
		}
	}
}
