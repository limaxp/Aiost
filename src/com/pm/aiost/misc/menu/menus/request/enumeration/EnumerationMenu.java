package com.pm.aiost.misc.menu.menus.request.enumeration;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World.Environment;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import com.mojang.authlib.GameProfile;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.eventHandler.EventHandler;
import com.pm.aiost.game.GameType;
import com.pm.aiost.item.custom.Slot;
import com.pm.aiost.item.spell.Spell;
import com.pm.aiost.misc.menu.MenuHelper;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.menu.inventoryMenu.inventoryMenus.ArrayInventoryMenu;
import com.pm.aiost.misc.other.PlayerHead;
import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.particleEffect.particle.ParticleType;
import com.pm.aiost.misc.registry.AiostRegistry;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.misc.utils.worldEdit.Brush;
import com.pm.aiost.misc.utils.worldEdit.Brush.BrushMode;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.player.unlockable.UnlockableType;
import com.pm.aiost.server.world.EnvironmentHelper;
import com.pm.aiost.server.world.object.tileObject.TileObjectType;
import com.pm.aiost.server.world.type.AiostWorldType;

import net.minecraft.server.v1_15_R1.EntityTypes;

public class EnumerationMenu<T> extends ArrayInventoryMenu {

	private static final List<String> ENCHANTMENT_LORE = Arrays.asList(GRAY + "Click to choose this enchantment");
	private static final List<String> ENTITY_TYPE_LORE = Arrays.asList(GRAY + "Click to choose this entity type");
	private static final List<String> PARTICLE_TYPE_LORE = Arrays.asList(GRAY + "Click to choose this particle type");
	private static final List<String> SPELL_LORE = Arrays.asList(GRAY + "Click to choose this spell");
	private static final List<String> EQUIPMENT_SLOT_LORE = Arrays.asList(GRAY + "Click to choose this equipment slot");
	private static final List<String> POTION_EFFECT_LORE = Arrays.asList(GRAY + "Click to choose this potion effect");
	private static final List<String> TILE_OBJECT_TYPE_LORE = Arrays.asList(GRAY + "Click to choose this block effect");
	private static final List<String> PARTICLE_LORE = Arrays.asList(GRAY + "Click to choose this particle");
	private static final List<String> GAME_PROFILE_LORE = Arrays.asList(GRAY + "Click to choose this profile");
	private static final List<String> WORLD_TYPE_LORE = Arrays.asList(GRAY + "Click to choose this world type");
	private static final List<String> ENVIRONMENT_LORE = Arrays.asList(GRAY + "Click to choose this environment");
	private static final List<String> UNLOCKABLE_TYPE_LORE = Arrays
			.asList(GRAY + "Click to choose this unlockable type");
	private static final List<String> EVENT_HANDLER_LORE = Arrays.asList(GRAY + "Click to choose this event handler");
	private static final List<String> BRUSH_LORE = Arrays.asList(GRAY + "Click to choose this brush");
	private static final List<String> BRUSH_MODE_LORE = Arrays.asList(GRAY + "Click to choose this brush mode");
	private static final List<String> TYPE_LORE = Arrays.asList(GRAY + "Click to choose this type");

	private final IntFunction<T> typeSupplier;

	public EnumerationMenu(String name, List<T> types, Function<T, ItemStack> itemSupplier) {
		this(name, types.size(), types::get, itemSupplier);
	}

	public EnumerationMenu(String name, T[] types, Function<T, ItemStack> itemSupplier) {
		this(name, types.length, (i) -> types[i], itemSupplier);
	}

	public EnumerationMenu(String name, int size, IntFunction<T> typeSupplier, Function<T, ItemStack> itemSupplier) {
		this(name, size, typeSupplier);
		ItemStack[] items = new ItemStack[size];
		for (int i = 0; i < items.length; i++)
			items[i] = itemSupplier.apply(typeSupplier.apply(i));
		set(items);
	}

	public EnumerationMenu(String name, IntFunction<T> typeSupplier, ItemStack... items) {
		this(name, items.length, typeSupplier);
		set(items);
	}

	public EnumerationMenu(String name, IntFunction<T> typeSupplier, Collection<ItemStack> items) {
		this(name, items.size(), typeSupplier);
		set(items);
	}

	private EnumerationMenu(String name, int size, IntFunction<T> typeSupplier) {
		super(name, size, false);
		this.typeSupplier = typeSupplier;
		setBackLink(ServerPlayer::openMenuRequestPrevMenu);
	}

	@Override
	protected void inventoryClickCallback(ServerPlayer serverPlayer, InventoryClickEvent event) {
		event.setCancelled(true);
		if (event.getCurrentItem() != null)
			serverPlayer.setMenuRequestResult(
					typeSupplier.apply(InventoryMenu.parseIndex(event.getView().getTitle(), event.getSlot())));
	}

	public static ItemStack createItem(Enchantment type) {
		return MetaHelper.setMeta(Material.ENCHANTED_BOOK, GRAY + BOLD + type.getKey().getKey().replace('_', ' '),
				ENCHANTMENT_LORE);
	}

	public static ItemStack createItem(EffectType<?> type) {
		return MetaHelper.setMeta(Material.NETHER_STAR, GRAY + BOLD + type.displayName,
				MenuHelper.createLore(GRAY + "Click to choose this effect", GRAY, type.lore));
	}

	public static ItemStack createItem(EntityTypes<?> type) {
		String name = type.f();
		name = name.substring(name.lastIndexOf(".") + 1).replace("_", " ");
		name = name.substring(0, 1).toUpperCase() + name.substring(1);
		return MetaHelper.setMeta(new ItemStack(Material.CHICKEN_SPAWN_EGG), GRAY + BOLD + name, ENTITY_TYPE_LORE);
	}

	public static ItemStack createItem(ParticleType<?> type) {
		return MetaHelper.setMeta(Material.FIREWORK_ROCKET, GRAY + BOLD + type.displayName, PARTICLE_TYPE_LORE);
	}

	public static ItemStack createItem(Spell spell) {
		return MetaHelper.setMeta(Material.SNOWBALL, GRAY + BOLD + spell.getDisplayName(), SPELL_LORE);
	}

	public static ItemStack createItem(GameType<?> type) {
		return MetaHelper.setMeta(type.item.clone(), GRAY + BOLD + type.name,
				MenuHelper.createLore(GRAY + "Click to choose this game type", GRAY, type.getLore()));
	}

	public static ItemStack createItem(Slot slot) {
		return MetaHelper.setMeta(Material.ENCHANTED_BOOK, GRAY + BOLD + slot.name, EQUIPMENT_SLOT_LORE);
	}

	public static ItemStack createItem(PotionEffectType type) {
		String name = type.getName().replace("_", " ");
		name = name.substring(0, 1) + name.substring(1).toLowerCase();
		return MetaHelper.hidePotionEffects(new ItemStack(Material.POTION), GRAY + BOLD + name, POTION_EFFECT_LORE);
	}

	public static ItemStack createItem(TileObjectType<?> type) {
		return MetaHelper.setMeta(Material.STONE, GRAY + BOLD + type.displayName, TILE_OBJECT_TYPE_LORE);
	}

	public static ItemStack createItem(Particle particle) {
		String name = particle.name().replace("_", " ");
		name = name.substring(0, 1) + name.substring(1).toLowerCase();
		return MetaHelper.setMeta(new ItemStack(Material.FIREWORK_STAR), GRAY + BOLD + name, PARTICLE_LORE);
	}

	public static ItemStack createItem(GameProfile profile) {
		return PlayerHead.create(profile, GRAY + BOLD + profile.getName(), GAME_PROFILE_LORE);
	}

	public static ItemStack createItem(AiostWorldType<?> worldType) {
		return MetaHelper.setMeta(Material.STONE, GRAY + BOLD + worldType.name, WORLD_TYPE_LORE);
	}

	public static ItemStack createItem(Environment environment) {
		return MetaHelper.setMeta(EnvironmentHelper.getMaterial(environment),
				GRAY + BOLD + EnvironmentHelper.getDisplayName(environment), ENVIRONMENT_LORE);
	}

	public static ItemStack createItem(UnlockableType<?> unlockableType) {
		return MetaHelper.hideAttributes(unlockableType.getDisplayItem().clone(), GRAY + BOLD + unlockableType.name,
				UNLOCKABLE_TYPE_LORE);
	}

	public static ItemStack createItem(Supplier<EventHandler> eventHandler) {
		return MetaHelper.setMeta(new ItemStack(Material.RED_BANNER),
				GRAY + BOLD + AiostRegistry.EVENT_HANDLER.getKey(eventHandler), EVENT_HANDLER_LORE);
	}

	public static ItemStack createItem(IParticle particle) {
		List<String> lore = new ArrayList<String>();
		lore.add(GRAY + "Click to choose this particle effect");
		lore.add(null);
		particle.createDescription(lore);
		return MetaHelper.setMeta(Material.FIREWORK_STAR, GRAY + BOLD + AiostRegistry.PARTICLES.getKey(particle), lore);
	}

	public static ItemStack createItem(Brush brush) {
		return MetaHelper.setMeta(new ItemStack(Material.ARROW), GRAY + BOLD + brush.name, BRUSH_LORE);
	}

	public static ItemStack createItem(BrushMode brush) {
		return MetaHelper.setMeta(new ItemStack(Material.GUNPOWDER), GRAY + BOLD + brush.name().toLowerCase(),
				BRUSH_MODE_LORE);
	}

	public static ItemStack createItem(Material type) {
		ItemStack is = new ItemStack(type);
		ItemMeta im = is.getItemMeta();
		if (im != null) {
			im.setLore(TYPE_LORE);
			is.setItemMeta(im);
		}
		return is;
	}
}
