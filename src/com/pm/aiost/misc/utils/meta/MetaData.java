package com.pm.aiost.misc.utils.meta;

import java.util.List;
import java.util.concurrent.Callable;

import javax.annotation.Nullable;

import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;

import com.pm.aiost.Aiost;

public class MetaData {

	private static Plugin plugin = Aiost.getPlugin();

	public static void set(Metadatable metable, String key, Object obj) {
		metable.setMetadata(key, new FixedMetadataValue(plugin, obj));
	}

	public static void set(Metadatable metable, String key, Callable<Object> callable) {
		metable.setMetadata(key, new LazyMetadataValue(plugin, callable));
	}

	public static void remove(Metadatable metable, String key) {
		metable.removeMetadata(key, plugin);
	}

	public static boolean has(Metadatable metable, String key) {
		return metable.hasMetadata(key);
	}

	public static Object get(Metadatable metable, String key) {
		List<MetadataValue> values = metable.getMetadata(key);
		if (values.size() > 0)
			return values.get(0).value();
		return null;
	}

	public static Object getSafe(Metadatable metable, String key) {
		List<MetadataValue> values = metable.getMetadata(key);
		for (MetadataValue val : values) {
			if (val.getOwningPlugin() == plugin)
				return val.value();
		}
		return null;
	}

	public static long getLong(Metadatable metable, String key) {
		List<MetadataValue> values = metable.getMetadata(key);
		if (values.size() > 0)
			return values.get(0).asLong();
		return 0;
	}

	public static long getLongSafe(Metadatable metable, String key) {
		List<MetadataValue> values = metable.getMetadata(key);
		for (MetadataValue val : values) {
			if (val.getOwningPlugin() == plugin)
				return val.asLong();
		}
		return 0;
	}

	public static long getInt(Metadatable metable, String key) {
		List<MetadataValue> values = metable.getMetadata(key);
		if (values.size() > 0)
			return values.get(0).asInt();
		return 0;
	}

	public static int getIntSafe(Metadatable metable, String key) {
		List<MetadataValue> values = metable.getMetadata(key);
		for (MetadataValue val : values) {
			if (val.getOwningPlugin() == plugin)
				return val.asInt();
		}
		return 0;
	}

	public static short getShort(Metadatable metable, String key) {
		List<MetadataValue> values = metable.getMetadata(key);
		if (values.size() > 0)
			return values.get(0).asShort();
		return 0;
	}

	public static short getShortSafe(Metadatable metable, String key) {
		List<MetadataValue> values = metable.getMetadata(key);
		for (MetadataValue val : values) {
			if (val.getOwningPlugin() == plugin)
				return val.asShort();
		}
		return 0;
	}

	public static byte getByte(Metadatable metable, String key) {
		List<MetadataValue> values = metable.getMetadata(key);
		if (values.size() > 0)
			return values.get(0).asByte();
		return 0;
	}

	public static byte getByteSafe(Metadatable metable, String key) {
		List<MetadataValue> values = metable.getMetadata(key);
		for (MetadataValue val : values) {
			if (val.getOwningPlugin() == plugin)
				return val.asByte();
		}
		return 0;
	}

	public static float getFloat(Metadatable metable, String key) {
		List<MetadataValue> values = metable.getMetadata(key);
		if (values.size() > 0)
			return values.get(0).asFloat();
		return 0;
	}

	public static float getFloatSafe(Metadatable metable, String key) {
		List<MetadataValue> values = metable.getMetadata(key);
		for (MetadataValue val : values) {
			if (val.getOwningPlugin() == plugin)
				return val.asFloat();
		}
		return 0;
	}

	public static double getDouble(Metadatable metable, String key) {
		List<MetadataValue> values = metable.getMetadata(key);
		if (values.size() > 0)
			return values.get(0).asDouble();
		return 0;
	}

	public static double getDoubleSafe(Metadatable metable, String key) {
		List<MetadataValue> values = metable.getMetadata(key);
		for (MetadataValue val : values) {
			if (val.getOwningPlugin() == plugin)
				return val.asDouble();
		}
		return 0;
	}

	public static boolean getBoolean(Metadatable metable, String key) {
		List<MetadataValue> values = metable.getMetadata(key);
		if (values.size() > 0)
			return values.get(0).asBoolean();
		return false;
	}

	public static boolean getBooleanSafe(Metadatable metable, String key) {
		List<MetadataValue> values = metable.getMetadata(key);
		for (MetadataValue val : values) {
			if (val.getOwningPlugin() == plugin)
				return val.asBoolean();
		}
		return false;
	}

	public static @Nullable String getString(Metadatable metable, String key) {
		List<MetadataValue> values = metable.getMetadata(key);
		if (values.size() > 0)
			return values.get(0).asString();
		return null;
	}

	public static @Nullable String getStringSafe(Metadatable metable, String key) {
		List<MetadataValue> values = metable.getMetadata(key);
		for (MetadataValue val : values) {
			if (val.getOwningPlugin() == plugin)
				return val.asString();
		}
		return null;
	}
}
