package com.pm.aiost.misc.packet.object.objects;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2IntMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.Aiost;
import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.dataWatcher.AiostDataWatcherObject;
import com.pm.aiost.entity.dataWatcher.AiostDataWatcherRegistry;
import com.pm.aiost.entity.dataWatcher.EmptyDataWatcher;
import com.pm.aiost.event.events.PacketThingAttackEvent;
import com.pm.aiost.item.ItemGroups;
import com.pm.aiost.item.Items;
import com.pm.aiost.item.custom.Slot;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.misc.packet.object.PacketObject;
import com.pm.aiost.misc.packet.object.PacketObjectType;
import com.pm.aiost.misc.packet.object.PacketObjectTypes;
import com.pm.aiost.misc.utils.nbt.NBTHelper;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.player.ServerPlayer;
import com.pm.aiost.server.world.ServerWorld;

public class Furniture extends PacketObject {

	public static final int ARMOR_STAND_ID = AiostEntityTypes.getId(AiostEntityTypes.ARMOR_STAND);
	public static final EmptyDataWatcher DATA_WATCHER;
	public static final List<ItemStack> FURNITURES = ItemGroups.get("furniture").values();
	public static final Int2IntMap INDEX_MAP;

	static {
		DATA_WATCHER = new EmptyDataWatcher();
		DATA_WATCHER.register(new AiostDataWatcherObject<>(0, AiostDataWatcherRegistry.BYTE), (byte) 0x20);

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
		return PacketFactory.packetEntityLivingSpawn(id, UUID.randomUUID(), ARMOR_STAND_ID, x + 0.5, y - 1.188, z + 0.5,
				yaw, 0);
	}

	public Object createMetadataPacket() {
		return PacketFactory.packetEntityMetadata(id, DATA_WATCHER, true);
	}

	public Object createEquipmentPacket() {
		if (is != null)
			return PacketFactory.packetEntityEquipment(id, Slot.HEAD.nmsSlot, is);
		else
			return PacketFactory.packetEntityEquipment(id, Slot.HEAD.nmsSlot, FURNITURES.get(furnitureID));
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		if (nbt.hasKey("mat")) {
			ItemStack is = new ItemStack(Material.valueOf(nbt.getString("mat")));
			setItemStack(NBTHelper.setNBT(NMS.getNMS(is), nbt.getCompound("itemNBT")));
		} else
			setType(nbt.getInt("fur"));
		yaw = nbt.getFloat("yaw");
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		super.save(nbt);
		if (is != null) {
			nbt.setString("mat", is.getType().name());
			nbt.set("itemNBT", NBTHelper.getNBT(NMS.getNMS(is)));
		} else
			nbt.setInt("fur", furnitureID);
		if (yaw != 0)
			nbt.setFloat("yaw", yaw);
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
