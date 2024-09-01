package com.pm.aiost.misc.packet.entity.entities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.Aiost;
import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.item.Items;
import com.pm.aiost.item.custom.Slot;
import com.pm.aiost.misc.event.events.PacketObjectAttackEvent;
import com.pm.aiost.misc.nms.NBT;
import com.pm.aiost.misc.nms.NMS;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.misc.packet.entity.PacketEntity;
import com.pm.aiost.misc.packet.entity.PacketEntityType;
import com.pm.aiost.misc.packet.entity.PacketEntityTypes;
import com.pm.aiost.misc.packet.object.objects.Furniture;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.world.ServerWorld;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;

public class EntityFurniture extends PacketEntity {

	protected int furnitureID;
	protected @Nullable ItemStack is;
	protected Packet<?> equipmentPacket;

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
		PacketSender.sendNearby(world.world, x, y, z, PACKET_OBJECT_VISIBILE_RANGE, createSpawnPacket(),
				createMetadataPacket(), createEquipmentPacket());
	}

	@Override
	public void spawn(Player player) {
		PacketSender.send(player, createSpawnPacket(), createMetadataPacket(), createEquipmentPacket());
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return PacketFactory.packetEntitySpawn(id, uuid, x, y - 1.188, z, yaw, pitch, AiostEntityTypes.ARMOR_STAND);
	}

	protected Packet<?> createMetadataPacket() {
		return PacketFactory.packetEntityMetadata(id, Furniture.DATA_WATCHER);
	}

	protected Packet<?> createEquipmentPacket() {
		if (is != null)
			return PacketFactory.packetEntityEquipment(id, Slot.HEAD.nmsSlot, NMS.to(is));
		else
			return PacketFactory.packetEntityEquipment(id, Slot.HEAD.nmsSlot,
					NMS.to(Furniture.FURNITURES.get(furnitureID)));
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		if (NBT.hasKey(nbt, "itemNBT"))
			setItemStack(NBT.loadItem(nbt.getCompound("itemNBT")));
		else
			setType(nbt.getInt("fur"));
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		super.save(nbt);
		if (is != null) {
			nbt.putString("mat", is.getType().name());
			nbt.put("itemNBT", NBT.getNBT(NMS.to(is)));
		} else
			nbt.putInt("fur", furnitureID);
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
	public void defaultPlayerAttack(PacketObjectAttackEvent event) {
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
		PacketSender.sendNearby(world.world, x, y, z, PACKET_OBJECT_VISIBILE_RANGE, equipmentPacket);
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
