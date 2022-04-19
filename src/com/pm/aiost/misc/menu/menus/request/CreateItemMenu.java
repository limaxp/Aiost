package com.pm.aiost.misc.menu.menus.request;

import static com.pm.aiost.misc.utils.ChatColor.BLUE;
import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.PURPLE;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.menu.anvilMenu.AnvilMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.menus.ItemEnchantmentsMenu;
import com.pm.aiost.misc.menu.menus.ItemNBTMenu;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class CreateItemMenu extends SingleInventoryMenu {

	private static final ItemStack CHOOSE_ITEM = MetaHelper.setMeta(Material.STICK, PURPLE + BOLD + "Choose item",
			Arrays.asList(GRAY + "Left click to choose a material", GRAY + "Right click to choose custom item",
					GRAY + "Or drag item here to use it"));

	private static final ItemStack AMOUNT_ITEM = MetaHelper.setMeta(Material.BEETROOT_SEEDS,
			PURPLE + BOLD + "Change amount", Arrays.asList(GRAY + "Click to change stack size"));

	private static final ItemStack DAMAGE_ITEM = MetaHelper.setMeta(Material.ANVIL, PURPLE + BOLD + "Change damage",
			Arrays.asList(GRAY + "Click to change durability damage"));

	private static final ItemStack RENAME_ITEM = MetaHelper.setMeta(Material.NAME_TAG, PURPLE + BOLD + "Change name",
			Arrays.asList(GRAY + "Click to rename your item"));

	private static final ItemStack LORE_ITEM = MetaHelper.setMeta(Material.WRITABLE_BOOK, PURPLE + BOLD + "Change lore",
			Arrays.asList(GRAY + "Click to change item lore"));

	public static final ItemStack ENCHANTMENTS_ITEM = MetaHelper.setMeta(Material.ENCHANTING_TABLE,
			PURPLE + BOLD + "Change enchantments", Arrays.asList(GRAY + "Click to change item enchantments"));

	private static final ItemStack ACCEPT_ITEM = MetaHelper.setMeta(Material.NETHER_STAR, GOLD + BOLD + "Accept item",
			Arrays.asList(GRAY + "Click to accept item"));

	private static final ItemStack NBT_ITEM = MetaHelper.setMeta(Material.COMPARATOR, BLUE + BOLD + "Edit NBT",
			Arrays.asList(GRAY + "Click to edit item nbt"));

	private static final ItemStack[] ITEMS = new ItemStack[] { CHOOSE_ITEM, AMOUNT_ITEM, DAMAGE_ITEM, RENAME_ITEM,
			LORE_ITEM, ENCHANTMENTS_ITEM, ACCEPT_ITEM };

	private static final int CHOOSE_ITEM_SLOT = 10;

	protected ItemStack item;

	public CreateItemMenu() {
		super(BOLD + "Choose Item", 3, true);
		item = new ItemStack(Material.STICK);
		set(ITEMS);
		addBorderItem(17, NBT_ITEM);
		setBackLink(ServerPlayer::openMenuRequestPrevMenu);
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
				new ItemEnchantmentsMenu(serverPlayer).open(serverPlayer);
				break;

			case 16:
				serverPlayer.setMenuRequestResult(item);
				break;

			case 26:
				ItemNBTMenu.getMenu().open(serverPlayer);
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
			serverPlayer.doMenuRequest(CHOOSE_ITEM, () -> new SingleMenuRequest(ItemMenu.getMenu()) {

				@Override
				public void onResult(ServerPlayer serverPlayer, Object obj) {
					setItem((Material) obj);
				}

				@Override
				public void openRequest(ServerPlayer serverPlayer) {
					CreateItemMenu.this.open(serverPlayer);
				}
			});

		else if (event.getClick() == ClickType.RIGHT)
			serverPlayer.doMenuRequest(ITEMS, () -> new SingleMenuRequest(CustomItemMenu.getMenu()) {

				@Override
				public void onResult(ServerPlayer serverPlayer, Object obj) {
					setClone((ItemStack) obj);
				}

				@Override
				public void openRequest(ServerPlayer serverPlayer) {
					CreateItemMenu.this.open(serverPlayer);
				}
			});
	}

	protected void chooseAmount(ServerPlayer serverPlayer) {
		serverPlayer.doMenuRequest(AMOUNT_ITEM, () -> new SingleMenuRequest(new NumberMenu(BOLD + "Choose amount")) {

			@Override
			public void onResult(ServerPlayer serverPlayer, Object obj) {
				setAmount(((Double) obj).intValue());
			}

			@Override
			public void openRequest(ServerPlayer serverPlayer) {
				CreateItemMenu.this.open(serverPlayer);
			}
		});
	}

	protected void chooseDamage(ServerPlayer serverPlayer) {
		serverPlayer.doMenuRequest(DAMAGE_ITEM, () -> new SingleMenuRequest(new NumberMenu(BOLD + "Choose damage")) {

			@Override
			public void onResult(ServerPlayer serverPlayer, Object obj) {
				setDamage(((Double) obj).shortValue());
			}

			@Override
			public void openRequest(ServerPlayer serverPlayer) {
				CreateItemMenu.this.open(serverPlayer);
			}
		});
	}

	protected void openLoreMenu(ServerPlayer serverPlayer) {
		serverPlayer.doMenuRequest(LORE_ITEM,
				() -> new SingleMenuRequest(serverPlayer.getOrCreateMenu(CreateTextMenu.class, CreateTextMenu::new)) {

					@SuppressWarnings("unchecked")
					@Override
					public void onResult(ServerPlayer serverPlayer, Object obj) {
						setItem(MetaHelper.set(item, (List<String>) obj));
					}

					@Override
					public void openRequest(ServerPlayer serverPlayer) {
						CreateItemMenu.this.open(serverPlayer);
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
}
