package com.pm.aiost.item.block;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.item.custom.NMSItems;
import com.pm.aiost.misc.nms.NBT;
import com.pm.aiost.misc.nms.NMS;

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
		place(NMS.to(loc.getWorld()), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), is);
	}

	public static void place(Block block, net.minecraft.world.item.ItemStack is) {
		block.setType(Material.SPAWNER);
		place(NMS.to(block.getWorld()), block.getX(), block.getY(), block.getZ(), is);
	}

	public static void place(World world, int x, int y, int z, ItemStack is) {
		place(NMS.to(world), x, y, z, NMS.to(is));
	}

	public static void place(ServerLevel world, int x, int y, int z, net.minecraft.world.item.ItemStack is) {
		BlockPos pos = new BlockPos(x, y, z);
		ChunkAccess chunk = world.getChunk(pos);
		SpawnerBlockEntity spawner = new SpawnerBlockEntity(pos, Blocks.SPAWNER.defaultBlockState());
		CompoundTag nbt = chunk.getBlockEntityNbt(pos);
		NBT.setSpawnerStats(nbt, (short) 0, (short) 0, (short) 0, (short) 0);
		CompoundTag entityTag = NBT.addSpawnData(nbt);
		NBT.setEntityId(entityTag, "minecraft:armor_stand");
		NBT.setMarker(entityTag, true);
		NBT.setInvisible(entityTag, true);

		ListTag armorList = NBT.addArmorItemsList(entityTag);
		armorList.add(new CompoundTag());
		armorList.add(new CompoundTag());
		armorList.add(new CompoundTag());
		if (NBT.hasTag(is))
			NBT.addItem(armorList, NMSItems.getBase(is.getItem()), (byte) 1, NBT.getNBT(is));
		else
			NBT.addItem(armorList, NMSItems.getBase(is.getItem()), (byte) 1);

		chunk.setBlockEntityNbt(nbt);
		world.setBlockEntity(spawner);
	}

	public static void blockBreak(BlockBreakEvent event) {
		// TODO: fix this!
		if (event.getBlock().getType() == Material.SPAWNER) {
			Block block = event.getBlock();
			ServerLevel world = NMS.to(block.getWorld());
			BlockPos pos = new BlockPos(block.getX(), block.getY(), block.getZ());
			SpawnerBlockEntity spawner = (SpawnerBlockEntity) world.getBlockEntity(pos);
			CompoundTag nbt = world.getChunk(pos).getBlockEntityNbt(pos);
			System.out.println(nbt);
			CompoundTag entityTag = NBT.getSpawnData(nbt);
			String entityId = NBT.getEntityId(entityTag);
			System.out.println(entityId);
			if (entityId.equals("minecraft:armor_stand")) {
				block.setType(Material.AIR);
				event.setCancelled(true);
				ItemStack item = NBT.loadItem(NBT.getArmorItemsList(entityTag).getCompound(3));
				block.getWorld().dropItem(block.getLocation(), item);
			}
		}
	}
}
