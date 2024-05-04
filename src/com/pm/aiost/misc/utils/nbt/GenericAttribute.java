package com.pm.aiost.misc.utils.nbt;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;
import static com.pm.aiost.misc.utils.ChatColor.GRAY;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.collection.list.IdentityArrayList;
import com.pm.aiost.misc.utils.meta.MetaHelper;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class GenericAttribute {

	private static final Map<String, GenericAttribute> NAME_MAP = new HashMap<String, GenericAttribute>();
	private static final IdentityArrayList<GenericAttribute> LIST = new IdentityArrayList<GenericAttribute>();
	private static final List<ItemStack> ITEM_LIST = new IdentityArrayList<ItemStack>();
	private static final List<ItemStack> ITEM_LIST_VIEW = Collections.unmodifiableList(ITEM_LIST);

	static {
		add("ARMOR", Attributes.ARMOR, MetaHelper.hideAttributes(new ItemStack(Material.IRON_CHESTPLATE),
				GRAY + BOLD + "Armor", Arrays.asList(GRAY + "Click to add armor attribute")));

		add("ARMOR_TOUGHNESS", Attributes.ARMOR_TOUGHNESS, MetaHelper.setMeta(Material.SHIELD,
				GRAY + BOLD + "Armor thoughness", Arrays.asList(GRAY + "Click to add armor thoughness attribute")));

		add("ATTACK_DAMAGE", Attributes.ATTACK_DAMAGE, MetaHelper.hideAttributes(new ItemStack(Material.IRON_AXE),
				GRAY + BOLD + "Attack damage", Arrays.asList(GRAY + "Click to add attack damage attribute")));

		add("ATTACK_KNOCKBACK", Attributes.ATTACK_KNOCKBACK, MetaHelper.setMeta(Material.SLIME_BLOCK,
				GRAY + BOLD + "Attack knockback", Arrays.asList(GRAY + "Click to add attack knockback attribute")));

		add("ATTACK_SPEED", Attributes.ATTACK_SPEED, MetaHelper.hideAttributes(new ItemStack(Material.IRON_SWORD),
				GRAY + BOLD + "Attack speed", Arrays.asList(GRAY + "Click to add attack speed attribute")));

		add("FLYING_SPEED", Attributes.FLYING_SPEED, MetaHelper.setMeta(Material.ELYTRA, GRAY + BOLD + "Flying speed",
				Arrays.asList(GRAY + "Click to add flying speed attribute")));

		add("FOLLOW_RANGE", Attributes.FOLLOW_RANGE, MetaHelper.setMeta(Material.ZOMBIE_HEAD,
				GRAY + BOLD + "Follow range", Arrays.asList(GRAY + "Click to add follow range attribute")));

		add("KNOCKBACK_RESISTANCE", Attributes.KNOCKBACK_RESISTANCE,
				MetaHelper.setMeta(Material.STONE, GRAY + BOLD + "Knockback resistance",
						Arrays.asList(GRAY + "Click to add knockback resistance attribute")));

		add("LUCK", Attributes.LUCK, MetaHelper.setMeta(Material.BELL, GRAY + BOLD + "Luck",
				Arrays.asList(GRAY + "Click to add luck attribute")));

		add("MAX_HEALTH", Attributes.MAX_HEALTH, MetaHelper.setMeta(Material.GOLDEN_APPLE, GRAY + BOLD + "Max health",
				Arrays.asList(GRAY + "Click to add max health attribute")));

		add("MOVEMENT_SPEED", Attributes.MOVEMENT_SPEED,
				MetaHelper.hideAttributes(new ItemStack(Material.LEATHER_BOOTS), GRAY + BOLD + "Movement speed",
						Arrays.asList(GRAY + "Click to add movement speed attribute")));
	}

	public static void add(String name, Holder<Attribute> holder, ItemStack item) {
		Attribute attribute = holder.value();
		int id = LIST.size();
		GenericAttribute genericAttribute = new GenericAttribute(id, name, attribute, item);
		NAME_MAP.put(name, genericAttribute);
		LIST.add(genericAttribute);
		ITEM_LIST.add(item);
	}

	public static GenericAttribute get(String name) {
		return NAME_MAP.get(name);
	}

	public static GenericAttribute get(int id) {
		return LIST.get(id);
	}

	public static List<ItemStack> getItems() {
		return ITEM_LIST_VIEW;
	}

	public static int size() {
		return LIST.size();
	}

	public final int id;
	public final String name;
	public final Attribute attribute;
	public final ItemStack item;

	protected GenericAttribute(int id, String name, Attribute attribute, ItemStack item) {
		this.id = id;
		this.name = name;
		this.attribute = attribute;
		this.item = item;
	}
}
