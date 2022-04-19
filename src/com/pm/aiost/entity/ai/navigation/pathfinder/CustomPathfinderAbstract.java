package com.pm.aiost.entity.ai.navigation.pathfinder;

import com.pm.aiost.entity.CustomInsentient;

import net.minecraft.server.v1_15_R1.ChunkCache;
import net.minecraft.server.v1_15_R1.EntityInsentient;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.IBlockAccess;
import net.minecraft.server.v1_15_R1.MathHelper;
import net.minecraft.server.v1_15_R1.PathType;
import net.minecraft.server.v1_15_R1.PathfinderAbstract;

public abstract class CustomPathfinderAbstract extends PathfinderAbstract {

	protected CustomInsentient b;
	protected EntityLiving b_;

	public abstract PathType a(IBlockAccess paramIBlockAccess, int paramInt1, int paramInt2, int paramInt3,
			CustomInsentient paramEntityInsentient, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean1,
			boolean paramBoolean2);

	@Override
	public PathType a(IBlockAccess paramIBlockAccess, int paramInt1, int paramInt2, int paramInt3,
			EntityInsentient paramEntityInsentient, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean1,
			boolean paramBoolean2) {
		return a(paramIBlockAccess, paramInt1, paramInt2, paramInt3, (CustomInsentient) paramEntityInsentient,
				paramInt4, paramInt5, paramInt6, paramBoolean1, paramBoolean2);
	}

	public void a(ChunkCache var0, CustomInsentient var1) {
		this.a = var0;
		this.b = var1;
		this.b_ = var1.getEntity();
		this.c.clear();
		this.d = MathHelper.d(var1.getWidth() + 1.0F);
		this.e = MathHelper.d(var1.getHeight() + 1.0F);
		this.f = MathHelper.d(var1.getWidth() + 1.0F);
	}

	@Override
	public void a() {
		this.a = null;
		this.b = null;
	}
}
