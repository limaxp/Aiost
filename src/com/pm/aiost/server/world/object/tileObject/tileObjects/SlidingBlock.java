package com.pm.aiost.server.world.object.tileObject.tileObjects;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.pm.aiost.server.world.ServerWorld;
import com.pm.aiost.server.world.object.TickingObject;
import com.pm.aiost.server.world.object.tileObject.TileObject;
import com.pm.aiost.server.world.object.tileObject.TileObjectType;
import com.pm.aiost.server.world.object.tileObject.TileObjectTypes;

public class SlidingBlock extends TileObject implements TickingObject {

	public SlidingBlock(ServerWorld world) {
		super(world);
	}

	@Override
	public void tick() {
		Location center = new Location(world.world, x + 0.5D, y, z + 0.5D);
		for (Entity entity : world.world.getNearbyEntities(center, 1.0D, 1.0D, 1.0D,
				(entity) -> entity instanceof LivingEntity)) {
			Location loc = entity.getLocation();
			world.world.spawnParticle(Particle.BLOCK_CRACK, center, 10, 0.0, 0.0, 0.0, 0.0, getBlock().getBlockData(),
					false);
			int x, z;
			if (loc.getBlockX() > this.x)
				x = this.x - 1;
			else if (loc.getBlockX() < this.x)
				x = this.x + 1;
			else
				x = this.x;

			if (loc.getBlockZ() > this.z)
				z = this.z - 1;
			else if (loc.getBlockZ() < this.z)
				z = this.z + 1;
			else
				z = this.z;
			move(x, y, z);
			break;
		}
	}

	@Override
	public TileObjectType<?> getTileObjectType() {
		return TileObjectTypes.SLIDING_BLOCK;
	}
}
