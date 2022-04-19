package com.pm.aiost.server.world.effects;

import java.io.File;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.misc.utils.nbt.NBTHelper;
import com.pm.aiost.misc.utils.nbt.NBTType;

import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.NBTTagList;

public class WorldEffectLoader {

	private final File dir;

	public WorldEffectLoader(File dir) {
		this.dir = dir;
		if (!dir.exists())
			dir.mkdir();
	}

	public void load(int id, WorldEffects worldEffects) {
		File effectFile = new File(dir, Integer.toString(id));
		if (!effectFile.exists())
			return;

		NBTTagCompound nbt = NBTHelper.fromFile(effectFile);
		NBTTagList effectList = nbt.getList("effects", NBTType.COMPOUND);
		int size = effectList.size();
		if (size > 0) {
			Effect[] effects = new Effect[size];
			for (int i = 0; i < size; i++)
				effects[i] = Effect.loadNBT(effectList.getCompound(i));
			worldEffects.addSynchronized(id, effects);
		}

		NBTTagList selfEffectList = nbt.getList("selfEffects", NBTType.COMPOUND);
		size = selfEffectList.size();
		if (size > 0) {
			Effect[] selfEffects = new Effect[size];
			for (int i = 0; i < size; i++)
				selfEffects[i] = Effect.loadNBT(selfEffectList.getCompound(i));
			worldEffects.addSelfSynchronized(id, selfEffects);
		}
	}

	public void save(int id, Effect[] effects, Effect[] selfEffects) {
		File effectFile = new File(dir, Integer.toString(id));
		if (effectFile.exists())
			effectFile.delete();

		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList effectList = new NBTTagList();
		for (int i = 0; i < effects.length; i++)
			effectList.add(Effect.saveNBT(effects[i], new NBTTagCompound()));
		nbt.set("effects", effectList);

		NBTTagList selfEffectList = new NBTTagList();
		for (int i = 0; i < selfEffects.length; i++)
			selfEffectList.add(Effect.saveNBT(selfEffects[i], new NBTTagCompound()));
		nbt.set("selfEffects", selfEffectList);
		NBTHelper.toFile(effectFile, nbt);
	}

	public boolean fileExists(int id) {
		return new File(dir, Integer.toString(id)).exists();
	}

	public String[] getFileNames() {
		return dir.list();
	}
}
