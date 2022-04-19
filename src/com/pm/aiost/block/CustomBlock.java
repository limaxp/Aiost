package com.pm.aiost.block;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.item.nms.NMSItems;
import com.pm.aiost.misc.utils.nbt.NBTHelper;
import com.pm.aiost.misc.utils.nms.NMS;

import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.NBTTagList;
import net.minecraft.server.v1_15_R1.TileEntityMobSpawner;

public class CustomBlock {

	public static void place(Location loc, ItemStack is) {
		loc.getBlock().setType(Material.SPAWNER);
		place(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), is);
	}

	public static void place(Block block, ItemStack is) {
		block.setType(Material.SPAWNER);
		place(block.getWorld(), block.getX(), block.getY(), block.getZ(), is);
	}

	public static void place(Location loc, net.minecraft.server.v1_15_R1.ItemStack is) {
		loc.getBlock().setType(Material.SPAWNER);
		place(((CraftWorld) loc.getWorld()).getHandle(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), is);
	}

	public static void place(Block block, net.minecraft.server.v1_15_R1.ItemStack is) {
		block.setType(Material.SPAWNER);
		place(((CraftWorld) block.getWorld()).getHandle(), block.getX(), block.getY(), block.getZ(), is);
	}

	public static void place(World world, int x, int y, int z, ItemStack is) {
		place(((CraftWorld) world).getHandle(), x, y, z, NMS.getNMS(is));
	}

	public static void place(net.minecraft.server.v1_15_R1.World world, int x, int y, int z,
			net.minecraft.server.v1_15_R1.ItemStack is) {
		TileEntityMobSpawner spawner = new TileEntityMobSpawner();
		NBTTagCompound nbt = spawner.b();
		NBTHelper.setSpawnerStats(nbt, (short) 0, (short) 0, (short) 0, (short) 0);
		NBTTagCompound entityTag = NBTHelper.addSpawnData(nbt);
		NBTHelper.setEntityId(entityTag, "minecraft:armor_stand");
		NBTHelper.setMarker(entityTag, true);
		NBTHelper.setInvisible(entityTag, true);

		NBTTagList armorList = NBTHelper.addArmorItemsList(entityTag);
		armorList.add(new NBTTagCompound());
		armorList.add(new NBTTagCompound());
		armorList.add(new NBTTagCompound());
		if (is.hasTag())
			NBTHelper.addItem(armorList, NMSItems.getBase(is.getItem()), (byte) 1, NBTHelper.getNBT(is));
		else
			NBTHelper.addItem(armorList, NMSItems.getBase(is.getItem()), (byte) 1);

		spawner.load(nbt);
		world.setTileEntity(new BlockPosition(x, y, z), spawner);
	}

	public static void blockBreak(BlockBreakEvent event) {
		// TODO: fix this!
		if (event.getBlock().getType() == Material.SPAWNER) {
			Block block = event.getBlock();
			TileEntityMobSpawner spawner = (TileEntityMobSpawner) ((CraftWorld) block.getWorld()).getHandle()
					.getTileEntity(new BlockPosition(block.getX(), block.getY(), block.getZ()));
			NBTTagCompound nbt = spawner.b();
			System.out.println(nbt);
			NBTTagCompound entityTag = NBTHelper.getSpawnData(nbt);
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
