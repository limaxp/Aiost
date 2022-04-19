package com.pm.aiost.entity.npc;

import com.mojang.authlib.GameProfile;
import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.ai.pathfinderGoal.custom.CustomPathfinderGoalLookAtPlayer;
import com.pm.aiost.entity.ai.pathfinderGoal.custom.CustomPathfinderGoalMeleeAttack;
import com.pm.aiost.entity.ai.pathfinderGoal.custom.CustomPathfinderGoalNearestAttackableTarget;
import com.pm.aiost.entity.ai.pathfinderGoal.custom.CustomPathfinderGoalRandomLookaround;
import com.pm.aiost.entity.ai.pathfinderGoal.custom.CustomPathfinderGoalRandomStrollLand;

import net.minecraft.server.v1_15_R1.DifficultyDamageScaler;
import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.EnumDifficulty;
import net.minecraft.server.v1_15_R1.EnumItemSlot;
import net.minecraft.server.v1_15_R1.EnumMobSpawn;
import net.minecraft.server.v1_15_R1.GeneratorAccess;
import net.minecraft.server.v1_15_R1.GroupDataEntity;
import net.minecraft.server.v1_15_R1.ItemStack;
import net.minecraft.server.v1_15_R1.Items;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.World;

public class NpcEnemy extends NpcInsentient {

	public NpcEnemy(EntityTypes<? extends NpcEnemy> entitytypes, World world) {
		super(world);
	}

	public NpcEnemy(World world) {
		super(world);
	}

	public NpcEnemy(World world, GameProfile profile) {
		super(world, profile);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void initPathfinder() {
		this.goalSelector.a(2, new CustomPathfinderGoalMeleeAttack(this, 1.0D, true));
		this.goalSelector.a(3, new CustomPathfinderGoalRandomStrollLand(this, 1.0D));
		this.goalSelector.a(4, new CustomPathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(4, new CustomPathfinderGoalRandomLookaround(this));
		this.targetSelector.a(1, new CustomPathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
	}

	@Override
	public EntityTypes<?> getCustomEntityType() {
		return AiostEntityTypes.ENEMY_NPC;
	}

	@Override
	public GroupDataEntity prepare(GeneratorAccess generatoraccess, DifficultyDamageScaler difficultydamagescaler,
			EnumMobSpawn enummobspawn, GroupDataEntity groupdataentity, NBTTagCompound nbttagcompound) {
		a(difficultydamagescaler);
		return groupdataentity;
	}

	@Override
	public void a(DifficultyDamageScaler difficultydamagescaler) {
		super.a(difficultydamagescaler);
		if (this.random.nextFloat() < ((this.world.getDifficulty() == EnumDifficulty.HARD) ? 0.05F : 0.01F)) {
			int i = this.random.nextInt(5);

			switch (i) {
			case 1:
				setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
				break;

			case 2:
				setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
				break;

			case 3:
				setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
				break;

			case 4:
				setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.IRON_HOE));
				break;

			default:
				break;
			}
		}
	}
}
