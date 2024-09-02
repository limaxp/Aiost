package com.pm.aiost.misc.nms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R4.CraftRegistry;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pm.aiost.item.custom.AiostItems;
import com.pm.aiost.misc.log.Logger;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.item.Item;

public class NBT {

	public static final String COMPONENTS_KEY = "components";
	public static final String CUSTOM_DATA_KEY = DataComponents.CUSTOM_DATA.toString();
	public static final String CUSTOM_NAME_KEY = DataComponents.CUSTOM_NAME.toString();
	public static final String LORE_KEY = DataComponents.LORE.toString();
	public static final String DAMAGE_KEY = DataComponents.DAMAGE.toString();
	public static final String CUSTOM_MODEL_DATA_KEY = DataComponents.CUSTOM_MODEL_DATA.toString();
	public static final String BLOCK_ENTITY_KEY = DataComponents.BLOCK_ENTITY_DATA.toString();
	public static final String ENCHANTMENTS_KEY = DataComponents.ENCHANTMENTS.toString();
	public static final String PATTERNS_KEY = DataComponents.BANNER_PATTERNS.toString();
	public static final String NAME_KEY = "name";
	public static final String ID_KEY = "id";
	public static final String PROFILE_KEY = DataComponents.PROFILE.toString();
	public static final String HIDE_ADDITIONAL_TOOLTIP_KEY = DataComponents.HIDE_ADDITIONAL_TOOLTIP.toString();
	public static final String HIDE_TOOLTIP_KEY = DataComponents.HIDE_TOOLTIP.toString();
	public static final String CAN_DESTROY_KEY = DataComponents.CAN_BREAK.toString();
	public static final String CAN_PLACE_ON_KEY = DataComponents.CAN_PLACE_ON.toString();
	public static final String ARMOR_ITEMS_KEY = "armorItems";
	public static final String UNBREAKABLE_KEY = DataComponents.UNBREAKABLE.toString();
	public static final String COUNT_KEY = "count";
	public static final String SLOT_KEY = "slot";
	public static final String TAG_KEY = "tag";
	public static final String ENTITY_TAG_KEY = DataComponents.ENTITY_DATA.toString();
	public static final String SPAWN_DATA_KEY = "spawnData";
	public static final String INVISIBLE_KEY = "Invisible";
	public static final String MARKER_KEY = "Marker";
	public static final String ATTRIBUTE_MODIFIERS_KEY = DataComponents.ATTRIBUTE_MODIFIERS.toString();
	public static final String ATTRIBUTE_NAME_KEY = "type";
	public static final String AMOUNT_KEY = "amount";
	public static final String OPERATION_KEY = "operation";
	public static final String UUID_LEAST_KEY = "UUIDLeast";
	public static final String UUID_MOST_KEY = "UUIDMost";
	public static final String SPAWN_RANGE_KEY = "SpawnRange";
	public static final String SPAWN_COUNT_KEY = "SpawnCount";
	public static final String REQUIRED_PLAYER_RANGE_KEY = "RequiredPlayerRange";
	public static final String MAX_NEARBY_ENTITIES_KEY = "MaxNearbyEntities";
	public static final String ITEM_EFFECT_KEY = "item_effect";
	public static final String WORLD_EFFECT_KEY = "world_effect";

	public static class NBTType {

		public static final byte END = 0;
		public static final byte BYTE = 1;
		public static final byte SHORT = 2;
		public static final byte INT = 3;
		public static final byte LONG = 4;
		public static final byte FLOAT = 5;
		public static final byte DOUBLE = 6;
		public static final byte BYTE_ARRAY = 7;
		public static final byte STRING = 8;
		public static final byte LIST = 9;
		public static final byte COMPOUND = 10;
		public static final byte INT_ARRAY = 11;
	}

	public static class HideFlag {

		public static final byte HIDE_ENCHANTMENTS = 1;
		public static final byte HIDE_ATTRIBUTE_MODIFIERS = 2;
		public static final byte HIDE_UNBREAKABLE = 4;
		public static final byte HIDE_CAN_DESTROY = 8;
		public static final byte HIDE_CAN_PLACE_ON = 16;
		public static final byte HIDE_OTHERS = 32;
	}

	public static CompoundTag fromString(String s) {
		try {
			return TagParser.parseTag(s);
		} catch (CommandSyntaxException e) {
			Logger.err("NBTHelper: Error on parsing nbt string '" + s + "'", e);
			return new CompoundTag();
		}
	}

	public static byte[] toBytes(CompoundTag tag) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				DataOutputStream dos = new DataOutputStream(baos)) {
			tag.write(dos);
			dos.flush();
			return baos.toByteArray();
		} catch (IOException e) {
			Logger.err("NBTHelper: Error on writing nbt to byte array", e);
			return new byte[0];
		}
	}

	public static CompoundTag fromBytes(byte[] bytes) {
		try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes))) {
			return NbtIo.read(dis, NbtAccounter.unlimitedHeap());
		} catch (IOException e) {
			Logger.err("NBTHelper: Error on reading nbt from byte array", e);
			return new CompoundTag();
		}
	}

	public static boolean toFile(File effectFile, CompoundTag tag) {
		try (DataOutputStream dau = new DataOutputStream(
				new BufferedOutputStream(new DeflaterOutputStream(new FileOutputStream(effectFile))))) {
			tag.write(dau);
			return true;
		} catch (FileNotFoundException e) {
			Logger.err("NBTHelper: Error! No file found for name: " + effectFile.getName(), e);
		} catch (IOException e) {
			Logger.err("NBTHelper: Error on saving file with name: " + effectFile.getName(), e);
		}
		return false;
	}

	public static CompoundTag fromFile(File effectFile) {
		try (DataInputStream dis = new DataInputStream(
				new BufferedInputStream(new InflaterInputStream(new FileInputStream(effectFile))))) {
			return NbtIo.read(dis, NbtAccounter.unlimitedHeap());
		} catch (FileNotFoundException e) {
			Logger.err("NBTHelper: Error! No file found for name: " + effectFile.getName(), e);
		} catch (IOException e) {
			Logger.err("NBTHelper: Error on loading file with name: " + effectFile.getName(), e);
		}
		return new CompoundTag();
	}

	public static boolean hasKey(CompoundTag tag, String key) {
		return tag.contains(key);
	}

	public static boolean hasKey(DataComponentMap tag, DataComponentType<?> type) {
		return tag.has(type);
	}

	public static CompoundTag getNBT(net.minecraft.world.entity.Entity entity) {
		CompoundTag tag = new CompoundTag();
		entity.saveWithoutId(tag);
		return tag;
	}

	public static void setNBT(net.minecraft.world.entity.Entity entity, CompoundTag tag) {
		entity.load(tag);
	}

	public static CompoundTag getNBT(ItemStack is) {
		return getNBT(NMS.to(is));
	}

	public static CompoundTag getNBT(net.minecraft.world.item.ItemStack is) {
		return (CompoundTag) is.save(CraftRegistry.getMinecraftRegistry());
	}

	public static boolean hasTag(ItemStack is) {
		return hasTag(NMS.to(is));
	}

	public static boolean hasTag(net.minecraft.world.item.ItemStack is) {
		return is.getComponents() != null; // TODO NOT NEEDED!
	}

	public static CompoundTag saveItem(CompoundTag tag, ItemStack is) {
		return saveItem(tag, NMS.to(is));
	}

	public static CompoundTag saveItem(CompoundTag tag, net.minecraft.world.item.ItemStack is) {
		return (CompoundTag) is.save(CraftRegistry.getMinecraftRegistry(), tag);
	}

	public static ItemStack loadItem(CompoundTag tag) {
		return CraftItemStack.asBukkitCopy(loadNMSItem(tag));
	}

	public static net.minecraft.world.item.ItemStack loadNMSItem(CompoundTag tag) {
		return loadNMSItem(tag, net.minecraft.world.item.ItemStack.EMPTY);
	}

	public static net.minecraft.world.item.ItemStack loadNMSItem(CompoundTag tag,
			net.minecraft.world.item.ItemStack defaultValue) {
		Optional<net.minecraft.world.item.ItemStack> optResult = net.minecraft.world.item.ItemStack
				.parse(CraftRegistry.getMinecraftRegistry(), tag);
		if (optResult.isEmpty())
			return defaultValue;
		return optResult.get();
	}

	public static StringTag asJsonText(String text) {
		return asJsonText(text, ChatColor.WHITE, false);
	}

	public static StringTag asJsonText(String text, ChatColor color, boolean italic) {
		return asJsonText(text, color.asBungee().getName(), italic);
	}

	public static StringTag asJsonText(String text, String color, boolean italic) {
		return StringTag.valueOf("{\"text\":\"" + text + "\",\"color\":\"" + color + "\",\"italic\":"
				+ (italic ? "true" : "false") + "}");
	}

	public static CompoundTag getOrAddCompound(CompoundTag tag, String key) {
		if (hasKey(tag, key))
			return tag.getCompound(key);
		CompoundTag result = new CompoundTag();
		tag.put(key, result);
		return result;
	}

	public static CompoundTag getOrAddComponents(CompoundTag tag) {
		return getOrAddCompound(tag, COMPONENTS_KEY);
	}

	public static CompoundTag addComponents(CompoundTag tag) {
		CompoundTag display = new CompoundTag();
		tag.put(COMPONENTS_KEY, display);
		return display;
	}

	public static void removeComponents(CompoundTag tag) {
		tag.remove(COMPONENTS_KEY);
	}

	public static CompoundTag getComponents(CompoundTag tag) {
		return tag.getCompound(COMPONENTS_KEY);
	}

	public static boolean hasComponents(CompoundTag tag) {
		return hasKey(tag, COMPONENTS_KEY);
	}

	public static void setDisplayName(CompoundTag tag, String name) {
		tag.put(CUSTOM_NAME_KEY, asJsonText(name));
	}

	public static void setLore(CompoundTag tag, List<String> lore) {
		ListTag nbtList = new ListTag();
		int size = lore.size();
		for (int i = 0; i < size; i++)
			nbtList.add(asJsonText(lore.get(i)));
		tag.put(LORE_KEY, nbtList);
	}

	public static void setDurability(CompoundTag tag, short durability) {
		tag.putShort(DAMAGE_KEY, durability);
	}

	public static void addHideFlag(CompoundTag tag, int hideFlag) {
		tag.putInt(HIDE_ADDITIONAL_TOOLTIP_KEY, tag.getInt(HIDE_ADDITIONAL_TOOLTIP_KEY) + hideFlag);
	}

	public static void removeHideFlag(CompoundTag tag, int hideFlag) {
		tag.putInt(HIDE_ADDITIONAL_TOOLTIP_KEY, tag.getInt(HIDE_ADDITIONAL_TOOLTIP_KEY) - hideFlag);
	}

	public static boolean hasHideFlag(CompoundTag tag, int hideFlag) {
		return (tag.getInt(HIDE_ADDITIONAL_TOOLTIP_KEY) & hideFlag) > 0;
	}

	public static boolean switchHideFlag(CompoundTag tag, int hideFlag) {
		if (hasHideFlag(tag, hideFlag)) {
			removeHideFlag(tag, hideFlag);
			return false;
		} else {
			addHideFlag(tag, hideFlag);
			return true;
		}
	}

	public static ListTag addCanDestroyList(CompoundTag tag) {
		ListTag canDestroy = new ListTag();
		tag.put(CAN_DESTROY_KEY, canDestroy);
		return canDestroy;
	}

	public static ListTag getCanDestroyList(CompoundTag tag) {
		if (hasKey(tag, CAN_DESTROY_KEY))
			return tag.getList(CAN_DESTROY_KEY, NBTType.STRING);
		else
			return addCanDestroyList(tag);
	}

	public static ListTag addCanPlaceOnList(CompoundTag tag) {
		ListTag list = new ListTag();
		tag.put(CAN_PLACE_ON_KEY, list);
		return list;
	}

	public static ListTag getCanPlaceOnList(CompoundTag tag) {
		if (hasKey(tag, CAN_PLACE_ON_KEY))
			return tag.getList(CAN_PLACE_ON_KEY, NBTType.STRING);
		else
			return addCanPlaceOnList(tag);
	}

	public static void addMaterial(ListTag list, Material mat) {
		addMaterial(list, materialToString(mat));
	}

	public static void addMaterial(ListTag list, String mat) {
		list.add(StringTag.valueOf(mat));
	}

	public static void removeMaterial(ListTag list, Material mat) {
		removeMaterial(list, materialToString(mat));
	}

	public static void removeMaterial(ListTag list, String mat) {
		for (int i = 0; i < list.size(); i++) {
			if (list.getString(i).equals(mat)) {
				list.remove(i);
				break;
			}
		}
	}

	public static ListTag addArmorItemsList(CompoundTag entityTag) {
		ListTag items = new ListTag();
		entityTag.put(ARMOR_ITEMS_KEY, items);
		return items;
	}

	public static ListTag getArmorItemsList(CompoundTag entityTag) {
		return entityTag.getList(ARMOR_ITEMS_KEY, NBTType.COMPOUND);
	}

	public static void addItem(ListTag items, Item nmsItem, byte count, CompoundTag nbt) {
		addItem(items, AiostItems.getKey(nmsItem).getPath(), count, nbt);
	}

	public static void addItem(ListTag items, String mat, byte count, CompoundTag nbt) {
		CompoundTag item = new CompoundTag();
		setItem(item, mat, count, nbt);
		items.add(item);
	}

	public static void addItem(ListTag items, Item nmsItem, byte count) {
		addItem(items, AiostItems.getKey(nmsItem).getPath(), count);
	}

	public static void addItem(ListTag items, String mat, byte count) {
		CompoundTag item = new CompoundTag();
		setItem(item, mat, count);
		items.add(item);
	}

	public static void setItem(CompoundTag item, String mat, byte count, CompoundTag nbt) {
		item.putString(ID_KEY, mat);
		item.putByte(COUNT_KEY, count);
		item.put(TAG_KEY, nbt);
	}

	public static void setItem(CompoundTag item, String mat, byte count) {
		item.putString(ID_KEY, mat);
		item.putByte(COUNT_KEY, count);
	}

	public static boolean switchUnbreakable(CompoundTag tag) {
		if (tag.getBoolean(UNBREAKABLE_KEY)) {
			tag.putBoolean(UNBREAKABLE_KEY, false);
			return false;
		} else {
			tag.putBoolean(UNBREAKABLE_KEY, true);
			return true;
		}
	}

	public static CompoundTag addSpawnData(CompoundTag tag) {
		CompoundTag entityTag = new CompoundTag();
		tag.put(SPAWN_DATA_KEY, entityTag);
		return entityTag;
	}

	public static CompoundTag getSpawnData(CompoundTag tag) {
		return tag.getCompound(SPAWN_DATA_KEY);
	}

	// TODO: chek and change name getting
	public static void setEntityId(CompoundTag entityTag, String entityName) {
		entityName = convetToNBTName(entityName);
		entityTag.putString(ID_KEY, entityName);
	}

	public static String getEntityId(CompoundTag entityTag) {
		return entityTag.getString(ID_KEY);
	}

	public static void setInvisible(CompoundTag entityTag, boolean IsInvisible) {
		entityTag.putBoolean(INVISIBLE_KEY, IsInvisible);
	}

	public static void setMarker(CompoundTag entityTag, boolean isMarker) {
		entityTag.putBoolean(MARKER_KEY, isMarker);
	}

	public static ListTag addAttributeModifiersList(CompoundTag tag) {
		ListTag attributeModifiers = new ListTag();
		tag.put(ATTRIBUTE_MODIFIERS_KEY, attributeModifiers);
		return attributeModifiers;
	}

	public static ListTag getAttributeModifiersList(CompoundTag tag) {
		if (hasKey(tag, ATTRIBUTE_MODIFIERS_KEY))
			return tag.getList(ATTRIBUTE_MODIFIERS_KEY, NBTType.COMPOUND);
		return addAttributeModifiersList(tag);
	}

	public static void addAttributeModifier(ListTag attributeModifiers, String attribute, double value, String slot) {
		CompoundTag attributeCompound = new CompoundTag();
		attributeCompound.putString(ATTRIBUTE_NAME_KEY, attribute);
		attributeCompound.putString(NAME_KEY, attribute);
		attributeCompound.putDouble(AMOUNT_KEY, value);
		attributeCompound.putInt(OPERATION_KEY, 0);
		attributeCompound.putInt(UUID_LEAST_KEY, 894654);
		attributeCompound.putInt(UUID_MOST_KEY, 2872);
		attributeCompound.putString(SLOT_KEY, slot);
		attributeModifiers.add(attributeCompound);
	}

	public static void removeAttributeModifier(ListTag attributeModifiers, String attribute, String slot) {
		CompoundTag attributeCompound;
		for (int i = 0; i < attributeModifiers.size(); i++) {
			attributeCompound = attributeModifiers.getCompound(i);
			if (attributeCompound.getString(ATTRIBUTE_NAME_KEY).equals(attribute)
					&& attributeCompound.getString(SLOT_KEY).equals(slot)) {
				attributeModifiers.remove(i);
				break;
			}
		}
	}

	public static void setAttributeModifier(ListTag attributeModifiers, String attribute, String slot, double value) {
		CompoundTag attributeCompound;
		for (int i = 0; i < attributeModifiers.size(); i++) {
			attributeCompound = attributeModifiers.getCompound(i);
			if (attributeCompound.getString(ATTRIBUTE_NAME_KEY).equals(attribute)
					&& attributeCompound.getString(SLOT_KEY).equals(slot)) {
				attributeCompound.putDouble(AMOUNT_KEY, value);
				break;
			}
		}
	}

	public static void setAttributeModifierSlot(ListTag attributeModifiers, String attribute, String slot,
			double value) {
		CompoundTag attributeCompound;
		for (int i = 0; i < attributeModifiers.size(); i++) {
			attributeCompound = attributeModifiers.getCompound(i);
			if (attributeCompound.getString(ATTRIBUTE_NAME_KEY).equals(attribute)
					&& attributeCompound.getDouble(AMOUNT_KEY) == value) {
				attributeCompound.putString(SLOT_KEY, slot);
				break;
			}
		}
	}

	public static void setCustomModelData(CompoundTag tag, int id) {
		tag.putInt(CUSTOM_MODEL_DATA_KEY, id);
	}

	public static int getCustomModelData(CompoundTag tag) {
		return tag.getInt(CUSTOM_MODEL_DATA_KEY);
	}

	public static void setSpawnerStats(CompoundTag tag, short range, short count, short playerRange,
			short maxNearbyEntities) {
		tag.putShort(SPAWN_RANGE_KEY, range);
		tag.putShort(SPAWN_COUNT_KEY, count);
		tag.putShort(REQUIRED_PLAYER_RANGE_KEY, playerRange);
		tag.putShort(MAX_NEARBY_ENTITIES_KEY, maxNearbyEntities);
	}

	public static String convetToNBTName(String entityName) {
		switch (entityName) {
		case "Horse":
			return "EntityHorse";
		case "Magmacube":
			return "LavaSlime";
		case "Mushroom":
			return "MushroomCow";
		case "Ocelot":
			return "Ozelot";
		case "ZombieVillager":
			return "Zombie";
		case "Donkey":
			return "EntityHorse";
		case "ElderGuardian":
			return "Guardian";
		case "Ponny":
			return "EntityHorse";
		case "Mule":
			return "EntityHorse";
		case "SkeletonHorse":
			return "EntityHorse";
		case "ZombieHorse":
			return "EntityHorse";

		default:
			return entityName;
		}
	}

	public static String materialToString(Material mat) {
		return "minecraft:".concat(mat.name().toLowerCase());
	}

	public static Material stringToMaterial(String string) {
		return Material.getMaterial(string.substring(string.indexOf(':') + 1).toUpperCase());
	}

	public static StringTag createNBTTagString(String text) {
		return StringTag.valueOf(text);
	}

	public static void setItemEffect(CompoundTag tag, int effectId) {
		getOrAddCompound(tag, CUSTOM_DATA_KEY).putInt(ITEM_EFFECT_KEY, effectId);
	}

	public static void removeItemEffect(CompoundTag tag) {
		getOrAddCompound(tag, CUSTOM_DATA_KEY).remove(ITEM_EFFECT_KEY);
	}

	public static int getItemEffect(CompoundTag tag) {
		return getOrAddCompound(tag, CUSTOM_DATA_KEY).getInt(ITEM_EFFECT_KEY);
	}

	public static boolean hasItemEffect(CompoundTag tag) {
		return hasKey(getOrAddCompound(tag, CUSTOM_DATA_KEY), ITEM_EFFECT_KEY);
	}

	public static ItemStack setItemEffect(ItemStack is, int id) {
		net.minecraft.world.item.ItemStack nmsItem = NMS.to(is);
		CompoundTag tag = getNBT(nmsItem);
		CompoundTag components = getOrAddComponents(tag);
		setItemEffect(components, id);
		return NMS.from(loadNMSItem(tag));
	}

	public static void setWorldEffect(CompoundTag tag, int effectId) {
		getOrAddCompound(tag, CUSTOM_DATA_KEY).putInt(WORLD_EFFECT_KEY, effectId);
	}

	public static void removeWorldEffect(CompoundTag tag) {
		getOrAddCompound(tag, CUSTOM_DATA_KEY).remove(WORLD_EFFECT_KEY);
	}

	public static int getWorldEffect(CompoundTag tag) {
		return getOrAddCompound(tag, CUSTOM_DATA_KEY).getInt(WORLD_EFFECT_KEY);
	}

	public static boolean hasWorldEffect(CompoundTag tag) {
		return hasKey(getOrAddCompound(tag, CUSTOM_DATA_KEY), WORLD_EFFECT_KEY);
	}

	public static ItemStack setWorldEffect(ItemStack is, int id) {
		net.minecraft.world.item.ItemStack nmsItem = NMS.to(is);
		CompoundTag tag = NBT.getNBT(nmsItem);
		CompoundTag components = getOrAddComponents(tag);
		NBT.setWorldEffect(components, id);
		return NMS.from(loadNMSItem(tag));
	}
}
