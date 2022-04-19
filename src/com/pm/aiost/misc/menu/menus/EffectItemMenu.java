package com.pm.aiost.misc.menu.menus;

import static com.pm.aiost.misc.utils.ChatColor.BLUE;
import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;
import static com.pm.aiost.misc.utils.ChatColor.PURPLE;
import static com.pm.aiost.misc.utils.ChatColor.RED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.collection.EffectEntryBuilder;
import com.pm.aiost.event.effect.effects.PlaceEffectBlockEffect;
import com.pm.aiost.event.effect.effects.PlaceTileObjectEffect;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.SingleInventoryMenu;
import com.pm.aiost.misc.menu.menus.request.CreateItemMenu;
import com.pm.aiost.misc.menu.menus.request.WorldEffectsMenu;
import com.pm.aiost.misc.menu.menus.request.WorldEffectsMenu.EffectEntry;
import com.pm.aiost.misc.menu.menus.request.creation.CreationMenus;
import com.pm.aiost.misc.menu.request.requests.SingleMenuRequest;
import com.pm.aiost.misc.other.Banner;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.misc.utils.nbt.NBTHelper;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.ServerWorld;
import com.pm.aiost.server.world.object.tileObject.TileObject;

public class EffectItemMenu extends SingleInventoryMenu {

	private static final ItemStack EFFECT_BLOCK_SYMBOL = MetaHelper.setMeta(Material.STONE,
			PURPLE + BOLD + "Block mode",
			Arrays.asList(GRAY + "Click to change mode to item", "", GRAY + " -Changes effect creation mode"));

	private static final ItemStack EFFECT_ITEM_SYMBOL = MetaHelper.setMeta(Material.STICK, BLUE + BOLD + "Item mode",
			Arrays.asList(GRAY + "Click to change mode to block", "", GRAY + " -Changes effect creation mode"));

	private static final ItemStack BLOCK_EFFECT_SYMBOL = MetaHelper.setMeta(Material.RED_BANNER,
			GRAY + BOLD + "Block effect", Arrays.asList(GRAY + "Click to set block effect"));

	private static final ItemStack CHOOSE_EFFECT_SYMBOL = MetaHelper.setMeta(Material.WRITABLE_BOOK,
			GRAY + BOLD + "Choose effect", Arrays.asList(GRAY + "Click to choose existing effect"));

	private static final ItemStack CHOOSE_BLOCK_SYMBOL = MetaHelper.setMeta(Material.STONE,
			PURPLE + BOLD + "Choose block", Arrays.asList(GRAY + "Click to change your block"));

	private static final ItemStack NO_BLOCK_MODE_SYMBOL = MetaHelper.setMeta(Material.BARRIER,
			RED + BOLD + "Block mode required!", Arrays.asList(GRAY + "You must use block mode to do this!"));

	private static final ItemStack CHOOSE_ITEM_SYMBOL = MetaHelper.setMeta(Material.STICK, BLUE + BOLD + "Choose item",
			Arrays.asList(GRAY + "Click to change your item"));

	private static final ItemStack NUMBER_1_SYMBOL = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "Effect 1",
			Arrays.asList(GRAY + "Click to set effect 1"), Banner.onePattern(DyeColor.BLACK, DyeColor.WHITE));

	private static final ItemStack NUMBER_2_SYMBOL = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "Effect 2",
			Arrays.asList(GRAY + "Click to set effect 2"), Banner.twoPattern(DyeColor.BLACK, DyeColor.WHITE));

	private static final ItemStack NUMBER_3_SYMBOL = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "Effect 3",
			Arrays.asList(GRAY + "Click to set effect 3"), Banner.threePattern(DyeColor.BLACK, DyeColor.WHITE));

	private static final ItemStack NUMBER_4_SYMBOL = Banner.create(Material.BLACK_BANNER, GRAY + BOLD + "Effect 4",
			Arrays.asList(GRAY + "Click to set effect 4"), Banner.fourPattern(DyeColor.BLACK, DyeColor.WHITE));

	private static final ItemStack GET_ITEM = MetaHelper.setMeta(Material.DISPENSER, GOLD + BOLD + "Create item",
			Arrays.asList(GRAY + "Click to get your item"));

	private static final ItemStack RESET_ITEM = MetaHelper.setMeta(Material.LAVA_BUCKET, RED + BOLD + "Reset settings",
			Arrays.asList(GRAY + "Click to reset all settings"));

	private static final ItemStack[] ITEMS = new ItemStack[] { CHOOSE_BLOCK_SYMBOL, NUMBER_1_SYMBOL, NUMBER_2_SYMBOL,
			NUMBER_3_SYMBOL, NUMBER_4_SYMBOL, RESET_ITEM, GET_ITEM };

	private static final ItemStack DEFAULT_BLOCK = new ItemStack(Material.STONE);
	private static final ItemStack DEFAULT_ITEM = new ItemStack(Material.STICK);
	private static final int MAX_EFFECT_SIZE = 4;
	private static final int ITEM_SLOT = 10;
	private static final int FIRST_BANNER_SLOT = 11;
	private static final int MODE_SLOT = 26;

	private ItemStack item;
	private Effect[] effects;
	private TileObject tileObject;
	private int currentSlot;
	private boolean blockMode;
	private boolean hasChangedEffects;
	private boolean hasChangedTileObject;
	private boolean hasChangedId;
	private boolean updateItem;
	private int effectId;
	private int blockEffectId;
	private ServerWorld serverWorld;

	public EffectItemMenu() {
		super(BOLD + "Effect Item Menu", 3, true);
		item = DEFAULT_BLOCK.clone();
		effects = new Effect[MAX_EFFECT_SIZE];
		currentSlot = 0;
		blockMode = true;
		hasChangedEffects = false;
		hasChangedTileObject = false;
		hasChangedId = false;
		updateItem = false;
		effectId = -1;
		blockEffectId = -1;
		initMenu();
	}

	private void initMenu() {
		set(ITEMS);
		addBorderItems(new int[] { 0, 9, 17 }, CHOOSE_EFFECT_SYMBOL, BLOCK_EFFECT_SYMBOL, EFFECT_BLOCK_SYMBOL);
		setBackLink(PlayerWorldItemMenu.getMenu());
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		if (event.getSlot() == ITEM_SLOT) {
			if (event.getCursor().getAmount() != 0)
				setItem(event.getCursor().clone());
			else {
				CreateItemMenu createItemMenu = (CreateItemMenu) serverPlayer.getOrCreateMenu(CreateItemMenu.class,
						CreateItemMenu::new);
				createItemMenu.setItem(item);
				serverPlayer.doMenuRequest(EFFECT_BLOCK_SYMBOL, () -> new SingleMenuRequest(createItemMenu) {

					@Override
					public void openRequest(ServerPlayer serverPlayer) {
						EffectItemMenu.this.open(serverPlayer);
					}

					@Override
					public void onResult(ServerPlayer serverPlayer, Object obj) {
						EffectItemMenu.this.setItem((ItemStack) obj);
					}
				});
			}
			return;
		}

		ItemStack is = event.getCurrentItem();
		if (is != null) {
			switch (is.getType()) {
			case BLACK_BANNER:
				currentSlot = event.getSlot();
				serverPlayer.doMenuRequest(NUMBER_1_SYMBOL,
						() -> new SingleMenuRequest(CreationMenus.getEffectMenu(serverPlayer)) {

							@Override
							public void onResult(ServerPlayer serverPlayer, Object obj) {
								setCurrentEffect((Effect) obj);
							}

							@Override
							public void openRequest(ServerPlayer serverPlayer) {
								EffectItemMenu.this.open(serverPlayer);
							}
						});
				break;

			case RED_BANNER:
				currentSlot = event.getSlot();
				if (blockMode)
					serverPlayer.doMenuRequest(BLOCK_EFFECT_SYMBOL,
							() -> new SingleMenuRequest(CreationMenus.getTileObjectMenu(serverPlayer)) {

								@Override
								public void onResult(ServerPlayer serverPlayer, Object obj) {
									setTileObject((TileObject) obj);
								}

								@Override
								public void openRequest(ServerPlayer serverPlayer) {
									EffectItemMenu.this.open(serverPlayer);
								}
							});
				else
					displayInSlot(event, NO_BLOCK_MODE_SYMBOL, 40);
				break;

			case WRITABLE_BOOK:
				serverPlayer
						.doMenuRequest(CHOOSE_EFFECT_SYMBOL,
								() -> new SingleMenuRequest(
										serverPlayer.getServerWorld().getOrCreateMenu(WorldEffectsMenu.class,
												() -> new WorldEffectsMenu(serverPlayer.getServerWorld()))) {

									@Override
									public void onResult(ServerPlayer serverPlayer, Object obj) {
										EffectEntry entry = (EffectEntry) obj;
										setEffects(entry);
									}

									@Override
									public void openRequest(ServerPlayer serverPlayer) {
										EffectItemMenu.this.open(serverPlayer);
									}
								});
				break;

			case DISPENSER:
				registerEffects(serverPlayer);
				serverPlayer.addItem(item);
				break;

			case LAVA_BUCKET:
				reset();
				break;

			case STONE:
				blockMode = false;
				reset();
				break;

			case STICK:
				blockMode = true;
				reset();
				break;

			default:
				break;
			}

		}
	}

	private void reset() {
		if (blockMode)
			resetBlock();
		else
			resetItem();
		updateItem = false;
		hasChangedEffects = false;
		hasChangedTileObject = false;
		hasChangedId = false;
		tileObject = null;
		effectId = -1;
		blockEffectId = -1;
		resetEffects();
	}

	private void resetBlock() {
		this.item = DEFAULT_BLOCK;
		Inventory inv = getInventory();
		inv.setItem(ITEM_SLOT, CHOOSE_BLOCK_SYMBOL);
		inv.setItem(MODE_SLOT, EFFECT_BLOCK_SYMBOL);
		inv.setItem(18, BLOCK_EFFECT_SYMBOL);
	}

	private void resetItem() {
		this.item = DEFAULT_ITEM;
		Inventory inv = getInventory();
		inv.setItem(ITEM_SLOT, CHOOSE_ITEM_SYMBOL);
		inv.setItem(MODE_SLOT, EFFECT_ITEM_SYMBOL);
		inv.setItem(18, BLOCK_EFFECT_SYMBOL);
	}

	private void resetEffects() {
		Inventory inv = getInventory();
		for (int i = 0; i < MAX_EFFECT_SIZE; i++) {
			effects[i] = null;
			inv.setItem(i + FIRST_BANNER_SLOT, ITEMS[i + 1]);
		}
	}

	private void registerEffects(ServerPlayer serverPlayer) {
		if (serverWorld != serverPlayer.getServerWorld()) {
			if (serverWorld != null) {
				hasChangedEffects = true;
				hasChangedTileObject = true;
				hasChangedId = false;
				updateItem = true;
			}
			serverWorld = serverPlayer.getServerWorld();
		}

		if (blockMode) {
			List<Effect> effects = new ArrayList<Effect>();
			if (hasChangedEffects) {
				hasChangedEffects = false;
				EffectEntryBuilder entryBuilder = buildEffects(new EffectEntryBuilder());
				if (!entryBuilder.isEmpty()) {
					blockEffectId = entryBuilder.createWorldEntry(serverWorld);
					if (blockEffectId != -1)
						effects.add(new PlaceEffectBlockEffect(blockEffectId));
				}
				if (tileObject != null) {
					hasChangedTileObject = false;
					effects.add(new PlaceTileObjectEffect(tileObject));
				}
			} else if (hasChangedId) {
				hasChangedId = false;
				effects.add(new PlaceEffectBlockEffect(effectId));
				if (tileObject != null)
					effects.add(new PlaceTileObjectEffect(tileObject));
			}

			if (hasChangedTileObject) {
				hasChangedTileObject = false;
				effects.add(new PlaceTileObjectEffect(tileObject));
				if (blockEffectId != -1)
					effects.add(new PlaceEffectBlockEffect(blockEffectId));
			}

			if (effects.size() > 0)
				effectId = serverWorld.getWorldEffects().addTemp(effects.toArray(new Effect[effects.size()]));
		}

		else {
			if (hasChangedEffects) {
				hasChangedEffects = false;
				EffectEntryBuilder entryBuilder = buildEffects(new EffectEntryBuilder());
				if (!entryBuilder.isEmpty())
					effectId = entryBuilder.createWorldEntry(serverWorld);
			}
		}

		if (updateItem) {
			updateItem = false;
			if (effectId != -1)
				item = NBTHelper.setWorldEffect(item, effectId);
		}
	}

	private EffectEntryBuilder buildEffects(EffectEntryBuilder entryBuilder) {
		for (int i = 0; i < MAX_EFFECT_SIZE; i++) {
			Effect effect = this.effects[i];
			if (effect != null)
				entryBuilder.add(effect);
		}
		return entryBuilder;
	}

	public void setItem(ItemStack is) {
		item = is;
		getInventory().setItem(ITEM_SLOT, is);
		updateItem = true;
	}

	public void setEffects(Effect[] effects) {
		for (int i = 0; i < effects.length; i++)
			insertEffect(i, effects[i]);
		hasChangedEffects = true;
		updateItem = true;
	}

	public void setEffects(EffectEntry entry) {
		effectId = entry.id;
		int i = 0;
		for (; i < entry.effects.length; i++)
			insertEffect(i, entry.effects[i]);
		for (int j = 0; j < entry.selfEffects.length; j++)
			insertEffect(i + j, entry.selfEffects[j]);
		hasChangedId = true;
		updateItem = true;
	}

	public void setEffect(int index, Effect effect) {
		insertEffect(index, effect);
		hasChangedEffects = true;
		updateItem = true;
	}

	public void setCurrentEffect(Effect effect) {
		insertEffect(currentSlot - FIRST_BANNER_SLOT, effect);
		hasChangedEffects = true;
		updateItem = true;
	}

	private void insertEffect(int index, Effect effect) {
		effects[index] = effect;
		List<String> lore = new ArrayList<String>();
		lore.add(GRAY + "Click to set effect " + (index + 1));
		lore.add(null);
		effect.createDescription(lore);
		getInventory().setItem(index + FIRST_BANNER_SLOT,
				Banner.create(Material.BLACK_BANNER, GRAY + BOLD + effect.getType().displayName, lore,
						Banner.numberPattern(index + 1, DyeColor.BLACK, DyeColor.WHITE)));
	}

	public void setTileObject(TileObject tileObject) {
		if (tileObject == this.tileObject)
			return;
		this.tileObject = tileObject;
		List<String> lore = new ArrayList<String>();
		lore.add(GRAY + "Click to set block effect");
		lore.add(null);
		tileObject.createDescription(lore);
		getInventory().setItem(currentSlot, MetaHelper.setMeta(Material.RED_BANNER,
				GRAY + BOLD + tileObject.getTileObjectType().displayName, lore));
		hasChangedTileObject = true;
		updateItem = true;
	}
}
