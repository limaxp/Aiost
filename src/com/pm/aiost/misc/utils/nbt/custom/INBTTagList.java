package com.pm.aiost.misc.utils.nbt.custom;

import java.util.List;

import net.minecraft.server.v1_15_R1.NBTBase;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.NBTTagList;

public interface INBTTagList extends NBTBase, List<NBTBase> {

	public NBTTagList getNMS();

	public NBTBase set(int paramInt, NBTBase paramT);

	public void add(int paramInt, NBTBase paramT);

	public NBTBase remove(int paramInt);

	public NBTBase get(int var0);

	public boolean a(int paramInt, NBTBase paramNBTBase);

	public boolean b(int paramInt, NBTBase paramNBTBase);

	public NBTTagCompound getCompound(int var0);

	public String getString(int var0);

	public int size();

	public boolean isEmpty();

	public void clear();
}
