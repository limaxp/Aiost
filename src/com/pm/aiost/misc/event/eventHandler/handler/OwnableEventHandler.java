package com.pm.aiost.misc.event.eventHandler.handler;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.pm.aiost.entity.goal.FollowOwnerGoal;
import com.pm.aiost.entity.goal.NearestAttackableTargetGoalExceptTeammates;
import com.pm.aiost.misc.event.eventHandler.EventHandler;
import com.pm.aiost.misc.event.eventHandler.TickableHandler;
import com.pm.aiost.misc.nms.NMS;
import com.pm.aiost.player.ServerPlayer;

import net.minecraft.world.entity.Mob;

public class OwnableEventHandler implements EventHandler, TickableHandler {

	protected LivingEntity entity;
	protected LivingEntity owner;
	protected int duration = -1;

	public OwnableEventHandler() {
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public OwnableEventHandler(LivingEntity entity, LivingEntity owner) {
		setEntity(entity);
		setOwner(owner);

		net.minecraft.world.entity.LivingEntity entityNMS = NMS.to(entity);
		if (entityNMS instanceof Mob) {
			Mob mobNMS = (Mob) entityNMS;
			mobNMS.goalSelector.addGoal(1, new FollowOwnerGoal(mobNMS, this, 1.0D, 10.0F, 2.0F, false));
			mobNMS.targetSelector.addGoal(3, new NearestAttackableTargetGoalExceptTeammates(mobNMS, this,
					net.minecraft.world.entity.player.Player.class, true));
		}
	}

	@Override
	public void onTick(Entity entity) {
		if (duration-- == 0) {
			entity.remove();
			entity.getWorld().spawnParticle(Particle.PORTAL, entity.getLocation(), 20, 0.5F, 0.5F, 0.5F, 0.1F, null,
					false);
		}
	}

	@Override
	public void onPlayerInteractAtEntity(ServerPlayer serverPlayer, PlayerInteractAtEntityEvent event) {
		System.out.println("OWNABLE INTERACT");
		if (serverPlayer.player == owner && serverPlayer.player.isSneaking())
			ownerItemMove(serverPlayer.player);
	}

	public LivingEntity getEntity() {
		return entity;
	}

	public void setEntity(LivingEntity entity) {
		this.entity = entity;
	}

	public LivingEntity getOwner() {
		return owner;
	}

	public void setOwner(LivingEntity owner) {
		if (this.owner instanceof Player)
			removeTeam((Player) this.owner);

		if (owner instanceof Player)
			setTeam((Player) owner);
		this.owner = owner;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration / 4;
	}

	public void setTeam(Player owner) {
		Scoreboard scoreboard = owner.getScoreboard();
		@SuppressWarnings("deprecation")
		Team team = scoreboard.getPlayerTeam(owner);
		if (team != null)
			team.addEntry(entity.getUniqueId().toString());
	}

	public void removeTeam(Player owner) {
		Scoreboard scoreboard = owner.getScoreboard();
		@SuppressWarnings("deprecation")
		Team team = scoreboard.getPlayerTeam(owner);
		if (team != null)
			team.removeEntry(entity.getUniqueId().toString());
	}

	public void ownerItemMove(Player player) {
		ItemStack is = player.getInventory().getItemInMainHand();
		Material material = is.getType();
		if (material == Material.AIR) {
			if (unequip(player, EquipmentSlot.HAND))
				return;
			if (unequip(player, EquipmentSlot.OFF_HAND))
				return;
			if (unequip(player, EquipmentSlot.HEAD))
				return;
			if (unequip(player, EquipmentSlot.CHEST))
				return;
			if (unequip(player, EquipmentSlot.LEGS))
				return;
			if (unequip(player, EquipmentSlot.FEET))
				return;
			return;
		}

		EquipmentSlot slot = material.getEquipmentSlot();
		if (entity.getEquipment().getItem(slot).getType() == Material.AIR) {
			entity.getEquipment().setItem(slot, is);
			player.getEquipment().setItem(EquipmentSlot.HAND, new ItemStack(Material.AIR));
		}
	}

	public boolean unequip(Player player, EquipmentSlot slot) {
		ItemStack slotIs = entity.getEquipment().getItem(slot);
		if (slotIs.getType() != Material.AIR) {
			player.getEquipment().setItem(EquipmentSlot.HAND, slotIs);
			entity.getEquipment().setItem(slot, new ItemStack(Material.AIR));
			return true;
		}
		return false;
	}
}
