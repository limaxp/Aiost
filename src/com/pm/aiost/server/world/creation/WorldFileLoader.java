package com.pm.aiost.server.world.creation;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.pm.aiost.misc.SpigotConfigManager;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.utils.FileUtils;

public class WorldFileLoader {

	private static final File WORLDS_FOLDER;

	static {
		WORLDS_FOLDER = new File(SpigotConfigManager.getAiostFolderPath(), "worlds");
		if (!WORLDS_FOLDER.exists())
			WORLDS_FOLDER.mkdir();
	}

	public static void loadWorld(UUID uuid, File dest) {
		loadWorld(uuid.toString(), dest);
	}

	public static void loadWorld(String uuid, File dest) {
		File src = new File(WORLDS_FOLDER, uuid);
		try {
			FileUtils.copyDirectory(src, dest);
		} catch (IOException e) {
			Logger.err("WorldLoader: Error on loading world with id '" + uuid + "'", e);
			try {
				FileUtils.delete(dest);
			} catch (IOException e1) {
				// swallow
			}
		}
	}

	public static boolean saveWorld(UUID uuid, World world) {
		return saveWorld(uuid.toString(), world);
	}

	public static boolean saveWorld(String uuid, World world) {
		return saveWorld(uuid, new File(Bukkit.getWorldContainer(), world.getName()));
	}

	public static boolean saveWorld(UUID uuid, File src) {
		return saveWorld(uuid.toString(), src);
	}

	public static boolean saveWorld(String uuid, File src) {
		File dest = new File(WORLDS_FOLDER, uuid);
		try {
			if (dest.exists())
				FileUtils.delete(dest);
			FileUtils.copyDirectory(src, dest);
			deleteEventHandlerFile(dest);
			deleteUUIDFile(dest);
			return true;
		} catch (IOException e) {
			Logger.err("WorldLoader: Error on saving world with id '" + uuid + "'", e);
			try {
				FileUtils.delete(dest);
			} catch (IOException e1) {
				// swallow
			}
		}
		return false;
	}

	public static boolean deleteWorld(UUID uuid) {
		return deleteWorld(uuid.toString());
	}

	public static boolean deleteWorld(String uuid) {
		File dest = new File(WORLDS_FOLDER, uuid);
		if (dest.exists()) {
			try {
				FileUtils.delete(dest);
				return true;
			} catch (IOException e) {
				Logger.err("WorldLoader: Error on deleting world with id '" + uuid + "'", e);
			}
		}
		return false;
	}

	private static void deleteEventHandlerFile(File file) throws IOException {
		File eventHandlerFile = new File(file, "aiost" + File.separator + "eventHandler.yml");
		if (eventHandlerFile.exists())
			FileUtils.delete(eventHandlerFile);
	}

	private static void deleteUUIDFile(File file) throws IOException {
		File uuidFile = new File(file, "uid.dat");
		if (uuidFile.exists())
			FileUtils.delete(uuidFile);
	}

	@Nullable
	public static File getWorld(UUID uuid) {
		return getWorld(uuid.toString());
	}

	@Nullable
	public static File getWorld(String uuid) {
		File file = new File(WORLDS_FOLDER, uuid);
		if (file.exists())
			return file;
		return null;
	}
}
