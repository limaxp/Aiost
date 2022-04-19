package com.pm.aiost.misc.utils.nbt.custom;

import net.minecraft.server.v1_15_R1.NBTTagCompound;

public class NBTCompound extends NBTTagCompound implements INBTTagCompound {

	public NBTCompound() {
	}

	@Override
	public NBTTagCompound getNMS() {
		return this;
	}
}
