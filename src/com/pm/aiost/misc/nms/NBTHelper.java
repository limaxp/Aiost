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
import java.util.function.Consumer;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R4.CraftRegistry;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R4.util.CraftChatMessage;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.item.custom.NMSItems;
import com.pm.aiost.misc.log.Logger;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class NBTHelper {

	public static final String DISPLAY_KEY = "display";
	public static final String NAME_KEY = "Name";
	public static final String LORE_KEY = "Lore";
	public static final String COLOR_KEY = "color";
	public static final String ENCHANTMENTS_KEY = "Enchantments";
	public static final String ID_KEY = "id";
	public static final String LEVEL_KEY = "lvl";
	public static final String BLOCK_ENTITY_KEY = "BlockEntityTag";
	public static final String PATTERNS_KEY = "Patterns";
	public static final String PATTERN_KEY = "Pattern";
	public static final String SKULL_OWNER_KEY = "SkullOwner";
	public static final String PROPERTIES_KEY = "Properties";
	public static final String TEXTURES_KEY = "textures";
	public static final String SIGNATURE_KEY = "Signature";
	public static final String VALUE_KEY = "Value";
	public static final String HIDE_FLAGS_KEY = "HideFlags";
	public static final String CAN_DESTROY_KEY = "CanDestroy";
	public static final String CAN_PLACE_ON_KEY = "CanPlaceOn";
	public static final String ITEMS_KEY = "Items";
	public static final String ARMOR_ITEMS_KEY = "ArmorItems";
	public static final String UNBREAKABLE_KEY = "Unbreakable";
	public static final String COUNT_KEY = "Count";
	public static final String SLOT_KEY = "Slot";
	public static final String TAG_KEY = "tag";
	public static final String DAMAGE_KEY = "Damage";
	public static final String CUSTOM_MODEL_DATA_KEY = "CustomModelData";
	public static final String ENTITY_TAG_KEY = "EntityTag";
	public static final String SPAWN_DATA_KEY = "SpawnData";
	public static final String CUSTOM_NAME_KEY = "CustomName";
	public static final String CUSTOM_NAME_VISIBLE_KEY = "CustomNameVisible";
	public static final String NO_AI_KEY = "NoAI";
	public static final String INVISIBLE_KEY = "Invisible";
	public static final String SILENT_KEY = "Silent";
	public static final String MARKER_KEY = "Marker";
	public static final String ATTRIBUTE_MODIFIERS_KEY = "AttributeModifiers";
	public static final String ATTRIBUTE_NAME_KEY = "AttributeName";
	public static final String AMOUNT_KEY = "Amount";
	public static final String OPERATION_KEY = "Operation";
	public static final String UUID_LEAST_KEY = "UUIDLeast";
	public static final String UUID_MOST_KEY = "UUIDMost";
	public static final String SPAWN_RANGE_KEY = "SpawnRange";
	public static final String SPAWN_COUNT_KEY = "SpawnCount";
	public static final String REQUIRED_PLAYER_RANGE_KEY = "RequiredPlayerRange";
	public static final String MAX_NEARBY_ENTITIES_KEY = "MaxNearbyEntities";
	public static final String ITEM_EFFECT_KEY = "ITEM_EFFECT";
	public static final String WORLD_EFFECT_KEY = "WORLD_EFFECT";

	public static boolean hasKey(CompoundTag tag, String key) {
		return tag.get(key) != null;
	}

	public static CompoundTag getNBT(ItemStack is) {
		return getNBT(NMS.getNMS(is));
	}

	public static CompoundTag getNBT(net.minecraft.world.item.ItemStack is) {
		return (CompoundTag) is.save(CraftRegistry.getMinecraftRegistry());
	}

	public static ItemStack setNBT(net.minecraft.world.item.ItemStack is, CompoundTag nbtTag) {
		Optional<net.minecraft.world.item.ItemStack> optResult = net.minecraft.world.item.ItemStack
				.parse(CraftRegistry.getMinecraftRegistry(), nbtTag);
		if (optResult.isEmpty())
			return NMS.getBukkit(is);
		return NMS.getBukkit(optResult.get());
	}

	public static boolean hasTag(ItemStack is) {
		return hasTag(NMS.getNMS(is));
	}

	public static boolean hasTag(net.minecraft.world.item.ItemStack is) {
		return is.getComponents() != null; // TODO NOT NEEDED!
	}

	public static boolean hasKey(DataComponentMap tag, DataComponentType<?> type) {
		return tag.has(type);
	}

	public static ItemStack modifyNBT(ItemStack is, Consumer<CompoundTag> consumer) {
		return modifyNBT(NMS.getNMS(is), consumer);
	}

	public static ItemStack modifyNBT(net.minecraft.world.item.ItemStack is, Consumer<CompoundTag> consumer) {
		CompoundTag nbtTag = getNBT(is);
		consumer.accept(nbtTag);
		setNBT(is, nbtTag);
		return NMS.getBukkit(is);
	}

	public static CompoundTag getNBT(net.minecraft.world.entity.Entity entity) {
		CompoundTag nbtTag = new CompoundTag();
		entity.saveWithoutId(nbtTag);
		return nbtTag;
	}

	public static void setNBT(net.minecraft.world.entity.Entity entity, CompoundTag nbtTag) {
		entity.load(nbtTag);
	}

	public static void modifyNBT(Entity entity, Consumer<CompoundTag> consumer) {
		modifyNBT(NMS.getNMS(entity), consumer);
	}

	public static void modifyNBT(net.minecraft.world.entity.Entity entity, Consumer<CompoundTag> consumer) {
		CompoundTag nbtTag = getNBT(entity);
		consumer.accept(nbtTag);
		setNBT(entity, nbtTag);
	}

	public static CompoundTag fromString(String s) {
		try {
			return TagParser.parseTag(s);
		} catch (CommandSyntaxException e) {
			Logger.err("NBTHelper: Error on parsing nbt string '" + s + "'", e);
			return new CompoundTag();
		}
	}

	public static byte[] toBytes(CompoundTag nbtTag) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				DataOutputStream dos = new DataOutputStream(baos)) {
			nbtTag.write(dos);
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

	public static boolean toFile(File effectFile, CompoundTag nbt) {
		try (DataOutputStream dau = new DataOutputStream(
				new BufferedOutputStream(new DeflaterOutputStream(new FileOutputStream(effectFile))))) {
			nbt.write(dau);
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

	public static CompoundTag getOrAddDisplay(CompoundTag nbtTag) {
		if (hasKey(nbtTag, DISPLAY_KEY))
			return nbtTag.getCompound(DISPLAY_KEY);
		else
			return addDisplay(nbtTag);
	}

	public static CompoundTag addDisplay(CompoundTag nbtTag) {
		CompoundTag display = new CompoundTag();
		nbtTag.put(DISPLAY_KEY, display);
		return display;
	}

	public static void removeDisplay(CompoundTag nbtTag) {
		nbtTag.remove(DISPLAY_KEY);
	}

	public static CompoundTag getDisplay(CompoundTag nbtTag) {
		return nbtTag.getCompound(DISPLAY_KEY);
	}

	public static boolean hasDisplay(CompoundTag nbtTag) {
		return hasKey(nbtTag, DISPLAY_KEY);
	}

	public static void setDisplayName(CompoundTag display, String name) {
		display.put(NAME_KEY, StringTag.valueOf(CraftChatMessage.fromStringToJSON(name)));
	}

	public static void removeDisplayName(CompoundTag display) {
		display.remove(NAME_KEY);
	}

	public static String getDisplayName(CompoundTag display) {
		return ((StringTag) display.get(NAME_KEY)).getAsString();
	}

	public static boolean hasDisplayName(CompoundTag display) {
		return hasKey(display, NAME_KEY);
	}

	public static void setLore(CompoundTag display, List<String> lore) {
		ListTag nbtList = new ListTag();
		int size = lore.size();
		for (int i = 0; i < size; i++)
			nbtList.add(StringTag.valueOf(CraftChatMessage.fromStringToJSON(lore.get(i))));
		display.put(LORE_KEY, nbtList);
	}

	public static void removeLore(CompoundTag display) {
		display.remove(LORE_KEY);
	}

	public static ListTag getLore(CompoundTag display) {
		return display.getList(LORE_KEY, NBTType.STRING);
	}

	public static String getLore(CompoundTag display, int index) {
		return display.getList(LORE_KEY, NBTType.STRING).get(index).getAsString();
	}

	public static boolean hasLore(CompoundTag display) {
		return hasKey(display, LORE_KEY);
	}

	public static void setColor(CompoundTag display, int color) {
		display.putInt(COLOR_KEY, color);
	}

	public static void removeColor(CompoundTag display) {
		display.remove(COLOR_KEY);
	}

	public static int getColor(CompoundTag display) {
		return display.getInt(COLOR_KEY);
	}

	public static boolean hasColor(CompoundTag display) {
		return hasKey(display, COLOR_KEY);
	}

	public static ListTag addEnchantmentList(CompoundTag nbtTag) {
		ListTag ench = new ListTag();
		nbtTag.put(ENCHANTMENTS_KEY, ench);
		return ench;
	}

	public static void removeEnchantmentList(CompoundTag nbtTag) {
		nbtTag.remove(ENCHANTMENTS_KEY);
	}

	public static ListTag getEnchantmentList(CompoundTag nbtTag) {
		if (hasKey(nbtTag, ENCHANTMENTS_KEY))
			return nbtTag.getList(ENCHANTMENTS_KEY, NBTType.COMPOUND);
		return addEnchantmentList(nbtTag);
	}

	public static boolean hasEnchantmentList(CompoundTag nbtTag) {
		return hasKey(nbtTag, ENCHANTMENTS_KEY);
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

	public static CompoundTag addBlockEntity(CompoundTag nbtTag) {
		CompoundTag blockEntityTag = new CompoundTag();
		nbtTag.put(BLOCK_ENTITY_KEY, blockEntityTag);
		return blockEntityTag;
	}

	public static void removeBlockEntity(CompoundTag nbtTag) {
		nbtTag.remove(BLOCK_ENTITY_KEY);
	}

	public static CompoundTag getBlockEntity(CompoundTag nbtTag) {
		return nbtTag.getCompound(BLOCK_ENTITY_KEY);
	}

	public static boolean hasBlockEntity(CompoundTag nbtTag) {
		return hasKey(nbtTag, BLOCK_ENTITY_KEY);
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

	public static CompoundTag addSkullOwner(CompoundTag nbtTag) {
		CompoundTag skullOwner = new CompoundTag();
		nbtTag.put(SKULL_OWNER_KEY, skullOwner);
		return skullOwner;
	}

	public static void removeSkullOwner(CompoundTag nbtTag) {
		nbtTag.remove(SKULL_OWNER_KEY);
	}

	public static CompoundTag getSkullOwner(CompoundTag nbtTag) {
		return nbtTag.getCompound(SKULL_OWNER_KEY);
	}

	public static boolean hasSkullOwner(CompoundTag nbtTag) {
		return hasKey(nbtTag, SKULL_OWNER_KEY);
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

	public static void setHideFlags(CompoundTag nbtTag, int hideFlags) {
		nbtTag.putInt(HIDE_FLAGS_KEY, hideFlags);
	}

	public static void removeHideFlags(CompoundTag nbtTag) {
		nbtTag.remove(HIDE_FLAGS_KEY);
	}

	public static int getHideFlags(CompoundTag nbtTag) {
		return nbtTag.getInt(HIDE_FLAGS_KEY);
	}

	public static boolean hasHideFlags(CompoundTag nbtTag) {
		return hasKey(nbtTag, HIDE_FLAGS_KEY);
	}

	public static void addHideFlag(CompoundTag nbtTag, int hideFlag) {
		nbtTag.putInt(HIDE_FLAGS_KEY, nbtTag.getInt(HIDE_FLAGS_KEY) + hideFlag);
	}

	public static void removeHideFlag(CompoundTag nbtTag, int hideFlag) {
		nbtTag.putInt(HIDE_FLAGS_KEY, nbtTag.getInt(HIDE_FLAGS_KEY) - hideFlag);
	}

	public static boolean hasHideFlag(CompoundTag nbtTag, int hideFlag) {
		return (nbtTag.getInt(HIDE_FLAGS_KEY) & hideFlag) > 0;
	}

	public static boolean switchHideFlag(CompoundTag nbtTag, int hideFlag) {
		if (hasHideFlag(nbtTag, hideFlag)) {
			removeHideFlag(nbtTag, hideFlag);
			return false;
		} else {
			addHideFlag(nbtTag, hideFlag);
			return true;
		}
	}

	public static ListTag addCanDestroyList(CompoundTag nbtTag) {
		ListTag canDestroy = new ListTag();
		nbtTag.put(CAN_DESTROY_KEY, canDestroy);
		return canDestroy;
	}

	public static void removeCanDestroyList(CompoundTag nbtTag) {
		nbtTag.remove(CAN_DESTROY_KEY);
	}

	public static ListTag getCanDestroyList(CompoundTag nbtTag) {
		if (hasKey(nbtTag, CAN_DESTROY_KEY))
			return nbtTag.getList(CAN_DESTROY_KEY, NBTType.STRING);
		else
			return addCanDestroyList(nbtTag);
	}

	public static boolean hasCanDestroyList(CompoundTag nbtTag) {
		return hasKey(nbtTag, CAN_DESTROY_KEY);
	}

	public static void setCanDestroy(CompoundTag nbtTag, Material... materials) {
		ListTag canDestroy = addCanDestroyList(nbtTag);
		for (Material mat : materials)
			canDestroy.add(StringTag.valueOf(materialToString(mat)));
	}

	public static void setCanDestroy(CompoundTag nbtTag, String... materials) {
		ListTag canDestroy = addCanDestroyList(nbtTag);
		for (String mat : materials)
			canDestroy.add(StringTag.valueOf(mat));
	}

	public static ListTag addCanPlaceOnList(CompoundTag nbtTag) {
		ListTag list = new ListTag();
		nbtTag.put(CAN_PLACE_ON_KEY, list);
		return list;
	}

	public static void removeCanPlaceOnList(CompoundTag nbtTag) {
		nbtTag.remove(CAN_PLACE_ON_KEY);
	}

	public static ListTag getCanPlaceOnList(CompoundTag nbtTag) {
		if (hasKey(nbtTag, CAN_PLACE_ON_KEY))
			return nbtTag.getList(CAN_PLACE_ON_KEY, NBTType.STRING);
		else
			return addCanPlaceOnList(nbtTag);
	}

	public static boolean hasCanPlaceOnList(CompoundTag nbtTag) {
		return hasKey(nbtTag, CAN_PLACE_ON_KEY);
	}

	public static void setCanPlaceOn(CompoundTag nbtTag, Material... materials) {
		ListTag canPlaceOn = addCanPlaceOnList(nbtTag);
		for (Material mat : materials)
			canPlaceOn.add(StringTag.valueOf(materialToString(mat)));
	}

	public static void setCanPlaceOn(CompoundTag nbtTag, String... materials) {
		ListTag canPlaceOn = addCanPlaceOnList(nbtTag);
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
		addItem(items, NMSItems.getKey(nmsItem).getNamespace(), count, fromString(nbt));
	}

	public static void addItem(ListTag items, String mat, byte count, String nbt) {
		addItem(items, mat, count, fromString(nbt));
	}

	public static void addItem(ListTag items, Material mat, byte count, CompoundTag nbt) {
		addItem(items, materialToString(mat), count, nbt);
	}

	public static void addItem(ListTag items, Item nmsItem, byte count, CompoundTag nbt) {
		addItem(items, NMSItems.getKey(nmsItem).getNamespace(), count, nbt);
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
		addItem(items, NMSItems.getKey(nmsItem).getNamespace(), count);
	}

	public static void addItem(ListTag items, String mat, byte count) {
		CompoundTag item = new CompoundTag();
		setItem(item, mat, count);
		items.add(item);
	}

	public static void addItem(ListTag items, ItemStack is) {
		addItem(items, NMS.getNMS(is));
	}

	public static void addItem(ListTag items, net.minecraft.world.item.ItemStack is) {
		items.add(is.save(CraftRegistry.getMinecraftRegistry()));
	}

	public static void setItem(CompoundTag item, Material mat, byte count, String nbt) {
		setItem(item, materialToString(mat), count, fromString(nbt));
	}

	public static void setItem(CompoundTag item, Item nmsItem, byte count, String nbt) {
		setItem(item, NMSItems.getKey(nmsItem).getNamespace(), count, fromString(nbt));
	}

	public static void setItem(CompoundTag item, String mat, byte count, String nbt) {
		setItem(item, mat, count, fromString(nbt));
	}

	public static void setItem(CompoundTag item, Material mat, byte count, CompoundTag nbt) {
		setItem(item, materialToString(mat), count, nbt);
	}

	public static void setItem(CompoundTag item, Item nmsItem, byte count, CompoundTag nbt) {
		setItem(item, NMSItems.getKey(nmsItem).getNamespace(), count, nbt);
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
		setItem(item, NMSItems.getKey(nmsItem).getNamespace(), count);
	}

	public static void setItem(CompoundTag item, String mat, byte count) {
		item.putString(ID_KEY, mat);
		item.putByte(COUNT_KEY, count);
	}

	public static void setItem(CompoundTag item, ItemStack is) {
		setItem(item, NMS.getNMS(is));
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

	public static CompoundTag saveItem(CompoundTag item, ItemStack is) {
		return saveItem(item, NMS.getNMS(is));
	}

	public static CompoundTag saveItem(CompoundTag item, net.minecraft.world.item.ItemStack is) {
		return (CompoundTag) is.save(CraftRegistry.getMinecraftRegistry(), item);
	}

	public static ItemStack loadItem(CompoundTag item) {
		return CraftItemStack.asBukkitCopy(loadNMSItem(item));
	}

	public static net.minecraft.world.item.ItemStack loadNMSItem(CompoundTag item) {
		Optional<net.minecraft.world.item.ItemStack> optResult = net.minecraft.world.item.ItemStack
				.parse(CraftRegistry.getMinecraftRegistry(), item);
		if (optResult.isEmpty())
			return new net.minecraft.world.item.ItemStack(Items.AIR);
		return optResult.get();
	}

	public static void setUnbreakable(CompoundTag nbtTag, boolean unbreakable) {
		nbtTag.putBoolean(UNBREAKABLE_KEY, unbreakable);
	}

	public static void getUnbreakable(CompoundTag nbtTag) {
		nbtTag.getBoolean(UNBREAKABLE_KEY);
	}

	public static void hasUnbreakable(CompoundTag nbtTag) {
		hasKey(nbtTag, UNBREAKABLE_KEY);
	}

	public static boolean switchUnbreakable(CompoundTag nbtTag) {
		if (nbtTag.getBoolean(UNBREAKABLE_KEY)) {
			nbtTag.putBoolean(UNBREAKABLE_KEY, false);
			return false;
		} else {
			nbtTag.putBoolean(UNBREAKABLE_KEY, true);
			return true;
		}
	}

	public static CompoundTag addEntityTag(CompoundTag nbtTag) {
		CompoundTag entityTag = new CompoundTag();
		nbtTag.put(ENTITY_TAG_KEY, entityTag);
		return entityTag;
	}

	public static void removeEntityTag(CompoundTag nbtTag) {
		nbtTag.remove(ENTITY_TAG_KEY);
	}

	public static CompoundTag getEntityTag(CompoundTag nbtTag) {
		return nbtTag.getCompound(ENTITY_TAG_KEY);
	}

	public static boolean hasEntityTag(CompoundTag nbtTag) {
		return hasKey(nbtTag, ENTITY_TAG_KEY);
	}

	public static CompoundTag addSpawnData(CompoundTag nbtTag) {
		CompoundTag entityTag = new CompoundTag();
		nbtTag.put(SPAWN_DATA_KEY, entityTag);
		return entityTag;
	}

	public static void removeSpawnData(CompoundTag nbtTag) {
		nbtTag.remove(SPAWN_DATA_KEY);
	}

	public static CompoundTag getSpawnData(CompoundTag nbtTag) {
		return nbtTag.getCompound(SPAWN_DATA_KEY);
	}

	public static boolean hasSpawnData(CompoundTag nbtTag) {
		return hasKey(nbtTag, SPAWN_DATA_KEY);
	}

	public static void setEntityId(CompoundTag entityTag, EntityType entityType) {
		setEntityId(entityTag, entityType.name());
	}

	public static void setEntityId(CompoundTag entityTag, net.minecraft.world.entity.EntityType<?> entityType) {
		setEntityId(entityTag, AiostEntityTypes.getKey(entityType).getNamespace());
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

	public static void setNameVisible(CompoundTag entityTag, boolean isVisible) {
		entityTag.putBoolean(CUSTOM_NAME_VISIBLE_KEY, isVisible);
	}

	public static void removeNameVisible(CompoundTag entityTag) {
		entityTag.remove(CUSTOM_NAME_VISIBLE_KEY);
	}

	public static boolean hasNameVisible(CompoundTag entityTag) {
		return entityTag.getBoolean(CUSTOM_NAME_VISIBLE_KEY);
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

	public static ListTag addAttributeModifiersList(CompoundTag nbtTag) {
		ListTag attributeModifiers = new ListTag();
		nbtTag.put(ATTRIBUTE_MODIFIERS_KEY, attributeModifiers);
		return attributeModifiers;
	}

	public static void removeAttributeModifiersList(CompoundTag nbtTag) {
		nbtTag.remove(ATTRIBUTE_MODIFIERS_KEY);
	}

	public static ListTag getAttributeModifiersList(CompoundTag nbtTag) {
		if (hasKey(nbtTag, ATTRIBUTE_MODIFIERS_KEY))
			return nbtTag.getList(ATTRIBUTE_MODIFIERS_KEY, NBTType.COMPOUND);
		return addAttributeModifiersList(nbtTag);
	}

	public static boolean hasAttributeModifiersList(CompoundTag nbtTag) {
		return hasKey(nbtTag, ATTRIBUTE_MODIFIERS_KEY);
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

	public static void setCustomModelData(CompoundTag nbtTag, int id) {
		nbtTag.putInt(CUSTOM_MODEL_DATA_KEY, id);
	}

	public static void removeCustomModelData(CompoundTag nbtTag) {
		nbtTag.remove(CUSTOM_MODEL_DATA_KEY);
	}

	public static int getCustomModelData(CompoundTag nbtTag) {
		return nbtTag.getInt(CUSTOM_MODEL_DATA_KEY);
	}

	public static boolean hasCustomModelData(CompoundTag nbtTag) {
		return hasKey(nbtTag, CUSTOM_MODEL_DATA_KEY);
	}

	public static void setSpawnRange(CompoundTag nbtTag, short range) {
		nbtTag.putShort(SPAWN_RANGE_KEY, range);
	}

	public static void removeSpawnRange(CompoundTag nbtTag) {
		nbtTag.remove(SPAWN_RANGE_KEY);
	}

	public static short getSpawnRange(CompoundTag nbtTag) {
		return nbtTag.getShort(SPAWN_RANGE_KEY);
	}

	public static boolean hasSpawnRange(CompoundTag nbtTag) {
		return hasKey(nbtTag, SPAWN_RANGE_KEY);
	}

	public static void setSpawnCount(CompoundTag nbtTag, short count) {
		nbtTag.putShort(SPAWN_COUNT_KEY, count);
	}

	public static void removeSpawnCount(CompoundTag nbtTag) {
		nbtTag.remove(SPAWN_COUNT_KEY);
	}

	public static short getSpawnCount(CompoundTag nbtTag) {
		return nbtTag.getShort(SPAWN_COUNT_KEY);
	}

	public static boolean hasSpawnCount(CompoundTag nbtTag) {
		return hasKey(nbtTag, SPAWN_COUNT_KEY);
	}

	public static void setRequiredPlayerRange(CompoundTag nbtTag, short playerRange) {
		nbtTag.putShort(REQUIRED_PLAYER_RANGE_KEY, playerRange);
	}

	public static void removeRequiredPlayerRange(CompoundTag nbtTag) {
		nbtTag.remove(REQUIRED_PLAYER_RANGE_KEY);
	}

	public static short getRequiredPlayerRange(CompoundTag nbtTag) {
		return nbtTag.getShort(REQUIRED_PLAYER_RANGE_KEY);
	}

	public static boolean hasRequiredPlayerRange(CompoundTag nbtTag) {
		return hasKey(nbtTag, REQUIRED_PLAYER_RANGE_KEY);
	}

	public static void setMaxNearbyEntities(CompoundTag nbtTag, short maxNearbyEntities) {
		nbtTag.putShort(MAX_NEARBY_ENTITIES_KEY, maxNearbyEntities);
	}

	public static void removeMaxNearbyEntities(CompoundTag nbtTag) {
		nbtTag.remove(MAX_NEARBY_ENTITIES_KEY);
	}

	public static short getMaxNearbyEntities(CompoundTag nbtTag) {
		return nbtTag.getShort(MAX_NEARBY_ENTITIES_KEY);
	}

	public static boolean hasMaxNearbyEntities(CompoundTag nbtTag) {
		return hasKey(nbtTag, MAX_NEARBY_ENTITIES_KEY);
	}

	public static void setSpawnerStats(CompoundTag nbtTag, short range, short count, short playerRange,
			short maxNearbyEntities) {
		nbtTag.putShort(SPAWN_RANGE_KEY, range);
		nbtTag.putShort(SPAWN_COUNT_KEY, count);
		nbtTag.putShort(REQUIRED_PLAYER_RANGE_KEY, playerRange);
		nbtTag.putShort(MAX_NEARBY_ENTITIES_KEY, maxNearbyEntities);
	}

	public static void setX(CompoundTag nbtTag, int x) {
		nbtTag.putInt("x", x);
	}

	public static void setY(CompoundTag nbtTag, int y) {
		nbtTag.putInt("y", y);
	}

	public static void setZ(CompoundTag nbtTag, int z) {
		nbtTag.putInt("z", z);
	}

	public static void setPos(CompoundTag nbtTag, int x, int y, int z) {
		nbtTag.putInt("x", x);
		nbtTag.putInt("y", y);
		nbtTag.putInt("z", z);
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

	public static void setItemEffect(CompoundTag nbtTag, int effectId) {
		nbtTag.putInt(ITEM_EFFECT_KEY, effectId);
	}

	public static void removeItemEffect(CompoundTag nbtTag) {
		nbtTag.remove(ITEM_EFFECT_KEY);
	}

	public static int getItemEffect(CompoundTag nbtTag) {
		return nbtTag.getInt(ITEM_EFFECT_KEY);
	}

	public static boolean hasItemEffect(CompoundTag nbtTag) {
		return hasKey(nbtTag, ITEM_EFFECT_KEY);
	}

	public static ItemStack setItemEffect(ItemStack is, int id) {
		net.minecraft.world.item.ItemStack nmsItem = NMS.getNMS(is);
		CompoundTag nbtTag = NBTHelper.getNBT(nmsItem);
		NBTHelper.setItemEffect(nbtTag, id);
		return NBTHelper.setNBT(nmsItem, nbtTag);
	}

	public static void setWorldEffect(CompoundTag nbtTag, int effectId) {
		nbtTag.putInt(WORLD_EFFECT_KEY, effectId);
	}

	public static void removeWorldEffect(CompoundTag nbtTag) {
		nbtTag.remove(WORLD_EFFECT_KEY);
	}

	public static int getWorldEffect(CompoundTag nbtTag) {
		return nbtTag.getInt(WORLD_EFFECT_KEY);
	}

	public static boolean hasWorldEffect(CompoundTag nbtTag) {
		return hasKey(nbtTag, WORLD_EFFECT_KEY);
	}

	public static ItemStack setWorldEffect(ItemStack is, int id) {
		net.minecraft.world.item.ItemStack nmsItem = NMS.getNMS(is);
		CompoundTag nbtTag = NBTHelper.getNBT(nmsItem);
		NBTHelper.setWorldEffect(nbtTag, id);
		return NBTHelper.setNBT(nmsItem, nbtTag);
	}

	public static void setDurability(CompoundTag nbtTag, short durability) {
		nbtTag.putShort(DAMAGE_KEY, durability);
	}

	public static void removeDurability(CompoundTag nbtTag) {
		nbtTag.remove(DAMAGE_KEY);
	}

	public static short getDurability(CompoundTag nbtTag) {
		return nbtTag.getShort(DAMAGE_KEY);
	}

	public static boolean hasDurability(CompoundTag nbtTag) {
		return hasKey(nbtTag, DAMAGE_KEY);
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
}
