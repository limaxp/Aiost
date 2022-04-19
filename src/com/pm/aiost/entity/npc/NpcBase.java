package com.pm.aiost.entity.npc;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import com.mojang.authlib.GameProfile;
import com.pm.aiost.Aiost;
import com.pm.aiost.entity.AiostEntity;
import com.pm.aiost.entity.AiostEntityTypes;
import com.pm.aiost.entity.npc.network.EmptyNetHandler;
import com.pm.aiost.entity.npc.network.EmptyNetworkManager;
import com.pm.aiost.entity.npc.profile.Profiles;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.misc.utils.reflection.Reflection;

import net.minecraft.server.v1_15_R1.DamageSource;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.EnumGamemode;
import net.minecraft.server.v1_15_R1.EnumItemSlot;
import net.minecraft.server.v1_15_R1.EnumProtocolDirection;
import net.minecraft.server.v1_15_R1.ItemStack;
import net.minecraft.server.v1_15_R1.MinecraftServer;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.NetworkManager;
import net.minecraft.server.v1_15_R1.PlayerInteractManager;
import net.minecraft.server.v1_15_R1.World;
import net.minecraft.server.v1_15_R1.WorldServer;

public class NpcBase extends EntityPlayer implements AiostEntity {

	// TODO: NPC spawning does not call CreatureSpawnEvent and EntitySpawnEvent!

	public static final byte SKIN_OVERLAY_VIEWABLE_MASK = 0x01 + 0x02 + 0x04 + 0x08 + 0x10 + 0x20 + 0x40;

	private static final MethodHandle ENTITYPLAYER_ADVANCEMENT_DATA_PLAYER_SET = Reflection
			.unreflectSetter(EntityPlayer.class, "advancementDataPlayer");

	private static final File NULL_FILE = new File("");

	public NpcBase(EntityTypes<? extends NpcBase> entitytypes, World world) {
		this(world.getWorld().getHandle());
	}

	public NpcBase(World world) {
		this(world.getWorld().getHandle());
	}

	public NpcBase(WorldServer worldServer) {
		this(worldServer.getServer().getServer(), worldServer);
	}

	public NpcBase(MinecraftServer server, WorldServer worldServer) {
		this(server, worldServer, Profiles.getRandom(), new PlayerInteractManager(worldServer));
	}

	public NpcBase(World world, GameProfile profile) {
		this(world.getWorld().getHandle(), profile);
	}

	public NpcBase(WorldServer worldServer, GameProfile profile) {
		this(worldServer.getServer().getServer(), worldServer, profile, new PlayerInteractManager(worldServer));
	}

	public NpcBase(MinecraftServer server, WorldServer worldServer, GameProfile profile) {
		this(server, worldServer, profile, new PlayerInteractManager(worldServer));
	}

	public NpcBase(MinecraftServer server, WorldServer worldServer, GameProfile profile,
			PlayerInteractManager playerInteractManager) {
		super(server, worldServer, profile, playerInteractManager);

		this.uniqueID = UUID.randomUUID();
		playerInteractManager.setGameMode(EnumGamemode.SURVIVAL);

		try {
			NetworkManager conn = new EmptyNetworkManager(EnumProtocolDirection.CLIENTBOUND);
			playerConnection = new EmptyNetHandler(server, conn, this);
			conn.setPacketListener(playerConnection);
		} catch (IOException e) {
			// swallow
		}

		EmptyAdvancementDataPlayer.clear(getAdvancementData());
		try {
			ENTITYPLAYER_ADVANCEMENT_DATA_PLAYER_SET.invoke(this,
					new EmptyAdvancementDataPlayer(server, NULL_FILE, this));
		} catch (Throwable e) {
			Logger.err("NpcBase: Error on setting advancementDataPlayer field!", e);
		}
	}

	protected void initDatawatcher() {
		super.initDatawatcher();
		this.datawatcher.set(bq, SKIN_OVERLAY_VIEWABLE_MASK);
	}

	@Override
	public NBTTagCompound save(NBTTagCompound nbt) {
		super.b(nbt);
		saveNBT(nbt);
		nbt.setString("profileName", getProfile().getName());
		return nbt;
	}

	@Override
	public void a(NBTTagCompound nbt) {
		super.a(nbt);
		// TODO: Make save and load work!
		if (nbt.hasKey("profileName"))
			Profiles.get(nbt.getString("profileName"));
	}

	@Override
	public void tick() {
		super.tick();
		livingEntityBaseTick();
		movementTick(); // TODO check if this can be removed somehow!
	}

	protected void livingEntityBaseTick() {
		entityBaseTick();
		this.az = this.aA;
		if (this.hurtTicks > 0) {
			this.hurtTicks -= 1;
		}
		tickPotionEffects();
		this.aU = this.aT;
		this.aJ = this.aI;
		this.aL = this.aK;
		this.lastYaw = this.yaw;
		this.lastPitch = this.pitch;
	}

	@Override
	public void setSlot(EnumItemSlot enumitemslot, ItemStack itemstack) {
		super.setSlot(enumitemslot, itemstack);
		PacketSender.sendNMSNearby(world, locX(), locY(), locZ(),
				PacketFactory.packetEntityEquipment(getId(), enumitemslot, itemstack));
	}

	@Override
	protected boolean isFrozen() {
		return getHealth() <= 0.0F || isSleeping();
	}

	@Override
	public boolean damageEntity(DamageSource damagesource, float f) {
		boolean damaged = super.damageEntity(damagesource, f);
		if (damaged && this.velocityChanged) {
			this.velocityChanged = false;
			Bukkit.getScheduler().runTask(Aiost.getPlugin(), () -> NpcBase.this.velocityChanged = true);
		}
		return damaged;
	}

	@Override
	public void die() {
		super.die();
		getAdvancementData().a();
	}

	@Override
	public void die(DamageSource damagesource) {
		if (dead)
			return;

		super.die(damagesource);
		Bukkit.getScheduler().runTaskLater(Aiost.getPlugin(), () -> ((WorldServer) world).removeEntity(this), 35);
	}

	@Override
	public EntityTypes<?> getCustomEntityType() {
		return AiostEntityTypes.BASE_NPC;
	}

	@Override
	public CraftPlayer getBukkitEntity() {
		CraftEntity bukkitEntity = NMS.getBukkitEntity(this);
		if (bukkitEntity == null)
			NMS.setBukkitEntity(this, bukkitEntity = new BaseNpc(this));
		return (CraftPlayer) bukkitEntity;
	}

	public static class BaseNpc extends CraftPlayer {

		protected BaseNpc(NpcBase entity) {
			super((CraftServer) Bukkit.getServer(), entity);
		}

		@Override
		public NpcBase getHandle() {
			return (NpcBase) this.entity;
		}

		@Override
		public List<MetadataValue> getMetadata(String metadataKey) {
			return server.getEntityMetadata().getMetadata(this, metadataKey);
		}

		@Override
		public boolean hasMetadata(String metadataKey) {
			return server.getEntityMetadata().hasMetadata(this, metadataKey);
		}

		@Override
		public void removeMetadata(String metadataKey, Plugin owningPlugin) {
			server.getEntityMetadata().removeMetadata(this, metadataKey, owningPlugin);
		}

		@Override
		public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
			server.getEntityMetadata().setMetadata(this, metadataKey, newMetadataValue);
		}
	}
}
