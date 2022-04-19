package com.pm.aiost.misc.registry;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Supplier;

import com.mojang.authlib.GameProfile;
import com.pm.aiost.block.tileEntity.AiostTileEntityTypes;
import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.eventHandler.EventHandler;
import com.pm.aiost.event.eventHandler.EventHandlerRegistry;
import com.pm.aiost.game.GameType;
import com.pm.aiost.game.GameTypes;
import com.pm.aiost.item.spell.Spell;
import com.pm.aiost.item.spell.Spells;
import com.pm.aiost.misc.packet.entity.PacketEntityType;
import com.pm.aiost.misc.packet.entity.PacketEntityTypes;
import com.pm.aiost.misc.packet.object.PacketObjectType;
import com.pm.aiost.misc.packet.object.PacketObjectTypes;
import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.particleEffect.particle.ParticleType;
import com.pm.aiost.misc.particleEffect.particle.ParticleTypes;
import com.pm.aiost.player.unlockable.UnlockableType;
import com.pm.aiost.player.unlockable.UnlockableTypes;
import com.pm.aiost.server.world.object.tileObject.TileObjectType;
import com.pm.aiost.server.world.object.tileObject.TileObjectTypes;
import com.pm.aiost.server.world.type.AiostWorldType;
import com.pm.aiost.server.world.type.AiostWorldTypes;

public abstract class AiostRegistry<T> {

	public static final AiostRegistry<Supplier<EventHandler>> EVENT_HANDLER = new BaseRegistry<Supplier<EventHandler>>(
			"EventHandler");

	public static final AiostRegistry<GameType<?>> GAMES = new BaseRegistry<GameType<?>>("Game");

	public static final AiostRegistry<UnlockableType<?>> UNLOCKABLE_TYPES = new BaseRegistry<UnlockableType<?>>(
			"UnlockableType");

	public static final AiostRegistry<PacketObjectType<?>> PACKET_OBJECTS = new BaseRegistry<PacketObjectType<?>>(
			"PacketObject");

	public static final AiostRegistry<PacketEntityType<?>> PACKET_ENTITIES = new BaseRegistry<PacketEntityType<?>>(
			"PacketEntity");

	public static final AiostRegistry<TileObjectType<?>> TILE_OBJECTS = new BaseRegistry<TileObjectType<?>>(
			"TileObject");

	public static final AiostRegistry<EffectType<?>> EFFECTS = new BaseRegistry<EffectType<?>>("Effect");

	public static final AiostRegistry<Spell> SPELLS = new BaseRegistry<Spell>("Spell");

	public static final AiostRegistry<ParticleType<?>> PARTICLE_TYPES = new BaseRegistry<ParticleType<?>>(
			"ParticleType");

	public static final AiostRegistry<IParticle> PARTICLES = new BaseRegistry<IParticle>("Particle");

	public static final AiostRegistry<GameProfile> PROFILES = new GenIdRegistry<GameProfile>("Profile");

	public static final AiostRegistry<AiostWorldType<?>> WORLD_TYPES = new BaseRegistry<AiostWorldType<?>>("WorldType");

	@SuppressWarnings("unused")
	public static void init() {
		AiostEntityTypes.init();
		AiostTileEntityTypes.init();
		EventHandlerRegistry.init();
		GameType<?> gameType = GameTypes.SPLEEF;
		UnlockableType<?> unlockableType = UnlockableTypes.HATS;
		Spell spell = Spells.FLAME;
		ParticleType<?> particleType = ParticleTypes.BEAM;
		EffectType<?> effectType = EffectTypes.POTION_EFFECT;
		PacketObjectType<?> packetObjectType = PacketObjectTypes.FURNITURE;
		PacketEntityType<?> packetEntityType = PacketEntityTypes.ENTITY_FURNITURE;
		TileObjectType<?> tileObjectType = TileObjectTypes.BLINKING_BLOCK;
		AiostWorldType<?> worldType = AiostWorldTypes.DEFAULT;
	}

	public static void terminate() {
		AiostEntityTypes.terminate();
		AiostTileEntityTypes.terminate();
	}

	public final String name;

	public AiostRegistry(String name) {
		this.name = name;
	}

	public abstract int register(String key, T value);

	public abstract boolean register(int id, String key, T value);

	public abstract T get(String key);

	public abstract T getOrDefault(String key, T defaultValue);

	public abstract T get(int id);

	public abstract T getOrDefault(int id, T defaultValue);

	public abstract T getByIndex(int index);

	public abstract String getKey(T value);

	public abstract Collection<T> values();

	public abstract Iterator<T> iterator();

	public abstract int size();
}
