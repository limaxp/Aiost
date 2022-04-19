package com.pm.aiost.event.effect.effects;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.pm.aiost.Aiost;
import com.pm.aiost.event.AiostEventFactory;
import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectAction;
import com.pm.aiost.event.effect.EffectCondition;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.events.PlayerEquipItemEvent.EquipmentAction;
import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest.SimpleSingleMenuRequest;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.player.ServerPlayer;

public class StackEffect extends Effect {

	private static final byte[] ACTIONS = { EffectAction.ITEM_PICKUP, EffectAction.INVENTORY_PLACE,
			EffectAction.INVENTORY_DRAG, EffectAction.INVENTORY_CREATIVE_PLACE };

	private int size;

	public StackEffect() {
		super(ACTIONS, EffectCondition.SELF);
	}

	public StackEffect(int size) {
		super(ACTIONS, EffectCondition.SELF);
		this.size = size;
	}

	@Override
	public void onPlayerPickupItem(ServerPlayer serverPlayer, EntityPickupItemEvent event) {
		Player player = serverPlayer.player;
		PlayerInventory inventory = player.getInventory();
		ItemStack is = event.getItem().getItemStack();
		int amount = is.getAmount();
		for (ItemStack inventoryItem : inventory) {
			if (inventoryItem != null && inventoryItem.isSimilar(is)) {
				int inventoryAmount = inventoryItem.getAmount();

				if (inventoryAmount < size) {
					int newAmount = inventoryAmount + amount;
					if (newAmount < size) {
						inventoryItem.setAmount(newAmount);
						event.getItem().remove();
						event.setCancelled(true);
						return;
					} else {
						inventoryItem.setAmount(size);
						amount = newAmount - size;
					}
				} else
					continue;
			}
		}

		if (is.getAmount() != amount)
			is.setAmount(amount);

		HashMap<Integer, ItemStack> notAdded = inventory.addItem(is);
		if (notAdded.size() < 1) {
			event.getItem().remove();
			event.setCancelled(true);
			if (inventory.getItemInMainHand().equals(is))
				AiostEventFactory.callPlayerEquipHandItemEvent(serverPlayer, is, EquipmentSlot.HAND,
						EquipmentAction.CUSTOM);
		}
	}

	@Override
	public void onInventoryClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		ItemStack cursor = event.getCursor();
		ItemStack is = event.getCurrentItem();
		if (cursor.isSimilar(is)) {
			ClickType clickType = event.getClick();
			if (clickType.equals(ClickType.LEFT)) {
				int amount = is.getAmount() + cursor.getAmount();
				if (amount > size) {
					is.setAmount(size);
					cursor.setAmount(amount - size);
				} else {
					is.setAmount(amount);
					event.getWhoClicked().setItemOnCursor(null);
				}
			} else if (clickType.equals(ClickType.RIGHT)) {
				is.setAmount(is.getAmount() + 1);
				int amount = cursor.getAmount();
				if (amount > 1)
					cursor.setAmount(amount - 1);
				else
					event.getWhoClicked().setItemOnCursor(null);
			}
			event.setCancelled(true);
		}
	}

	@Override
	public void onInventoryDrag(ServerPlayer serverPlayer, InventoryDragEvent event) {
		HumanEntity player = event.getWhoClicked();
		ItemStack cursor = event.getCursor();
		Map<Integer, ItemStack> items = event.getNewItems();
		int length = items.size();
		Inventory inventory = event.getInventory();
		if (length == 1) {
			int slot = event.getRawSlots().iterator().next();
			if (slot >= inventory.getSize()) {
				inventory = player.getInventory();
				slot = event.getInventorySlots().iterator().next();
				if (slot > 35)
					slot -= 36;
			}

			ItemStack slotItem = inventory.getItem(slot);
			if (slotItem != null) {
				int amount;
				if (event.getType() == DragType.EVEN) {
					amount = slotItem.getAmount() + cursor.getAmount();
					if (amount > size) {
						slotItem.setAmount(size);
						cursor.setAmount(amount - size);
						Bukkit.getScheduler().runTaskLater(Aiost.getPlugin(), () -> player.setItemOnCursor(cursor), 1);
					} else {
						slotItem.setAmount(amount);
						Bukkit.getScheduler().runTaskLater(Aiost.getPlugin(), () -> player.setItemOnCursor(null), 1);
					}
				} else {
					amount = slotItem.getAmount() + 1;
					if (amount > size)
						return;
					slotItem.setAmount(amount);
					int cursorAmount = cursor.getAmount();
					if (cursorAmount > 1) {
						cursor.setAmount(cursorAmount - 1);
						Bukkit.getScheduler().runTaskLater(Aiost.getPlugin(), () -> player.setItemOnCursor(cursor), 1);
					} else
						Bukkit.getScheduler().runTaskLater(Aiost.getPlugin(), () -> player.setItemOnCursor(null), 1);
				}
				event.setCancelled(true);
			} else if (event.getType() == DragType.EVEN) {
				inventory.setItem(slot, cursor);
				Bukkit.getScheduler().runTaskLater(Aiost.getPlugin(), () -> player.setItemOnCursor(null), 1);
				event.setCancelled(true);
			}
		} else {
			Iterator<Integer> indexes = event.getRawSlots().iterator();
			Iterator<Integer> indexes2 = event.getInventorySlots().iterator();
			int cursorAmount = cursor.getAmount();
			int lengthDivided;
			int amountLeft;
			if (event.getType() == DragType.EVEN) {
				lengthDivided = cursorAmount / length;
				amountLeft = cursorAmount % length;
			} else {
				lengthDivided = 1;
				amountLeft = cursorAmount - length;
			}
			int slot;
			int slot2 = 0;
			ItemStack slotItem;
			for (ItemStack is : items.values()) {
				slot = indexes.next();
				if (indexes2.hasNext())
					slot2 = indexes2.next();
				if (slot >= inventory.getSize()) {
					inventory = player.getInventory();
					slot = slot2;
					if (slot > 35)
						slot -= 36;
				}

				slotItem = inventory.getItem(slot);
				if (slotItem != null) {
					int amount = slotItem.getAmount() + lengthDivided;
					if (amount > size) {
						slotItem.setAmount(size);
						amountLeft += amount - size;
					} else
						slotItem.setAmount(amount);
				} else {
					is.setAmount(lengthDivided);
					inventory.setItem(slot, is);
				}
			}

			if (amountLeft > 0) {
				cursor.setAmount(amountLeft);
				Bukkit.getScheduler().runTaskLater(Aiost.getPlugin(), () -> player.setItemOnCursor(cursor), 1);
			} else
				Bukkit.getScheduler().runTaskLater(Aiost.getPlugin(), () -> player.setItemOnCursor(null), 1);
			event.setCancelled(true);
		}
	}

	@Override
	public void onInventoryCreative(ServerPlayer serverPlayer, InventoryCreativeEvent event) {
		ItemStack cursor = event.getCursor();
		ItemStack is = event.getCurrentItem();
//		System.out.println("StackInventoryCreativePlace");
//		System.out.println(event.getAction());
//		System.out.println(event.getClick());
//		Inventory inventory = event.getInventory();
//		if (is == null || is.getType() == Material.AIR) {
//			System.out.println("Hi");
//			player.setItemOnCursor(null);
//			event.setCursor(null);
//			inventory.setItem(event.getSlot(), event.getCursor());
//			event.setCurrentItem(cursor);
//			event.setCancelled(true);
//		}
//
//		else if (is.isSimilar(cursor)) {
//			System.out.println("Hi2");
//			is.setAmount(cursor.getAmount());
//		}
	}

	@Override
	public boolean equals(Effect effect) {
		if (((StackEffect) effect).size != size)
			return false;
		return true;
	}

	@Override
	public void load(ConfigurationSection effectSection) {
		size = effectSection.getInt("size");
	}

	@Override
	public void load(Effect effect) {
		StackEffect stack = (StackEffect) effect;
		if (stack.size != 0)
			size = stack.size;
	}

	@Override
	public void load(INBTTagCompound nbt) {
		size = nbt.getInt("size");
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		nbt.setInt("size", size);
		return nbt;
	}

	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleSingleMenuRequest(requestConsumer, targetConsumer, new NumberMenu(BOLD + "Choose size")) {

			@Override
			public void onResult(ServerPlayer serverPlayer, Object obj) {
				StackEffect.this.size = ((Double) obj).intValue();
			}
		};
	}

	@Override
	public EffectType<? extends StackEffect> getType() {
		return EffectTypes.STACK;
	}

	@Override
	public void setDefault() {
		size = 16;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "Size: " + ChatColor.DARK_GRAY + size);
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}

}
