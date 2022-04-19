package com.pm.aiost.misc.menu.menus.request;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.menus.request.enumeration.EnumerationMenus;
import com.pm.aiost.misc.menu.request.requests.CallbackMenuRequest;
import com.pm.aiost.misc.other.Banner;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class EffectEntryMenu extends SingleInventoryMenu {

	protected static final ItemStack ADD_EFFECT_ITEM = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "Add effect",
			Arrays.asList(GRAY + "Click to add a effect"), Banner.plusPattern(DyeColor.BLACK, DyeColor.WHITE));

	protected static final ItemStack ACCEPT_EFFECT_ITEM = MetaHelper.setMeta(Material.NETHER_STAR,
			GRAY + BOLD + "Accept effects", Arrays.asList(GRAY + "Click to accept effects"));

	protected List<Effect> effects;

	public EffectEntryMenu(List<Effect> effects) {
		super(BOLD + "Modify Effect", 3, true);
		this.effects = effects;
		int i = 0;
		for (; i < effects.size(); i++)
			setItem(effects.get(i), 10 + i);
		getInventory().setItem(i + 10, ADD_EFFECT_ITEM);
		getInventory().setItem(i + 11, ACCEPT_EFFECT_ITEM);
		setBackLink(ServerPlayer::openMenuRequestPrevMenu);
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			switch (is.getType()) {

			case RED_BANNER:
				ClickType clickType = event.getClick();
				if (clickType == ClickType.LEFT || clickType == ClickType.RIGHT)
					serverPlayer.doMenuRequest(effects,
							() -> new CallbackMenuRequest(EnumerationMenus.EFFECT_TYPE_MENU) {

								@Override
								public void onResult(ServerPlayer serverPlayer, Object obj) {
									setEffect(serverPlayer, (EffectType<?>) obj, event.getSlot());
								}

								@Override
								public void openRequest(ServerPlayer serverPlayer) {
									EffectEntryMenu.this.open(serverPlayer);
								}
							});
				else if (clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT)
					removeEffect(event.getSlot());
				break;

			case BLACK_BANNER:
				serverPlayer.doMenuRequest(effects, () -> new CallbackMenuRequest(EnumerationMenus.EFFECT_TYPE_MENU) {

					@Override
					public void onResult(ServerPlayer serverPlayer, Object obj) {
						addEffect(serverPlayer, (EffectType<?>) obj, event.getSlot());
					}

					@Override
					public void openRequest(ServerPlayer serverPlayer) {
						EffectEntryMenu.this.open(serverPlayer);
					}
				});
				break;

			case NETHER_STAR:
				acceptClick(serverPlayer, event);
				break;

			default:
				break;
			}
		}
	}

	private void setEffect(ServerPlayer serverPlayer, EffectType<?> type, int slot) {
		Effect effect = createEffect(serverPlayer, type, slot);
		setItem(effect, slot);
		effects.set(slot - 10, effect);
	}

	private void addEffect(ServerPlayer serverPlayer, EffectType<?> type, int slot) {
		Effect effect = createEffect(serverPlayer, type, slot);
		setItem(effect, slot);
		if (slot - 10 < 3) {
			getInventory().setItem(slot + 1, ADD_EFFECT_ITEM);
			getInventory().setItem(slot + 2, ACCEPT_EFFECT_ITEM);
		} else
			getInventory().setItem(slot + 1, ACCEPT_EFFECT_ITEM);
		effects.add(effect);
	}

	private void removeEffect(int slot) {
		int index = slot - 10;
		if (index < effects.size()) {
			int size = effects.size();
			Inventory inv = getInventory();
			for (int i = index; i < size - 1; i++)
				inv.setItem(i + 10, inv.getItem(i + 11));
			inv.setItem(size + 9, ADD_EFFECT_ITEM);
			inv.setItem(size + 10, ACCEPT_EFFECT_ITEM);
			inv.setItem(size + 11, null);
		}
		effects.remove(index);
	}

	private Effect createEffect(ServerPlayer serverPlayer, EffectType<?> type, int slot) {
		Effect effect = type.create();
		effect.setDefault();
		serverPlayer.doMenuRequest(effect.getClass(), effect.getMenuRequest(serverPlayer, (serverPlayer1) -> {
			setItem(effect, slot);
			open(serverPlayer1);
		}));
		return effect;
	}

	private void setItem(Effect effect, int slot) {
		List<String> lore = new ArrayList<String>();
		lore.add(GRAY + "Click to change effect");
		lore.add(GRAY + "Shift click to remove effect");
		lore.add(null);
		effect.createDescription(lore);
		getInventory().setItem(slot, MetaHelper.setMeta(new ItemStack(Material.RED_BANNER),
				GRAY + BOLD + effect.getType().displayName, lore));
	}

	private void acceptClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		serverPlayer.setMenuRequestResult(effects);
	}
}