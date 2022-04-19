package com.pm.aiost.entity.ownable;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.pm.aiost.entity.RideableEntity;

import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityInsentient;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.EnumHand;
import net.minecraft.server.v1_15_R1.EnumItemSlot;
import net.minecraft.server.v1_15_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_15_R1.Item;
import net.minecraft.server.v1_15_R1.ItemArmor;
import net.minecraft.server.v1_15_R1.ItemStack;
import net.minecraft.server.v1_15_R1.Items;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.Scoreboard;
import net.minecraft.server.v1_15_R1.ScoreboardTeam;

public interface OwnableEntity extends RideableEntity {

	public void setOwnerUUID(UUID id);

	public UUID getOwnerUUID();

	public void setBoundTime(int time);

	public int getBoundTime();

	public default void ownableEntityTick() {
		int boundTime = getBoundTime();
		if (boundTime > 0) {
			boundTime--;
			setBoundTime(boundTime);
			if (boundTime == 0) {
				die();
				// TODO: spawn particles here!
			}
		}
	}

	public default void rightClicked(EntityHuman entityhuman, EnumHand enumhand) {
		if (enumhand == EnumHand.MAIN_HAND) {
			if (entityhuman.isSneaking())
				ownerItemMove(entityhuman);
			else
				startOwnerRiding(entityhuman);
		}
	}

	public default void startOwnerRiding(EntityHuman entityhuman) {
		EntityLiving owner = getOwner();
		if (owner != null) {
			if (entityhuman == owner)
				entityhuman.startRiding(getEntity());
			else
				entityhuman.sendMessage(ChatSerializer.a("{\"text\": \"" + ChatColor.RED + "" + ChatColor.BOLD
						+ "You cannot ride another ones pet!!" + "\"}"));
		}
	}

	public default void ownerItemMove(EntityHuman entityhuman) {
		if (entityhuman != getOwner())
			return;

		ItemStack is = entityhuman.getItemInMainHand();
		Item item = is.getItem();
		if (item != Items.AIR) {
			if (item instanceof ItemArmor) {
				EnumItemSlot armorSlot = ((ItemArmor) item).b();
				if (getEquipment(armorSlot).getItem() == Items.AIR)
					entityhuman.setSlot(EnumItemSlot.MAINHAND, getEquipment(armorSlot));
				setSlot(armorSlot, is);
			} else {
				if (getEquipment(EnumItemSlot.MAINHAND).getItem() == Items.AIR) {
					setSlot(EnumItemSlot.MAINHAND, is);
					entityhuman.setSlot(EnumItemSlot.MAINHAND, ItemStack.a);
				} else if (getEquipment(EnumItemSlot.OFFHAND).getItem() == Items.AIR) {
					setSlot(EnumItemSlot.OFFHAND, is);
					entityhuman.setSlot(EnumItemSlot.MAINHAND, ItemStack.a);
				}
			}
		} else
			moveSingleEquipment(entityhuman, EnumItemSlot.MAINHAND);
	}

	public default void moveSingleEquipment(EntityHuman entity, EnumItemSlot target) {
		if (moveEquipment(entity, EnumItemSlot.MAINHAND, target))
			return;
		if (moveEquipment(entity, EnumItemSlot.OFFHAND, target))
			return;
		if (moveEquipment(entity, EnumItemSlot.HEAD, target))
			return;
		if (moveEquipment(entity, EnumItemSlot.CHEST, target))
			return;
		if (moveEquipment(entity, EnumItemSlot.LEGS, target))
			return;
		if (moveEquipment(entity, EnumItemSlot.FEET, target))
			return;
	}

	public default void moveEquipment(EntityHuman entity) {
		moveEquipment(entity, EnumItemSlot.MAINHAND);
		moveEquipment(entity, EnumItemSlot.OFFHAND);
		moveEquipment(entity, EnumItemSlot.HEAD);
		moveEquipment(entity, EnumItemSlot.CHEST);
		moveEquipment(entity, EnumItemSlot.LEGS);
		moveEquipment(entity, EnumItemSlot.FEET);
	}

	public default void moveEquipment(EntityInsentient entity) {
		moveEquipment(entity, EnumItemSlot.MAINHAND);
		moveEquipment(entity, EnumItemSlot.OFFHAND);
		moveEquipment(entity, EnumItemSlot.HEAD);
		moveEquipment(entity, EnumItemSlot.CHEST);
		moveEquipment(entity, EnumItemSlot.LEGS);
		moveEquipment(entity, EnumItemSlot.FEET);
	}

	public default boolean moveEquipment(EntityHuman entity, EnumItemSlot slot) {
		return moveEquipment(entity, slot, slot);
	}

	public default boolean moveEquipment(EntityHuman entity, EnumItemSlot slot, EnumItemSlot target) {
		ItemStack slotIs = getEquipment(slot);
		if (slotIs.getItem() != Items.AIR) {
			entity.setSlot(target, slotIs);
			setSlot(slot, ItemStack.a);
			return true;
		}
		return false;
	}

	public default boolean moveEquipment(EntityInsentient entity, EnumItemSlot slot) {
		return moveEquipment(entity, slot, slot);
	}

	public default boolean moveEquipment(EntityInsentient entity, EnumItemSlot slot, EnumItemSlot target) {
		ItemStack slotIs = getEquipment(slot);
		if (slotIs.getItem() != Items.AIR) {
			entity.setSlot(target, slotIs);
			setSlot(slot, ItemStack.a);
			return true;
		}
		return false;
	}

	public default void saveNBT(NBTTagCompound nbttagcompound) {
		RideableEntity.super.saveNBT(nbttagcompound);
		nbttagcompound.setString("ownerUUID", getOwnerUUID().toString());
		nbttagcompound.setInt("boundTime", getBoundTime());
	}

	public default void loadNBT(NBTTagCompound nbttagcompound) {
		setOwnerUUID(UUID.fromString(nbttagcompound.getString("ownerUUID")));
		setBoundTime(nbttagcompound.getInt("boundTime"));
	}

	public default void setOwner(LivingEntity owner) {
		setOwner(((CraftLivingEntity) owner).getHandle());
	}

	public default void setOwner(EntityLiving owner) {
		removeTeam();
		if (owner != null)
			setOwnerUUID(owner.getUniqueID());
		else
			setOwnerUUID(null);
	}

	public default void setOwner(Player player) {
		setOwner(((CraftPlayer) player).getHandle());
	}

	public default void setOwner(EntityHuman owner) {
		removeTeam();
		if (owner != null) {
			setTeam(owner);
			setOwnerUUID(owner.getUniqueID());
		} else
			setOwnerUUID(null);
	}

	public default EntityHuman getOwner() {
		try {
			UUID var0 = getOwnerUUID();
			if (var0 == null)
				return null;
			return getWorld().b(var0);
		} catch (IllegalArgumentException var0) {
			return null;
		}
	}

	public default void setTeam(EntityHuman owner) {
		Scoreboard scoreboard = owner.getScoreboard();
		ScoreboardTeam team = scoreboard.getPlayerTeam(owner.getName());
		if (team != null)
			scoreboard.addPlayerToTeam(getUniqueID().toString(), team);
	}

	public default void removeTeam() {
		EntityLiving owner = getOwner();
		if (owner instanceof EntityHuman) {
			EntityHuman ownerHuman = (EntityHuman) owner;
			if (owner != null) {
				Scoreboard scoreboard = ownerHuman.getScoreboard();
				ScoreboardTeam team = scoreboard.getPlayerTeam(owner.getName());
				if (team != null)
					scoreboard.removePlayerFromTeam(getUniqueID().toString(), team);
			}
		}
	}

	// TODO: Finish this! Check if working! If yes also do UUID like that!
//	public static final DataWatcherObject<Byte> bz = DataWatcher.a(EntityInsentient.class, DataWatcherRegistry.a);
//	public static final DataWatcherObject<Optional<UUID>> bA = DataWatcher.a(EntityInsentient.class,
//			DataWatcherRegistry.o);

	public default boolean isSitting() {
		return false;
//		return ((((Byte) getDataWatcher().get(bz)).byteValue() & 1) != 0);
	}

	public default void setSitting(boolean var0) {
//		byte var1 = ((Byte) getDataWatcher().get(bz)).byteValue();
//		if (var0) {
//			getDataWatcher().set(bz, Byte.valueOf((byte) (var1 | 1)));
//		} else {
//			getDataWatcher().set(bz, Byte.valueOf((byte) (var1 & 0xFFFFFFFE)));
//		}
	}
}
