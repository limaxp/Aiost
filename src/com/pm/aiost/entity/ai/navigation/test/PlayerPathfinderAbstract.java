package com.pm.aiost.entity.ai.navigation.test;

import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import com.pm.aiost.entity.npc.NpcInsentient;

import net.minecraft.server.v1_15_R1.ChunkCache;
import net.minecraft.server.v1_15_R1.MathHelper;
import net.minecraft.server.v1_15_R1.PathPoint;
import net.minecraft.server.v1_15_R1.PathfinderAbstract;

public abstract class PlayerPathfinderAbstract extends PathfinderAbstract {
	protected ChunkCache a;
	protected NpcInsentient b;
	protected final Int2ObjectMap c = new Int2ObjectOpenHashMap();
	protected int d;
	protected int e;
	protected int f;
	protected boolean g;
	protected boolean h;
	protected boolean i;

	@Override
	public void a() {
		this.a = null;
		this.b = null;
	}

	@Override
	public void a(boolean var0) {
		this.g = var0;
	}

	public void a(ChunkCache var0, NpcInsentient var1) {
		this.a = var0;
		this.b = var1;
		this.c.clear();
		this.d = MathHelper.d(var1.getWidth() + 1.0F);
		this.e = MathHelper.d(var1.getHeight() + 1.0F);
		this.f = MathHelper.d(var1.getWidth() + 1.0F);
	}

	@Override
	protected PathPoint a(int var0, int var1, int var2) {
		return (PathPoint) this.c.computeIfAbsent(PathPoint.b(var0, var1, var2), (var3) -> {
			return new PathPoint(var0, var1, var2);
		});
	}

	@Override
	public void b(boolean var0) {
		this.h = var0;
	}

	@Override
	public boolean c() {
		return this.g;
	}

	@Override
	public void c(boolean var0) {
		this.i = var0;
	}

	@Override
	public boolean d() {
		return this.h;
	}

	@Override
	public boolean e() {
		return this.i;
	}

}