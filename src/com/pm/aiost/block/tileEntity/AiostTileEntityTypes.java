package com.pm.aiost.block.tileEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;

import net.minecraft.server.v1_15_R1.Block;
import net.minecraft.server.v1_15_R1.DataConverterRegistry;
import net.minecraft.server.v1_15_R1.DataConverterTypes;
import net.minecraft.server.v1_15_R1.IRegistry;
import net.minecraft.server.v1_15_R1.MinecraftKey;
import net.minecraft.server.v1_15_R1.SharedConstants;
import net.minecraft.server.v1_15_R1.TileEntity;
import net.minecraft.server.v1_15_R1.TileEntityTypes;

public class AiostTileEntityTypes<T extends TileEntity> extends TileEntityTypes<T> {

	private static final List<TileEntityTypes<?>> VALUES = new ArrayList<TileEntityTypes<?>>();

	public static void init() {
	}

	public static void terminate() {
		for (TileEntityTypes<?> tileEntityType : VALUES) {
			// TODO: Also need to unregister IRegistry.BLOCK_ENTITY_TYPE
			removeTileFromEntityTree(getName(tileEntityType));
		}
	}

//	public static final TileEntityTypes<TileEntityMobSpawner> MOB_SPAWNER_BLOCK = a("mob_spawner_block", "mob_spawner",
//			TileEntityTypes.a.a(TileEntityMobSpawnerBlock::new, new Block[] { Blocks.SPAWNER }));

	public static <T extends TileEntity> TileEntityTypes<T> a(String name, String extend_from,
			TileEntityTypes.a<T> tileentitytypes_a) {
		Type<?> type = addToTileEntityTree(name, extend_from);
		TileEntityTypes<T> tileEntityType = IRegistry.a(IRegistry.BLOCK_ENTITY_TYPE, name, tileentitytypes_a.a(type));
		VALUES.add(tileEntityType);
		return tileEntityType;
	}

	@SuppressWarnings("unchecked")
	protected static Type<?> addToTileEntityTree(String name, String extend_from) {
		Map<Object, Type<?>> dataTypes = (Map<Object, Type<?>>) DataConverterRegistry.a()
				.getSchema(DataFixUtils.makeKey(SharedConstants.getGameVersion().getWorldVersion()))
				.findChoiceType(DataConverterTypes.BLOCK_ENTITY).types();
		Type<?> type = dataTypes.get("minecraft:" + extend_from);
		dataTypes.put("minecraft:" + name, type);
		return type;
	}

	@SuppressWarnings("unchecked")
	protected static void removeTileFromEntityTree(MinecraftKey key) {
		Map<Object, Type<?>> typeMap = (Map<Object, Type<?>>) DataConverterRegistry.a()
				.getSchema(DataFixUtils.makeKey(SharedConstants.getGameVersion().getWorldVersion()))
				.findChoiceType(DataConverterTypes.BLOCK_ENTITY).types();
		typeMap.remove(key.toString());
	}

	public static MinecraftKey getName(TileEntityTypes<?> tileentitytypes) {
		return IRegistry.BLOCK_ENTITY_TYPE.getKey(tileentitytypes);
	}

	public AiostTileEntityTypes(Supplier<? extends T> var0, Set<Block> var1, Type<?> var2) {
		super(var0, var1, var2);
	}
}
