package com.pm.aiost.misc.packet.entity.entities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.Aiost;
import com.pm.aiost.event.events.PacketThingAttackEvent;
import com.pm.aiost.item.Items;
import com.pm.aiost.item.custom.Slot;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.misc.packet.entity.PacketEntity;
import com.pm.aiost.misc.packet.entity.PacketEntityType;
import com.pm.aiost.misc.packet.entity.PacketEntityTypes;
import com.pm.aiost.misc.packet.object.objects.Furniture;
import com.pm.aiost.misc.utils.nbt.NBTHelper;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.ServerWorld;

public class EntityFurniture extends PacketEntity {

	protected int furnitureID;
	protected @Nullable ItemStack is;
	protected Object equipmentPacket;

	public EntityFurniture(ServerWorld world) {
		super(world);
	}

	public EntityFurniture(ServerWorld world, int furnitureID) {
		super(world);
		setType(furnitureID);
	}

	public EntityFurniture(ServerWorld world, ItemStack is) {
		super(world);
		setItemStack(is);
	}

	@Override
	public void spawn() {
		PacketSender.sendNear_(world.world, x, y, z, PACKET_OBJECT_VISIBILE_RANGE, createSpawnPacket(),
				createMetadataPacket(), createEquipmentPacket());
	}

	@Override
	public void spawn(Player player) {
		PacketSender.send_(player, createSpawnPacket(), createMetadataPacket(), createEquipmentPacket());
	}

	@Override
	public Object createSpawnPacket() {
		return PacketFactory.packetEntityLivingSpawn(id, uuid, Furniture.ARMOR_STAND_ID, x, y - 1.188, z, yaw, pitch);
	}

	protected Object createMetadataPacket() {
		return PacketFactory.packetEntityMetadata(id, Furniture.DATA_WATCHER, true);
	}

	protected Object createEquipmentPacket() {
		if (is != null)
			return PacketFactory.packetEntityEquipment(id, Slot.HEAD.nmsSlot, is);
		else
			return PacketFactory.packetEntityEquipment(id, Slot.HEAD.nmsSlot, Furniture.FURNITURES.get(furnitureID));
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		if (nbt.hasKey("mat")) {
			ItemStack is = new ItemStack(Material.valueOf(nbt.getString("mat")));
			setItemStack(NBTHelper.setNBT(NMS.getNMS(is), nbt.getCompound("itemNBT")));
		} else
			setType(nbt.getInt("fur"));
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		super.save(nbt);
		if (is != null) {
			nbt.setString("mat", is.getType().name());
			nbt.set("itemNBT", NBTHelper.getNBT(NMS.getNMS(is)));
		} else
			nbt.setInt("fur", furnitureID);
		return nbt;
	}

	@Override
	public void onPlayerAttack(ServerPlayer serverPlayer) {
		GameMode gameMode = serverPlayer.player.getGameMode();
		if (gameMode == GameMode.SURVIVAL) {
			Bukkit.getScheduler().runTask(Aiost.getPlugin(),
					() -> world.world.dropItemNaturally(new Location(world.world, x, y, z), getItemStack()));
			remove();
		} else if (gameMode == GameMode.CREATIVE)
			remove();
	}

	@Override
	public void defaultPlayerAttack(PacketThingAttackEvent event) {
		// empty so doesn't get cancelled!
	}

	public void setType(int id) {
		furnitureID = id;
	}

	public int getType() {
		return furnitureID;
	}

	public void setItemStack(@Nullable ItemStack is) {
		int index = Furniture.INDEX_MAP.getOrDefault((int) Items.getEffectID(is), -1);
		if (index != -1)
			setType(index);
		else
			this.is = is;
	}

	public void updateItemStack() {
		PacketSender.sendNear_(world.world, x, y, z, PACKET_OBJECT_VISIBILE_RANGE, equipmentPacket);
	}

	public @Nonnull ItemStack getItemStack() {
		if (is != null)
			return is;
		return Furniture.FURNITURES.get(furnitureID).clone();
	}

	public @Nonnull ItemStack getItemStackDirect() {
		if (is != null)
			return is;
		return Furniture.FURNITURES.get(furnitureID);
	}

	@Override
	public String getName() {
		return getItemStackDirect().getItemMeta().getDisplayName();
	}

	@Override
	public PacketEntityType<?> getPacketEntityType() {
		return PacketEntityTypes.ENTITY_FURNITURE;
	}
}
