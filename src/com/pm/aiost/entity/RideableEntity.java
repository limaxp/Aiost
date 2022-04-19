package com.pm.aiost.entity;

import com.pm.aiost.misc.utils.nms.NMS;

import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.GenericAttributes;
import net.minecraft.server.v1_15_R1.MathHelper;
import net.minecraft.server.v1_15_R1.Vec3D;

public interface RideableEntity extends AiostLiving {

	public void super_e(Vec3D vec3d);

	public void super_setYawPitch(float yaw, float pitch);

	public default void riding(Vec3D vec3d) {
		if (isAlive()) {
			EntityLiving entity = getEntity();
			if (isVehicle() && getPassengers().get(0) instanceof EntityLiving) {
				EntityLiving entityLiving = (EntityLiving) getPassengers().get(0);

				entity.lastYaw = entity.yaw = entityLiving.yaw;
				entity.pitch = entityLiving.pitch * 0.5F;
				super_setYawPitch(entity.yaw, entity.pitch);
				entity.aI = entity.yaw;
				entity.aK = entity.aI;

				float f = entityLiving.aZ * 0.5F;
				float f1 = entityLiving.bb;

				if (f1 <= 0.0F)
					f1 *= 0.25F;

				boolean jumping = NMS.isJumping(entityLiving);
				if (jumping && entity.onGround) {
					Vec3D vec3d1 = getMot();
					setMot(vec3d1.x, 0.5, vec3d1.z);
				}

				entity.aM = entity.dt() * 0.1F;
				if (entity.cj()) {
					entity.o((float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
					super_e(new Vec3D(f, vec3d.y, f1));
				} else if (entityLiving instanceof EntityHuman) {
					setMot(Vec3D.a);
				}

				entity.aC = entity.aD;
				double d0 = entity.locX() - entity.lastX;
				double d1 = entity.locZ() - entity.lastZ;
				float f4 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;
				if (f4 > 1.0F)
					f4 = 1.0F;

				entity.aD += (f4 - entity.aD) * 0.4F;
				entity.aE += entity.aD;
			} else {
				entity.aM = 0.02F;
				super_e(vec3d);
			}
		}
	}
}
