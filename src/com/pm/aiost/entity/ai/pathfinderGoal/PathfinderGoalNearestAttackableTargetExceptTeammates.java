package com.pm.aiost.entity.ai.pathfinderGoal;

import java.util.function.Predicate;

import org.bukkit.craftbukkit.libs.jline.internal.Nullable;

import com.pm.aiost.entity.ownable.OwnableEntity;
import com.pm.aiost.entity.ownable.OwnableInsentient;

import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_15_R1.Scoreboard;
import net.minecraft.server.v1_15_R1.ScoreboardTeam;

public class PathfinderGoalNearestAttackableTargetExceptTeammates<T extends EntityLiving>
		extends PathfinderGoalNearestAttackableTarget<T> {

	protected OwnableInsentient ownableEntity;

	public PathfinderGoalNearestAttackableTargetExceptTeammates(OwnableInsentient ownableEntity, Class<T> oclass,
			boolean flag) {
		super(ownableEntity.getEntity(), oclass, flag, false);
		this.ownableEntity = ownableEntity;
	}

	public PathfinderGoalNearestAttackableTargetExceptTeammates(OwnableInsentient ownableEntity, Class<T> oclass,
			boolean flag, boolean flag1) {
		super(ownableEntity.getEntity(), oclass, 10, flag, flag1, null);
		this.ownableEntity = ownableEntity;
	}

	public PathfinderGoalNearestAttackableTargetExceptTeammates(OwnableInsentient ownableEntity, Class<T> oclass, int i,
			boolean flag, boolean flag1, @Nullable Predicate<EntityLiving> predicate) {
		super(ownableEntity.getEntity(), oclass, i, flag, flag1, predicate);
		this.ownableEntity = ownableEntity;
	}

	@Override
	public boolean a() {
		boolean a = super.a();
		if (c != null) {
			if (c instanceof EntityHuman) {
				return check((EntityHuman) c, a);
			}

			else if (c instanceof OwnableEntity) {
				EntityHuman targetOwner = ((OwnableEntity) this.d).getOwner();
				if (targetOwner != null)
					return check(targetOwner, a);
			}
		}
		return a;
	}

	protected boolean check(EntityHuman targetPlayer, boolean bool) {
		if (targetPlayer == ownableEntity.getOwner())
			return false;

		Scoreboard targetScoreboard = targetPlayer.getScoreboard();
		if (targetScoreboard != null) {
			ScoreboardTeam targetTeam = targetScoreboard.getPlayerTeam(targetPlayer.getName());
			if (targetTeam != null) {
				Scoreboard scoreboard = ownableEntity.getOwner().getScoreboard();
				if (scoreboard != null) {
					ScoreboardTeam team = scoreboard.getPlayerTeam(ownableEntity.getOwner().getName());
					if (targetTeam.isAlly(team)) {
						return false;
					}
				}
			}
		}
		return bool;
	}
}
