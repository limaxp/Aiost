package com.pm.aiost.player.unlockable;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.collection.list.UnorderedIdentityArrayList;
import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.menu.menus.PlayerSkillMenu;
import com.pm.aiost.misc.menu.menus.UnlockableMenu;
import com.pm.aiost.misc.packet.disguise.Disguise;
import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.registry.AiostRegistry;
import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.player.settings.PlayerSettings;

import net.minecraft.server.v1_15_R1.EntityTypes;

public class UnlockableTypes {

	private static final List<UnlockableType<?>> INITIALIZEABLE_TYPES = new UnorderedIdentityArrayList<UnlockableType<?>>();

	public static final UnlockableType<IParticle> TRAILS = new UnlockableType<IParticle>("Trails") {

		@Override
		public void load(ConfigurationSection section) {
			UnlockableManager.loadParticles(section, this, new ItemStack(Material.LEATHER_BOOTS));
		}

		@Override
		public void init(ServerPlayer serverPlayer) {
			int current = serverPlayer.getSetting(PlayerSettings.TRAIL);
			if (current > 0)
				serverPlayer.addParticle(getObject(current));
		}

		@Override
		public void set(ServerPlayer serverPlayer, short id) {
			int current = serverPlayer.getSetting(PlayerSettings.TRAIL);
			if (current > 0)
				serverPlayer.removeParticle(getObject(current));
			serverPlayer.addParticle(getObject(id));
			serverPlayer.setSetting(PlayerSettings.TRAIL, id);
		}

		@Override
		public void remove(ServerPlayer serverPlayer, short id) {
			int current = serverPlayer.getSetting(PlayerSettings.TRAIL);
			if (current < 1)
				return;
			serverPlayer.removeParticle(getObject(current));
			serverPlayer.setSetting(PlayerSettings.TRAIL, (short) 0);
		}

		@Override
		public short get(ServerPlayer serverPlayer) {
			return serverPlayer.getSetting(PlayerSettings.TRAIL);
		}
	};

	public static final UnlockableType<IParticle> PARTICLE_EFFECTS = new UnlockableType<IParticle>("Particle Effects") {

		@Override
		public void load(ConfigurationSection section) {
			UnlockableManager.loadParticles(section, this, new ItemStack(Material.FIREWORK_STAR));
		}

		@Override
		public void init(ServerPlayer serverPlayer) {
			int current = serverPlayer.getSetting(PlayerSettings.PARTICLE_EFFECT);
			if (current > 0)
				serverPlayer.addParticle(getObject(current));
		}

		@Override
		public void set(ServerPlayer serverPlayer, short id) {
			int current = serverPlayer.getSetting(PlayerSettings.PARTICLE_EFFECT);
			if (current > 0)
				serverPlayer.removeParticle(getObject(current));
			serverPlayer.addParticle(getObject(id));
			serverPlayer.setSetting(PlayerSettings.PARTICLE_EFFECT, id);
		}

		@Override
		public void remove(ServerPlayer serverPlayer, short id) {
			int current = serverPlayer.getSetting(PlayerSettings.PARTICLE_EFFECT);
			if (current < 1)
				return;
			serverPlayer.removeParticle(getObject(current));
			serverPlayer.setSetting(PlayerSettings.PARTICLE_EFFECT, (short) 0);
		}

		@Override
		public short get(ServerPlayer serverPlayer) {
			return serverPlayer.getSetting(PlayerSettings.PARTICLE_EFFECT);
		}
	};

	public static final UnlockableType<IParticle> PROJECTILE_PARTICLE = new UnlockableType<IParticle>(
			"Projectile Particle") {

		@Override
		public void load(ConfigurationSection section) {
			UnlockableManager.loadParticles(section, this, new ItemStack(Material.ARROW));
		}

		@Override
		public void init(ServerPlayer serverPlayer) {
		}

		@Override
		public void set(ServerPlayer serverPlayer, short id) {
			serverPlayer.setSetting(PlayerSettings.PROJECTILE_PARTICLE, id);
		}

		@Override
		public void remove(ServerPlayer serverPlayer, short id) {
			serverPlayer.setSetting(PlayerSettings.PROJECTILE_PARTICLE, (short) 0);
		}

		@Override
		public short get(ServerPlayer serverPlayer) {
			return serverPlayer.getSetting(PlayerSettings.PROJECTILE_PARTICLE);
		}

		@Override
		public boolean initializePlayer() {
			return false;
		};
	};

	public static final UnlockableType<Disguise> MORPHS = new UnlockableType<Disguise>("Morphs") {

		@Override
		public void load(ConfigurationSection section) {
			UnlockableManager.loadMorphs(section, this, new ItemStack(Material.SKELETON_SPAWN_EGG));
		}

		@Override
		public void init(ServerPlayer serverPlayer) {
			// TODO: this doesnt work! probably must be set different or later
			int current = serverPlayer.getSetting(PlayerSettings.MORPH);
			if (current > 0)
				serverPlayer.setDefaultDisguise(getObject(current));
		}

		@Override
		public void set(ServerPlayer serverPlayer, short id) {
			serverPlayer.setDefaultDisguise(getObject(id));
			serverPlayer.setSetting(PlayerSettings.MORPH, id);
		}

		@Override
		public void remove(ServerPlayer serverPlayer, short id) {
			serverPlayer.removeDefaultDisguise();
			serverPlayer.setSetting(PlayerSettings.MORPH, (short) 0);
		}

		@Override
		public short get(ServerPlayer serverPlayer) {
			return serverPlayer.getSetting(PlayerSettings.MORPH);
		}
	};

	public static final UnlockableType<EntityTypes<?>> PETS = new UnlockableType<EntityTypes<?>>("Pets") {

		@Override
		public void load(ConfigurationSection section) {
			UnlockableManager.loadEntityTypes(section, this, new ItemStack(Material.CHICKEN_SPAWN_EGG));
		}

		@Override
		public void init(ServerPlayer serverPlayer) {
			int current = serverPlayer.getSetting(PlayerSettings.PET);
			if (current > 0)
				serverPlayer.spawnPet(current);
		}

		@Override
		public void set(ServerPlayer serverPlayer, short id) {
			serverPlayer.spawnPet(id);
			serverPlayer.setSetting(PlayerSettings.PET, id);
		}

		@Override
		public void remove(ServerPlayer serverPlayer, short id) {
			if (serverPlayer.hasPet()) {
				serverPlayer.despawnPet();
				serverPlayer.setSetting(PlayerSettings.PET, (short) 0);
			}
		}

		@Override
		public short get(ServerPlayer serverPlayer) {
			return serverPlayer.getSetting(PlayerSettings.PET);
		}
	};

	// TODO
	public static final UnlockableType<ItemStack> HATS = new UnlockableType<ItemStack>("Hats") {

		@Override
		public void load(ConfigurationSection section) {
			UnlockableManager.loadItemStacks(section, this, new ItemStack(Material.GOLDEN_HELMET));
		}

		@Override
		public void init(ServerPlayer serverPlayer) {
		}

		@Override
		public void set(ServerPlayer serverPlayer, short id) {
			serverPlayer.setHelmet(getObject(id));
			serverPlayer.setSetting(PlayerSettings.HAT, id);
		}

		@Override
		public void remove(ServerPlayer serverPlayer, short id) {
			int current = serverPlayer.getSetting(PlayerSettings.HAT);
			if (current < 1)
				return;
			serverPlayer.setHelmet(null);
			serverPlayer.setSetting(PlayerSettings.HAT, (short) 0);
		}

		@Override
		public short get(ServerPlayer serverPlayer) {
			return serverPlayer.getSetting(PlayerSettings.HAT);
		}

		@Override
		public boolean initializePlayer() {
			return false;
		};
	};

	// TODO
	public static final UnlockableType<ItemStack> SUITS = new UnlockableType<ItemStack>("Suits") {

		@Override
		public void load(ConfigurationSection section) {
			UnlockableManager.loadItemStacks(section, this, new ItemStack(Material.IRON_CHESTPLATE));
		}

		@Override
		public void init(ServerPlayer serverPlayer) {
		}

		@Override
		public void set(ServerPlayer serverPlayer, short id) {
			ItemStack is = getObject(id);
			Object item = NMS.getNMS(is).getItem();
			if (NMS.isArmor(item)) {
				System.out.println(NMS.getArmorSlot(item));
				serverPlayer.setItem(NMS.getArmorSlot(item), is);
			}
		}

		@Override
		public void remove(ServerPlayer serverPlayer, short id) {
			int current = serverPlayer.getSetting(PlayerSettings.HAT);
			if (current < 1)
				return;
			serverPlayer.setHelmet(null);
			serverPlayer.setSetting(PlayerSettings.HAT, (short) 0);
		}

		@Override
		public short get(ServerPlayer serverPlayer) {
			return serverPlayer.getSetting(PlayerSettings.HAT);
		}

		@Override
		public boolean initializePlayer() {
			return false;
		};
	};

	public static final UnlockableType<Effect> SKILLS = new UnlockableType<Effect>("Skills") {

		@Override
		public void load(ConfigurationSection section) {
			UnlockableManager.loadEffects(section, this, new ItemStack(Material.NETHER_STAR));
		}

		@Override
		public void init(ServerPlayer serverPlayer) {
		}

		@Override
		public void set(ServerPlayer serverPlayer, short id) {
			// TODO
			System.out.println(getObject(id).getName());
			serverPlayer.addEffect(getObject(id));
		}

		@Override
		public void remove(ServerPlayer serverPlayer, short id) {
			System.out.println(getObject(id).getName());
			serverPlayer.removeEffect(getObject(id));
		}

		@Override
		public short get(ServerPlayer serverPlayer) {
			return -1;
		}

		@Override
		public UnlockableMenu createMenu(ServerPlayer serverPlayer) {
			return new PlayerSkillMenu(serverPlayer, this);
		};

		@Override
		public boolean initializePlayer() {
			return false;
		};
	};

	static int add(UnlockableType<?> type) {
		int id = AiostRegistry.UNLOCKABLE_TYPES.register(type.name, type);
		if (AiostRegistry.GAMES.get(id) != null)
			Logger.warn("Unlockable name collision! '" + type.name + "' has collision with game '"
					+ AiostRegistry.GAMES.get(id).name + "'");
		if (type.initializePlayer())
			addInitializeAble(type);
		return id;
	}

	private static void addInitializeAble(UnlockableType<?> type) {
		if (!INITIALIZEABLE_TYPES.contains(type))
			INITIALIZEABLE_TYPES.add(type);
	}

	public static void init(ServerPlayer serverPlayer) {
		for (UnlockableType<?> type : INITIALIZEABLE_TYPES)
			type.init(serverPlayer);
	}
}
