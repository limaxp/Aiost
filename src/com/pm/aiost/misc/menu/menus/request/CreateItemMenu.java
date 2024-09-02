package com.pm.aiost.misc.menu.menus.request;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.PURPLE;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.pm.aiost.misc.menu.AnvilMenu;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.menus.ItemAttributeModifierMenu;
import com.pm.aiost.misc.menu.menus.ItemCanPlaceMenu;
import com.pm.aiost.misc.menu.menus.ItemCanPlaceMenu.ItemCanDestroyMenu;
import com.pm.aiost.misc.menu.menus.ItemHideFlagsMenu;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.utils.ChatColor;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class CreateItemMenu extends SingleInventoryMenu {

	private static final int CHOOSE_ITEM_SLOT = 10;

	protected ItemStack item;

	public CreateItemMenu() {
		super(BOLD + "Choose Item", 4, true);
		item = new ItemStack(Material.STICK);
		setBackLink(ServerPlayer::openMenuRequestPrev);
		set(MetaHelper.setMeta(Material.STICK, PURPLE + BOLD + "Choose item",
				Arrays.asList(GRAY + "Left click to choose a material", GRAY + "Right click to choose custom item",
						GRAY + "Or drag item here to use it")),

				MetaHelper.setMeta(Material.BEETROOT_SEEDS, PURPLE + BOLD + "Change amount",
						Arrays.asList(GRAY + "Click to change stack size")),

				MetaHelper.setMeta(Material.ANVIL, PURPLE + BOLD + "Change damage",
						Arrays.asList(GRAY + "Click to change durability damage")),

				MetaHelper.setMeta(Material.NAME_TAG, PURPLE + BOLD + "Change name",
						Arrays.asList(GRAY + "Click to rename your item")),

				MetaHelper.setMeta(Material.WRITABLE_BOOK, PURPLE + BOLD + "Change lore",
						Arrays.asList(GRAY + "Click to change item lore")),

				MetaHelper.setMeta(Material.ENCHANTING_TABLE, PURPLE + BOLD + "Change enchantments",
						Arrays.asList(GRAY + "Click to change item enchantments")),

				MetaHelper.setMeta(Material.IRON_SWORD, PURPLE + BOLD + "Attribute modifiers",
						Arrays.asList(GRAY + "Click to change attribute modifiers")),

				MetaHelper.setMeta(Material.DIAMOND, PURPLE + BOLD + "Unbreakable",
						Arrays.asList(GRAY + "Click to change breakable status")),

				MetaHelper.setMeta(Material.CRAFTING_TABLE, PURPLE + BOLD + "Custom modeldata",
						Arrays.asList(GRAY + "Click to change custom model data")),

				MetaHelper.setMeta(Material.WHITE_BANNER, PURPLE + BOLD + "Hide flags",
						Arrays.asList(GRAY + "Click to change hide flags")),

				MetaHelper.setMeta(Material.STONE, PURPLE + BOLD + "Can place on",
						Arrays.asList(GRAY + "Click to change can place on block")),

				MetaHelper.setMeta(Material.IRON_PICKAXE, PURPLE + BOLD + "Can break",
						Arrays.asList(GRAY + "Click to change can break blocks")),

				null,

				MetaHelper.setMeta(Material.NETHER_STAR, GOLD + BOLD + "Accept item",
						Arrays.asList(GRAY + "Click to accept item")));
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		if (event.getCurrentItem() != null) {
			switch (event.getSlot()) {

			case CHOOSE_ITEM_SLOT:
				chooseItemClick(serverPlayer, event);
				break;

			case 11:
				chooseAmount(serverPlayer);
				break;

			case 12:
				chooseDamage(serverPlayer);
				break;

			case 13:
				renameItemMenu().open(serverPlayer);
				break;

			case 14:
				openLoreMenu(serverPlayer);
				break;

			case 15:
				serverPlayer.menuRequest(new SingleMenuRequest(new EnchantmentsMenu(item.getItemMeta().getEnchants()),
						this::open, false) {

					@Override
					protected void onResult(ServerPlayer serverPlayer, Object obj) {
						@SuppressWarnings("unchecked")
						Map<Enchantment, Integer> enchantments = (Map<Enchantment, Integer>) obj;
						ItemMeta meta = item.getItemMeta();
						meta.removeEnchantments();
						for (Entry<Enchantment, Integer> entry : enchantments.entrySet())
							meta.addEnchant(entry.getKey(), entry.getValue(), true);
						item.setItemMeta(meta);
						getInventory().setItem(CHOOSE_ITEM_SLOT, item);
					}
				});
				break;

			case 16:
				new ItemAttributeModifierMenu(serverPlayer).open(serverPlayer);
				break;

			case 19:
				unbreakableClick(serverPlayer, event.getSlot());
				break;

			case 20:
				createChangeCustomModeldataMenu(serverPlayer).open(serverPlayer);
				break;

			case 21:
				ItemHideFlagsMenu.getMenu().open(serverPlayer);
				break;

			case 22:
				new ItemCanPlaceMenu(serverPlayer).open(serverPlayer);
				break;

			case 23:
				new ItemCanDestroyMenu(serverPlayer).open(serverPlayer);
				break;

			case 25:
				serverPlayer.setMenuRequestResult(item);
				break;

			default:
				break;
			}
		}
	}

	private final void chooseItemClick(ServerPlayer serverPlayer, InventoryClickEvent event) {
		if (event.getCursor().getAmount() != 0)
			setItem(event.getCursor().clone(), event.getInventory());
		else
			chooseItem(serverPlayer, event);
	}

	protected void chooseItem(ServerPlayer serverPlayer, InventoryClickEvent event) {
		if (event.getClick() == ClickType.LEFT)
			serverPlayer.menuRequest(new SingleMenuRequest(ItemMenu.getMenu(), CreateItemMenu.this::open, false) {

				@Override
				public void onResult(ServerPlayer serverPlayer, Object obj) {
					setItem((Material) obj);
				}
			});

		else if (event.getClick() == ClickType.RIGHT)
			serverPlayer.menuRequest(new SingleMenuRequest(CustomItemMenu.getMenu(), CreateItemMenu.this::open, false) {

				@Override
				public void onResult(ServerPlayer serverPlayer, Object obj) {
					setClone((ItemStack) obj);
				}
			});
	}

	protected void chooseAmount(ServerPlayer serverPlayer) {
		serverPlayer.menuRequest(
				new SingleMenuRequest(new NumberMenu(BOLD + "Choose amount"), CreateItemMenu.this::open, false) {

					@Override
					public void onResult(ServerPlayer serverPlayer, Object obj) {
						setAmount(((Double) obj).intValue());
					}
				});
	}

	protected void chooseDamage(ServerPlayer serverPlayer) {
		serverPlayer.menuRequest(
				new SingleMenuRequest(new NumberMenu(BOLD + "Choose damage"), CreateItemMenu.this::open, false) {

					@Override
					public void onResult(ServerPlayer serverPlayer, Object obj) {
						setDamage(((Double) obj).shortValue());
					}
				});
	}

	protected void openLoreMenu(ServerPlayer serverPlayer) {
		serverPlayer.menuRequest(
				new SingleMenuRequest(serverPlayer.getOrCreateMenu(CreateTextMenu.class, CreateTextMenu::new),
						CreateItemMenu.this::open, false) {

					@SuppressWarnings("unchecked")
					@Override
					public void onResult(ServerPlayer serverPlayer, Object obj) {
						setItem(MetaHelper.set(item, (List<String>) obj));
					}
				});
	}

	private final AnvilMenu renameItemMenu() {
		AnvilMenu menu = new AnvilMenu(BOLD + "Choose name", item) {
			@Override
			public void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
				event.setCancelled(true);
				if (event.getSlot() == 2) {
					setItem(event.getCurrentItem());
					CreateItemMenu.this.open(serverPlayer);
				}
			}
		};
		menu.setBackLink(this);
		return menu;
	}

	public final void setItem(Material material) {
		setItem(new ItemStack(material), getInventory());
	}

	public final void setItem(ItemStack is) {
		setItem(is, getInventory());
	}

	public final void setClone(ItemStack is) {
		setItem(is.clone(), getInventory());
	}

	private final void setItem(ItemStack is, Inventory inv) {
		item = is;
		inv.setItem(CHOOSE_ITEM_SLOT, is);
	}

	public ItemStack getItem() {
		return item;
	}

	protected final void setAmount(int amount) {
		item.setAmount(amount > 1 ? amount : 1);
		getInventory().setItem(CHOOSE_ITEM_SLOT, item);
	}

	@SuppressWarnings("deprecation")
	protected final void setDamage(short damage) {
		item.setDurability(damage > 0 ? damage : 0);
		getInventory().setItem(CHOOSE_ITEM_SLOT, item);
	}

	private void unbreakableClick(ServerPlayer serverPlayer, int slot) {
		ItemMeta itemMeta = item.getItemMeta();
		ItemStack clone = item.clone();
		if (!itemMeta.isUnbreakable()) {
			itemMeta.setUnbreakable(true);
			clone.setType(Material.LIME_DYE);
		} else {
			itemMeta.setUnbreakable(false);
			clone.setType(Material.GRAY_DYE);
		}
		item.setItemMeta(itemMeta);
		InventoryMenu.displayInSlot(serverPlayer.player, clone, slot);
	}

	private AnvilMenu createChangeCustomModeldataMenu(ServerPlayer serverPlayer) {
		ItemMeta itemMeta = item.getItemMeta();
		int customModelData = 0;
		if (itemMeta.hasCustomModelData())
			customModelData = itemMeta.getCustomModelData();

		AnvilMenu menu = new AnvilMenu(BOLD + "custom modeldata",
				MetaHelper.setMeta(Material.PAPER, Integer.toString(customModelData))) {
			@Override
			public void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
				event.setCancelled(true);
				if (event.getSlot() != 2)
					return;

				int id;
				try {
					id = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName());
				} catch (NumberFormatException e) {
					serverPlayer.player.sendMessage(ChatColor.RED + "Your input must be a number!");
					return;
				}
				itemMeta.setCustomModelData(id);
				item.setItemMeta(itemMeta);
				CreateItemMenu.this.open(serverPlayer);
			}
		};
		menu.setBackLink(this);
		return menu;
	}
}
