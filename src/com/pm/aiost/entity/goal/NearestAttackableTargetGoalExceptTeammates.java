package com.pm.aiost.entity.goal;

import java.util.function.Predicate;

import org.bukkit.entity.Entity;

import com.pm.aiost.misc.event.eventHandler.EventHandler;
import com.pm.aiost.misc.event.eventHandler.EventHandlerManager;
import com.pm.aiost.misc.event.eventHandler.handler.OwnableEventHandler;
import com.pm.aiost.misc.nms.NMS;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;

public class NearestAttackableTargetGoalExceptTeammates<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

	protected OwnableEventHandler handler;

	public NearestAttackableTargetGoalExceptTeammates(Mob ownableEntity, OwnableEventHandler handler, Class<T> oclass,
			boolean flag) {
		super(ownableEntity, oclass, flag, false);
		this.handler = handler;
	}

	public NearestAttackableTargetGoalExceptTeammates(Mob ownableEntity, OwnableEventHandler handler, Class<T> oclass,
			boolean flag, boolean flag1) {
		super(ownableEntity, oclass, 10, flag, flag1, null);
		this.handler = handler;
	}

	public NearestAttackableTargetGoalExceptTeammates(Mob ownableEntity, OwnableEventHandler handler, Class<T> oclass,
			int i, boolean flag, boolean flag1, Predicate<LivingEntity> predicate) {
		super(ownableEntity, oclass, i, flag, flag1, predicate);
		this.handler = handler;
	}

	@Override
	public boolean canUse() {
		boolean canUse = super.canUse();
		if (!canUse)
			return false;

		if (target instanceof Player && !(check((Player) target)))
			return false;

		else {
			EventHandler handler = EventHandlerManager.get(NMS.from(target));
			if (handler instanceof OwnableEventHandler) {
				Entity targetOwner = ((OwnableEventHandler) handler).getOwner();
				if (targetOwner instanceof org.bukkit.entity.Player)
					return check((Player) NMS.to(targetOwner));
			}
		}
		return canUse;
	}

	protected boolean check(Player targetPlayer) {
		if (targetPlayer.getId() == handler.getOwner().getEntityId())
			return false;

		Scoreboard targetScoreboard = targetPlayer.getScoreboard();
		if (targetScoreboard == null)
			return false;

		PlayerTeam targetTeam = targetScoreboard.getPlayerTeam(targetPlayer.getScoreboardName());
		if (targetTeam == null)
			return false;

		if (!(handler.getOwner() instanceof org.bukkit.entity.Player))
			return false;

		Scoreboard scoreboard = NMS.to((org.bukkit.entity.Player) handler.getOwner()).getScoreboard();
		if (scoreboard == null)
			return false;

		PlayerTeam team = scoreboard.getPlayerTeam(handler.getOwner().getName());
		if (targetTeam.isAlliedTo(team))
			return false;

		return true;
	}
}
