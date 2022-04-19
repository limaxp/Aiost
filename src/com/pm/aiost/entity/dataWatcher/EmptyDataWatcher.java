package com.pm.aiost.entity.dataWatcher;

import com.pm.aiost.entity.AiostEntityTypes;

import net.minecraft.server.v1_15_R1.DataWatcher;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.Packet;

public class EmptyDataWatcher extends DataWatcher {

	public static final Entity NULL_ENTITY = new Entity(AiostEntityTypes.PLAYER, null) {

		@Override
		public Packet<?> L() {
			return null;
		}

		@Override
		protected void a(NBTTagCompound arg0) {
		}

		@Override
		protected void b(NBTTagCompound arg0) {
		}

		@Override
		protected void initDatawatcher() {
		}

	};

	public EmptyDataWatcher() {
		super(NULL_ENTITY);
	}
}
