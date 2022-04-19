package com.pm.aiost.misc.utils.nbt.custom;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Set;

import net.minecraft.server.v1_15_R1.NBTBase;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.NBTTagList;

public interface INBTTagCompound extends NBTBase {

	public NBTTagCompound getNMS();

	public void write(DataOutput var0) throws IOException;

	public Set<String> getKeys();

	public byte getTypeId();

	public NBTBase set(String var0, NBTBase var1);

	public void setByte(String var0, byte var1);

	public void setShort(String var0, short var1);

	public void setInt(String var0, int var1);

	public void setLong(String var0, long var1);

	public void setFloat(String var0, float var1);

	public void setDouble(String var0, double var1);

	public void setString(String var0, String var1);

	public void setByteArray(String var0, byte[] var1);

	public void setIntArray(String var0, int[] var1);

	public void setBoolean(String var0, boolean var1);

	public NBTBase get(String var0);

	public boolean hasKey(String var0);

	public boolean hasKeyOfType(String var0, int var1);

	public byte getByte(String var0);

	public short getShort(String var0);

	public int getInt(String var0);

	public long getLong(String var0);

	public float getFloat(String var0);

	public double getDouble(String var0);

	public String getString(String var0);

	public byte[] getByteArray(String var0);

	public int[] getIntArray(String var0);

	public long[] getLongArray(String var0);

	public NBTTagCompound getCompound(String var0);

	public NBTTagList getList(String var0, int var1);

	public boolean getBoolean(String var0);

	public void remove(String var0);

	public boolean isEmpty();
}
