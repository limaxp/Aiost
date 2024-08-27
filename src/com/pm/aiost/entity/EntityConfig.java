package com.pm.aiost.entity;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.misc.nms.NMS;
import com.pm.aiost.misc.packet.disguise.DisguiseManager;
import com.pm.aiost.misc.packet.disguise.disguises.DisguiseEntityLiving;
import com.pm.aiost.misc.packet.disguise.disguises.DisguisePlayer;
import com.pm.aiost.misc.profile.Profiles;
import com.pm.aiost.misc.registry.AiostRegistry;

import net.minecraft.world.entity.EntityType;

public class EntityConfig {

	public static final EntityConfig CHICKEN_HOSTILE = register("chicken_hostile", EntityType.WOLF, (e) -> {
		((Wolf) e).setAngry(true);
		DisguiseManager.setDisguise((LivingEntity) e, new DisguiseEntityLiving(AiostEntityTypes.CHICKEN));
	});

	public static final EntityConfig CREEPER_FAKE = register("creeper_fake", EntityType.CHICKEN, (e) -> {
		DisguiseManager.setDisguise((LivingEntity) e, new DisguiseEntityLiving(AiostEntityTypes.CREEPER));
	});

	public static final EntityConfig CREEPER_MELEE = register("creeper_melee", EntityType.SKELETON, (e) -> {
		((LivingEntity) e).getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
		DisguiseManager.setDisguise((LivingEntity) e, new DisguiseEntityLiving(AiostEntityTypes.CREEPER));
	});

	public static final EntityConfig WITCH_MELEE = register("witch_melee", EntityType.SKELETON, (e) -> {
		((LivingEntity) e).getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
		DisguiseManager.setDisguise((LivingEntity) e, new DisguiseEntityLiving(AiostEntityTypes.WITCH));
	});

	public static final EntityConfig BLAZE_MELEE = register("blaze_melee", EntityType.SKELETON, (e) -> {
		((LivingEntity) e).getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
		DisguiseManager.setDisguise((LivingEntity) e, new DisguiseEntityLiving(AiostEntityTypes.BLAZE));
	});

	public static final EntityConfig SKELETON_MELEE = register("skeleton_melee", EntityType.SKELETON, (e) -> {
		((LivingEntity) e).getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
	});

	public static final EntityConfig ORC = register("orc", EntityType.SKELETON, (e) -> {
		((LivingEntity) e).getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
		DisguiseManager.setDisguise((LivingEntity) e, new DisguisePlayer(Profiles.ORC));
	});

	private static final Map<EntityType<?>, EntityConfig> WRAPPED = new IdentityHashMap<EntityType<?>, EntityConfig>(
			AiostEntityTypes.size());

	public static EntityConfig register(String name, EntityType<?> type, Consumer<Entity> func) {
		EntityConfig entityConfig = new EntityConfig(name, type, func);
		AiostRegistry.ENTITY_CONFIGS.register(entityConfig.name, entityConfig);
		return entityConfig;
	}

	public static EntityConfig wrap(EntityType<?> type) {
		EntityConfig wrap = WRAPPED.get(type);
		if (wrap != null)
			return wrap;
		wrap = new EntityConfig(AiostEntityTypes.getKey(type).getPath(), type, (e) -> {
		});
		WRAPPED.put(type, wrap);
		return wrap;
	}

	public static EntityConfig get(String name) {
		EntityConfig entityConfig = AiostRegistry.ENTITY_CONFIGS.get(name);
		if (entityConfig != null)
			return entityConfig;
		return wrap(AiostEntityTypes.get(name));
	}

	public static void save(ConfigurationSection section, String key, List<EntityConfig> entityConfigs) {
		int size = entityConfigs.size();
		String[] typeNames = new String[size];
		for (int i = 0; i < size; i++)
			typeNames[i] = entityConfigs.get(i).name;
		section.set(key, typeNames);
	}

	public static List<EntityConfig> load(ConfigurationSection section, String key) {
		List<String> typeNames = section.getStringList(key);
		List<EntityConfig> entityTypes = new ArrayList<EntityConfig>();
		int size = typeNames.size();
		for (int i = 0; i < size; i++)
			entityTypes.add(get(typeNames.get(i)));
		return entityTypes;
	}

	public final String name;
	public final EntityType<?> type;
	private final Consumer<Entity> func;

	private EntityConfig(String name, EntityType<?> type, Consumer<Entity> func) {
		this.name = name;
		this.type = type;
		this.func = func;
	}

	public final net.minecraft.world.entity.Entity spawn(Location loc) {
		net.minecraft.world.entity.Entity entity = AiostEntityTypes.spawnEntity(type, loc);
		func.accept(NMS.from(entity));
		return entity;
	}
}
