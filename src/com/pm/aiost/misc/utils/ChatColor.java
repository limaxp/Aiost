package com.pm.aiost.misc.utils;

import java.util.Random;

public class ChatColor {

	public static final int COLOR_AMOUNT = 16;
	public static final int COLOR_AMOUNT_NO_BLACK_WHITE = COLOR_AMOUNT - 2;

	static final String[] COLORS;
	static final String[] COLORS_NO_BLACK_WHITE;

	private static final Random random = new Random();

	public static final String BLACK = "§0";
	public static final String DARK_BLUE = "§1";
	public static final String DARK_GREEN = "§2";
	public static final String DARK_AQUA = "§3";
	public static final String DARK_RED = "§4";
	public static final String DARK_PURPLE = "§5";
	public static final String GOLD = "§6";
	public static final String GRAY = "§7";
	public static final String DARK_GRAY = "§8";
	public static final String BLUE = "§9";
	public static final String GREEN = "§a";
	public static final String AQUA = "§b";
	public static final String RED = "§c";
	public static final String PURPLE = "§d";
	public static final String YELLOW = "§e";
	public static final String WHITE = "§f";

	public static final String MAGIC = "§k";
	public static final String BOLD = "§l";
	public static final String STRIKETHROUGH = "§m";
	public static final String UNDERLINE = "§n";
	public static final String ITALIC = "§o";
	public static final String RESET = "§r";

	static {
		COLORS = new String[] { BLACK, DARK_BLUE, DARK_GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE, GOLD, GRAY, DARK_GRAY,
				BLUE, GREEN, AQUA, RED, PURPLE, YELLOW, ChatColor.WHITE };

		COLORS_NO_BLACK_WHITE = new String[] { DARK_BLUE, DARK_GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE, GOLD, GRAY,
				DARK_GRAY, BLUE, GREEN, AQUA, RED, PURPLE, YELLOW };
	}

	public static String randomColor() {
		return COLORS[random.nextInt(COLOR_AMOUNT)];
	}

	public static String getColor(int index) {
		return COLORS[index >= COLOR_AMOUNT ? index % COLOR_AMOUNT : index];
	}

	public static String getColorNoBlackWhite(int index) {
		return COLORS_NO_BLACK_WHITE[index >= COLOR_AMOUNT_NO_BLACK_WHITE ? index % COLOR_AMOUNT_NO_BLACK_WHITE
				: index];
	}

	public static String[] values() {
		return COLORS;
	}

	public static String stripColor(String input) {
		return org.bukkit.ChatColor.stripColor(input);
	}
}
