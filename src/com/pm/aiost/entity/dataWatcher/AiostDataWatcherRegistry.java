package com.pm.aiost.entity.dataWatcher;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;

import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.DataWatcherRegistry;
import net.minecraft.server.v1_15_R1.DataWatcherSerializer;
import net.minecraft.server.v1_15_R1.EntityPose;
import net.minecraft.server.v1_15_R1.EnumDirection;
import net.minecraft.server.v1_15_R1.IBlockData;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.ItemStack;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.ParticleParam;
import net.minecraft.server.v1_15_R1.Vector3f;
import net.minecraft.server.v1_15_R1.VillagerData;

public class AiostDataWatcherRegistry {

	public static final DataWatcherSerializer<Byte> BYTE = DataWatcherRegistry.a;

	public static final DataWatcherSerializer<Integer> INTEGER = DataWatcherRegistry.b;

	public static final DataWatcherSerializer<Float> FLOAT = DataWatcherRegistry.c;

	public static final DataWatcherSerializer<String> STRING = DataWatcherRegistry.d;

	public static final DataWatcherSerializer<IChatBaseComponent> I_CHAT_BASE_COMPONENT = DataWatcherRegistry.e;

	public static final DataWatcherSerializer<Optional<IChatBaseComponent>> OPTIONAL_I_CHAT_BASE_COMPONENT = DataWatcherRegistry.f;

	public static final DataWatcherSerializer<ItemStack> ITEMSTACK = DataWatcherRegistry.g;

	public static final DataWatcherSerializer<Optional<IBlockData>> OPTIONAL_IBLOCKDATA = DataWatcherRegistry.h;

	public static final DataWatcherSerializer<Boolean> BOOLEAN = DataWatcherRegistry.i;

	public static final DataWatcherSerializer<ParticleParam> PARTICLE_PARAM = DataWatcherRegistry.j;

	public static final DataWatcherSerializer<Vector3f> VECTOR3F = DataWatcherRegistry.k;

	public static final DataWatcherSerializer<BlockPosition> BLOCK_POSITION = DataWatcherRegistry.l;

	public static final DataWatcherSerializer<Optional<BlockPosition>> OPTIONAL_BLOCK_POSITION = DataWatcherRegistry.m;

	public static final DataWatcherSerializer<EnumDirection> ENUM_DIRECTION = DataWatcherRegistry.n;

	public static final DataWatcherSerializer<Optional<UUID>> OPTIONAL_UUID = DataWatcherRegistry.o;

	public static final DataWatcherSerializer<NBTTagCompound> NBT_TAG_COMPOUND = DataWatcherRegistry.p;

	public static final DataWatcherSerializer<VillagerData> VILLAGER_DATA = DataWatcherRegistry.q;

	public static final DataWatcherSerializer<OptionalInt> OPTIONAL_INT = DataWatcherRegistry.r;

	public static final DataWatcherSerializer<EntityPose> ENTITY_POSE = DataWatcherRegistry.s;
}
