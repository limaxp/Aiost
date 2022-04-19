package com.pm.aiost.item.spell;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.LivingEntity;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.projectile.CustomProjectile;
import com.pm.aiost.entity.projectile.projectiles.ParticleProjectile;
import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectAction;
import com.pm.aiost.event.effect.EffectCondition;
import com.pm.aiost.event.effect.effects.AuraEffect;
import com.pm.aiost.event.effect.effects.HealEffect;
import com.pm.aiost.event.effect.effects.SetBlockOnFireEffect;
import com.pm.aiost.event.effect.effects.SetOnWaterEffect;
import com.pm.aiost.event.effect.effects.ShootBlockUpEffect;
import com.pm.aiost.item.spell.spells.DisguiseSpell;
import com.pm.aiost.item.spell.spells.EffectSpell;
import com.pm.aiost.item.spell.spells.HealSpell;
import com.pm.aiost.item.spell.spells.ProjectileSpell;
import com.pm.aiost.item.spell.spells.SummonEntitySpell;
import com.pm.aiost.misc.packet.disguise.disguises.DisguiseEntityLiving;
import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.particleEffect.particle.particles.DataParticle;
import com.pm.aiost.misc.particleEffect.particle.particles.Particle;
import com.pm.aiost.misc.registry.AiostRegistry;

public class Spells {

	private static final Effect SET_BLOCK_ON_FIRE_EFFECT = new SetBlockOnFireEffect(new byte[] { EffectAction.TICK },
			EffectCondition.NONE);

	private static final Effect SET_BLOCK_ON_WATER_EFFECT = new SetOnWaterEffect(new byte[] { EffectAction.TICK },
			EffectCondition.NONE);

	private static final Effect SHOOT_UP_BLOCK_EFFECT = new ShootBlockUpEffect(new byte[] { EffectAction.TICK },
			EffectCondition.NONE, 1.0);

	private static final Effect HEAL_EFFECT = new HealEffect(new byte[] { EffectAction.TICK }, EffectCondition.NONE,
			10.0);

	public static final ProjectileSpell FLAME = a(
			new ProjectileSpell("Flame", 10, 2.0, 2.0F, Sound.ITEM_FIRECHARGE_USE) {

				@Override
				public CustomProjectile createProjectile(LivingEntity entity) {
					ParticleProjectile projectile = new ParticleProjectile(entity, FLAME_PARTICLE);
					projectile.setEffect(SET_BLOCK_ON_FIRE_EFFECT);
					projectile.setDamage(3.0F);
					return projectile;
				}
			});

	public static final ProjectileSpell FIRE_BALL = a(
			new ProjectileSpell("Fireball", 20, 6.0, 1.0F, Sound.ITEM_FIRECHARGE_USE) {

				@Override
				public CustomProjectile createProjectile(LivingEntity entity) {
					ParticleProjectile projectile = new ParticleProjectile(entity, FIRE_BALL_PARTICLE);
					projectile.setEffect(SET_BLOCK_ON_FIRE_EFFECT);
					projectile.setDamage(8.0F);
					projectile.setKnockback(2.0F);
					projectile.setDuration(60);
					return projectile;
				}
			});

	public static final EffectSpell FIRE_AURA = a(new EffectSpell("FireAura", "Fire aura", 800, 12.0, 400,
			new AuraEffect(new Particle(org.bukkit.Particle.FLAME, 50, 5, 0, false), SET_BLOCK_ON_FIRE_EFFECT, 5)));

	public static final ProjectileSpell WATER_BEAM = a(
			new ProjectileSpell("WaterBeam", "Water beam", 10, 2.0, 2.0F, Sound.ENTITY_PLAYER_SPLASH) {

				@Override
				public CustomProjectile createProjectile(LivingEntity entity) {
					ParticleProjectile projectile = new ParticleProjectile(entity, WATER_BEAM_PARTICLE);
					projectile.setEffect(SET_BLOCK_ON_WATER_EFFECT);
					projectile.setDamage(3.0F);
					return projectile;
				}
			});

	public static final ProjectileSpell FOUNTAIN = a(
			new ProjectileSpell("Fountain", 20, 6.0, 1.0F, Sound.ENTITY_PLAYER_SPLASH) {

				@Override
				public CustomProjectile createProjectile(LivingEntity entity) {
					ParticleProjectile projectile = new ParticleProjectile(entity, FOUNTAIN_PARTICLE);
					projectile.setEffect(SET_BLOCK_ON_WATER_EFFECT);
					projectile.setDamage(8.0F);
					projectile.setKnockback(2.0F);
					projectile.setDuration(60);
					return projectile;
				}
			});

	public static final EffectSpell WATER_AURA = a(
			new EffectSpell("WaterAura", "Water aura", 800, 12.0, 400, new AuraEffect(
					new Particle(org.bukkit.Particle.WATER_WAKE, 100, 5, 0, false), SET_BLOCK_ON_WATER_EFFECT, 5)));

	public static final ProjectileSpell EARTH_BEAM = a(
			new ProjectileSpell("EarthBeam", "Earth beam", 10, 2.0, 2.0F, Sound.BLOCK_GRASS_BREAK) {

				@Override
				public CustomProjectile createProjectile(LivingEntity entity) {
					ParticleProjectile projectile = new ParticleProjectile(entity, EARTH_BEAM_PARTICLE);
					projectile.setEffect(SHOOT_UP_BLOCK_EFFECT);
					projectile.setDamage(3.0F);
					return projectile;
				}
			});

	public static final ProjectileSpell EARTH_BALL = a(
			new ProjectileSpell("Earthball", 20, 6.0, 1.0F, Sound.BLOCK_GRASS_BREAK) {

				@Override
				public CustomProjectile createProjectile(LivingEntity entity) {
					ParticleProjectile projectile = new ParticleProjectile(entity, EARTH_BALL_PARTICLE);
					projectile.setEffect(SHOOT_UP_BLOCK_EFFECT);
					projectile.setDamage(8.0F);
					projectile.setKnockback(2.0F);
					projectile.setDuration(60);
					return projectile;
				}
			});

	public static final EffectSpell EARTH_AURA = a(new EffectSpell("EarthAura", "Earth aura", 800, 12.0, 500,
			new AuraEffect(new DataParticle<BlockData>(org.bukkit.Particle.BLOCK_DUST, 50, 5, 0, false,
					Bukkit.createBlockData(Material.DIRT)), SHOOT_UP_BLOCK_EFFECT, 5)));

	public static final ProjectileSpell WIND_BLOW = a(
			new ProjectileSpell("WindBlow", "Wind blow", 10, 2.0, 2.0F, Sound.ENTITY_LLAMA_SPIT) {

				@Override
				public CustomProjectile createProjectile(LivingEntity entity) {
					ParticleProjectile projectile = new ParticleProjectile(entity, WIND_BLOW_PARTICLE);
					projectile.setDamage(3.0F);
					projectile.setKnockback(3.0F);
					return projectile;
				}
			});

	public static final ProjectileSpell WIND_SHOT = a(
			new ProjectileSpell("WindShot", "Wind shot", 20, 6.0, 1.0F, Sound.ENTITY_LLAMA_SPIT) {

				@Override
				public CustomProjectile createProjectile(LivingEntity entity) {
					ParticleProjectile projectile = new ParticleProjectile(entity, WIND_SHOT_PARTICLE);
					projectile.setDamage(8.0F);
					projectile.setKnockback(4.0F);
					projectile.setDuration(60);
					return projectile;
				}
			});

	// TODO: make wind aura effect

	public static final EffectSpell WIND_AURA = a(new EffectSpell("WindAura", "Wind aura", 800, 12.0, 500,
			new AuraEffect(new Particle(org.bukkit.Particle.CLOUD, 50, 5, 0, false), SHOOT_UP_BLOCK_EFFECT, 5)));

	// TODO: ice spells?

	public static final HealSpell HEAL = a(new HealSpell("Heal", 400, 10.0, 10.0));

	public static final ProjectileSpell HEAL_OTHER = a(
			new ProjectileSpell("HealOther", "Heal other", 20, 6.0, 1.0F, Sound.BLOCK_CONDUIT_ACTIVATE) {

				@Override
				public CustomProjectile createProjectile(LivingEntity entity) {
					ParticleProjectile projectile = new ParticleProjectile(entity, HEAL_OTHER_PARTICLE);
					projectile.setEffect(HEAL_EFFECT);
					projectile.setDamage(0.0F);
					projectile.setDuration(60);
					return projectile;
				}
			});

	public static final EffectSpell HEAL_AURA = a(new EffectSpell("HealAura", "Heal aura", 800, 20.0, 400,
			new AuraEffect(new Particle(org.bukkit.Particle.VILLAGER_HAPPY, 100, 5, 0, false),
					new HealEffect(new byte[] { EffectAction.TICK }, EffectCondition.NONE, 2.0), 5)));

	public static final SummonEntitySpell SUMMON_ZOMBIE = a(
			new SummonEntitySpell("SummonZombie", "Summon Zombie", 20, 8, AiostEntityTypes.OWNABLE_ZOMBIE, 1200));

	public static final SummonEntitySpell SUMMON_SKELETON = a(new SummonEntitySpell("SummonSkeleton", "Summon Skeleton",
			25, 10, AiostEntityTypes.OWNABLE_SKELETON, 1200));

	public static final SummonEntitySpell SUMMON_WITHER_SKELETON = a(new SummonEntitySpell("SummonWitherSkeleton",
			"Summon Wither Skeleton", 30, 14, AiostEntityTypes.OWNABLE_WITHER_SKELETON, 1200));

	public static final SummonEntitySpell SUMMON_ENDERMAN = a(new SummonEntitySpell("SummonEnderman", "Summon Enderman",
			30, 14, AiostEntityTypes.OWNABLE_ENDERMAN, 1200));

	// TODO: Conjured Weapons?

	public static final DisguiseSpell TRANSFORM_TO_ZOMBIE = a(new DisguiseSpell("TransformToZombie",
			"Transform to Zombie", 25, 10.0, new DisguiseEntityLiving(AiostEntityTypes.ZOMBIE), 800));

	// TODO: Touch spells

	// TODO: Wave spells

	// TODO: Spells with aoe damage

	public static <T extends Spell> T a(T spell) {
		AiostRegistry.SPELLS.register(spell.getName(), spell);
		return spell;
	}

	private static final IParticle FLAME_PARTICLE = new Particle(org.bukkit.Particle.FLAME, 3, 0.1F, 0, false);

	private static final IParticle FIRE_BALL_PARTICLE = new Particle(org.bukkit.Particle.FLAME, 10, 0.1F, 0, false);

	private static final IParticle WATER_BEAM_PARTICLE = new Particle(org.bukkit.Particle.WATER_WAKE, 5, 0.1F, 0,
			false);

	private static final IParticle FOUNTAIN_PARTICLE = new Particle(org.bukkit.Particle.WATER_WAKE, 20, 0.1F, 0, false);

	private static final IParticle EARTH_BEAM_PARTICLE = new DataParticle<BlockData>(org.bukkit.Particle.BLOCK_DUST, 3,
			0.1F, 0, false, Bukkit.createBlockData(Material.DIRT));

	private static final IParticle EARTH_BALL_PARTICLE = new DataParticle<BlockData>(org.bukkit.Particle.BLOCK_DUST, 10,
			0.1F, 0, false, Bukkit.createBlockData(Material.DIRT));

	private static final IParticle WIND_BLOW_PARTICLE = new Particle(org.bukkit.Particle.CLOUD, 3, 0.1F, 0, false);

	private static final IParticle WIND_SHOT_PARTICLE = new Particle(org.bukkit.Particle.CLOUD, 10, 0.1F, 0, false);

	private static final IParticle HEAL_OTHER_PARTICLE = new Particle(org.bukkit.Particle.VILLAGER_HAPPY, 5, 0.1F, 0,
			false);

}
