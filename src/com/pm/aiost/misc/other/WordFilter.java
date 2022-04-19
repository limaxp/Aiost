package com.pm.aiost.misc.other;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.ConfigurationSection;

import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.utils.FileUtils;

public class WordFilter {

	private static final Map<String, SpecialWord> SPECIAL_WORDS = new HashMap<String, SpecialWord>();
	private static final BlockedWord BLOCKED_WORD = new BlockedWord();

	private static int largestWordSize;

	static {
		loadCusswordFile();
	}

	private static final void loadCusswordFile() {
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(FileUtils.getResourceStream(WordFilter.class, "Cusswords.dat")))) {
			String line;
			while ((line = reader.readLine()) != null)
				addBlocked(line);
		} catch (IOException e) {
			Logger.err("WordFilter: Error! Could not load Cusswords.dat", e);
		}
	}

	public static void loadConfig(ConfigurationSection section) {
		addBlocked(section.getStringList("blocked"));

		ConfigurationSection symbolSection = section.getConfigurationSection("symbols");
		Set<String> symbolStrings = symbolSection.getKeys(false);
		for (String symbolString : symbolStrings)
			addSymbol(symbolString, symbolSection.getString(symbolString).charAt(0));
	}

	public static void addBlocked(String... strings) {
		for (int i = 0; i < strings.length; i++)
			addBlocked(strings[i]);
	}

	public static void addBlocked(Iterable<String> strings) {
		for (String string : strings)
			addBlocked(string);
	}

	public static void addBlocked(String string) {
		if (largestWordSize < string.length())
			largestWordSize = string.length();
		SPECIAL_WORDS.put(string.toLowerCase(), BLOCKED_WORD);
	}

	public static void addSymbol(String string, char symbol) {
		if (largestWordSize < string.length())
			largestWordSize = string.length();
		SPECIAL_WORDS.put(string.toLowerCase(), new SymbolWord(symbol));
	}

	public static boolean containsBlocked(String string) {
		string = removeLeatSpeak(string.toLowerCase());
		int size = string.length();
		for (int start = 0; start < size; start++) {
			for (int offset = 1; offset < (size + 1 - start) && offset < largestWordSize; offset++) {
				if (SPECIAL_WORDS.get(string.substring(start, start + offset)) instanceof BlockedWord)
					return true;
			}
		}
		return false;
	}

	public static String censor(String string) {
		String scanString = removeLeatSpeak(string.toLowerCase());
		int size = scanString.length();
		for (int start = 0; start < size; start++) {
			for (int offset = 1; offset < (size + 1 - start) && offset < largestWordSize; offset++) {
				if (SPECIAL_WORDS.get(scanString.substring(start, start + offset)) instanceof BlockedWord) {
					string = string.substring(0, start) + StringUtils.repeat("*", offset)
							+ string.substring(start + offset, size);
				}
			}
		}
		return string;
	}

	public static String process(String string) {
		String scanString = removeLeatSpeak(string.toLowerCase());
		int size = scanString.length();
		int removed = 0;
		for (int start = 0; start < size; start++) {
			for (int offset = 1; offset < (size + 1 - start) && offset < largestWordSize; offset++) {
				String currentWord = scanString.substring(start, start + offset);
				SpecialWord specialWord = SPECIAL_WORDS.get(currentWord);
				if (specialWord != null) {
					String specialWordString = specialWord.getString(currentWord);
					string = string.substring(0, start - removed) + specialWordString
							+ string.substring(start + offset - removed, size - removed);
					removed += offset - specialWordString.length();
				}
			}
		}
		return string;
	}

	private static String removeLeatSpeak(String string) {
		StringBuilder stringBuilder = new StringBuilder(string);
		int size = string.length();
		for (int i = 0; i < size; i++) {
			switch (string.charAt(i)) {
			case '1':
			case '!':
				stringBuilder.setCharAt(i, 'i');
				break;

			case '3':
				stringBuilder.setCharAt(i, 'e');
				break;

			case '4':
			case '@':
				stringBuilder.setCharAt(i, 'a');
				break;

			case '5':
				stringBuilder.setCharAt(i, 's');
				break;

			case '7':
			case '+':
				stringBuilder.setCharAt(i, 't');
				break;

			case '0':
				stringBuilder.setCharAt(i, 'o');
				break;

			case '9':
				stringBuilder.setCharAt(i, 'g');
				break;

			default:
				break;
			}
		}
		return stringBuilder.toString();
	}

	public static SpecialWord get(String key) {
		return SPECIAL_WORDS.get(key);
	}

	public static boolean contains(String key) {
		return SPECIAL_WORDS.containsKey(key);
	}

	public static interface SpecialWord {

		public String getString(String string);
	}

	public static class BlockedWord implements SpecialWord {

		@Override
		public String getString(String string) {
			return StringUtils.repeat("*", string.length());
		}
	}

	public static class SymbolWord implements SpecialWord {

		public final char symbol;

		public SymbolWord(char symbol) {
			this.symbol = symbol;
		}

		@Override
		public String getString(String string) {
			return Character.toString(symbol);
		}
	}
}
