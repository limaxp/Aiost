package com.pm.aiost.event.effect;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import com.pm.aiost.event.effect.effects.AuraEffect;
import com.pm.aiost.event.effect.effects.BlockOffhandEffect;
import com.pm.aiost.event.effect.effects.BreakBallEffect;
import com.pm.aiost.event.effect.effects.BreakBlockEffect;
import com.pm.aiost.event.effect.effects.BreakBlocksEffect;
import com.pm.aiost.event.effect.effects.BreakPlaneEffect;
import com.pm.aiost.event.effect.effects.BuildStaffEffect;
import com.pm.aiost.event.effect.effects.CancelEventEffect;
import com.pm.aiost.event.effect.effects.CastSpellEffect;
import com.pm.aiost.event.effect.effects.DamageAuraEffect;
import com.pm.aiost.event.effect.effects.DamageEffect;
import com.pm.aiost.event.effect.effects.ExtinguishFireEffect;
import com.pm.aiost.event.effect.effects.GraplingHookEffect;
import com.pm.aiost.event.effect.effects.HammerEffect;
import com.pm.aiost.event.effect.effects.HealEffect;
import com.pm.aiost.event.effect.effects.KillEffect;
import com.pm.aiost.event.effect.effects.LaunchArmorStandEffect;
import com.pm.aiost.event.effect.effects.LaunchBlockEffect;
import com.pm.aiost.event.effect.effects.LaunchItemEffect;
import com.pm.aiost.event.effect.effects.LaunchParticleEffect;
import com.pm.aiost.event.effect.effects.LaunchProjectileEffect;
import com.pm.aiost.event.effect.effects.LaunchTNTEffect;
import com.pm.aiost.event.effect.effects.LuckyBlockEffect;
import com.pm.aiost.event.effect.effects.ManaRegenEffect;
import com.pm.aiost.event.effect.effects.PlaceCustomBlockEffect;
import com.pm.aiost.event.effect.effects.PlaceFurnitureEffect;
import com.pm.aiost.event.effect.effects.PotionEffect;
import com.pm.aiost.event.effect.effects.SetArrowStatsEffect;
import com.pm.aiost.event.effect.effects.SetBlockOnFireEffect;
import com.pm.aiost.event.effect.effects.SetOnFireEffect;
import com.pm.aiost.event.effect.effects.SetOnWaterEffect;
import com.pm.aiost.event.effect.effects.SetProjectileStatsEffect;
import com.pm.aiost.event.effect.effects.ShootBlockUpEffect;
import com.pm.aiost.event.effect.effects.SpawnPointEffect;
import com.pm.aiost.event.effect.effects.StackEffect;
import com.pm.aiost.event.effect.effects.TeleportEffect;
import com.pm.aiost.event.effect.effects.TeleportToCursorEffect;
import com.pm.aiost.event.effect.effects.TeleportToLocEffect;
import com.pm.aiost.event.effect.effects.TeleportToWorldEffect;
import com.pm.aiost.event.effect.effects.ThrowArmorStandEffect;
import com.pm.aiost.event.effect.effects.ThrowBlockEffect;
import com.pm.aiost.event.effect.effects.ThrowItemEffect;
import com.pm.aiost.event.effect.effects.ThrowTNTEffect;
import com.pm.aiost.event.effect.effects.TimedAuraEffect;
import com.pm.aiost.event.effect.effects.TreeCapitatorEffect;
import com.pm.aiost.misc.registry.AiostRegistry;

public class EffectTypes {

	public static void init() {
		BlockOffhandEffect.init();
	}

	public static final EffectType<PotionEffect> POTION_EFFECT = a("PotionEffect", "Potion effect",
			Arrays.asList("Applies an potion effect"), PotionEffect::new);

	public static final EffectType<BreakBlockEffect> BREAK_BLOCK = a("BreakBlock", "Break block",
			Arrays.asList("Breaks a single block"), BreakBlockEffect::new);

	public static final EffectType<BreakBlocksEffect> BREAK_BLOCKS = a("BreakBlocks", "Break blocks",
			Arrays.asList("Breaks a cube of blocks with given radius"), BreakBlocksEffect::new);

	public static final EffectType<BreakBallEffect> BREAK_BALL = a("BreakBall", "Break ball",
			Arrays.asList("Breaks a ball of block with given radius"), BreakBallEffect::new);

	public static final EffectType<BreakPlaneEffect> BREAK_PLANE = a("BreakPlane", "Break plane",
			Arrays.asList("Breaks a plane of block with given radius"), BreakPlaneEffect::new);

	public static final EffectType<HammerEffect> HAMMER_EFFECT = a("HammerEffect", "Hammer effect",
			Arrays.asList("The effect a hammer uses when breaking a block"), HammerEffect::new);

	public static final EffectType<BuildStaffEffect> BUILD_STAFF_EFFECT = a("BuildStaffEffect", "Build staff effect",
			Arrays.asList("Helps with building"), BuildStaffEffect::new);

	public static final EffectType<SetProjectileStatsEffect> SET_PROJECTILE_STATS = a("SetProjectileStats",
			"Set projectile stats", Arrays.asList("Sets stats of a shoot projectile"), SetProjectileStatsEffect::new);

	public static final EffectType<SetArrowStatsEffect> SET_ARROW_STATS = a("SetArrowStats", "Set arrow stats",
			Arrays.asList("Sets stats of a shoot arrow"), SetArrowStatsEffect::new);

	public static final EffectType<LaunchProjectileEffect> LAUNCH_PROJECTILE = a("LaunchProjectile",
			"Launch projectile", Arrays.asList("Launches choosen projetile"), LaunchProjectileEffect::new);

	public static final EffectType<LaunchItemEffect> LAUNCH_ITEM = a("LaunchItem", "Launch item",
			Arrays.asList("Launches an given item"), LaunchItemEffect::new);

	public static final EffectType<LaunchArmorStandEffect> LAUNCH_ARMORSTAND = a("LaunchArmorStand",
			"Launch armorstand", Arrays.asList("Launches an armorstand with given item"), LaunchArmorStandEffect::new);

	public static final EffectType<LaunchTNTEffect> LAUNCH_TNT = a("LaunchTNT", "Launch tnt",
			Arrays.asList("Launches a tnt"), LaunchTNTEffect::new);

	public static final EffectType<LaunchParticleEffect> LAUNCH_PARTICLE = a("LaunchParticle", "Launch Particle",
			Arrays.asList("Launches a particle"), LaunchParticleEffect::new);

	public static final EffectType<LaunchBlockEffect> LAUNCH_BLOCK = a("LaunchBlock", "Launch block",
			Arrays.asList("Launches a block"), LaunchBlockEffect::new);

	public static final EffectType<ThrowItemEffect> THROW_ITEM = a("ThrowItem", "Throw item",
			Arrays.asList("Throws the item this effect is used on"), ThrowItemEffect::new);

	public static final EffectType<ThrowArmorStandEffect> THROW_ARMORSTAND = a("ThrowArmorStand", "Throw armorstand",
			Arrays.asList("Throws the item this effect is used on as an armorstand"), ThrowArmorStandEffect::new);

	public static final EffectType<ThrowTNTEffect> THROW_TNT = a("ThrowTNT", "Throw tnt",
			Arrays.asList("Throws the tnt this effect is used on"), ThrowTNTEffect::new);

	public static final EffectType<ThrowBlockEffect> THROW_BLOCK = a("ThrowBlock", "Throw block",
			Arrays.asList("Throws the block this effect is used on"), ThrowBlockEffect::new);

	public static final EffectType<CancelEventEffect> CANCEL_EVENT = a("CancelEvent", "Cancel event",
			Arrays.asList("Cancels the choosen events"), CancelEventEffect::new);

	public static final EffectType<BlockOffhandEffect> BLOCK_OFF_HAND = a("BlockOffhand", "Block off hand",
			Arrays.asList("The effect bihanders use to block the offhand slot"), BlockOffhandEffect::getInstance);

	public static final EffectType<StackEffect> STACK = a("Stack",
			Arrays.asList("Makes an unstackable item stackable to given size"), StackEffect::new);

	public static final EffectType<PlaceCustomBlockEffect> PLACE_CUSTOM_BLOCK = a("PlaceCustomBlock",
			"Place custom block", Arrays.asList("Places the item it is used on as a custom block"),
			PlaceCustomBlockEffect::new);

	public static final EffectType<PlaceFurnitureEffect> PLACE_FURNITURE = a("PlaceFurniture", "Place furniture",
			Arrays.asList("Places the item it is used on as a furniture"), PlaceFurnitureEffect::new);

	public static final EffectType<TeleportEffect> TELEPORT = a("Teleport",
			Arrays.asList("Teleports user some coordinates away"), TeleportEffect::new);

	public static final EffectType<TeleportToLocEffect> TELEPORT_TO_LOC = a("TeleportToLoc", "Teleport to location",
			Arrays.asList("Teleports user to given location"), TeleportToLocEffect::new);

	public static final EffectType<TeleportToWorldEffect> TELEPORT_TO_WORLD = a("TeleportToWorld", "Teleport to world",
			Arrays.asList("Teleports user to given world location"), TeleportToWorldEffect::new);

	public static final EffectType<TeleportToCursorEffect> TELEPORT_TO_CURSOR = a("TeleportToCursor",
			"Teleport to cursor", Arrays.asList("Teleports user to the block they are looking at"),
			TeleportToCursorEffect::new);

	public static final EffectType<CastSpellEffect> CAST_SPELL = a("CastSpell", "Cast spell",
			Arrays.asList("Casts the given Spell"), CastSpellEffect::new);

	public static final EffectType<AuraEffect> AURA = a("Aura",
			Arrays.asList("Does apply given effect to entities nearby"), AuraEffect::new);

	public static final EffectType<DamageAuraEffect> DAMAGE_AURA = a("DamageAura", "Damage aura",
			Arrays.asList("Does apply damage to entities nearby"), DamageAuraEffect::new);

	public static final EffectType<TimedAuraEffect> TIMED_AURA = a("TimedAura", "Timed aura",
			Arrays.asList("Does apply given effect to entities nearby for the given time"), TimedAuraEffect::new);

	public static final EffectType<ManaRegenEffect> MANA_REGEN = a("ManaRegeneration", "Mana regeneration",
			Arrays.asList("Regenerate users mana by the given anount"), ManaRegenEffect::new);

	public static final EffectType<HealEffect> HEAL = a("Heal", "Heal", Arrays.asList("Heals user by the given amount"),
			HealEffect::new);

	public static final EffectType<DamageEffect> DAMAGE = a("Damage", "Damage",
			Arrays.asList("Damages user by the given amount"), DamageEffect::new);

	public static final EffectType<KillEffect> KILL = a("Kill", "Kill", Arrays.asList("Kills user"), KillEffect::new);

	public static final EffectType<SpawnPointEffect> SPAWNPOINT = a("Spawnpoint", "Spawnpoint",
			Arrays.asList("Sets user spawnpoint to current location"), SpawnPointEffect::new);

	public static final EffectType<SetOnFireEffect> SET_ON_FIRE = a("SetOnFire", "Set on fire",
			Arrays.asList("Sets entity on fire"), SetOnFireEffect::new);

	public static final EffectType<SetBlockOnFireEffect> SET_BLOCK_ON_FIRE = a("SetBlockOnFire", "Set block on fire",
			Arrays.asList("Sets block on fire"), SetBlockOnFireEffect::new);

	public static final EffectType<ExtinguishFireEffect> EXTINGUISH_FIRE = a("ExtinguishFire", "Extinguish fire",
			Arrays.asList("Extinguishes fire"), ExtinguishFireEffect::new);

	public static final EffectType<SetOnWaterEffect> SET_ON_WATER = a("SetOnWater", "Set on water",
			Arrays.asList("Sets block on water"), SetOnWaterEffect::new);

	public static final EffectType<ShootBlockUpEffect> SHOOT_BLOCK_UP = a("ShootBlockUp", "Shoot block up",
			Arrays.asList("Shoots block up"), ShootBlockUpEffect::new);

	public static final EffectType<GraplingHookEffect> GRAPLING_HOOK = a("GraplingHook", "Grapling hook",
			Arrays.asList("It's a grappling hook ok?"), GraplingHookEffect::new);

	public static final EffectType<TreeCapitatorEffect> TREE_CAPITATOR = a("TreeCapitator", "Tree capitator",
			Arrays.asList("Completely breaks a a tree and its leaves"), TreeCapitatorEffect::getInstance);

	public static final EffectType<LuckyBlockEffect> LUCKY_BLOCK = a("LuckyBlock", "Lucky block",
			Arrays.asList("Triggers a lucky block effect"), LuckyBlockEffect::getInstance);

	public static <T extends Effect> EffectType<T> a(String name, Supplier<T> constructor) {
		return a(name, name, EffectType.EMPTY_LORE, constructor);
	}

	public static <T extends Effect> EffectType<T> a(String name, List<String> lore, Supplier<T> constructor) {
		return a(name, name, lore, constructor);
	}

	public static <T extends Effect> EffectType<T> a(String name, String displayName, Supplier<T> constructor) {
		return a(name, displayName, EffectType.EMPTY_LORE, constructor);
	}

	public static <T extends Effect> EffectType<T> a(String name, String displayName, List<String> lore,
			Supplier<T> constructor) {
		EffectType<T> type = new EffectType<T>(name, displayName, lore, constructor);
		AiostRegistry.EFFECTS.register(type.name, type);
		EffectRegistry.register(type.name, type.constructor);
		return type;
	}
}