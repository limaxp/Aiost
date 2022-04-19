package com.pm.aiost.entity.ai.navigation.pathfinder;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.pm.aiost.entity.CustomInsentient;

import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.ChunkCache;
import net.minecraft.server.v1_15_R1.Path;
import net.minecraft.server.v1_15_R1.PathDestination;
import net.minecraft.server.v1_15_R1.PathEntity;
import net.minecraft.server.v1_15_R1.PathPoint;

public class CustomPathfinder {

	private final Path a;
	private final Set<PathPoint> b;
	private final PathPoint[] c;
	private final int d;
	private final CustomPathfinderAbstract e;

	public CustomPathfinder(CustomPathfinderAbstract var0, int var1) {
		this.a = new Path();
		this.b = Sets.newHashSet();
		this.c = new PathPoint[32];

		this.e = var0;
		this.d = var1;
	}

	@Nullable
	public PathEntity a(ChunkCache var0, CustomInsentient var1, Set<BlockPosition> var2, float var3, int var4,
			float var5) {
		this.a.a();
		this.e.a(var0, var1);
		PathPoint var6 = this.e.b();

		@SuppressWarnings({ "unchecked", "rawtypes" })
		Map<PathDestination, BlockPosition> var7 = (Map) var2.stream().collect(
				Collectors.toMap(var0x -> this.e.a(var0x.getX(), var0x.getY(), var0x.getZ()), Function.identity()));
		PathEntity var8 = a(var6, var7, var3, var4, var5);

		this.e.a();
		return var8;
	}

	@SuppressWarnings("rawtypes")
	@Nullable
	private PathEntity a(PathPoint var0, Map<PathDestination, BlockPosition> var1, float var2, int var3, float var4) {
		Set<PathDestination> var5 = var1.keySet();
		var0.e = 0.0F;
		var0.f = this.a(var0, var5);
		var0.g = var0.f;
		this.a.a();
		this.b.clear();
		this.a.a(var0);
		int var6 = 0;
		int var7 = (int) (this.d * var4);
		while (!this.a.e()) {
			++var6;
			if (var6 >= var7) {
				break;
			}

			PathPoint var8 = this.a.c();
			var8.i = true;
			var5.stream().filter((var2x) -> {
				return var8.c(var2x) <= var3;
			}).forEach(PathDestination::e);
			if (var5.stream().anyMatch(PathDestination::f)) {
				break;
			}

			if (var8.a(var0) < var2) {
				int var9 = this.e.a(this.c, var8);

				for (int var10 = 0; var10 < var9; ++var10) {
					PathPoint var11 = this.c[var10];
					float var12 = var8.a(var11);
					var11.j = var8.j + var12;
					float var13 = var8.e + var12 + var11.k;
					if (var11.j < var2 && (!var11.c() || var13 < var11.e)) {
						var11.h = var8;
						var11.e = var13;
						var11.f = this.a(var11, var5) * 1.5F;
						if (var11.c()) {
							this.a.a(var11, var11.e + var11.f);
						} else {
							var11.g = var11.e + var11.f;
							this.a.a(var11);
						}
					}
				}
			}
		}

		Stream var8;
		if (var5.stream().anyMatch(PathDestination::f)) {
			var8 = var5.stream().filter(PathDestination::f).map((var1x) -> {
				return this.a(var1x.d(), var1.get(var1x), true);
			}).sorted(Comparator.comparingInt(PathEntity::e));
		} else {
			var8 = var5.stream().map(var1x -> a(var1x.d(), (BlockPosition) var1.get(var1x), false))
					.sorted(Comparator.comparingDouble(PathEntity::l).thenComparingInt(PathEntity::e));
		}

		Optional var9 = var8.findFirst();
		if (!var9.isPresent()) {
			return null;
		} else {
			PathEntity var10 = (PathEntity) var9.get();
			return var10;
		}
	}

	private float a(PathPoint var0, Set<PathDestination> var1) {
		float var2 = Float.MAX_VALUE;
		for (PathDestination var4 : var1) {
			float var5 = var0.a(var4);
			var4.a(var5, var0);
			var2 = Math.min(var5, var2);
		}
		return var2;
	}

	private PathEntity a(PathPoint var0, BlockPosition var1, boolean var2) {
		List<PathPoint> var3 = Lists.newArrayList();
		PathPoint var4 = var0;
		var3.add(0, var4);
		while (var4.h != null) {
			var4 = var4.h;
			var3.add(0, var4);
		}
		return new PathEntity(var3, var1, var2);
	}
}
