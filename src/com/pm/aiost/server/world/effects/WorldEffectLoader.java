package com.pm.aiost.server.world.effects;

import java.io.File;

import com.pm.aiost.effect.Effect;
import com.pm.aiost.misc.nms.NBT;
import com.pm.aiost.misc.nms.NBT.NBTType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

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

		CompoundTag nbt = NBT.fromFile(effectFile);
		ListTag effectList = nbt.getList("effects", NBTType.COMPOUND);
		int size = effectList.size();
		if (size > 0) {
			Effect[] effects = new Effect[size];
			for (int i = 0; i < size; i++)
				effects[i] = Effect.loadNBT(effectList.getCompound(i));
			worldEffects.addSynchronized(id, effects);
		}

		ListTag selfEffectList = nbt.getList("selfEffects", NBTType.COMPOUND);
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

		CompoundTag nbt = new CompoundTag();
		ListTag effectList = new ListTag();
		for (int i = 0; i < effects.length; i++)
			effectList.add(Effect.saveNBT(effects[i], new CompoundTag()));
		nbt.put("effects", effectList);

		ListTag selfEffectList = new ListTag();
		for (int i = 0; i < selfEffects.length; i++)
			selfEffectList.add(Effect.saveNBT(selfEffects[i], new CompoundTag()));
		nbt.put("selfEffects", selfEffectList);
		NBT.toFile(effectFile, nbt);
	}

	public boolean fileExists(int id) {
		return new File(dir, Integer.toString(id)).exists();
	}

	public String[] getFileNames() {
		return dir.list();
	}
}
