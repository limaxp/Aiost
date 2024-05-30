package com.pm.aiost.entity.entities;

import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.misc.profile.Profiles;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class NpcBase extends Player {

	public NpcBase(EntityType<? extends NpcBase> entitytypes, Level level) {
		this(level, BlockPos.ZERO, 0.0F, Profiles.ORC);
	}

	public NpcBase(Level level, BlockPos blockposition, float f, GameProfile gameprofile) {
		super(level, blockposition, f, gameprofile);
		this.uuid = UUID.randomUUID();
	}

	@Override
	public boolean isCreative() {
		return false;
	}

	@Override
	public boolean isSpectator() {
		return false;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbttagcompound) {
		super.addAdditionalSaveData(nbttagcompound);
		AiostEntityTypes.saveNBT(nbttagcompound, AiostEntityTypes.NPC_BASE);
	}
}
