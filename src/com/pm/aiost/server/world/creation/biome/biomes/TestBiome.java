package com.pm.aiost.server.world.creation.biome.biomes;

import net.minecraft.server.v1_15_R1.BiomeBase;
import net.minecraft.server.v1_15_R1.BiomeDecoratorGroups;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.EnumCreatureType;
import net.minecraft.server.v1_15_R1.WorldGenFeatureConfiguration;
import net.minecraft.server.v1_15_R1.WorldGenFeatureVillageConfiguration;
import net.minecraft.server.v1_15_R1.WorldGenMineshaft;
import net.minecraft.server.v1_15_R1.WorldGenMineshaftConfiguration;
import net.minecraft.server.v1_15_R1.WorldGenSurface;
import net.minecraft.server.v1_15_R1.WorldGenerator;

public class TestBiome extends BiomeBase {

	public TestBiome() {
		super((new BiomeBase.a())

				.a(WorldGenSurface.G, WorldGenSurface.v).a(BiomeBase.Precipitation.RAIN).a(BiomeBase.Geography.PLAINS)
				.a(0.125F).b(0.05F).c(0.8F).d(0.4F).a(4159204).b(329011).a((String) null));

		a(WorldGenerator.VILLAGE.b(new WorldGenFeatureVillageConfiguration("village/plains/town_centers", 6)));
		a(WorldGenerator.PILLAGER_OUTPOST.b(WorldGenFeatureConfiguration.e));
		a(WorldGenerator.MINESHAFT.b(new WorldGenMineshaftConfiguration(0.004D, WorldGenMineshaft.Type.NORMAL)));
		a(WorldGenerator.STRONGHOLD.b(WorldGenFeatureConfiguration.e));

		BiomeDecoratorGroups.a(this);

		BiomeDecoratorGroups.c(this);
		BiomeDecoratorGroups.d(this);
		BiomeDecoratorGroups.f(this);
		BiomeDecoratorGroups.Y(this);
		BiomeDecoratorGroups.g(this);
		BiomeDecoratorGroups.h(this);
		BiomeDecoratorGroups.l(this);
		BiomeDecoratorGroups.R(this);
		BiomeDecoratorGroups.Z(this);
		BiomeDecoratorGroups.aa(this);
		BiomeDecoratorGroups.am(this);
		BiomeDecoratorGroups.ap(this);

		a(EnumCreatureType.CREATURE, new BiomeBase.BiomeMeta(EntityTypes.SHEEP, 12, 4, 4));
		a(EnumCreatureType.CREATURE, new BiomeBase.BiomeMeta(EntityTypes.PIG, 10, 4, 4));
		a(EnumCreatureType.CREATURE, new BiomeBase.BiomeMeta(EntityTypes.CHICKEN, 10, 4, 4));
		a(EnumCreatureType.CREATURE, new BiomeBase.BiomeMeta(EntityTypes.COW, 8, 4, 4));
		a(EnumCreatureType.CREATURE, new BiomeBase.BiomeMeta(EntityTypes.HORSE, 5, 2, 6));
		a(EnumCreatureType.CREATURE, new BiomeBase.BiomeMeta(EntityTypes.DONKEY, 1, 1, 3));
		a(EnumCreatureType.AMBIENT, new BiomeBase.BiomeMeta(EntityTypes.BAT, 10, 8, 8));
		a(EnumCreatureType.MONSTER, new BiomeBase.BiomeMeta(EntityTypes.SPIDER, 100, 4, 4));
		a(EnumCreatureType.MONSTER, new BiomeBase.BiomeMeta(EntityTypes.ZOMBIE, 95, 4, 4));
		a(EnumCreatureType.MONSTER, new BiomeBase.BiomeMeta(EntityTypes.ZOMBIE_VILLAGER, 5, 1, 1));
		a(EnumCreatureType.MONSTER, new BiomeBase.BiomeMeta(EntityTypes.SKELETON, 100, 4, 4));
		a(EnumCreatureType.MONSTER, new BiomeBase.BiomeMeta(EntityTypes.CREEPER, 100, 4, 4));
		a(EnumCreatureType.MONSTER, new BiomeBase.BiomeMeta(EntityTypes.SLIME, 100, 4, 4));
		a(EnumCreatureType.MONSTER, new BiomeBase.BiomeMeta(EntityTypes.ENDERMAN, 10, 1, 4));
		a(EnumCreatureType.MONSTER, new BiomeBase.BiomeMeta(EntityTypes.WITCH, 5, 1, 1));
	}
}
