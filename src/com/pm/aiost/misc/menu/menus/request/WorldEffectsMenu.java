package com.pm.aiost.misc.menu.menus.request;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.collection.list.FastArrayList;
import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.collection.EffectEntryBuilder;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.LazyInventoryMenu;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.ServerWorld;
import com.pm.aiost.server.world.effects.WorldEffects;

public class WorldEffectsMenu extends LazyInventoryMenu {

	protected final WorldEffects worldEffects;
	protected final String[] fileNames;
	protected final EffectEntry[] effects;

	public WorldEffectsMenu(ServerWorld serverWorld) {
		this(serverWorld.getWorldEffects());
	}

	public WorldEffectsMenu(WorldEffects wordlEffects) {
		this(wordlEffects, wordlEffects.getFileNames());
	}

	public WorldEffectsMenu(WorldEffects worldEffects, String[] fileNames) {
		super(BOLD + "World effects", fileNames.length, false);
		this.worldEffects = worldEffects;
		this.fileNames = fileNames;
		effects = new EffectEntry[fileNames.length];
		setBackLink(ServerPlayer::openMenuRequestPrevMenu);
	}

	@Override
	protected void buildInventory(Inventory inv, int index) {
		int factor = index * MAX_ITEMS;
		int maxLength = factor + MAX_ITEMS;
		for (int i = factor; i < maxLength && i < fileNames.length; i++) {
			int id = Integer.parseInt(fileNames[i]);
			EffectEntry data = new EffectEntry(id, worldEffects.get(id), worldEffects.getSelfArray(id));
			this.effects[i] = data;
			setItem(data, i - factor);
		}
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack is = event.getCurrentItem();
		if (is != null) {
			switch (is.getType()) {

			case BLACK_BANNER:
				ClickType clickType = event.getClick();
				if (clickType == ClickType.LEFT)
					answerClick(serverPlayer, event);
				else if (clickType == ClickType.RIGHT) {
					int index = InventoryMenu.parseIndex(event.getView().getTitle(), event.getSlot());
					serverPlayer.doMenuRequest(effects, () -> new SingleMenuRequest(
							new EffectEntryMenu(new FastArrayList<Effect>(effects[index].toArray()))) {

						@SuppressWarnings("unchecked")
						@Override
						public void onResult(ServerPlayer serverPlayer, Object obj) {
							updateEffect((List<Effect>) obj, event);
						}

						@Override
						public void openRequest(ServerPlayer serverPlayer) {
							WorldEffectsMenu.this.open(serverPlayer);
						}
					});
				}
				break;

			default:
				break;
			}
		}
	}

	private void updateEffect(List<Effect> effects, InventoryClickEvent event) {
		int slot = event.getSlot();
		int index = InventoryMenu.parseIndex(event.getView().getTitle(), slot);
		int id = Integer.parseInt(fileNames[index]);
		EffectEntryBuilder entryBuilder = new EffectEntryBuilder();
		entryBuilder.addAll(effects);
		entryBuilder.replaceWorldEntry(id, worldEffects);
		EffectEntry data = new EffectEntry(id, worldEffects.get(id), worldEffects.getSelfArray(id));
		this.effects[index] = data;
		setItem(data, slot);
	}

	private void setItem(EffectEntry data, int slot) {
		Effect[] effects = data.effects;
		Effect[] selfEffects = data.selfEffects;
		List<String> lore = new ArrayList<String>();
		lore.add(GRAY + "Left click to choose effect");
		lore.add(GRAY + "Right click to modify effect");
		int index = 1;
		for (int i = 0; i < effects.length; i++) {
			lore.add(null);
			lore.add(GRAY + "Effect " + (index++) + ':');
			effects[i].createDescription(lore);
		}
		for (int i = 0; i < selfEffects.length; i++) {
			lore.add(null);
			lore.add(GRAY + "Effect " + (index++) + ':');
			selfEffects[i].createDescription(lore);
		}
		getInventory().setItem(slot,
				MetaHelper.setMeta(new ItemStack(Material.BLACK_BANNER), GRAY + BOLD + "Effect " + (slot + 1), lore));
	}

	private void answerClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		serverPlayer
				.setMenuRequestResult(effects[InventoryMenu.parseIndex(event.getView().getTitle(), event.getSlot())]);
	}

	public static class EffectEntry {

		public final int id;
		public final Effect[] effects;
		public final Effect[] selfEffects;

		public EffectEntry(int id, Effect[] effects, Effect[] selfEffects) {
			this.id = id;
			this.effects = effects;
			this.selfEffects = selfEffects;
		}

		public Effect[] toArray() {
			Effect[] array = new Effect[effects.length + selfEffects.length];
			int i = 0;
			for (; i < effects.length; i++)
				array[i] = effects[i];
			for (int j = 0; j < selfEffects.length; j++)
				array[i + j] = selfEffects[j];
			return array;
		}
	}
}