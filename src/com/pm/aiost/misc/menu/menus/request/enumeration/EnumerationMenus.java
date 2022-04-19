package com.pm.aiost.misc.menu.menus.request.enumeration;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World.Environment;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;

import com.mojang.authlib.GameProfile;
import com.pm.aiost.block.BlockMaterial;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.eventHandler.EventHandler;
import com.pm.aiost.event.eventHandler.EventHandlerRegistry;
import com.pm.aiost.game.GameType;
import com.pm.aiost.item.custom.Slot;
import com.pm.aiost.item.spell.Spell;
import com.pm.aiost.misc.menu.inventoryMenu.InventoryMenu;
import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.particleEffect.particle.ParticleType;
import com.pm.aiost.misc.registry.AiostRegistry;
import com.pm.aiost.misc.utils.nbt.GenericAttribute;
import com.pm.aiost.misc.utils.worldEdit.Brush;
import com.pm.aiost.misc.utils.worldEdit.Brush.BrushMode;
import com.pm.aiost.player.unlockable.UnlockableType;
import com.pm.aiost.server.world.object.tileObject.TileObjectType;
import com.pm.aiost.server.world.type.AiostWorldType;

import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.IRegistry;

public class EnumerationMenus {

	public static final InventoryMenu ENCHANTMENT_MENU = new EnumerationMenu<Enchantment>(BOLD + "Choose enchantment",
			Enchantment.values(), EnumerationMenu::createItem);

	public static final InventoryMenu EFFECT_TYPE_MENU = new EnumerationMenu<EffectType<?>>(BOLD + "Choose Effect",
			AiostRegistry.EFFECTS.size(), AiostRegistry.EFFECTS::getByIndex, EnumerationMenu::createItem);

	public static final InventoryMenu ENTITY_TYPE_MENU = new EnumerationMenu<EntityTypes<?>>(
			BOLD + "Choose entity type", iteratorToList(IRegistry.ENTITY_TYPE.iterator()), EnumerationMenu::createItem);

	public static final InventoryMenu PARTICLE_TYPE_MENU = new EnumerationMenu<ParticleType<?>>(
			BOLD + "Choose particle type", AiostRegistry.PARTICLE_TYPES.size(),
			AiostRegistry.PARTICLE_TYPES::getByIndex, EnumerationMenu::createItem);

	public static final InventoryMenu SPELL_MENU = new EnumerationMenu<Spell>(BOLD + "Choose spell",
			AiostRegistry.SPELLS.size(), AiostRegistry.SPELLS::getByIndex, EnumerationMenu::createItem);

	public static final InventoryMenu GAME_TYPE_MENU = new EnumerationMenu<GameType<?>>(BOLD + "Choose game type",
			AiostRegistry.GAMES.size(), AiostRegistry.GAMES::getByIndex, EnumerationMenu::createItem);

	public static final InventoryMenu EQUIPMENT_SLOT_MENU = new EnumerationMenu<Slot>(BOLD + "Choose slot",
			Slot.values(), EnumerationMenu::createItem);

	public static final InventoryMenu POTION_EFFECT_TYPE_MENU = new EnumerationMenu<PotionEffectType>(
			BOLD + "Choose potion effect", PotionEffectType.values(), EnumerationMenu::createItem);

	public static final InventoryMenu TILE_OBJECT_TYPE_MENU = new EnumerationMenu<TileObjectType<?>>(
			BOLD + "Choose block effect", AiostRegistry.TILE_OBJECTS.size(), AiostRegistry.TILE_OBJECTS::get,
			EnumerationMenu::createItem);

	public static final InventoryMenu PARTICLE_MENU = new EnumerationMenu<Particle>(BOLD + "Choose particle",
			Particle.values(), EnumerationMenu::createItem);

	public static final InventoryMenu GENERIC_ATTRIBUTE_MENU = new EnumerationMenu<GenericAttribute>(
			BOLD + "Choose attribute", GenericAttribute::get, GenericAttribute.getItems());

	public static final InventoryMenu GAME_RPOFILES_MENU = new EnumerationMenu<GameProfile>(BOLD + "Choose profile",
			AiostRegistry.PROFILES.size(), AiostRegistry.PROFILES::getByIndex, EnumerationMenu::createItem);

	public static final InventoryMenu WORLD_TYPE_MENU = new EnumerationMenu<AiostWorldType<?>>(
			BOLD + "Choose world type", AiostRegistry.WORLD_TYPES.size(), AiostRegistry.WORLD_TYPES::getByIndex,
			EnumerationMenu::createItem);

	public static final InventoryMenu ENVIRONMENT_MENU = new EnumerationMenu<Environment>(BOLD + "Choose environment",
			Environment.values(), EnumerationMenu::createItem);

	public static final InventoryMenu UNLOCKABLE_TYPE_MENU = new EnumerationMenu<UnlockableType<?>>(
			BOLD + "Choose unlockable type", AiostRegistry.UNLOCKABLE_TYPES.size(),
			AiostRegistry.UNLOCKABLE_TYPES::getByIndex, EnumerationMenu::createItem);

	public static final InventoryMenu EVENT_HANDLER_MENU = new EnumerationMenu<Supplier<EventHandler>>(
			BOLD + "Choose event handler", AiostRegistry.EVENT_HANDLER.size(), AiostRegistry.EVENT_HANDLER::getByIndex,
			EnumerationMenu::createItem);

	public static final InventoryMenu PARTICLE_EFFECT_MENU = new EnumerationMenu<IParticle>(
			BOLD + "Choose particle effect", AiostRegistry.PARTICLES.size(), AiostRegistry.PARTICLES::getByIndex,
			EnumerationMenu::createItem);

	public static final InventoryMenu REGION_EVENT_HANDLER_MENU = new EnumerationMenu<Supplier<EventHandler>>(
			BOLD + "Choose type", EventHandlerRegistry.getRegionEventHandler(), EnumerationMenu::createItem);

	public static final InventoryMenu BRUSH_MENU = new EnumerationMenu<Brush>(BOLD + "Choose brush", Brush.values(),
			EnumerationMenu::createItem);

	public static final InventoryMenu BRUSH_MODE_MENU = new EnumerationMenu<BrushMode>(BOLD + "Choose mode",
			BrushMode.values(), EnumerationMenu::createItem);

	public static final InventoryMenu BLOCK_MENU = new EnumerationMenu<Material>(BOLD + "Choose block",
			BlockMaterial.size(), BlockMaterial::get, EnumerationMenu::createItem);

	public static final InventoryMenu PROJECTILE_CLASS_MENU = ProjectileClassMenu.MENU;

	private static <T> List<T> iteratorToList(Iterator<T> iterator) {
		List<T> list = new ArrayList<T>();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		return list;
	}
}
