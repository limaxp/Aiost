package com.pm.aiost.misc.packet.object.objects;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.Aiost;
import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.event.events.PacketThingAttackEvent;
import com.pm.aiost.item.ItemGroups;
import com.pm.aiost.item.Items;
import com.pm.aiost.item.custom.Slot;
import com.pm.aiost.misc.nms.NBTHelper;
import com.pm.aiost.misc.nms.NMS;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.misc.packet.object.PacketObject;
import com.pm.aiost.misc.packet.object.PacketObjectType;
import com.pm.aiost.misc.packet.object.PacketObjectTypes;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.ServerWorld;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData.DataValue;

public class Furniture extends PacketObject {

	public static final List<DataValue<?>> DATA_WATCHER;
	public static final List<ItemStack> FURNITURES = ItemGroups.get("furniture").values();
	public static final Int2IntMap INDEX_MAP;

	static {
		DATA_WATCHER = Arrays
				.asList(DataValue.create(new EntityDataAccessor<Byte>(0, EntityDataSerializers.BYTE), (byte) 0x20));

		INDEX_MAP = new Int2IntOpenHashMap();
		for (int i = 0; i < FURNITURES.size(); i++)
			INDEX_MAP.put((int) Items.getEffectID(FURNITURES.get(i)), i);
	}

	protected int furnitureID;
	protected @Nullable ItemStack is;
	public float yaw;

	public Furniture(ServerWorld world) {
		super(world);
	}

	public Furniture(ServerWorld world, int furnitureID) {
		super(world);
		setType(furnitureID);
	}

	public Furniture(ServerWorld world, ItemStack is) {
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
		return PacketFactory.packetEntitySpawn(id, UUID.randomUUID(), x + 0.5, y - 1.188, z + 0.5, yaw, 0,
				AiostEntityTypes.ARMOR_STAND);
	}

	public Object createMetadataPacket() {
		return PacketFactory.packetEntityMetadata(id, DATA_WATCHER);
	}

	public Object createEquipmentPacket() {
		if (is != null)
			return PacketFactory.packetEntityEquipment(id, Slot.HEAD.nmsSlot, NMS.to(is));
		else
			return PacketFactory.packetEntityEquipment(id, Slot.HEAD.nmsSlot, NMS.to(FURNITURES.get(furnitureID)));
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		if (NBTHelper.hasKey(nbt, "mat")) {
			ItemStack is = new ItemStack(Material.valueOf(nbt.getString("mat")));
			setItemStack(NBTHelper.setNBT(NMS.to(is), nbt.getCompound("itemNBT")));
		} else
			setType(nbt.getInt("fur"));
		yaw = nbt.getFloat("yaw");
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		super.save(nbt);
		if (is != null) {
			nbt.putString("mat", is.getType().name());
			nbt.put("itemNBT", NBTHelper.getNBT(NMS.to(is)));
		} else
			nbt.putInt("fur", furnitureID);
		if (yaw != 0)
			nbt.putFloat("yaw", yaw);
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

	@Override
	public void setPositionRotation(int x, int y, int z, float yaw, float pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
	}

	public void setType(int id) {
		furnitureID = id;
	}

	public int getType() {
		return furnitureID;
	}

	public void setItemStack(@Nullable ItemStack is) {
		int index = INDEX_MAP.getOrDefault((int) Items.getEffectID(is), -1);
		if (index != -1)
			setType(index);
		else
			this.is = is;
	}

	public @Nonnull ItemStack getItemStack() {
		if (is != null)
			return is;
		return FURNITURES.get(furnitureID).clone();
	}

	public @Nonnull ItemStack getItemStackDirect() {
		if (is != null)
			return is;
		return FURNITURES.get(furnitureID);
	}

	public void updateItemStack() {
		PacketSender.sendNear_(world.world, x, y, z, PACKET_OBJECT_VISIBILE_RANGE, createEquipmentPacket());
	}

	@Override
	public String getName() {
		return getItemStackDirect().getItemMeta().getDisplayName();
	}

	@Override
	public PacketObjectType<?> getPacketObjectType() {
		return PacketObjectTypes.FURNITURE;
	}
}
