package com.pm.aiost.misc.other;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

public class Banner {

	public static ItemStack create(Material banner, Pattern pattern) {
		return set(new ItemStack(banner), pattern);
	}

	public static ItemStack create(Material banner, String name, Pattern pattern) {
		return set(new ItemStack(banner), name, pattern);
	}

	public static ItemStack create(Material banner, String name, List<String> lore, Pattern pattern) {
		return set(new ItemStack(banner), name, lore, pattern);
	}

	public static ItemStack set(ItemStack is, Pattern pattern) {
		BannerMeta bannerMeta = (BannerMeta) is.getItemMeta();
		bannerMeta.addPattern(pattern);
		is.setItemMeta(bannerMeta);
		return is;
	}

	public static ItemStack set(ItemStack is, String name, Pattern pattern) {
		BannerMeta bannerMeta = (BannerMeta) is.getItemMeta();
		bannerMeta.addPattern(pattern);
		bannerMeta.setDisplayName(name);
		is.setItemMeta(bannerMeta);
		return is;
	}

	public static ItemStack set(ItemStack is, String name, List<String> lore, Pattern pattern) {
		BannerMeta bannerMeta = (BannerMeta) is.getItemMeta();
		bannerMeta.addPattern(pattern);
		bannerMeta.setDisplayName(name);
		bannerMeta.setLore(lore);
		bannerMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		is.setItemMeta(bannerMeta);
		return is;
	}

	public static ItemStack create(Material banner, List<Pattern> patterns) {
		return set(new ItemStack(banner), patterns);
	}

	public static ItemStack create(Material banner, String name, List<Pattern> patterns) {
		return set(new ItemStack(banner), name, patterns);
	}

	public static ItemStack create(Material banner, String name, List<String> lore, List<Pattern> patterns) {
		return set(new ItemStack(banner), name, lore, patterns);
	}

	public static ItemStack set(ItemStack is, List<Pattern> patterns) {
		BannerMeta bannerMeta = (BannerMeta) is.getItemMeta();
		bannerMeta.setPatterns(patterns);
		is.setItemMeta(bannerMeta);
		return is;
	}

	public static ItemStack set(ItemStack is, String name, List<Pattern> patterns) {
		BannerMeta bannerMeta = (BannerMeta) is.getItemMeta();
		bannerMeta.setPatterns(patterns);
		bannerMeta.setDisplayName(name);
		is.setItemMeta(bannerMeta);
		return is;
	}

	public static ItemStack set(ItemStack is, String name, List<String> lore, List<Pattern> patterns) {
		BannerMeta bannerMeta = (BannerMeta) is.getItemMeta();
		bannerMeta.setPatterns(patterns);
		bannerMeta.setDisplayName(name);
		bannerMeta.setLore(lore);
		bannerMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		is.setItemMeta(bannerMeta);
		return is;
	}

	public static List<Pattern> zeroPattern(DyeColor baseColor, DyeColor color) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(color, PatternType.STRIPE_TOP));
		patterns.add(new Pattern(color, PatternType.STRIPE_RIGHT));
		patterns.add(new Pattern(color, PatternType.STRIPE_BOTTOM));
		patterns.add(new Pattern(color, PatternType.STRIPE_LEFT));
		patterns.add(new Pattern(baseColor, PatternType.BORDER));
		return patterns;
	}

	public static List<Pattern> onePattern(DyeColor baseColor, DyeColor color) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(color, PatternType.SQUARE_TOP_LEFT));
		patterns.add(new Pattern(color, PatternType.STRIPE_CENTER));
		patterns.add(new Pattern(color, PatternType.STRIPE_BOTTOM));
		patterns.add(new Pattern(baseColor, PatternType.BORDER));
		return patterns;
	}

	public static List<Pattern> twoPattern(DyeColor baseColor, DyeColor color) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(color, PatternType.STRIPE_TOP));
		patterns.add(new Pattern(baseColor, PatternType.RHOMBUS_MIDDLE));
		patterns.add(new Pattern(color, PatternType.STRIPE_DOWNLEFT));
		patterns.add(new Pattern(color, PatternType.STRIPE_BOTTOM));
		patterns.add(new Pattern(baseColor, PatternType.BORDER));
		return patterns;
	}

	public static List<Pattern> threePattern(DyeColor baseColor, DyeColor color) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(color, PatternType.STRIPE_MIDDLE));
		patterns.add(new Pattern(baseColor, PatternType.STRIPE_LEFT));
		patterns.add(new Pattern(color, PatternType.STRIPE_BOTTOM));
		patterns.add(new Pattern(color, PatternType.STRIPE_RIGHT));
		patterns.add(new Pattern(color, PatternType.STRIPE_TOP));
		patterns.add(new Pattern(baseColor, PatternType.BORDER));
		return patterns;
	}

	public static List<Pattern> fourPattern(DyeColor baseColor, DyeColor color) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(baseColor, PatternType.HALF_HORIZONTAL));
		patterns.add(new Pattern(color, PatternType.STRIPE_LEFT));
		patterns.add(new Pattern(baseColor, PatternType.STRIPE_BOTTOM));
		patterns.add(new Pattern(color, PatternType.STRIPE_RIGHT));
		patterns.add(new Pattern(color, PatternType.STRIPE_MIDDLE));
		patterns.add(new Pattern(baseColor, PatternType.BORDER));
		return patterns;
	}

	public static List<Pattern> fivePattern(DyeColor baseColor, DyeColor color) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(color, PatternType.STRIPE_BOTTOM));
		patterns.add(new Pattern(color, PatternType.STRIPE_DOWNRIGHT));
		patterns.add(new Pattern(baseColor, PatternType.CURLY_BORDER));
		patterns.add(new Pattern(color, PatternType.SQUARE_BOTTOM_LEFT));
		patterns.add(new Pattern(color, PatternType.STRIPE_TOP));
		patterns.add(new Pattern(baseColor, PatternType.BORDER));
		return patterns;
	}

	public static List<Pattern> sixPattern(DyeColor baseColor, DyeColor color) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(baseColor, PatternType.HALF_HORIZONTAL_MIRROR));
		patterns.add(new Pattern(color, PatternType.STRIPE_RIGHT));
		patterns.add(new Pattern(baseColor, PatternType.STRIPE_TOP));
		patterns.add(new Pattern(color, PatternType.STRIPE_MIDDLE));
		patterns.add(new Pattern(color, PatternType.STRIPE_BOTTOM));
		patterns.add(new Pattern(color, PatternType.STRIPE_LEFT));
		patterns.add(new Pattern(baseColor, PatternType.BORDER));
		return patterns;
	}

	public static List<Pattern> sevenPattern(DyeColor baseColor, DyeColor color) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(color, PatternType.STRIPE_TOP));
		patterns.add(new Pattern(baseColor, PatternType.DIAGONAL_RIGHT));
		patterns.add(new Pattern(color, PatternType.STRIPE_DOWNLEFT));
		patterns.add(new Pattern(color, PatternType.SQUARE_BOTTOM_LEFT));
		patterns.add(new Pattern(baseColor, PatternType.BORDER));
		return patterns;
	}

	public static List<Pattern> eightPattern(DyeColor baseColor, DyeColor color) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(color, PatternType.STRIPE_MIDDLE));
		patterns.add(new Pattern(color, PatternType.STRIPE_LEFT));
		patterns.add(new Pattern(color, PatternType.STRIPE_BOTTOM));
		patterns.add(new Pattern(color, PatternType.STRIPE_RIGHT));
		patterns.add(new Pattern(color, PatternType.STRIPE_TOP));
		patterns.add(new Pattern(baseColor, PatternType.BORDER));
		return patterns;
	}

	public static List<Pattern> ninePattern(DyeColor baseColor, DyeColor color) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(baseColor, PatternType.HALF_HORIZONTAL));
		patterns.add(new Pattern(color, PatternType.STRIPE_LEFT));
		patterns.add(new Pattern(baseColor, PatternType.STRIPE_BOTTOM));
		patterns.add(new Pattern(color, PatternType.STRIPE_TOP));
		patterns.add(new Pattern(color, PatternType.STRIPE_MIDDLE));
		patterns.add(new Pattern(color, PatternType.STRIPE_RIGHT));
		patterns.add(new Pattern(baseColor, PatternType.BORDER));
		return patterns;
	}

	public static List<Pattern> numberPattern(int number, DyeColor baseColor, DyeColor color) {
		switch (number) {
		case 0:
			return zeroPattern(baseColor, color);
		case 1:
			return onePattern(baseColor, color);
		case 2:
			return twoPattern(baseColor, color);
		case 3:
			return threePattern(baseColor, color);
		case 4:
			return fourPattern(baseColor, color);
		case 5:
			return fivePattern(baseColor, color);
		case 6:
			return sixPattern(baseColor, color);
		case 7:
			return sevenPattern(baseColor, color);
		case 8:
			return eightPattern(baseColor, color);
		case 9:
			return ninePattern(baseColor, color);
		default:
			return zeroPattern(baseColor, color);
		}
	}

	public static List<Pattern> plusPattern(DyeColor baseColor, DyeColor color) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(color, PatternType.STRIPE_MIDDLE));
		patterns.add(new Pattern(color, PatternType.STRAIGHT_CROSS));
		return patterns;
	}

	public static Pattern minusPattern(DyeColor baseColor, DyeColor color) {
		return new Pattern(color, PatternType.STRIPE_MIDDLE);
	}

	public static Pattern dotPattern(DyeColor baseColor, DyeColor color) {
		return new Pattern(color, PatternType.CIRCLE_MIDDLE);
	}

	public static List<Pattern> creeperPattern(DyeColor baseColor, DyeColor color) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(baseColor, PatternType.GRADIENT_UP));
		patterns.add(new Pattern(color, PatternType.CREEPER));
		return patterns;
	}

	public static List<Pattern> eyePattern(DyeColor color) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(color, PatternType.GRADIENT_UP));
		patterns.add(new Pattern(color, PatternType.FLOWER));
		return patterns;
	}

	public static List<Pattern> piratePattern(DyeColor color) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(color, PatternType.GRADIENT_UP));
		patterns.add(new Pattern(color, PatternType.SKULL));
		return patterns;
	}

	public static List<Pattern> templerPattern(DyeColor baseColor, DyeColor color) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(baseColor, PatternType.GRADIENT));
		patterns.add(new Pattern(color, PatternType.STRIPE_CENTER));
		patterns.add(new Pattern(color, PatternType.STRIPE_MIDDLE));
		return patterns;
	}

	public static List<Pattern> mojangPattern(DyeColor baseColor, DyeColor color) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(baseColor, PatternType.GRADIENT));
		patterns.add(new Pattern(color, PatternType.MOJANG));
		return patterns;
	}

	public static List<Pattern> aPattern(DyeColor baseColor, DyeColor color) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(baseColor, PatternType.HALF_HORIZONTAL));
		patterns.add(new Pattern(baseColor, PatternType.STRIPE_BOTTOM));
		patterns.add(new Pattern(color, PatternType.STRIPE_RIGHT));
		patterns.add(new Pattern(color, PatternType.STRIPE_TOP));
		patterns.add(new Pattern(color, PatternType.STRIPE_LEFT));
		return patterns;
	}

	public static List<Pattern> bPattern(DyeColor baseColor, DyeColor color) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(baseColor, PatternType.STRIPE_CENTER));
		patterns.add(new Pattern(color, PatternType.STRIPE_BOTTOM));
		patterns.add(new Pattern(color, PatternType.STRIPE_TOP));
		patterns.add(new Pattern(color, PatternType.STRIPE_MIDDLE));
		return patterns;
	}

	public static List<Pattern> cPattern(DyeColor color) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(color, PatternType.STRIPE_LEFT));
		patterns.add(new Pattern(color, PatternType.STRIPE_TOP));
		patterns.add(new Pattern(color, PatternType.STRIPE_BOTTOM));
		return patterns;
	}

	public static List<Pattern> dPattern(DyeColor baseColor, DyeColor color) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(color, PatternType.STRIPE_LEFT));
		patterns.add(new Pattern(color, PatternType.STRIPE_BOTTOM));
		patterns.add(new Pattern(baseColor, PatternType.DIAGONAL_RIGHT_MIRROR));
		patterns.add(new Pattern(color, PatternType.STRIPE_DOWNRIGHT));
		return patterns;
	}

	public static List<Pattern> ePattern(DyeColor baseColor, DyeColor color) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(color, PatternType.STRIPE_MIDDLE));
		patterns.add(new Pattern(baseColor, PatternType.STRIPE_RIGHT));
		patterns.add(new Pattern(color, PatternType.STRIPE_LEFT));
		patterns.add(new Pattern(color, PatternType.STRIPE_TOP));
		patterns.add(new Pattern(color, PatternType.STRIPE_BOTTOM));
		return patterns;
	}

	public static List<Pattern> fPattern(DyeColor baseColor, DyeColor color) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(color, PatternType.STRIPE_MIDDLE));
		patterns.add(new Pattern(baseColor, PatternType.STRIPE_RIGHT));
		patterns.add(new Pattern(color, PatternType.STRIPE_LEFT));
		patterns.add(new Pattern(color, PatternType.STRIPE_TOP));
		return patterns;
	}

	public static List<Pattern> gPattern(DyeColor baseColor, DyeColor color) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(color, PatternType.STRIPE_RIGHT));
		patterns.add(new Pattern(baseColor, PatternType.HALF_HORIZONTAL));
		patterns.add(new Pattern(color, PatternType.STRIPE_BOTTOM));
		patterns.add(new Pattern(color, PatternType.STRIPE_LEFT));
		patterns.add(new Pattern(color, PatternType.STRIPE_TOP));
		return patterns;
	}

	public static List<Pattern> hPattern(DyeColor color) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(color, PatternType.STRIPE_MIDDLE));
		patterns.add(new Pattern(color, PatternType.STRIPE_RIGHT));
		patterns.add(new Pattern(color, PatternType.STRIPE_LEFT));
		return patterns;
	}

	public static List<Pattern> rPattern(DyeColor baseColor, DyeColor color) {
		List<Pattern> patterns = new ArrayList<Pattern>();

		return patterns;
	}

	public static List<Pattern> xPattern(DyeColor baseColor, DyeColor color) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(color, PatternType.STRIPE_TOP));
		patterns.add(new Pattern(color, PatternType.STRIPE_BOTTOM));
		patterns.add(new Pattern(baseColor, PatternType.STRIPE_CENTER));
		patterns.add(new Pattern(color, PatternType.CROSS));
		return patterns;
	}

	public static List<Pattern> yPattern(DyeColor baseColor, DyeColor color) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(color, PatternType.CROSS));
		patterns.add(new Pattern(baseColor, PatternType.HALF_VERTICAL_MIRROR));
		patterns.add(new Pattern(color, PatternType.STRIPE_DOWNLEFT));
		return patterns;
	}

	public static List<Pattern> zPattern(DyeColor color) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(color, PatternType.STRIPE_TOP));
		patterns.add(new Pattern(color, PatternType.STRIPE_BOTTOM));
		patterns.add(new Pattern(color, PatternType.STRIPE_DOWNLEFT));
		return patterns;
	}
}
