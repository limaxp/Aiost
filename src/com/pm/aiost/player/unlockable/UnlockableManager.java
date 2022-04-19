package com.pm.aiost.player.unlockable;

import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectBuilder;
import com.pm.aiost.item.ItemLoader;
import com.pm.aiost.misc.SpigotConfigManager;
import com.pm.aiost.misc.packet.disguise.Disguise;
import com.pm.aiost.misc.packet.disguise.DisguiseBuilder;
import com.pm.aiost.misc.particleEffect.particle.IParticle;
import com.pm.aiost.misc.particleEffect.particle.ParticleBuilder;
import com.pm.aiost.misc.registry.AiostRegistry;
import com.pm.aiost.misc.utils.nms.NMS;

import net.minecraft.server.v1_15_R1.EntityTypes;

public class UnlockableManager {

	public static void init() {
		FileConfiguration unlockablesConfig = SpigotConfigManager.getUnlockableConfig();
		if (!unlockablesConfig.contains("Types"))
			return;
		List<String> nameList = unlockablesConfig.getStringList("Types");
		for (UnlockableType<?> type : AiostRegistry.UNLOCKABLE_TYPES.values())
			if (nameList.contains(type.name))
				if (unlockablesConfig.contains(type.name))
					type.load(unlockablesConfig.getConfigurationSection(type.name));
	}

	public static void loadEffects(ConfigurationSection unlockableSection, UnlockableType<Effect> type,
			ItemStack item) {
		Set<String> names = unlockableSection.getKeys(false);
		int size = names.size() + 1;
		Effect[] effects = new Effect[size];
		@SuppressWarnings("unchecked")
		List<String>[] descriptions = new List[size];
		int[] prices = new int[size];
		int i = 1;
		for (String name : names) {
			ConfigurationSection section = unlockableSection.getConfigurationSection(name);
			effects[i] = EffectBuilder.createEffect(section);
			prices[i] = section.getInt("price");
			descriptions[i] = section.getStringList("description");
			i++;
		}
		type.init(names, descriptions, prices, effects, item);
	}

	public static void loadParticles(ConfigurationSection unlockableSection, UnlockableType<IParticle> type,
			ItemStack item) {
		Set<String> names = unlockableSection.getKeys(false);
		int size = names.size() + 1;
		IParticle[] particleEffects = new IParticle[size];
		@SuppressWarnings("unchecked")
		List<String>[] descriptions = new List[size];
		int[] prices = new int[size];
		int i = 1;
		for (String name : names) {
			ConfigurationSection section = unlockableSection.getConfigurationSection(name);
			particleEffects[i] = ParticleBuilder.create(section);
			prices[i] = section.getInt("price");
			descriptions[i] = section.getStringList("description");
			i++;
		}
		type.init(names, descriptions, prices, particleEffects, item);
	}

	public static void loadItemStacks(ConfigurationSection hatsSection, UnlockableType<ItemStack> type,
			ItemStack item) {
		Set<String> names = hatsSection.getKeys(false);
		int size = names.size() + 1;
		ItemStack[] items = new ItemStack[size];
		@SuppressWarnings("unchecked")
		List<String>[] descriptions = new List[size];
		int[] prices = new int[size];
		int i = 1;
		for (String name : names) {
			ConfigurationSection section = hatsSection.getConfigurationSection(name);
			items[i] = ItemLoader.loadItem(section.get("item"));
			prices[i] = section.getInt("price");
			descriptions[i] = section.getStringList("description");
			i++;
		}
		type.init(names, descriptions, prices, items, item);
	}

	public static void loadEntityTypes(ConfigurationSection petsSection, UnlockableType<EntityTypes<?>> type,
			ItemStack item) {
		Set<String> names = petsSection.getKeys(false);
		int size = names.size() + 1;
		EntityTypes<?>[] entityTypes = new EntityTypes<?>[size];
		@SuppressWarnings("unchecked")
		List<String>[] descriptions = new List[size];
		int[] prices = new int[size];
		int i = 1;
		for (String name : names) {
			ConfigurationSection section = petsSection.getConfigurationSection(name);
			entityTypes[i] = AiostEntityTypes.getByKey(NMS.createMinecraftKey(section.getString("type").toLowerCase()));
			prices[i] = section.getInt("price");
			descriptions[i] = section.getStringList("description");
			i++;
		}
		type.init(names, descriptions, prices, entityTypes, item);
	}

	public static void loadMorphs(ConfigurationSection morphsSection, UnlockableType<Disguise> type, ItemStack item) {
		Set<String> names = morphsSection.getKeys(false);
		int size = names.size() + 1;
		Disguise[] disguises = new Disguise[size];
		@SuppressWarnings("unchecked")
		List<String>[] descriptions = new List[size];
		int[] prices = new int[size];
		int i = 1;
		for (String name : names) {
			ConfigurationSection section = morphsSection.getConfigurationSection(name);
			disguises[i] = DisguiseBuilder.create(section);
			prices[i] = section.getInt("price");
			descriptions[i] = section.getStringList("description");
			i++;
		}
		type.init(names, descriptions, prices, disguises, item);
	}
}
