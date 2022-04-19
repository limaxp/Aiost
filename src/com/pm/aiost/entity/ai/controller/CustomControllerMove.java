package com.pm.aiost.entity.ai.controller;

import com.pm.aiost.entity.CustomInsentient;
import com.pm.aiost.entity.ai.navigation.CustomNavigationAbstract;

import net.minecraft.server.v1_15_R1.Block;
import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.ControllerMove;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.EnumDirection;
import net.minecraft.server.v1_15_R1.GenericAttributes;
import net.minecraft.server.v1_15_R1.IBlockData;
import net.minecraft.server.v1_15_R1.MathHelper;
import net.minecraft.server.v1_15_R1.PathType;
import net.minecraft.server.v1_15_R1.PathfinderAbstract;
import net.minecraft.server.v1_15_R1.TagsBlock;
import net.minecraft.server.v1_15_R1.VoxelShape;

public class CustomControllerMove extends ControllerMove {

	protected final CustomInsentient a;
	protected final EntityLiving a_;

	public CustomControllerMove(CustomInsentient entity) {
		super(null);
		this.a = entity;
		a_ = a.getEntity();
	}

	@Override
	public void a() {
		if (this.h == Operation.STRAFE) {

			float var0 = (float) this.a.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue();
			float var1 = (float) this.e * var0;

			float var2 = this.f;
			float var3 = this.g;
			float var4 = MathHelper.c(var2 * var2 + var3 * var3);
			if (var4 < 1.0F) {
				var4 = 1.0F;
			}
			var4 = var1 / var4;
			var2 *= var4;
			var3 *= var4;

			float var5 = MathHelper.sin(a_.yaw * 0.017453292F);
			float var6 = MathHelper.cos(a_.yaw * 0.017453292F);
			float var7 = var2 * var6 - var3 * var5;
			float var8 = var3 * var6 + var2 * var5;

			CustomNavigationAbstract var9 = this.a.getNavigation();
			if (var9 != null) {
				PathfinderAbstract var10 = var9.q();
				if (var10 != null && var10.a(a_.world, MathHelper.floor(this.a.locX() + var7),
						MathHelper.floor(this.a.locY()), MathHelper.floor(this.a.locZ() + var8)) != PathType.WALKABLE) {
					this.f = 1.0F;
					this.g = 0.0F;
					var1 = var0;
				}
			}

			this.a.o(var1);
			this.a.r(this.f);
			this.a.t(this.g);

			this.h = Operation.WAIT;
		} else if (this.h == Operation.MOVE_TO) {
			this.h = Operation.WAIT;

			double var0 = this.b - this.a.locX();
			double var2 = this.d - this.a.locZ();
			double var4 = this.c - this.a.locY();
			double var6 = var0 * var0 + var4 * var4 + var2 * var2;
			if (var6 < 2.500000277905201E-7D) {
				this.a.r(0.0F);

				return;
			}
			float var8 = (float) (MathHelper.d(var2, var0) * 57.2957763671875D) - 90.0F;

			a_.yaw = a(a_.yaw, var8, 90.0F);
			this.a.o((float) (this.e * this.a.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue()));

			BlockPosition var9 = new BlockPosition(a_);
			IBlockData var10 = a_.world.getType(var9);
			Block var11 = var10.getBlock();
			VoxelShape var12 = var10.getCollisionShape(a_.world, var9);
			if ((var4 > a_.H && var0 * var0 + var2 * var2 < Math.max(1.0F, this.a.getWidth()))
					|| (!var12.isEmpty() && this.a.locY() < var12.c(EnumDirection.EnumAxis.Y) + var9.getY()
							&& !var11.a(TagsBlock.DOORS) && !var11.a(TagsBlock.FENCES))) {

				this.a.getControllerJump().jump();
				this.h = Operation.JUMPING;
			}
		} else if (this.h == Operation.JUMPING) {
			this.a.o((float) (this.e * this.a.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue()));
			if (a_.onGround) {
				this.h = Operation.WAIT;
			}
		} else {
			this.a.r(0.0F);
		}
	}
}
