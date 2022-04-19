package com.pm.aiost.misc.utils.nbt.custom;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Set;

import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.NBTBase;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.NBTTagList;
import net.minecraft.server.v1_15_R1.NBTTagType;

public class NBTCompoundWrapper implements INBTTagCompound {

	public final NBTTagCompound nbtTag;

	public NBTCompoundWrapper(NBTTagCompound nbtTag) {
		this.nbtTag = nbtTag;
	}

	@Override
	public NBTTagCompound getNMS() {
		return nbtTag;
	}

	@Override
	public IChatBaseComponent a(String var0, int var1) {
		return nbtTag.a(var0, var1);
	}

	@Override
	public NBTTagType<?> b() {
		return nbtTag.b();
	}

	@Override
	public NBTBase clone() {
		return nbtTag.clone();
	}

	@Override
	public void write(DataOutput var0) throws IOException {
		nbtTag.write(var0);
	}

	@Override
	public Set<String> getKeys() {
		return nbtTag.getKeys();
	}

	@Override
	public byte getTypeId() {
		return nbtTag.getTypeId();
	}

	@Override
	public NBTBase set(String var0, NBTBase var1) {
		return nbtTag.set(var0, var1);
	}

	@Override
	public void setByte(String var0, byte var1) {
		nbtTag.setByte(var0, var1);
	}

	@Override
	public void setShort(String var0, short var1) {
		nbtTag.setShort(var0, var1);
	}

	@Override
	public void setInt(String var0, int var1) {
		nbtTag.setInt(var0, var1);
	}

	@Override
	public void setLong(String var0, long var1) {
		nbtTag.setLong(var0, var1);
	}

	@Override
	public void setFloat(String var0, float var1) {
		nbtTag.setFloat(var0, var1);
	}

	@Override
	public void setDouble(String var0, double var1) {
		nbtTag.setDouble(var0, var1);
	}

	@Override
	public void setString(String var0, String var1) {
		nbtTag.setString(var0, var1);
	}

	@Override
	public void setByteArray(String var0, byte[] var1) {
		nbtTag.setByteArray(var0, var1);
	}

	@Override
	public void setIntArray(String var0, int[] var1) {
		nbtTag.setIntArray(var0, var1);
	}

	@Override
	public void setBoolean(String var0, boolean var1) {
		nbtTag.setBoolean(var0, var1);
	}

	@Override
	public NBTBase get(String var0) {
		return nbtTag.get(var0);
	}

	@Override
	public boolean hasKey(String var0) {
		return nbtTag.hasKey(var0);
	}

	@Override
	public boolean hasKeyOfType(String var0, int var1) {
		return nbtTag.hasKeyOfType(var0, var1);
	}

	@Override
	public byte getByte(String var0) {
		return nbtTag.getByte(var0);
	}

	@Override
	public short getShort(String var0) {
		return nbtTag.getShort(var0);
	}

	@Override
	public int getInt(String var0) {
		return nbtTag.getInt(var0);
	}

	@Override
	public long getLong(String var0) {
		return nbtTag.getLong(var0);
	}

	@Override
	public float getFloat(String var0) {
		return nbtTag.getFloat(var0);
	}

	@Override
	public double getDouble(String var0) {
		return nbtTag.getDouble(var0);
	}

	@Override
	public String getString(String var0) {
		return nbtTag.getString(var0);
	}

	@Override
	public byte[] getByteArray(String var0) {
		return nbtTag.getByteArray(var0);
	}

	@Override
	public int[] getIntArray(String var0) {
		return nbtTag.getIntArray(var0);
	}

	@Override
	public long[] getLongArray(String var0) {
		return nbtTag.getLongArray(var0);
	}

	@Override
	public NBTTagCompound getCompound(String var0) {
		return nbtTag.getCompound(var0);
	}

	@Override
	public NBTTagList getList(String var0, int var1) {
		return nbtTag.getList(var0, var1);
	}

	@Override
	public boolean getBoolean(String var0) {
		return nbtTag.getBoolean(var0);
	}

	@Override
	public void remove(String var0) {
		nbtTag.remove(var0);
	}

	@Override
	public boolean isEmpty() {
		return nbtTag.isEmpty();
	}

	@Override
	public boolean equals(Object obj) {
		return nbtTag.equals(obj);
	}
}