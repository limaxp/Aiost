package com.pm.aiost.block;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.item.nms.NMSItems;
import com.pm.aiost.misc.utils.nbt.NBTHelper;
import com.pm.aiost.misc.utils.nms.NMS;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;

public class CustomBlock {

	public static void place(Location loc, ItemStack is) {
		loc.getBlock().setType(Material.SPAWNER);
		place(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), is);
	}

	public static void place(Block block, ItemStack is) {
		block.setType(Material.SPAWNER);
		place(block.getWorld(), block.getX(), block.getY(), block.getZ(), is);
	}

	public static void place(Location loc, net.minecraft.world.item.ItemStack is) {
		loc.getBlock().setType(Material.SPAWNER);
		place(NMS.getNMS(loc.getWorld()), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), is);
	}

	public static void place(Block block, net.minecraft.world.item.ItemStack is) {
		block.setType(Material.SPAWNER);
		place(NMS.getNMS(block.getWorld()), block.getX(), block.getY(), block.getZ(), is);
	}

	public static void place(World world, int x, int y, int z, ItemStack is) {
		place(NMS.getNMS(world), x, y, z, NMS.getNMS(is));
	}

	public static void place(ServerLevel world, int x, int y, int z, net.minecraft.world.item.ItemStack is) {
		BlockPos pos = new BlockPos(x, y, z);
		ChunkAccess chunk = world.getChunk(pos);
		SpawnerBlockEntity spawner = new SpawnerBlockEntity(pos, Blocks.SPAWNER.defaultBlockState());
		CompoundTag nbt = chunk.getBlockEntityNbt(pos);
		NBTHelper.setSpawnerStats(nbt, (short) 0, (short) 0, (short) 0, (short) 0);
		CompoundTag entityTag = NBTHelper.addSpawnData(nbt);
		NBTHelper.setEntityId(entityTag, "minecraft:armor_stand");
		NBTHelper.setMarker(entityTag, true);
		NBTHelper.setInvisible(entityTag, true);

		ListTag armorList = NBTHelper.addArmorItemsList(entityTag);
		armorList.add(new CompoundTag());
		armorList.add(new CompoundTag());
		armorList.add(new CompoundTag());
		if (NBTHelper.hasTag(is))
			NBTHelper.addItem(armorList, NMSItems.getBase(is.getItem()), (byte) 1, NBTHelper.getNBT(is));
		else
			NBTHelper.addItem(armorList, NMSItems.getBase(is.getItem()), (byte) 1);

		chunk.setBlockEntityNbt(nbt);
		world.setBlockEntity(spawner);
	}

	public static void blockBreak(BlockBreakEvent event) {
		// TODO: fix this!
		if (event.getBlock().getType() == Material.SPAWNER) {
			Block block = event.getBlock();
			ServerLevel world = NMS.getNMS(block.getWorld());
			BlockPos pos = new BlockPos(block.getX(), block.getY(), block.getZ());
			SpawnerBlockEntity spawner = (SpawnerBlockEntity) world.getBlockEntity(pos);
			CompoundTag nbt = world.getChunk(pos).getBlockEntityNbt(pos);
			System.out.println(nbt);
			CompoundTag entityTag = NBTHelper.getSpawnData(nbt);
			String entityId = NBTHelper.getEntityId(entityTag);
			System.out.println(entityId);
			if (entityId.equals("minecraft:armor_stand")) {
				block.setType(Material.AIR);
				event.setCancelled(true);
				ItemStack item = NBTHelper.loadItem(NBTHelper.getArmorItemsList(entityTag).getCompound(3));
				block.getWorld().dropItem(block.getLocation(), item);
			}
		}
	}
}
