package com.pm.aiost.misc.utils.worldEdit;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nullable;

import com.pm.aiost.misc.SpigotConfigManager;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.utils.FileUtils;
import com.pm.aiost.misc.utils.nbt.NBTHelper;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.misc.utils.nbt.custom.NBTCompound;
import com.pm.aiost.misc.utils.nbt.custom.NBTCompoundWrapper;

public class SchematicFileLoader {

	private static final File SCHEMATIC_FOLDER;

	static {
		SCHEMATIC_FOLDER = new File(SpigotConfigManager.getAiostFolderPath(), "schematics");
		if (!SCHEMATIC_FOLDER.exists())
			SCHEMATIC_FOLDER.mkdir();
	}

	public static Schematic load(String name) {
		File src = new File(SCHEMATIC_FOLDER, name);
		if (!src.exists())
			return null;
		INBTTagCompound nbt = new NBTCompoundWrapper(NBTHelper.fromFile(src));
		Schematic schematic = new Schematic();
		schematic.load(nbt);
		return schematic;
	}

	public static boolean save(String name, Schematic schematic) {
		File dest = new File(SCHEMATIC_FOLDER, name);
		if (dest.exists()) {
			try {
				FileUtils.delete(dest);
			} catch (IOException e) {
				Logger.err("SchematicFileLoader: Error on deleting schematic with name '" + name + "'", e);
				return false;
			}
		}
		NBTCompound nbt = new NBTCompound();
		schematic.save(nbt);
		return NBTHelper.toFile(dest, nbt);
	}

	public static boolean delete(String name) {
		File dest = new File(SCHEMATIC_FOLDER, name);
		if (dest.exists()) {
			try {
				FileUtils.delete(dest);
				return true;
			} catch (IOException e) {
				Logger.err("SchematicFileLoader: Error on deleting schematic with name '" + name + "'", e);
			}
		}
		return false;
	}

	@Nullable
	public static File getFile(String uuid) {
		File file = new File(SCHEMATIC_FOLDER, uuid);
		if (file.exists())
			return file;
		return null;
	}
}