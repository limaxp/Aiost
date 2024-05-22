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
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.item.custom.NMSItems;
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
	public static final String CUSTOM_DATA_KEY = getName(DataComponents.CUSTOM_DATA);
	public static final String CUSTOM_NAME_KEY = getName(DataComponents.CUSTOM_NAME);
	public static final String LORE_KEY = getName(DataComponents.LORE);
	public static final String DAMAGE_KEY = getName(DataComponents.DAMAGE);
	public static final String CUSTOM_MODEL_DATA_KEY = getName(DataComponents.CUSTOM_MODEL_DATA);
	public static final String BLOCK_ENTITY_KEY = getName(DataComponents.BLOCK_ENTITY_DATA);
	public static final String ENCHANTMENTS_KEY = getName(DataComponents.ENCHANTMENTS);
	public static final String PATTERNS_KEY = getName(DataComponents.BANNER_PATTERNS);
	public static final String NAME_KEY = "name";
	public static final String COLOR_KEY = "color";
	public static final String ID_KEY = "id";
	public static final String LEVEL_KEY = "lvl";
	public static final String PATTERN_KEY = "pattern";
	public static final String PROFILE_KEY = getName(DataComponents.PROFILE);
	public static final String PROPERTIES_KEY = "properties";
	public static final String TEXTURES_KEY = "textures";
	public static final String SIGNATURE_KEY = "signature";
	public static final String VALUE_KEY = "value";
	public static final String HIDE_ADDITIONAL_TOOLTIP_KEY = getName(DataComponents.HIDE_ADDITIONAL_TOOLTIP);
	public static final String HIDE_TOOLTIP_KEY = getName(DataComponents.HIDE_TOOLTIP);
	public static final String CAN_DESTROY_KEY = getName(DataComponents.CAN_BREAK);
	public static final String CAN_PLACE_ON_KEY = getName(DataComponents.CAN_PLACE_ON);
	public static final String ITEMS_KEY = "items";
	public static final String ARMOR_ITEMS_KEY = "armorItems";
	public static final String UNBREAKABLE_KEY = getName(DataComponents.UNBREAKABLE);
	public static final String COUNT_KEY = "count";
	public static final String SLOT_KEY = "slot";
	public static final String TAG_KEY = "tag";
	public static final String ENTITY_TAG_KEY = getName(DataComponents.ENTITY_DATA);
	public static final String SPAWN_DATA_KEY = "spawnData";
	public static final String NO_AI_KEY = "NoAI";
	public static final String INVISIBLE_KEY = "Invisible";
	public static final String SILENT_KEY = "Silent";
	public static final String MARKER_KEY = "Marker";
	public static final String ATTRIBUTE_MODIFIERS_KEY = getName(DataComponents.ATTRIBUTE_MODIFIERS);
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

	private static String getName(DataComponentType<?> type) {
		return type.toString();
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

	public static void setDisplayName(CompoundTag tag, String name, ChatColor color, boolean italic) {
		tag.put(CUSTOM_NAME_KEY, asJsonText(name, color, italic));
	}

	public static void removeDisplayName(CompoundTag tag) {
		tag.remove(CUSTOM_NAME_KEY);
	}

	public static String getDisplayName(CompoundTag tag) {
		return ((StringTag) tag.get(CUSTOM_NAME_KEY)).getAsString();
	}

	public static boolean hasDisplayName(CompoundTag tag) {
		return hasKey(tag, CUSTOM_NAME_KEY);
	}

	public static void setLore(CompoundTag tag, List<String> lore) {
		ListTag nbtList = new ListTag();
		int size = lore.size();
		for (int i = 0; i < size; i++)
			nbtList.add(asJsonText(lore.get(i)));
		tag.put(LORE_KEY, nbtList);
	}

	public static void removeLore(CompoundTag tag) {
		tag.remove(LORE_KEY);
	}

	public static ListTag getLore(CompoundTag tag) {
		return tag.getList(LORE_KEY, NBTType.STRING);
	}

	public static String getLore(CompoundTag tag, int index) {
		return tag.getList(LORE_KEY, NBTType.STRING).get(index).getAsString();
	}

	public static boolean hasLore(CompoundTag tag) {
		return hasKey(tag, LORE_KEY);
	}

	public static void setDurability(CompoundTag tag, short durability) {
		tag.putShort(DAMAGE_KEY, durability);
	}

	public static void removeDurability(CompoundTag tag) {
		tag.remove(DAMAGE_KEY);
	}

	public static short getDurability(CompoundTag tag) {
		return tag.getShort(DAMAGE_KEY);
	}

	public static boolean hasDurability(CompoundTag tag) {
		return hasKey(tag, DAMAGE_KEY);
	}

	public static ListTag addEnchantmentList(CompoundTag tag) {
		ListTag ench = new ListTag();
		tag.put(ENCHANTMENTS_KEY, ench);
		return ench;
	}

	public static void removeEnchantmentList(CompoundTag tag) {
		tag.remove(ENCHANTMENTS_KEY);
	}

	public static ListTag getEnchantmentList(CompoundTag tag) {
		if (hasKey(tag, ENCHANTMENTS_KEY))
			return tag.getList(ENCHANTMENTS_KEY, NBTType.COMPOUND);
		return addEnchantmentList(tag);
	}

	public static boolean hasEnchantmentList(CompoundTag tag) {
		return hasKey(tag, ENCHANTMENTS_KEY);
	}

	public static void addEnchantment(ListTag ench, String id, short level) {
		CompoundTag enchantment = new CompoundTag();
		enchantment.putString(ID_KEY, id);
		enchantment.putShort(LEVEL_KEY, level);
		ench.add(enchantment);
	}

	public static void setEnchantment(ListTag ench, String id, short level) {
		for (int i = 0; i < ench.size(); i++) {
			if (ench.getCompound(i).getString(ID_KEY).equals(id)) {
				CompoundTag enchantment = ench.getCompound(i);
				if (enchantment.getShort(LEVEL_KEY) != level)
					enchantment.putShort(LEVEL_KEY, level);
				return;
			}
		}
		addEnchantment(ench, id, level);
	}

	public static void removeEnchantment(ListTag ench, String id) {
		for (int i = 0; i < ench.size(); i++) {
			if (ench.getCompound(i).getString(ID_KEY).equals(id)) {
				ench.remove(i);
				break;
			}
		}
	}

	public static CompoundTag getEnchantment(ListTag ench, String id) {
		for (int i = 0; i < ench.size(); i++) {
			if (ench.getCompound(i).getString(ID_KEY).equals(id))
				return ench.getCompound(i);
		}
		return null;
	}

	public static boolean hasEnchantment(ListTag ench, String id) {
		for (int i = 0; i < ench.size(); i++) {
			if (ench.getCompound(i).getString(ID_KEY).equals(id))
				return true;
		}
		return false;
	}

	public static short getEnchantmentLevel(ListTag ench, String id) {
		for (int i = 0; i < ench.size(); i++) {
			if (ench.getCompound(i).getString(ID_KEY).equals(id))
				return ench.getCompound(i).getShort(LEVEL_KEY);
		}
		return 0;
	}

	public static String getEnchantmentId(CompoundTag enchantment) {
		return enchantment.getString(ID_KEY);
	}

	public static short getEnchantmentLevel(CompoundTag enchantment) {
		return enchantment.getShort(LEVEL_KEY);
	}

	public static CompoundTag addBlockEntity(CompoundTag tag) {
		CompoundTag blockEntityTag = new CompoundTag();
		tag.put(BLOCK_ENTITY_KEY, blockEntityTag);
		return blockEntityTag;
	}

	public static void removeBlockEntity(CompoundTag tag) {
		tag.remove(BLOCK_ENTITY_KEY);
	}

	public static CompoundTag getBlockEntity(CompoundTag tag) {
		return tag.getCompound(BLOCK_ENTITY_KEY);
	}

	public static boolean hasBlockEntity(CompoundTag tag) {
		return hasKey(tag, BLOCK_ENTITY_KEY);
	}

	public static ListTag addPatternList(CompoundTag blockEntityTag) {
		ListTag patterns = new ListTag();
		blockEntityTag.put(PATTERNS_KEY, patterns);
		return patterns;
	}

	public static void removePatternList(CompoundTag blockEntityTag) {
		blockEntityTag.remove(PATTERNS_KEY);
	}

	public static ListTag getPatternList(CompoundTag blockEntityTag) {
		return blockEntityTag.getList(PATTERNS_KEY, NBTType.COMPOUND);
	}

	public static boolean hasPatternList(CompoundTag blockEntityTag) {
		return hasKey(blockEntityTag, PATTERNS_KEY);
	}

	public static void addPattern(ListTag patterns, String patternName, int color) {
		CompoundTag pattern = new CompoundTag();
		pattern.putString(PATTERN_KEY, patternName);
		pattern.putInt(COLOR_KEY, color);
		patterns.add(pattern);
	}

	public static void setPattern(ListTag patterns, String patternName, int color) {
		for (int i = 0; i < patterns.size(); i++) {
			if (patterns.getCompound(i).getString(PATTERN_KEY).equals(patternName)) {
				CompoundTag pattern = patterns.getCompound(i);
				if (pattern.getInt(COLOR_KEY) != color)
					pattern.putInt(COLOR_KEY, color);
				return;
			}
		}
		addPattern(patterns, patternName, color);
	}

	public static void removePattern(ListTag patterns, String pattern) {
		for (int i = 0; i < patterns.size(); i++) {
			if (patterns.getCompound(i).getString(PATTERN_KEY).equals(pattern)) {
				patterns.remove(i);
				break;
			}
		}
	}

	public static CompoundTag getPattern(ListTag patterns, String pattern) {
		for (int i = 0; i < patterns.size(); i++) {
			if (patterns.getCompound(i).getString(PATTERN_KEY).equals(pattern))
				return patterns.getCompound(i);
		}
		return null;
	}

	public static boolean hasPattern(ListTag patterns, String pattern) {
		for (int i = 0; i < patterns.size(); i++) {
			if (patterns.getCompound(i).getString(PATTERN_KEY).equals(pattern))
				return true;
		}
		return false;
	}

	public static int getPatternColor(ListTag patterns, String pattern) {
		for (int i = 0; i < patterns.size(); i++) {
			if (patterns.getCompound(i).getString(PATTERN_KEY).equals(pattern))
				return patterns.getCompound(i).getInt(COLOR_KEY);
		}
		return 0;
	}

	public static short getPatternName(CompoundTag pattern) {
		return pattern.getShort(PATTERN_KEY);
	}

	public static int getPatternColor(CompoundTag pattern) {
		return pattern.getInt(COLOR_KEY);
	}

	public static CompoundTag addSkullOwner(CompoundTag tag) {
		CompoundTag skullOwner = new CompoundTag();
		tag.put(PROFILE_KEY, skullOwner);
		return skullOwner;
	}

	public static void removeSkullOwner(CompoundTag tag) {
		tag.remove(PROFILE_KEY);
	}

	public static CompoundTag getSkullOwner(CompoundTag tag) {
		return tag.getCompound(PROFILE_KEY);
	}

	public static boolean hasSkullOwner(CompoundTag tag) {
		return hasKey(tag, PROFILE_KEY);
	}

	public static void setSkullOwnerId(CompoundTag skullOwner, String id) {
		skullOwner.putString(ID_KEY, id);
	}

	public static void removeSkullOwnerId(CompoundTag skullOwner) {
		skullOwner.remove(ID_KEY);
	}

	public static String getSkullOwnerId(CompoundTag skullOwner) {
		return skullOwner.getString(ID_KEY);
	}

	public static boolean hasSkullOwnerId(CompoundTag skullOwner) {
		return hasKey(skullOwner, ID_KEY);
	}

	public static void setSkullOwnerName(CompoundTag skullOwner, String name) {
		skullOwner.putString(NAME_KEY, name);
	}

	public static void removeSkullOwnerName(CompoundTag skullOwner) {
		skullOwner.remove(NAME_KEY);
	}

	public static String getSkullOwnerName(CompoundTag skullOwner) {
		return skullOwner.getString(NAME_KEY);
	}

	public static boolean hasSkullOwnerName(CompoundTag skullOwner) {
		return hasKey(skullOwner, NAME_KEY);
	}

	public static void setSkullTexture(CompoundTag skullOwner, String textureString) {
		CompoundTag properities = new CompoundTag();
		skullOwner.put(PROPERTIES_KEY, properities);
		ListTag textures = new ListTag();
		properities.put(TEXTURES_KEY, textures);
		CompoundTag texture = new CompoundTag();
		texture.putString(VALUE_KEY, textureString);
		textures.add(texture);
	}

	public static void setSkullTextures(CompoundTag skullOwner, List<String> textrueList) {
		CompoundTag properities = new CompoundTag();
		skullOwner.put(PROPERTIES_KEY, properities);
		ListTag textures = new ListTag();
		properities.put(TEXTURES_KEY, textures);
		for (String textrueListStrings : textrueList) {
			CompoundTag texture = new CompoundTag();
			String[] split0 = textrueListStrings.split(":\"");
			String[] split = split0[1].split("\"");
			texture.putString(SIGNATURE_KEY, split[0]);
			texture.putString(VALUE_KEY, split0[2].split("\"")[0]);
			textures.add(texture);
		}
	}

	public static void removeSkullTextures(CompoundTag skullOwner, List<String> textrueList) {
		skullOwner.remove(PROPERTIES_KEY);
	}

	public static ListTag getSkullTextures(CompoundTag skullOwner) {
		return skullOwner.getCompound(PROPERTIES_KEY).getList(TEXTURES_KEY, NBTType.COMPOUND);

	}

	public static CompoundTag getSkullTexture(CompoundTag skullOwner, int index) {
		return skullOwner.getList(LORE_KEY, NBTType.STRING).getCompound(index);
	}

	public static boolean hasSkullTextures(CompoundTag skullOwner) {
		if (hasKey(skullOwner, PROPERTIES_KEY)) {
			if (hasKey(skullOwner.getCompound(PROPERTIES_KEY), TEXTURES_KEY))
				return true;
		}
		return false;
	}

	public static void setHideFlags(CompoundTag tag, int hideFlags) {
		tag.putInt(HIDE_ADDITIONAL_TOOLTIP_KEY, hideFlags);
	}

	public static void removeHideFlags(CompoundTag tag) {
		tag.remove(HIDE_ADDITIONAL_TOOLTIP_KEY);
	}

	public static int getHideFlags(CompoundTag tag) {
		return tag.getInt(HIDE_ADDITIONAL_TOOLTIP_KEY);
	}

	public static boolean hasHideFlags(CompoundTag tag) {
		return hasKey(tag, HIDE_ADDITIONAL_TOOLTIP_KEY);
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

	public static void removeCanDestroyList(CompoundTag tag) {
		tag.remove(CAN_DESTROY_KEY);
	}

	public static ListTag getCanDestroyList(CompoundTag tag) {
		if (hasKey(tag, CAN_DESTROY_KEY))
			return tag.getList(CAN_DESTROY_KEY, NBTType.STRING);
		else
			return addCanDestroyList(tag);
	}

	public static boolean hasCanDestroyList(CompoundTag tag) {
		return hasKey(tag, CAN_DESTROY_KEY);
	}

	public static void setCanDestroy(CompoundTag tag, Material... materials) {
		ListTag canDestroy = addCanDestroyList(tag);
		for (Material mat : materials)
			canDestroy.add(StringTag.valueOf(materialToString(mat)));
	}

	public static void setCanDestroy(CompoundTag tag, String... materials) {
		ListTag canDestroy = addCanDestroyList(tag);
		for (String mat : materials)
			canDestroy.add(StringTag.valueOf(mat));
	}

	public static ListTag addCanPlaceOnList(CompoundTag tag) {
		ListTag list = new ListTag();
		tag.put(CAN_PLACE_ON_KEY, list);
		return list;
	}

	public static void removeCanPlaceOnList(CompoundTag tag) {
		tag.remove(CAN_PLACE_ON_KEY);
	}

	public static ListTag getCanPlaceOnList(CompoundTag tag) {
		if (hasKey(tag, CAN_PLACE_ON_KEY))
			return tag.getList(CAN_PLACE_ON_KEY, NBTType.STRING);
		else
			return addCanPlaceOnList(tag);
	}

	public static boolean hasCanPlaceOnList(CompoundTag tag) {
		return hasKey(tag, CAN_PLACE_ON_KEY);
	}

	public static void setCanPlaceOn(CompoundTag tag, Material... materials) {
		ListTag canPlaceOn = addCanPlaceOnList(tag);
		for (Material mat : materials)
			canPlaceOn.add(StringTag.valueOf(materialToString(mat)));
	}

	public static void setCanPlaceOn(CompoundTag tag, String... materials) {
		ListTag canPlaceOn = addCanPlaceOnList(tag);
		for (String mat : materials)
			canPlaceOn.add(StringTag.valueOf(mat));
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

	public static void hasMaterial(ListTag list, Material mat) {
		hasMaterial(list, materialToString(mat));
	}

	public static boolean hasMaterial(ListTag list, String mat) {
		for (int i = 0; i < list.size(); i++) {
			if (list.getString(i).equals(mat))
				return true;
		}
		return false;
	}

	public static ListTag addItemsList(CompoundTag blockEntityTag) {
		ListTag items = new ListTag();
		blockEntityTag.put(ITEMS_KEY, items);
		return items;
	}

	public static void removeItemsList(CompoundTag blockEntityTag) {
		blockEntityTag.remove(ITEMS_KEY);
	}

	public static ListTag getItemsList(CompoundTag blockEntityTag) {
		return blockEntityTag.getList(ITEMS_KEY, NBTType.COMPOUND);
	}

	public static boolean hasItemsList(CompoundTag blockEntityTag) {
		return hasKey(blockEntityTag, ITEMS_KEY);
	}

	public static ListTag addArmorItemsList(CompoundTag entityTag) {
		ListTag items = new ListTag();
		entityTag.put(ARMOR_ITEMS_KEY, items);
		return items;
	}

	public static void removeArmorItemsList(CompoundTag entityTag) {
		entityTag.remove(ARMOR_ITEMS_KEY);
	}

	public static ListTag getArmorItemsList(CompoundTag entityTag) {
		return entityTag.getList(ARMOR_ITEMS_KEY, NBTType.COMPOUND);
	}

	public static boolean hasArmorItemsList(CompoundTag entityTag) {
		return hasKey(entityTag, ARMOR_ITEMS_KEY);
	}

	public static void addEquipmentItem(ListTag items, byte slot, Material mat, short damage, byte count, String nbt) {
		addEquipmentItem(items, slot, materialToString(mat), damage, count, fromString(nbt));
	}

	public static void addEquipmentItem(ListTag items, byte slot, String mat, short damage, byte count, String nbt) {
		addEquipmentItem(items, slot, mat, damage, count, fromString(nbt));
	}

	public static void addEquipmentItem(ListTag items, byte slot, Material mat, short damage, byte count,
			CompoundTag nbt) {
		addEquipmentItem(items, slot, materialToString(mat), damage, count, nbt);
	}

	public static void addEquipmentItem(ListTag items, byte slot, String mat, short damage, byte count,
			CompoundTag nbt) {
		CompoundTag item = new CompoundTag();
		setEquipmentItem(item, slot, mat, damage, count, nbt);
		items.add(item);
	}

	public static void addEquipmentItem(ListTag items, byte slot, Material mat, short damage, byte count) {
		addEquipmentItem(items, slot, materialToString(mat), damage, count);
	}

	public static void addEquipmentItem(ListTag items, byte slot, String mat, short damage, byte count) {
		CompoundTag item = new CompoundTag();
		setEquipmentItem(item, slot, mat, damage, count);
		items.add(item);
	}

	public static void setEquipmentItem(ListTag items, byte slot, Material mat, short damage, byte count, String nbt) {
		setEquipmentItem(items, slot, materialToString(mat), damage, count, fromString(nbt));
	}

	public static void setEquipmentItem(ListTag items, byte slot, String mat, short damage, byte count, String nbt) {
		setEquipmentItem(items, slot, mat, damage, count, fromString(nbt));
	}

	public static void setEquipmentItem(ListTag items, byte slot, Material mat, short damage, byte count,
			CompoundTag nbt) {
		setEquipmentItem(items, slot, materialToString(mat), damage, count, nbt);
	}

	public static void setEquipmentItem(ListTag items, byte slot, String mat, short damage, byte count,
			CompoundTag nbt) {
		for (int i = 0; i < items.size(); i++) {
			if (items.getCompound(i).getByte(SLOT_KEY) == slot) {
				setEquipmentItem(items.getCompound(i), slot, mat, damage, count, nbt);
				return;
			}
		}
		addEquipmentItem(items, slot, mat, damage, count, nbt);
	}

	public static void setEquipmentItem(ListTag items, byte slot, Material mat, short damage, byte count) {
		setEquipmentItem(items, slot, materialToString(mat), damage, count);
	}

	public static void setEquipmentItem(ListTag items, byte slot, String mat, short damage, byte count) {
		for (int i = 0; i < items.size(); i++) {
			if (items.getCompound(i).getByte(SLOT_KEY) == slot) {
				setEquipmentItem(items.getCompound(i), slot, mat, damage, count);
				return;
			}
		}
		addEquipmentItem(items, slot, mat, damage, count);
	}

	public static void setEquipmentItem(CompoundTag item, byte slot, Material mat, short damage, byte count,
			String nbt) {
		setEquipmentItem(item, slot, materialToString(mat), damage, count, fromString(nbt));
	}

	public static void setEquipmentItem(CompoundTag item, byte slot, String mat, short damage, byte count, String nbt) {
		setEquipmentItem(item, slot, mat, damage, count, fromString(nbt));
	}

	public static void setEquipmentItem(CompoundTag item, byte slot, Material mat, short damage, byte count,
			CompoundTag nbt) {
		setEquipmentItem(item, slot, materialToString(mat), damage, count, nbt);
	}

	public static void setEquipmentItem(CompoundTag item, byte slot, String mat, short damage, byte count,
			CompoundTag nbt) {
		item.putByte(SLOT_KEY, slot);
		item.putString(ID_KEY, mat);
		item.putByte(COUNT_KEY, count);
		item.putShort(DAMAGE_KEY, damage);
		item.put(TAG_KEY, nbt);
	}

	public static void setEquipmentItem(CompoundTag item, byte slot, Material mat, short damage, byte count) {
		setEquipmentItem(item, slot, materialToString(mat), damage, count);
	}

	public static void setEquipmentItem(CompoundTag item, byte slot, String mat, short damage, byte count) {
		item.putByte(SLOT_KEY, slot);
		item.putString(ID_KEY, mat);
		item.putByte(COUNT_KEY, count);
		item.putShort(DAMAGE_KEY, damage);
	}

	public static void removeEquipedItem(ListTag items, byte slot) {
		for (int i = 0; i < items.size(); i++) {
			if (items.getCompound(i).getByte(SLOT_KEY) == slot) {
				items.remove(i);
				break;
			}
		}
	}

	public static CompoundTag getEquipedItem(ListTag items, byte slot) {
		for (int i = 0; i < items.size(); i++) {
			if (items.getCompound(i).getByte(SLOT_KEY) == slot)
				return items.getCompound(i);
		}
		return null;
	}

	public static boolean hasEquipedItem(ListTag items, byte slot) {
		for (int i = 0; i < items.size(); i++) {
			if (items.getCompound(i).getByte(SLOT_KEY) == slot)
				return true;
		}
		return false;
	}

	public static void addItem(ListTag items, Material mat, byte count, String nbt) {
		addItem(items, materialToString(mat), count, fromString(nbt));
	}

	public static void addItem(ListTag items, Item nmsItem, byte count, String nbt) {
		addItem(items, NMSItems.getKey(nmsItem).getPath(), count, fromString(nbt));
	}

	public static void addItem(ListTag items, String mat, byte count, String nbt) {
		addItem(items, mat, count, fromString(nbt));
	}

	public static void addItem(ListTag items, Material mat, byte count, CompoundTag nbt) {
		addItem(items, materialToString(mat), count, nbt);
	}

	public static void addItem(ListTag items, Item nmsItem, byte count, CompoundTag nbt) {
		addItem(items, NMSItems.getKey(nmsItem).getPath(), count, nbt);
	}

	public static void addItem(ListTag items, String mat, byte count, CompoundTag nbt) {
		CompoundTag item = new CompoundTag();
		setItem(item, mat, count, nbt);
		items.add(item);
	}

	public static void addItem(ListTag items, Material mat, byte count) {
		addItem(items, materialToString(mat), count);
	}

	public static void addItem(ListTag items, Item nmsItem, byte count) {
		addItem(items, NMSItems.getKey(nmsItem).getPath(), count);
	}

	public static void addItem(ListTag items, String mat, byte count) {
		CompoundTag item = new CompoundTag();
		setItem(item, mat, count);
		items.add(item);
	}

	public static void addItem(ListTag items, ItemStack is) {
		addItem(items, NMS.to(is));
	}

	public static void addItem(ListTag items, net.minecraft.world.item.ItemStack is) {
		items.add(is.save(CraftRegistry.getMinecraftRegistry()));
	}

	public static void setItem(CompoundTag item, Material mat, byte count, String nbt) {
		setItem(item, materialToString(mat), count, fromString(nbt));
	}

	public static void setItem(CompoundTag item, Item nmsItem, byte count, String nbt) {
		setItem(item, NMSItems.getKey(nmsItem).getPath(), count, fromString(nbt));
	}

	public static void setItem(CompoundTag item, String mat, byte count, String nbt) {
		setItem(item, mat, count, fromString(nbt));
	}

	public static void setItem(CompoundTag item, Material mat, byte count, CompoundTag nbt) {
		setItem(item, materialToString(mat), count, nbt);
	}

	public static void setItem(CompoundTag item, Item nmsItem, byte count, CompoundTag nbt) {
		setItem(item, NMSItems.getKey(nmsItem).getPath(), count, nbt);
	}

	public static void setItem(CompoundTag item, String mat, byte count, CompoundTag nbt) {
		item.putString(ID_KEY, mat);
		item.putByte(COUNT_KEY, count);
		item.put(TAG_KEY, nbt);
	}

	public static void setItem(CompoundTag item, Material mat, byte count) {
		setItem(item, materialToString(mat), count);
	}

	public static void setItem(CompoundTag item, Item nmsItem, byte count) {
		setItem(item, NMSItems.getKey(nmsItem).getPath(), count);
	}

	public static void setItem(CompoundTag item, String mat, byte count) {
		item.putString(ID_KEY, mat);
		item.putByte(COUNT_KEY, count);
	}

	public static void setItem(CompoundTag item, ItemStack is) {
		setItem(item, NMS.to(is));
	}

	public static void setItem(CompoundTag item, net.minecraft.world.item.ItemStack is) {
		is.save(CraftRegistry.getMinecraftRegistry(), item);
	}

	public static void removeItem(ListTag items, int index) {
		items.remove(index);
	}

	public static CompoundTag getItem(ListTag items, int index) {
		return items.getCompound(index);
	}

	public static boolean hasItem(ListTag items, int index) {
		return items.size() > index;
	}

	public static void setUnbreakable(CompoundTag tag, boolean unbreakable) {
		tag.putBoolean(UNBREAKABLE_KEY, unbreakable);
	}

	public static void getUnbreakable(CompoundTag tag) {
		tag.getBoolean(UNBREAKABLE_KEY);
	}

	public static void hasUnbreakable(CompoundTag tag) {
		hasKey(tag, UNBREAKABLE_KEY);
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

	public static CompoundTag addEntityTag(CompoundTag tag) {
		CompoundTag entityTag = new CompoundTag();
		tag.put(ENTITY_TAG_KEY, entityTag);
		return entityTag;
	}

	public static void removeEntityTag(CompoundTag tag) {
		tag.remove(ENTITY_TAG_KEY);
	}

	public static CompoundTag getEntityTag(CompoundTag tag) {
		return tag.getCompound(ENTITY_TAG_KEY);
	}

	public static boolean hasEntityTag(CompoundTag tag) {
		return hasKey(tag, ENTITY_TAG_KEY);
	}

	public static CompoundTag addSpawnData(CompoundTag tag) {
		CompoundTag entityTag = new CompoundTag();
		tag.put(SPAWN_DATA_KEY, entityTag);
		return entityTag;
	}

	public static void removeSpawnData(CompoundTag tag) {
		tag.remove(SPAWN_DATA_KEY);
	}

	public static CompoundTag getSpawnData(CompoundTag tag) {
		return tag.getCompound(SPAWN_DATA_KEY);
	}

	public static boolean hasSpawnData(CompoundTag tag) {
		return hasKey(tag, SPAWN_DATA_KEY);
	}

	public static void setEntityId(CompoundTag entityTag, EntityType entityType) {
		setEntityId(entityTag, entityType.name());
	}

	public static void setEntityId(CompoundTag entityTag, net.minecraft.world.entity.EntityType<?> entityType) {
		setEntityId(entityTag, AiostEntityTypes.getKey(entityType).getPath());
	}

	// TODO: chek and change name getting
	public static void setEntityId(CompoundTag entityTag, String entityName) {
		entityName = convetToNBTName(entityName);
		entityTag.putString(ID_KEY, entityName);
	}

	public static void removeEntityId(CompoundTag entityTag) {
		entityTag.remove(ID_KEY);
	}

	public static String getEntityId(CompoundTag entityTag) {
		return entityTag.getString(ID_KEY);
	}

	public static boolean hasEntityId(CompoundTag entityTag) {
		return hasKey(entityTag, ID_KEY);
	}

	public static void setEntityName(CompoundTag entityTag, String name) {
		entityTag.putString(CUSTOM_NAME_KEY, name);
	}

	public static void removeEntityName(CompoundTag entityTag) {
		entityTag.remove(CUSTOM_NAME_KEY);
	}

	public static String getEntityName(CompoundTag entityTag, String name) {
		return entityTag.getString(CUSTOM_NAME_KEY);
	}

	public static boolean hasEntityName(CompoundTag entityTag, String name) {
		return hasKey(entityTag, CUSTOM_NAME_KEY);
	}

	public static void setNoAi(CompoundTag entityTag, boolean hasNoAi) {
		entityTag.putBoolean(NO_AI_KEY, hasNoAi);
	}

	public static void removeNoAi(CompoundTag entityTag) {
		entityTag.remove(NO_AI_KEY);
	}

	public static boolean hasNoAi(CompoundTag entityTag) {
		return entityTag.getBoolean(NO_AI_KEY);
	}

	public static void setInvisible(CompoundTag entityTag, boolean IsInvisible) {
		entityTag.putBoolean(INVISIBLE_KEY, IsInvisible);
	}

	public static void removeInvisible(CompoundTag entityTag) {
		entityTag.remove(INVISIBLE_KEY);
	}

	public static boolean isInvisible(CompoundTag entityTag) {
		return entityTag.getBoolean(INVISIBLE_KEY);
	}

	public static void setSilent(CompoundTag entityTag, boolean isSilent) {
		entityTag.putBoolean(SILENT_KEY, isSilent);
	}

	public static void removeSilent(CompoundTag entityTag) {
		entityTag.remove(SILENT_KEY);
	}

	public static boolean isSilent(CompoundTag entityTag) {
		return entityTag.getBoolean(SILENT_KEY);
	}

	public static void setMarker(CompoundTag entityTag, boolean isMarker) {
		entityTag.putBoolean(MARKER_KEY, isMarker);
	}

	public static void removeMarker(CompoundTag entityTag) {
		entityTag.remove(MARKER_KEY);
	}

	public static boolean isMarker(CompoundTag entityTag) {
		return entityTag.getBoolean(MARKER_KEY);
	}

	public static ListTag addAttributeModifiersList(CompoundTag tag) {
		ListTag attributeModifiers = new ListTag();
		tag.put(ATTRIBUTE_MODIFIERS_KEY, attributeModifiers);
		return attributeModifiers;
	}

	public static void removeAttributeModifiersList(CompoundTag tag) {
		tag.remove(ATTRIBUTE_MODIFIERS_KEY);
	}

	public static ListTag getAttributeModifiersList(CompoundTag tag) {
		if (hasKey(tag, ATTRIBUTE_MODIFIERS_KEY))
			return tag.getList(ATTRIBUTE_MODIFIERS_KEY, NBTType.COMPOUND);
		return addAttributeModifiersList(tag);
	}

	public static boolean hasAttributeModifiersList(CompoundTag tag) {
		return hasKey(tag, ATTRIBUTE_MODIFIERS_KEY);
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

	public static double getAttributeModifier(ListTag attributeModifiers, String attribute, String slot) {
		CompoundTag attributeCompound;
		for (int i = 0; i < attributeModifiers.size(); i++) {
			attributeCompound = attributeModifiers.getCompound(i);
			if (attributeCompound.getString(ATTRIBUTE_NAME_KEY).equals(attribute)
					&& attributeCompound.getString(SLOT_KEY).equals(slot))
				return attributeCompound.getDouble(AMOUNT_KEY);
		}
		return 0;
	}

	public static boolean hasAttributeModifier(ListTag attributeModifiers, String attribute, String slot) {
		CompoundTag attributeCompound;
		for (int i = 0; i < attributeModifiers.size(); i++) {
			attributeCompound = attributeModifiers.getCompound(i);
			if (attributeCompound.getString(ATTRIBUTE_NAME_KEY).equals(attribute)
					&& attributeCompound.getString(SLOT_KEY).equals(slot))
				return true;
		}
		return false;
	}

	public static void setCustomModelData(CompoundTag tag, int id) {
		tag.putInt(CUSTOM_MODEL_DATA_KEY, id);
	}

	public static void removeCustomModelData(CompoundTag tag) {
		tag.remove(CUSTOM_MODEL_DATA_KEY);
	}

	public static int getCustomModelData(CompoundTag tag) {
		return tag.getInt(CUSTOM_MODEL_DATA_KEY);
	}

	public static boolean hasCustomModelData(CompoundTag tag) {
		return hasKey(tag, CUSTOM_MODEL_DATA_KEY);
	}

	public static void setSpawnRange(CompoundTag tag, short range) {
		tag.putShort(SPAWN_RANGE_KEY, range);
	}

	public static void removeSpawnRange(CompoundTag tag) {
		tag.remove(SPAWN_RANGE_KEY);
	}

	public static short getSpawnRange(CompoundTag tag) {
		return tag.getShort(SPAWN_RANGE_KEY);
	}

	public static boolean hasSpawnRange(CompoundTag tag) {
		return hasKey(tag, SPAWN_RANGE_KEY);
	}

	public static void setSpawnCount(CompoundTag tag, short count) {
		tag.putShort(SPAWN_COUNT_KEY, count);
	}

	public static void removeSpawnCount(CompoundTag tag) {
		tag.remove(SPAWN_COUNT_KEY);
	}

	public static short getSpawnCount(CompoundTag tag) {
		return tag.getShort(SPAWN_COUNT_KEY);
	}

	public static boolean hasSpawnCount(CompoundTag tag) {
		return hasKey(tag, SPAWN_COUNT_KEY);
	}

	public static void setRequiredPlayerRange(CompoundTag tag, short playerRange) {
		tag.putShort(REQUIRED_PLAYER_RANGE_KEY, playerRange);
	}

	public static void removeRequiredPlayerRange(CompoundTag tag) {
		tag.remove(REQUIRED_PLAYER_RANGE_KEY);
	}

	public static short getRequiredPlayerRange(CompoundTag tag) {
		return tag.getShort(REQUIRED_PLAYER_RANGE_KEY);
	}

	public static boolean hasRequiredPlayerRange(CompoundTag tag) {
		return hasKey(tag, REQUIRED_PLAYER_RANGE_KEY);
	}

	public static void setMaxNearbyEntities(CompoundTag tag, short maxNearbyEntities) {
		tag.putShort(MAX_NEARBY_ENTITIES_KEY, maxNearbyEntities);
	}

	public static void removeMaxNearbyEntities(CompoundTag tag) {
		tag.remove(MAX_NEARBY_ENTITIES_KEY);
	}

	public static short getMaxNearbyEntities(CompoundTag tag) {
		return tag.getShort(MAX_NEARBY_ENTITIES_KEY);
	}

	public static boolean hasMaxNearbyEntities(CompoundTag tag) {
		return hasKey(tag, MAX_NEARBY_ENTITIES_KEY);
	}

	public static void setSpawnerStats(CompoundTag tag, short range, short count, short playerRange,
			short maxNearbyEntities) {
		tag.putShort(SPAWN_RANGE_KEY, range);
		tag.putShort(SPAWN_COUNT_KEY, count);
		tag.putShort(REQUIRED_PLAYER_RANGE_KEY, playerRange);
		tag.putShort(MAX_NEARBY_ENTITIES_KEY, maxNearbyEntities);
	}

	public static void setX(CompoundTag tag, int x) {
		tag.putInt("x", x);
	}

	public static void setY(CompoundTag tag, int y) {
		tag.putInt("y", y);
	}

	public static void setZ(CompoundTag tag, int z) {
		tag.putInt("z", z);
	}

	public static void setPos(CompoundTag tag, int x, int y, int z) {
		tag.putInt("x", x);
		tag.putInt("y", y);
		tag.putInt("z", z);
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
