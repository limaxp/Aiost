package com.pm.aiost.misc.utils.nbt;

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
import java.util.function.Consumer;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftChatMessage;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pm.aiost.item.nms.NMSItems;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.utils.nms.NMS;

import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.Item;
import net.minecraft.server.v1_15_R1.MojangsonParser;
import net.minecraft.server.v1_15_R1.NBTReadLimiter;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.NBTTagList;
import net.minecraft.server.v1_15_R1.NBTTagString;

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

	public static NBTTagCompound getNBT(ItemStack is) {
		return getNBT(NMS.getNMS(is));
	}

	public static NBTTagCompound getNBT(net.minecraft.server.v1_15_R1.ItemStack is) {
		return is.hasTag() ? is.getTag() : new NBTTagCompound();
	}

	public static NBTTagCompound getNBT(net.minecraft.server.v1_15_R1.Entity entity) {
		NBTTagCompound nbtTag = new NBTTagCompound();
		entity.c(nbtTag);
		return nbtTag;
	}

	public static ItemStack setNBT(net.minecraft.server.v1_15_R1.ItemStack is, NBTTagCompound nbtTag) {
		is.setTag(nbtTag);
		return CraftItemStack.asCraftMirror(is);
	}

	public static void setNBT(net.minecraft.server.v1_15_R1.Entity entity, NBTTagCompound nbtTag) {
		entity.f(nbtTag);
	}

	public static ItemStack modifyNBT(ItemStack is, Consumer<NBTTagCompound> consumer) {
		return modifyNBT(NMS.getNMS(is), consumer);
	}

	public static ItemStack modifyNBT(net.minecraft.server.v1_15_R1.ItemStack is, Consumer<NBTTagCompound> consumer) {
		NBTTagCompound nbtTag = getNBT(is);
		consumer.accept(nbtTag);
		is.setTag(nbtTag);
		return NMS.getBukkit(is);
	}

	public static void modifyNBT(Entity entity, Consumer<NBTTagCompound> consumer) {
		modifyNBT(NMS.getNMS(entity), consumer);
	}

	public static void modifyNBT(net.minecraft.server.v1_15_R1.Entity entity, Consumer<NBTTagCompound> consumer) {
		NBTTagCompound nbtTag = getNBT(entity);
		consumer.accept(nbtTag);
		setNBT(entity, nbtTag);
	}

	public static NBTTagCompound fromString(String s) {
		try {
			return MojangsonParser.parse(s);
		} catch (CommandSyntaxException e) {
			Logger.err("NBTHelper: Error on parsing nbt string '" + s + "'", e);
			return new NBTTagCompound();
		}
	}

	public static byte[] toBytes(NBTTagCompound nbtTag) {
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

	public static NBTTagCompound fromBytes(byte[] bytes) {
		try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes))) {
			return NBTTagCompound.a.b(dis, 0, NBTReadLimiter.a);
		} catch (IOException e) {
			Logger.err("NBTHelper: Error on reading nbt from byte array", e);
			return new NBTTagCompound();
		}
	}

	public static boolean toFile(File effectFile, NBTTagCompound nbt) {
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

	public static NBTTagCompound fromFile(File effectFile) {
		try (DataInputStream dis = new DataInputStream(
				new BufferedInputStream(new InflaterInputStream(new FileInputStream(effectFile))))) {
			return NBTTagCompound.a.b(dis, 0, NBTReadLimiter.a);
		} catch (FileNotFoundException e) {
			Logger.err("NBTHelper: Error! No file found for name: " + effectFile.getName(), e);
		} catch (IOException e) {
			Logger.err("NBTHelper: Error on loading file with name: " + effectFile.getName(), e);
		}
		return new NBTTagCompound();
	}

	public static NBTTagCompound getOrAddDisplay(NBTTagCompound nbtTag) {
		if (nbtTag.hasKey(DISPLAY_KEY))
			return nbtTag.getCompound(DISPLAY_KEY);
		else
			return addDisplay(nbtTag);
	}

	public static NBTTagCompound addDisplay(NBTTagCompound nbtTag) {
		NBTTagCompound display = new NBTTagCompound();
		nbtTag.set(DISPLAY_KEY, display);
		return display;
	}

	public static void removeDisplay(NBTTagCompound nbtTag) {
		nbtTag.remove(DISPLAY_KEY);
	}

	public static NBTTagCompound getDisplay(NBTTagCompound nbtTag) {
		return nbtTag.getCompound(DISPLAY_KEY);
	}

	public static boolean hasDisplay(NBTTagCompound nbtTag) {
		return nbtTag.hasKey(DISPLAY_KEY);
	}

	public static void setDisplayName(NBTTagCompound display, String name) {
		display.set(NAME_KEY, NBTTagString.a(CraftChatMessage.toJSON(CraftChatMessage.wrapOrNull(name))));
	}

	public static void removeDisplayName(NBTTagCompound display) {
		display.remove(NAME_KEY);
	}

	public static String getDisplayName(NBTTagCompound display) {
		return ((NBTTagString) display.get(NAME_KEY)).asString();
	}

	public static boolean hasDisplayName(NBTTagCompound display) {
		return display.hasKey(NAME_KEY);
	}

	public static void setLore(NBTTagCompound display, List<String> lore) {
		NBTTagList nbtList = new NBTTagList();
		int size = lore.size();
		for (int i = 0; i < size; i++)
			nbtList.add(NBTTagString.a(CraftChatMessage.toJSON(CraftChatMessage.wrapOrNull(lore.get(i)))));
		display.set(LORE_KEY, nbtList);
	}

	public static void removeLore(NBTTagCompound display) {
		display.remove(LORE_KEY);
	}

	public static NBTTagList getLore(NBTTagCompound display) {
		return display.getList(LORE_KEY, NBTType.STRING);
	}

	public static String getLore(NBTTagCompound display, int index) {
		return display.getList(LORE_KEY, NBTType.STRING).get(index).asString();
	}

	public static boolean hasLore(NBTTagCompound display) {
		return display.hasKey(LORE_KEY);
	}

	public static void setColor(NBTTagCompound display, int color) {
		display.setInt(COLOR_KEY, color);
	}

	public static void removeColor(NBTTagCompound display) {
		display.remove(COLOR_KEY);
	}

	public static int getColor(NBTTagCompound display) {
		return display.getInt(COLOR_KEY);
	}

	public static boolean hasColor(NBTTagCompound display) {
		return display.hasKey(COLOR_KEY);
	}

	public static NBTTagList addEnchantmentList(NBTTagCompound nbtTag) {
		NBTTagList ench = new NBTTagList();
		nbtTag.set(ENCHANTMENTS_KEY, ench);
		return ench;
	}

	public static void removeEnchantmentList(NBTTagCompound nbtTag) {
		nbtTag.remove(ENCHANTMENTS_KEY);
	}

	public static NBTTagList getEnchantmentList(NBTTagCompound nbtTag) {
		if (nbtTag.hasKey(ENCHANTMENTS_KEY))
			return nbtTag.getList(ENCHANTMENTS_KEY, NBTType.COMPOUND);
		return addEnchantmentList(nbtTag);
	}

	public static boolean hasEnchantmentList(NBTTagCompound nbtTag) {
		return nbtTag.hasKey(ENCHANTMENTS_KEY);
	}

	public static void addEnchantment(NBTTagList ench, String id, short level) {
		NBTTagCompound enchantment = new NBTTagCompound();
		enchantment.setString(ID_KEY, id);
		enchantment.setShort(LEVEL_KEY, level);
		ench.add(enchantment);
	}

	public static void setEnchantment(NBTTagList ench, String id, short level) {
		for (int i = 0; i < ench.size(); i++) {
			if (ench.getCompound(i).getString(ID_KEY).equals(id)) {
				NBTTagCompound enchantment = ench.getCompound(i);
				if (enchantment.getShort(LEVEL_KEY) != level)
					enchantment.setShort(LEVEL_KEY, level);
				return;
			}
		}
		addEnchantment(ench, id, level);
	}

	public static void removeEnchantment(NBTTagList ench, String id) {
		for (int i = 0; i < ench.size(); i++) {
			if (ench.getCompound(i).getString(ID_KEY).equals(id)) {
				ench.remove(i);
				break;
			}
		}
	}

	public static NBTTagCompound getEnchantment(NBTTagList ench, String id) {
		for (int i = 0; i < ench.size(); i++) {
			if (ench.getCompound(i).getString(ID_KEY).equals(id))
				return ench.getCompound(i);
		}
		return null;
	}

	public static boolean hasEnchantment(NBTTagList ench, String id) {
		for (int i = 0; i < ench.size(); i++) {
			if (ench.getCompound(i).getString(ID_KEY).equals(id))
				return true;
		}
		return false;
	}

	public static short getEnchantmentLevel(NBTTagList ench, String id) {
		for (int i = 0; i < ench.size(); i++) {
			if (ench.getCompound(i).getString(ID_KEY).equals(id))
				return ench.getCompound(i).getShort(LEVEL_KEY);
		}
		return 0;
	}

	public static String getEnchantmentId(NBTTagCompound enchantment) {
		return enchantment.getString(ID_KEY);
	}

	public static short getEnchantmentLevel(NBTTagCompound enchantment) {
		return enchantment.getShort(LEVEL_KEY);
	}

	public static NBTTagCompound addBlockEntity(NBTTagCompound nbtTag) {
		NBTTagCompound blockEntityTag = new NBTTagCompound();
		nbtTag.set(BLOCK_ENTITY_KEY, blockEntityTag);
		return blockEntityTag;
	}

	public static void removeBlockEntity(NBTTagCompound nbtTag) {
		nbtTag.remove(BLOCK_ENTITY_KEY);
	}

	public static NBTTagCompound getBlockEntity(NBTTagCompound nbtTag) {
		return nbtTag.getCompound(BLOCK_ENTITY_KEY);
	}

	public static boolean hasBlockEntity(NBTTagCompound nbtTag) {
		return nbtTag.hasKey(BLOCK_ENTITY_KEY);
	}

	public static NBTTagList addPatternList(NBTTagCompound blockEntityTag) {
		NBTTagList patterns = new NBTTagList();
		blockEntityTag.set(PATTERNS_KEY, patterns);
		return patterns;
	}

	public static void removePatternList(NBTTagCompound blockEntityTag) {
		blockEntityTag.remove(PATTERNS_KEY);
	}

	public static NBTTagList getPatternList(NBTTagCompound blockEntityTag) {
		return blockEntityTag.getList(PATTERNS_KEY, NBTType.COMPOUND);
	}

	public static boolean hasPatternList(NBTTagCompound blockEntityTag) {
		return blockEntityTag.hasKey(PATTERNS_KEY);
	}

	public static void addPattern(NBTTagList patterns, String patternName, int color) {
		NBTTagCompound pattern = new NBTTagCompound();
		pattern.setString(PATTERN_KEY, patternName);
		pattern.setInt(COLOR_KEY, color);
		patterns.add(pattern);
	}

	public static void setPattern(NBTTagList patterns, String patternName, int color) {
		for (int i = 0; i < patterns.size(); i++) {
			if (patterns.getCompound(i).getString(PATTERN_KEY).equals(patternName)) {
				NBTTagCompound pattern = patterns.getCompound(i);
				if (pattern.getInt(COLOR_KEY) != color)
					pattern.setInt(COLOR_KEY, color);
				return;
			}
		}
		addPattern(patterns, patternName, color);
	}

	public static void removePattern(NBTTagList patterns, String pattern) {
		for (int i = 0; i < patterns.size(); i++) {
			if (patterns.getCompound(i).getString(PATTERN_KEY).equals(pattern)) {
				patterns.remove(i);
				break;
			}
		}
	}

	public static NBTTagCompound getPattern(NBTTagList patterns, String pattern) {
		for (int i = 0; i < patterns.size(); i++) {
			if (patterns.getCompound(i).getString(PATTERN_KEY).equals(pattern))
				return patterns.getCompound(i);
		}
		return null;
	}

	public static boolean hasPattern(NBTTagList patterns, String pattern) {
		for (int i = 0; i < patterns.size(); i++) {
			if (patterns.getCompound(i).getString(PATTERN_KEY).equals(pattern))
				return true;
		}
		return false;
	}

	public static int getPatternColor(NBTTagList patterns, String pattern) {
		for (int i = 0; i < patterns.size(); i++) {
			if (patterns.getCompound(i).getString(PATTERN_KEY).equals(pattern))
				return patterns.getCompound(i).getInt(COLOR_KEY);
		}
		return 0;
	}

	public static short getPatternName(NBTTagCompound pattern) {
		return pattern.getShort(PATTERN_KEY);
	}

	public static int getPatternColor(NBTTagCompound pattern) {
		return pattern.getInt(COLOR_KEY);
	}

	public static NBTTagCompound addSkullOwner(NBTTagCompound nbtTag) {
		NBTTagCompound skullOwner = new NBTTagCompound();
		nbtTag.set(SKULL_OWNER_KEY, skullOwner);
		return skullOwner;
	}

	public static void removeSkullOwner(NBTTagCompound nbtTag) {
		nbtTag.remove(SKULL_OWNER_KEY);
	}

	public static NBTTagCompound getSkullOwner(NBTTagCompound nbtTag) {
		return nbtTag.getCompound(SKULL_OWNER_KEY);
	}

	public static boolean hasSkullOwner(NBTTagCompound nbtTag) {
		return nbtTag.hasKey(SKULL_OWNER_KEY);
	}

	public static void setSkullOwnerId(NBTTagCompound skullOwner, String id) {
		skullOwner.setString(ID_KEY, id);
	}

	public static void removeSkullOwnerId(NBTTagCompound skullOwner) {
		skullOwner.remove(ID_KEY);
	}

	public static String getSkullOwnerId(NBTTagCompound skullOwner) {
		return skullOwner.getString(ID_KEY);
	}

	public static boolean hasSkullOwnerId(NBTTagCompound skullOwner) {
		return skullOwner.hasKey(ID_KEY);
	}

	public static void setSkullOwnerName(NBTTagCompound skullOwner, String name) {
		skullOwner.setString(NAME_KEY, name);
	}

	public static void removeSkullOwnerName(NBTTagCompound skullOwner) {
		skullOwner.remove(NAME_KEY);
	}

	public static String getSkullOwnerName(NBTTagCompound skullOwner) {
		return skullOwner.getString(NAME_KEY);
	}

	public static boolean hasSkullOwnerName(NBTTagCompound skullOwner) {
		return skullOwner.hasKey(NAME_KEY);
	}

	public static void setSkullTexture(NBTTagCompound skullOwner, String textureString) {
		NBTTagCompound properities = new NBTTagCompound();
		skullOwner.set(PROPERTIES_KEY, properities);
		NBTTagList textures = new NBTTagList();
		properities.set(TEXTURES_KEY, textures);
		NBTTagCompound texture = new NBTTagCompound();
		texture.setString(VALUE_KEY, textureString);
		textures.add(texture);
	}

	public static void setSkullTextures(NBTTagCompound skullOwner, List<String> textrueList) {
		NBTTagCompound properities = new NBTTagCompound();
		skullOwner.set(PROPERTIES_KEY, properities);
		NBTTagList textures = new NBTTagList();
		properities.set(TEXTURES_KEY, textures);
		for (String textrueListStrings : textrueList) {
			NBTTagCompound texture = new NBTTagCompound();
			String[] split0 = textrueListStrings.split(":\"");
			String[] split = split0[1].split("\"");
			texture.setString(SIGNATURE_KEY, split[0]);
			texture.setString(VALUE_KEY, split0[2].split("\"")[0]);
			textures.add(texture);
		}
	}

	public static void removeSkullTextures(NBTTagCompound skullOwner, List<String> textrueList) {
		skullOwner.remove(PROPERTIES_KEY);
	}

	public static NBTTagList getSkullTextures(NBTTagCompound skullOwner) {
		return skullOwner.getCompound(PROPERTIES_KEY).getList(TEXTURES_KEY, NBTType.COMPOUND);

	}

	public static NBTTagCompound getSkullTexture(NBTTagCompound skullOwner, int index) {
		return skullOwner.getList(LORE_KEY, NBTType.STRING).getCompound(index);
	}

	public static boolean hasSkullTextures(NBTTagCompound skullOwner) {
		if (skullOwner.hasKey(PROPERTIES_KEY)) {
			if (skullOwner.getCompound(PROPERTIES_KEY).hasKey(TEXTURES_KEY))
				return true;
		}
		return false;
	}

	public static void setHideFlags(NBTTagCompound nbtTag, int hideFlags) {
		nbtTag.setInt(HIDE_FLAGS_KEY, hideFlags);
	}

	public static void removeHideFlags(NBTTagCompound nbtTag) {
		nbtTag.remove(HIDE_FLAGS_KEY);
	}

	public static int getHideFlags(NBTTagCompound nbtTag) {
		return nbtTag.getInt(HIDE_FLAGS_KEY);
	}

	public static boolean hasHideFlags(NBTTagCompound nbtTag) {
		return nbtTag.hasKey(HIDE_FLAGS_KEY);
	}

	public static void addHideFlag(NBTTagCompound nbtTag, int hideFlag) {
		nbtTag.setInt(HIDE_FLAGS_KEY, nbtTag.getInt(HIDE_FLAGS_KEY) + hideFlag);
	}

	public static void removeHideFlag(NBTTagCompound nbtTag, int hideFlag) {
		nbtTag.setInt(HIDE_FLAGS_KEY, nbtTag.getInt(HIDE_FLAGS_KEY) - hideFlag);
	}

	public static boolean hasHideFlag(NBTTagCompound nbtTag, int hideFlag) {
		return (nbtTag.getInt(HIDE_FLAGS_KEY) & hideFlag) > 0;
	}

	public static boolean switchHideFlag(NBTTagCompound nbtTag, int hideFlag) {
		if (hasHideFlag(nbtTag, hideFlag)) {
			removeHideFlag(nbtTag, hideFlag);
			return false;
		} else {
			addHideFlag(nbtTag, hideFlag);
			return true;
		}
	}

	public static NBTTagList addCanDestroyList(NBTTagCompound nbtTag) {
		NBTTagList canDestroy = new NBTTagList();
		nbtTag.set(CAN_DESTROY_KEY, canDestroy);
		return canDestroy;
	}

	public static void removeCanDestroyList(NBTTagCompound nbtTag) {
		nbtTag.remove(CAN_DESTROY_KEY);
	}

	public static NBTTagList getCanDestroyList(NBTTagCompound nbtTag) {
		if (nbtTag.hasKey(CAN_DESTROY_KEY))
			return nbtTag.getList(CAN_DESTROY_KEY, NBTType.STRING);
		else
			return addCanDestroyList(nbtTag);
	}

	public static boolean hasCanDestroyList(NBTTagCompound nbtTag) {
		return nbtTag.hasKey(CAN_DESTROY_KEY);
	}

	public static void setCanDestroy(NBTTagCompound nbtTag, Material... materials) {
		NBTTagList canDestroy = addCanDestroyList(nbtTag);
		for (Material mat : materials)
			canDestroy.add(NBTTagString.a(materialToString(mat)));
	}

	public static void setCanDestroy(NBTTagCompound nbtTag, String... materials) {
		NBTTagList canDestroy = addCanDestroyList(nbtTag);
		for (String mat : materials)
			canDestroy.add(NBTTagString.a(mat));
	}

	public static NBTTagList addCanPlaceOnList(NBTTagCompound nbtTag) {
		NBTTagList list = new NBTTagList();
		nbtTag.set(CAN_PLACE_ON_KEY, list);
		return list;
	}

	public static void removeCanPlaceOnList(NBTTagCompound nbtTag) {
		nbtTag.remove(CAN_PLACE_ON_KEY);
	}

	public static NBTTagList getCanPlaceOnList(NBTTagCompound nbtTag) {
		if (nbtTag.hasKey(CAN_PLACE_ON_KEY))
			return nbtTag.getList(CAN_PLACE_ON_KEY, NBTType.STRING);
		else
			return addCanPlaceOnList(nbtTag);
	}

	public static boolean hasCanPlaceOnList(NBTTagCompound nbtTag) {
		return nbtTag.hasKey(CAN_PLACE_ON_KEY);
	}

	public static void setCanPlaceOn(NBTTagCompound nbtTag, Material... materials) {
		NBTTagList canPlaceOn = addCanPlaceOnList(nbtTag);
		for (Material mat : materials)
			canPlaceOn.add(NBTTagString.a(materialToString(mat)));
	}

	public static void setCanPlaceOn(NBTTagCompound nbtTag, String... materials) {
		NBTTagList canPlaceOn = addCanPlaceOnList(nbtTag);
		for (String mat : materials)
			canPlaceOn.add(NBTTagString.a(mat));
	}

	public static void addMaterial(NBTTagList list, Material mat) {
		addMaterial(list, materialToString(mat));
	}

	public static void addMaterial(NBTTagList list, String mat) {
		list.add(NBTTagString.a(mat));
	}

	public static void removeMaterial(NBTTagList list, Material mat) {
		removeMaterial(list, materialToString(mat));
	}

	public static void removeMaterial(NBTTagList list, String mat) {
		for (int i = 0; i < list.size(); i++) {
			if (list.getString(i).equals(mat)) {
				list.remove(i);
				break;
			}
		}
	}

	public static void hasMaterial(NBTTagList list, Material mat) {
		hasMaterial(list, materialToString(mat));
	}

	public static boolean hasMaterial(NBTTagList list, String mat) {
		for (int i = 0; i < list.size(); i++) {
			if (list.getString(i).equals(mat))
				return true;
		}
		return false;
	}

	public static NBTTagList addItemsList(NBTTagCompound blockEntityTag) {
		NBTTagList items = new NBTTagList();
		blockEntityTag.set(ITEMS_KEY, items);
		return items;
	}

	public static void removeItemsList(NBTTagCompound blockEntityTag) {
		blockEntityTag.remove(ITEMS_KEY);
	}

	public static NBTTagList getItemsList(NBTTagCompound blockEntityTag) {
		return blockEntityTag.getList(ITEMS_KEY, NBTType.COMPOUND);
	}

	public static boolean hasItemsList(NBTTagCompound blockEntityTag) {
		return blockEntityTag.hasKey(ITEMS_KEY);
	}

	public static NBTTagList addArmorItemsList(NBTTagCompound entityTag) {
		NBTTagList items = new NBTTagList();
		entityTag.set(ARMOR_ITEMS_KEY, items);
		return items;
	}

	public static void removeArmorItemsList(NBTTagCompound entityTag) {
		entityTag.remove(ARMOR_ITEMS_KEY);
	}

	public static NBTTagList getArmorItemsList(NBTTagCompound entityTag) {
		return entityTag.getList(ARMOR_ITEMS_KEY, NBTType.COMPOUND);
	}

	public static boolean hasArmorItemsList(NBTTagCompound entityTag) {
		return entityTag.hasKey(ARMOR_ITEMS_KEY);
	}

	public static void addEquipmentItem(NBTTagList items, byte slot, Material mat, short damage, byte count,
			String nbt) {
		addEquipmentItem(items, slot, materialToString(mat), damage, count, fromString(nbt));
	}

	public static void addEquipmentItem(NBTTagList items, byte slot, String mat, short damage, byte count, String nbt) {
		addEquipmentItem(items, slot, mat, damage, count, fromString(nbt));
	}

	public static void addEquipmentItem(NBTTagList items, byte slot, Material mat, short damage, byte count,
			NBTTagCompound nbt) {
		addEquipmentItem(items, slot, materialToString(mat), damage, count, nbt);
	}

	public static void addEquipmentItem(NBTTagList items, byte slot, String mat, short damage, byte count,
			NBTTagCompound nbt) {
		NBTTagCompound item = new NBTTagCompound();
		setEquipmentItem(item, slot, mat, damage, count, nbt);
		items.add(item);
	}

	public static void addEquipmentItem(NBTTagList items, byte slot, Material mat, short damage, byte count) {
		addEquipmentItem(items, slot, materialToString(mat), damage, count);
	}

	public static void addEquipmentItem(NBTTagList items, byte slot, String mat, short damage, byte count) {
		NBTTagCompound item = new NBTTagCompound();
		setEquipmentItem(item, slot, mat, damage, count);
		items.add(item);
	}

	public static void setEquipmentItem(NBTTagList items, byte slot, Material mat, short damage, byte count,
			String nbt) {
		setEquipmentItem(items, slot, materialToString(mat), damage, count, fromString(nbt));
	}

	public static void setEquipmentItem(NBTTagList items, byte slot, String mat, short damage, byte count, String nbt) {
		setEquipmentItem(items, slot, mat, damage, count, fromString(nbt));
	}

	public static void setEquipmentItem(NBTTagList items, byte slot, Material mat, short damage, byte count,
			NBTTagCompound nbt) {
		setEquipmentItem(items, slot, materialToString(mat), damage, count, nbt);
	}

	public static void setEquipmentItem(NBTTagList items, byte slot, String mat, short damage, byte count,
			NBTTagCompound nbt) {
		for (int i = 0; i < items.size(); i++) {
			if (items.getCompound(i).getByte(SLOT_KEY) == slot) {
				setEquipmentItem(items.getCompound(i), slot, mat, damage, count, nbt);
				return;
			}
		}
		addEquipmentItem(items, slot, mat, damage, count, nbt);
	}

	public static void setEquipmentItem(NBTTagList items, byte slot, Material mat, short damage, byte count) {
		setEquipmentItem(items, slot, materialToString(mat), damage, count);
	}

	public static void setEquipmentItem(NBTTagList items, byte slot, String mat, short damage, byte count) {
		for (int i = 0; i < items.size(); i++) {
			if (items.getCompound(i).getByte(SLOT_KEY) == slot) {
				setEquipmentItem(items.getCompound(i), slot, mat, damage, count);
				return;
			}
		}
		addEquipmentItem(items, slot, mat, damage, count);
	}

	public static void setEquipmentItem(NBTTagCompound item, byte slot, Material mat, short damage, byte count,
			String nbt) {
		setEquipmentItem(item, slot, materialToString(mat), damage, count, fromString(nbt));
	}

	public static void setEquipmentItem(NBTTagCompound item, byte slot, String mat, short damage, byte count,
			String nbt) {
		setEquipmentItem(item, slot, mat, damage, count, fromString(nbt));
	}

	public static void setEquipmentItem(NBTTagCompound item, byte slot, Material mat, short damage, byte count,
			NBTTagCompound nbt) {
		setEquipmentItem(item, slot, materialToString(mat), damage, count, nbt);
	}

	public static void setEquipmentItem(NBTTagCompound item, byte slot, String mat, short damage, byte count,
			NBTTagCompound nbt) {
		item.setByte(SLOT_KEY, slot);
		item.setString(ID_KEY, mat);
		item.setByte(COUNT_KEY, count);
		item.setShort(DAMAGE_KEY, damage);
		item.set(TAG_KEY, nbt);
	}

	public static void setEquipmentItem(NBTTagCompound item, byte slot, Material mat, short damage, byte count) {
		setEquipmentItem(item, slot, materialToString(mat), damage, count);
	}

	public static void setEquipmentItem(NBTTagCompound item, byte slot, String mat, short damage, byte count) {
		item.setByte(SLOT_KEY, slot);
		item.setString(ID_KEY, mat);
		item.setByte(COUNT_KEY, count);
		item.setShort(DAMAGE_KEY, damage);
	}

	public static void removeEquipedItem(NBTTagList items, byte slot) {
		for (int i = 0; i < items.size(); i++) {
			if (items.getCompound(i).getByte(SLOT_KEY) == slot) {
				items.remove(i);
				break;
			}
		}
	}

	public static NBTTagCompound getEquipedItem(NBTTagList items, byte slot) {
		for (int i = 0; i < items.size(); i++) {
			if (items.getCompound(i).getByte(SLOT_KEY) == slot)
				return items.getCompound(i);
		}
		return null;
	}

	public static boolean hasEquipedItem(NBTTagList items, byte slot) {
		for (int i = 0; i < items.size(); i++) {
			if (items.getCompound(i).getByte(SLOT_KEY) == slot)
				return true;
		}
		return false;
	}

	public static void addItem(NBTTagList items, Material mat, byte count, String nbt) {
		addItem(items, materialToString(mat), count, fromString(nbt));
	}

	public static void addItem(NBTTagList items, Item nmsItem, byte count, String nbt) {
		addItem(items, NMSItems.getKey(nmsItem).getKey(), count, fromString(nbt));
	}

	public static void addItem(NBTTagList items, String mat, byte count, String nbt) {
		addItem(items, mat, count, fromString(nbt));
	}

	public static void addItem(NBTTagList items, Material mat, byte count, NBTTagCompound nbt) {
		addItem(items, materialToString(mat), count, nbt);
	}

	public static void addItem(NBTTagList items, Item nmsItem, byte count, NBTTagCompound nbt) {
		addItem(items, NMSItems.getKey(nmsItem).getKey(), count, nbt);
	}

	public static void addItem(NBTTagList items, String mat, byte count, NBTTagCompound nbt) {
		NBTTagCompound item = new NBTTagCompound();
		setItem(item, mat, count, nbt);
		items.add(item);
	}

	public static void addItem(NBTTagList items, Material mat, byte count) {
		addItem(items, materialToString(mat), count);
	}

	public static void addItem(NBTTagList items, Item nmsItem, byte count) {
		addItem(items, NMSItems.getKey(nmsItem).getKey(), count);
	}

	public static void addItem(NBTTagList items, String mat, byte count) {
		NBTTagCompound item = new NBTTagCompound();
		setItem(item, mat, count);
		items.add(item);
	}

	public static void addItem(NBTTagList items, ItemStack is) {
		items.add(CraftItemStack.asNMSCopy(is).save(new NBTTagCompound()));
	}

	public static void addItem(NBTTagList items, net.minecraft.server.v1_15_R1.ItemStack is) {
		items.add(is.save(new NBTTagCompound()));
	}

	public static void setItem(NBTTagCompound item, Material mat, byte count, String nbt) {
		setItem(item, materialToString(mat), count, fromString(nbt));
	}

	public static void setItem(NBTTagCompound item, Item nmsItem, byte count, String nbt) {
		setItem(item, NMSItems.getKey(nmsItem).getKey(), count, fromString(nbt));
	}

	public static void setItem(NBTTagCompound item, String mat, byte count, String nbt) {
		setItem(item, mat, count, fromString(nbt));
	}

	public static void setItem(NBTTagCompound item, Material mat, byte count, NBTTagCompound nbt) {
		setItem(item, materialToString(mat), count, nbt);
	}

	public static void setItem(NBTTagCompound item, Item nmsItem, byte count, NBTTagCompound nbt) {
		setItem(item, NMSItems.getKey(nmsItem).getKey(), count, nbt);
	}

	public static void setItem(NBTTagCompound item, String mat, byte count, NBTTagCompound nbt) {
		item.setString(ID_KEY, mat);
		item.setByte(COUNT_KEY, count);
		item.set(TAG_KEY, nbt);
	}

	public static void setItem(NBTTagCompound item, Material mat, byte count) {
		setItem(item, materialToString(mat), count);
	}

	public static void setItem(NBTTagCompound item, Item nmsItem, byte count) {
		setItem(item, NMSItems.getKey(nmsItem).getKey(), count);
	}

	public static void setItem(NBTTagCompound item, String mat, byte count) {
		item.setString(ID_KEY, mat);
		item.setByte(COUNT_KEY, count);
	}

	public static void setItem(NBTTagCompound item, ItemStack is) {
		CraftItemStack.asNMSCopy(is).save(item);
	}

	public static void setItem(NBTTagCompound item, net.minecraft.server.v1_15_R1.ItemStack is) {
		is.save(item);
	}

	public static void removeItem(NBTTagList items, int index) {
		items.remove(index);
	}

	public static NBTTagCompound getItem(NBTTagList items, int index) {
		return items.getCompound(index);
	}

	public static boolean hasItem(NBTTagList items, int index) {
		return items.size() > index;
	}

	public static NBTTagCompound saveItem(NBTTagCompound item, ItemStack is) {
		return CraftItemStack.asNMSCopy(is).save(item);
	}

	public static NBTTagCompound saveItem(NBTTagCompound item, net.minecraft.server.v1_15_R1.ItemStack is) {
		return is.save(item);
	}

	public static ItemStack loadItem(NBTTagCompound item) {
		return CraftItemStack.asBukkitCopy(net.minecraft.server.v1_15_R1.ItemStack.a(item));
	}

	public static net.minecraft.server.v1_15_R1.ItemStack loadNMSItem(NBTTagCompound item) {
		return net.minecraft.server.v1_15_R1.ItemStack.a(item);
	}

	public static void setUnbreakable(NBTTagCompound nbtTag, boolean unbreakable) {
		nbtTag.setBoolean(UNBREAKABLE_KEY, unbreakable);
	}

	public static void getUnbreakable(NBTTagCompound nbtTag) {
		nbtTag.getBoolean(UNBREAKABLE_KEY);
	}

	public static void hasUnbreakable(NBTTagCompound nbtTag) {
		nbtTag.hasKey(UNBREAKABLE_KEY);
	}

	public static boolean switchUnbreakable(NBTTagCompound nbtTag) {
		if (nbtTag.getBoolean(UNBREAKABLE_KEY)) {
			nbtTag.setBoolean(UNBREAKABLE_KEY, false);
			return false;
		} else {
			nbtTag.setBoolean(UNBREAKABLE_KEY, true);
			return true;
		}
	}

	public static NBTTagCompound addEntityTag(NBTTagCompound nbtTag) {
		NBTTagCompound entityTag = new NBTTagCompound();
		nbtTag.set(ENTITY_TAG_KEY, entityTag);
		return entityTag;
	}

	public static void removeEntityTag(NBTTagCompound nbtTag) {
		nbtTag.remove(ENTITY_TAG_KEY);
	}

	public static NBTTagCompound getEntityTag(NBTTagCompound nbtTag) {
		return nbtTag.getCompound(ENTITY_TAG_KEY);
	}

	public static boolean hasEntityTag(NBTTagCompound nbtTag) {
		return nbtTag.hasKey(ENTITY_TAG_KEY);
	}

	public static NBTTagCompound addSpawnData(NBTTagCompound nbtTag) {
		NBTTagCompound entityTag = new NBTTagCompound();
		nbtTag.set(SPAWN_DATA_KEY, entityTag);
		return entityTag;
	}

	public static void removeSpawnData(NBTTagCompound nbtTag) {
		nbtTag.remove(SPAWN_DATA_KEY);
	}

	public static NBTTagCompound getSpawnData(NBTTagCompound nbtTag) {
		return nbtTag.getCompound(SPAWN_DATA_KEY);
	}

	public static boolean hasSpawnData(NBTTagCompound nbtTag) {
		return nbtTag.hasKey(SPAWN_DATA_KEY);
	}

	public static void setEntityId(NBTTagCompound entityTag, EntityType entityType) {
		setEntityId(entityTag, entityType.name());
	}

	public static void setEntityId(NBTTagCompound entityTag, EntityTypes<?> entityType) {
		setEntityId(entityTag, entityType.f());
	}

	// TODO: chek and change name getting
	public static void setEntityId(NBTTagCompound entityTag, String entityName) {
		entityName = convetToNBTName(entityName);
		entityTag.setString(ID_KEY, entityName);
	}

	public static void removeEntityId(NBTTagCompound entityTag) {
		entityTag.remove(ID_KEY);
	}

	public static String getEntityId(NBTTagCompound entityTag) {
		return entityTag.getString(ID_KEY);
	}

	public static boolean hasEntityId(NBTTagCompound entityTag) {
		return entityTag.hasKey(ID_KEY);
	}

	public static void setEntityName(NBTTagCompound entityTag, String name) {
		entityTag.setString(CUSTOM_NAME_KEY, name);
	}

	public static void removeEntityName(NBTTagCompound entityTag) {
		entityTag.remove(CUSTOM_NAME_KEY);
	}

	public static String getEntityName(NBTTagCompound entityTag, String name) {
		return entityTag.getString(CUSTOM_NAME_KEY);
	}

	public static boolean hasEntityName(NBTTagCompound entityTag, String name) {
		return entityTag.hasKey(CUSTOM_NAME_KEY);
	}

	public static void setNameVisible(NBTTagCompound entityTag, boolean isVisible) {
		entityTag.setBoolean(CUSTOM_NAME_VISIBLE_KEY, isVisible);
	}

	public static void removeNameVisible(NBTTagCompound entityTag) {
		entityTag.remove(CUSTOM_NAME_VISIBLE_KEY);
	}

	public static boolean hasNameVisible(NBTTagCompound entityTag) {
		return entityTag.getBoolean(CUSTOM_NAME_VISIBLE_KEY);
	}

	public static void setNoAi(NBTTagCompound entityTag, boolean hasNoAi) {
		entityTag.setBoolean(NO_AI_KEY, hasNoAi);
	}

	public static void removeNoAi(NBTTagCompound entityTag) {
		entityTag.remove(NO_AI_KEY);
	}

	public static boolean hasNoAi(NBTTagCompound entityTag) {
		return entityTag.getBoolean(NO_AI_KEY);
	}

	public static void setInvisible(NBTTagCompound entityTag, boolean IsInvisible) {
		entityTag.setBoolean(INVISIBLE_KEY, IsInvisible);
	}

	public static void removeInvisible(NBTTagCompound entityTag) {
		entityTag.remove(INVISIBLE_KEY);
	}

	public static boolean isInvisible(NBTTagCompound entityTag) {
		return entityTag.getBoolean(INVISIBLE_KEY);
	}

	public static void setSilent(NBTTagCompound entityTag, boolean isSilent) {
		entityTag.setBoolean(SILENT_KEY, isSilent);
	}

	public static void removeSilent(NBTTagCompound entityTag) {
		entityTag.remove(SILENT_KEY);
	}

	public static boolean isSilent(NBTTagCompound entityTag) {
		return entityTag.getBoolean(SILENT_KEY);
	}

	public static void setMarker(NBTTagCompound entityTag, boolean isMarker) {
		entityTag.setBoolean(MARKER_KEY, isMarker);
	}

	public static void removeMarker(NBTTagCompound entityTag) {
		entityTag.remove(MARKER_KEY);
	}

	public static boolean isMarker(NBTTagCompound entityTag) {
		return entityTag.getBoolean(MARKER_KEY);
	}

	public static NBTTagList addAttributeModifiersList(NBTTagCompound nbtTag) {
		NBTTagList attributeModifiers = new NBTTagList();
		nbtTag.set(ATTRIBUTE_MODIFIERS_KEY, attributeModifiers);
		return attributeModifiers;
	}

	public static void removeAttributeModifiersList(NBTTagCompound nbtTag) {
		nbtTag.remove(ATTRIBUTE_MODIFIERS_KEY);
	}

	public static NBTTagList getAttributeModifiersList(NBTTagCompound nbtTag) {
		if (nbtTag.hasKey(ATTRIBUTE_MODIFIERS_KEY))
			return nbtTag.getList(ATTRIBUTE_MODIFIERS_KEY, NBTType.COMPOUND);
		return addAttributeModifiersList(nbtTag);
	}

	public static boolean hasAttributeModifiersList(NBTTagCompound nbtTag) {
		return nbtTag.hasKey(ATTRIBUTE_MODIFIERS_KEY);
	}

	public static void addAttributeModifier(NBTTagList attributeModifiers, String attribute, double value,
			String slot) {
		NBTTagCompound attributeCompound = new NBTTagCompound();
		attributeCompound.setString(ATTRIBUTE_NAME_KEY, attribute);
		attributeCompound.setString(NAME_KEY, attribute);
		attributeCompound.setDouble(AMOUNT_KEY, value);
		attributeCompound.setInt(OPERATION_KEY, 0);
		attributeCompound.setInt(UUID_LEAST_KEY, 894654);
		attributeCompound.setInt(UUID_MOST_KEY, 2872);
		attributeCompound.setString(SLOT_KEY, slot);
		attributeModifiers.add(attributeCompound);
	}

	public static void removeAttributeModifier(NBTTagList attributeModifiers, String attribute, String slot) {
		NBTTagCompound attributeCompound;
		for (int i = 0; i < attributeModifiers.size(); i++) {
			attributeCompound = attributeModifiers.getCompound(i);
			if (attributeCompound.getString(ATTRIBUTE_NAME_KEY).equals(attribute)
					&& attributeCompound.getString(SLOT_KEY).equals(slot)) {
				attributeModifiers.remove(i);
				break;
			}
		}
	}

	public static void setAttributeModifier(NBTTagList attributeModifiers, String attribute, String slot,
			double value) {
		NBTTagCompound attributeCompound;
		for (int i = 0; i < attributeModifiers.size(); i++) {
			attributeCompound = attributeModifiers.getCompound(i);
			if (attributeCompound.getString(ATTRIBUTE_NAME_KEY).equals(attribute)
					&& attributeCompound.getString(SLOT_KEY).equals(slot)) {
				attributeCompound.setDouble(AMOUNT_KEY, value);
				break;
			}
		}
	}

	public static void setAttributeModifierSlot(NBTTagList attributeModifiers, String attribute, String slot,
			double value) {
		NBTTagCompound attributeCompound;
		for (int i = 0; i < attributeModifiers.size(); i++) {
			attributeCompound = attributeModifiers.getCompound(i);
			if (attributeCompound.getString(ATTRIBUTE_NAME_KEY).equals(attribute)
					&& attributeCompound.getDouble(AMOUNT_KEY) == value) {
				attributeCompound.setString(SLOT_KEY, slot);
				break;
			}
		}
	}

	public static double getAttributeModifier(NBTTagList attributeModifiers, String attribute, String slot) {
		NBTTagCompound attributeCompound;
		for (int i = 0; i < attributeModifiers.size(); i++) {
			attributeCompound = attributeModifiers.getCompound(i);
			if (attributeCompound.getString(ATTRIBUTE_NAME_KEY).equals(attribute)
					&& attributeCompound.getString(SLOT_KEY).equals(slot))
				return attributeCompound.getDouble(AMOUNT_KEY);
		}
		return 0;
	}

	public static boolean hasAttributeModifier(NBTTagList attributeModifiers, String attribute, String slot) {
		NBTTagCompound attributeCompound;
		for (int i = 0; i < attributeModifiers.size(); i++) {
			attributeCompound = attributeModifiers.getCompound(i);
			if (attributeCompound.getString(ATTRIBUTE_NAME_KEY).equals(attribute)
					&& attributeCompound.getString(SLOT_KEY).equals(slot))
				return true;
		}
		return false;
	}

	public static void setCustomModelData(NBTTagCompound nbtTag, int id) {
		nbtTag.setInt(CUSTOM_MODEL_DATA_KEY, id);
	}

	public static void removeCustomModelData(NBTTagCompound nbtTag) {
		nbtTag.remove(CUSTOM_MODEL_DATA_KEY);
	}

	public static int getCustomModelData(NBTTagCompound nbtTag) {
		return nbtTag.getInt(CUSTOM_MODEL_DATA_KEY);
	}

	public static boolean hasCustomModelData(NBTTagCompound nbtTag) {
		return nbtTag.hasKey(CUSTOM_MODEL_DATA_KEY);
	}

	public static void setSpawnRange(NBTTagCompound nbtTag, short range) {
		nbtTag.setShort(SPAWN_RANGE_KEY, range);
	}

	public static void removeSpawnRange(NBTTagCompound nbtTag) {
		nbtTag.remove(SPAWN_RANGE_KEY);
	}

	public static short getSpawnRange(NBTTagCompound nbtTag) {
		return nbtTag.getShort(SPAWN_RANGE_KEY);
	}

	public static boolean hasSpawnRange(NBTTagCompound nbtTag) {
		return nbtTag.hasKey(SPAWN_RANGE_KEY);
	}

	public static void setSpawnCount(NBTTagCompound nbtTag, short count) {
		nbtTag.setShort(SPAWN_COUNT_KEY, count);
	}

	public static void removeSpawnCount(NBTTagCompound nbtTag) {
		nbtTag.remove(SPAWN_COUNT_KEY);
	}

	public static short getSpawnCount(NBTTagCompound nbtTag) {
		return nbtTag.getShort(SPAWN_COUNT_KEY);
	}

	public static boolean hasSpawnCount(NBTTagCompound nbtTag) {
		return nbtTag.hasKey(SPAWN_COUNT_KEY);
	}

	public static void setRequiredPlayerRange(NBTTagCompound nbtTag, short playerRange) {
		nbtTag.setShort(REQUIRED_PLAYER_RANGE_KEY, playerRange);
	}

	public static void removeRequiredPlayerRange(NBTTagCompound nbtTag) {
		nbtTag.remove(REQUIRED_PLAYER_RANGE_KEY);
	}

	public static short getRequiredPlayerRange(NBTTagCompound nbtTag) {
		return nbtTag.getShort(REQUIRED_PLAYER_RANGE_KEY);
	}

	public static boolean hasRequiredPlayerRange(NBTTagCompound nbtTag) {
		return nbtTag.hasKey(REQUIRED_PLAYER_RANGE_KEY);
	}

	public static void setMaxNearbyEntities(NBTTagCompound nbtTag, short maxNearbyEntities) {
		nbtTag.setShort(MAX_NEARBY_ENTITIES_KEY, maxNearbyEntities);
	}

	public static void removeMaxNearbyEntities(NBTTagCompound nbtTag) {
		nbtTag.remove(MAX_NEARBY_ENTITIES_KEY);
	}

	public static short getMaxNearbyEntities(NBTTagCompound nbtTag) {
		return nbtTag.getShort(MAX_NEARBY_ENTITIES_KEY);
	}

	public static boolean hasMaxNearbyEntities(NBTTagCompound nbtTag) {
		return nbtTag.hasKey(MAX_NEARBY_ENTITIES_KEY);
	}

	public static void setSpawnerStats(NBTTagCompound nbtTag, short range, short count, short playerRange,
			short maxNearbyEntities) {
		nbtTag.setShort(SPAWN_RANGE_KEY, range);
		nbtTag.setShort(SPAWN_COUNT_KEY, count);
		nbtTag.setShort(REQUIRED_PLAYER_RANGE_KEY, playerRange);
		nbtTag.setShort(MAX_NEARBY_ENTITIES_KEY, maxNearbyEntities);
	}

	public static void setX(NBTTagCompound nbtTag, int x) {
		nbtTag.setInt("x", x);
	}

	public static void setY(NBTTagCompound nbtTag, int y) {
		nbtTag.setInt("y", y);
	}

	public static void setZ(NBTTagCompound nbtTag, int z) {
		nbtTag.setInt("z", z);
	}

	public static void setPos(NBTTagCompound nbtTag, int x, int y, int z) {
		nbtTag.setInt("x", x);
		nbtTag.setInt("y", y);
		nbtTag.setInt("z", z);
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

	public static void setItemEffect(NBTTagCompound nbtTag, int effectId) {
		nbtTag.setInt(ITEM_EFFECT_KEY, effectId);
	}

	public static void removeItemEffect(NBTTagCompound nbtTag) {
		nbtTag.remove(ITEM_EFFECT_KEY);
	}

	public static int getItemEffect(NBTTagCompound nbtTag) {
		return nbtTag.getInt(ITEM_EFFECT_KEY);
	}

	public static boolean hasItemEffect(NBTTagCompound nbtTag) {
		return nbtTag.hasKey(ITEM_EFFECT_KEY);
	}

	public static ItemStack setItemEffect(ItemStack is, int id) {
		net.minecraft.server.v1_15_R1.ItemStack nmsItem = NMS.getNMS(is);
		NBTTagCompound nbtTag = NBTHelper.getNBT(nmsItem);
		NBTHelper.setItemEffect(nbtTag, id);
		return NBTHelper.setNBT(nmsItem, nbtTag);
	}

	public static void setWorldEffect(NBTTagCompound nbtTag, int effectId) {
		nbtTag.setInt(WORLD_EFFECT_KEY, effectId);
	}

	public static void removeWorldEffect(NBTTagCompound nbtTag) {
		nbtTag.remove(WORLD_EFFECT_KEY);
	}

	public static int getWorldEffect(NBTTagCompound nbtTag) {
		return nbtTag.getInt(WORLD_EFFECT_KEY);
	}

	public static boolean hasWorldEffect(NBTTagCompound nbtTag) {
		return nbtTag.hasKey(WORLD_EFFECT_KEY);
	}

	public static ItemStack setWorldEffect(ItemStack is, int id) {
		net.minecraft.server.v1_15_R1.ItemStack nmsItem = NMS.getNMS(is);
		NBTTagCompound nbtTag = NBTHelper.getNBT(nmsItem);
		NBTHelper.setWorldEffect(nbtTag, id);
		return NBTHelper.setNBT(nmsItem, nbtTag);
	}

	public static void setDurability(NBTTagCompound nbtTag, short durability) {
		nbtTag.setShort(DAMAGE_KEY, durability);
	}

	public static void removeDurability(NBTTagCompound nbtTag) {
		nbtTag.remove(DAMAGE_KEY);
	}

	public static short getDurability(NBTTagCompound nbtTag) {
		return nbtTag.getShort(DAMAGE_KEY);
	}

	public static boolean hasDurability(NBTTagCompound nbtTag) {
		return nbtTag.hasKey(DAMAGE_KEY);
	}

	public static String materialToString(Material mat) {
		return "minecraft:".concat(mat.name().toLowerCase());
	}

	public static Material stringToMaterial(String string) {
		return Material.getMaterial(string.substring(string.indexOf(':') + 1).toUpperCase());
	}

	public static NBTTagString createNBTTagString(String text) {
		return NBTTagString.a(text);
	}
}
