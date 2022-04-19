package com.pm.aiost.misc.other;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class NullConfigurationSection implements ConfigurationSection {

	public static final NullConfigurationSection INSTANCE = new NullConfigurationSection();

	@Override
	public void addDefault(@Nonnull String arg0, @Nullable Object arg1) {

	}

	@Override
	public boolean contains(@Nonnull String arg0) {

		return false;
	}

	@Override
	public boolean contains(@Nonnull String arg0, boolean arg1) {

		return false;
	}

	@Override
	public @Nonnull ConfigurationSection createSection(@Nonnull String arg0) {

		return null;
	}

	@Override
	public @Nonnull ConfigurationSection createSection(@Nonnull String arg0, @Nonnull Map<?, ?> arg1) {

		return null;
	}

	@Override
	public @Nullable Object get(@Nonnull String arg0) {

		return null;
	}

	@Override
	public @Nullable Object get(@Nonnull String arg0, @Nullable Object arg1) {

		return null;
	}

	@Override
	public boolean getBoolean(@Nonnull String arg0) {

		return false;
	}

	@Override
	public boolean getBoolean(@Nonnull String arg0, boolean arg1) {

		return false;
	}

	@Override
	public @Nonnull List<Boolean> getBooleanList(@Nonnull String arg0) {

		return null;
	}

	@Override
	public @Nonnull List<Byte> getByteList(@Nonnull String arg0) {

		return null;
	}

	@Override
	public @Nonnull List<Character> getCharacterList(@Nonnull String arg0) {

		return null;
	}

	@Override
	public @Nullable Color getColor(@Nonnull String arg0) {

		return null;
	}

	@Override
	public @Nullable Color getColor(@Nonnull String arg0, @Nullable Color arg1) {

		return null;
	}

	@Override
	public @Nullable ConfigurationSection getConfigurationSection(@Nonnull String arg0) {

		return null;
	}

	@Override
	public @Nullable String getCurrentPath() {

		return null;
	}

	@Override
	public @Nullable ConfigurationSection getDefaultSection() {

		return null;
	}

	@Override
	public double getDouble(@Nonnull String arg0) {

		return 0;
	}

	@Override
	public double getDouble(@Nonnull String arg0, double arg1) {

		return 0;
	}

	@Override
	public @Nonnull List<Double> getDoubleList(@Nonnull String arg0) {

		return null;
	}

	@Override
	public @Nonnull List<Float> getFloatList(@Nonnull String arg0) {

		return null;
	}

	@Override
	public int getInt(@Nonnull String arg0) {

		return 0;
	}

	@Override
	public int getInt(@Nonnull String arg0, int arg1) {

		return 0;
	}

	@Override
	public @Nonnull List<Integer> getIntegerList(@Nonnull String arg0) {

		return null;
	}

	@Override
	public @Nullable ItemStack getItemStack(@Nonnull String arg0) {

		return null;
	}

	@Override
	public @Nullable ItemStack getItemStack(@Nonnull String arg0, @Nullable ItemStack arg1) {

		return null;
	}

	@Override
	public @Nonnull Set<String> getKeys(boolean arg0) {

		return null;
	}

	@Override
	public @Nullable List<?> getList(@Nonnull String arg0) {

		return null;
	}

	@Override
	public @Nullable List<?> getList(@Nonnull String arg0, @Nullable List<?> arg1) {

		return null;
	}

	@Override
	public @Nullable Location getLocation(@Nonnull String arg0) {

		return null;
	}

	@Override
	public @Nullable Location getLocation(@Nonnull String arg0, @Nullable Location arg1) {

		return null;
	}

	@Override
	public long getLong(@Nonnull String arg0) {

		return 0;
	}

	@Override
	public long getLong(@Nonnull String arg0, long arg1) {

		return 0;
	}

	@Override
	public @Nonnull List<Long> getLongList(@Nonnull String arg0) {

		return null;
	}

	@Override
	public @Nonnull List<Map<?, ?>> getMapList(@Nonnull String arg0) {

		return null;
	}

	@Override
	public @Nonnull String getName() {

		return null;
	}

	@Override
	public <T> T getObject(@Nonnull String arg0, @Nonnull Class<T> arg1) {

		return null;
	}

	@Override
	public <T> T getObject(@Nonnull String arg0, @Nonnull Class<T> arg1, @Nullable T arg2) {

		return null;
	}

	@Override
	public @Nullable OfflinePlayer getOfflinePlayer(@Nonnull String arg0) {

		return null;
	}

	@Override
	public @Nullable OfflinePlayer getOfflinePlayer(@Nonnull String arg0, @Nullable OfflinePlayer arg1) {

		return null;
	}

	@Override
	public @Nullable ConfigurationSection getParent() {

		return null;
	}

	@Override
	public @Nullable Configuration getRoot() {

		return null;
	}

	@Override
	public <T extends ConfigurationSerializable> T getSerializable(@Nonnull String arg0, @Nonnull Class<T> arg1) {

		return null;
	}

	@Override
	public <T extends ConfigurationSerializable> T getSerializable(@Nonnull String arg0, @Nonnull Class<T> arg1,
			@Nullable T arg2) {

		return null;
	}

	@Override
	public @Nonnull List<Short> getShortList(@Nonnull String arg0) {

		return null;
	}

	@Override
	public @Nullable String getString(@Nonnull String arg0) {

		return null;
	}

	@Override
	public @Nullable String getString(@Nonnull String arg0, @Nullable String arg1) {

		return null;
	}

	@Override
	public @Nonnull List<String> getStringList(@Nonnull String arg0) {

		return null;
	}

	@Override
	public @Nonnull Map<String, Object> getValues(boolean arg0) {

		return null;
	}

	@Override
	public @Nullable Vector getVector(@Nonnull String arg0) {

		return null;
	}

	@Override
	public @Nullable Vector getVector(@Nonnull String arg0, @Nullable Vector arg1) {

		return null;
	}

	@Override
	public boolean isBoolean(@Nonnull String arg0) {

		return false;
	}

	@Override
	public boolean isColor(@Nonnull String arg0) {

		return false;
	}

	@Override
	public boolean isConfigurationSection(@Nonnull String arg0) {

		return false;
	}

	@Override
	public boolean isDouble(@Nonnull String arg0) {

		return false;
	}

	@Override
	public boolean isInt(@Nonnull String arg0) {

		return false;
	}

	@Override
	public boolean isItemStack(@Nonnull String arg0) {

		return false;
	}

	@Override
	public boolean isList(@Nonnull String arg0) {

		return false;
	}

	@Override
	public boolean isLocation(@Nonnull String arg0) {

		return false;
	}

	@Override
	public boolean isLong(@Nonnull String arg0) {

		return false;
	}

	@Override
	public boolean isOfflinePlayer(@Nonnull String arg0) {

		return false;
	}

	@Override
	public boolean isSet(@Nonnull String arg0) {

		return false;
	}

	@Override
	public boolean isString(@Nonnull String arg0) {

		return false;
	}

	@Override
	public boolean isVector(@Nonnull String arg0) {

		return false;
	}

	@Override
	public void set(@Nonnull String arg0, @Nullable Object arg1) {

	}

}
