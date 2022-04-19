package com.pm.aiost.entity.ai.navigation;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;
import com.pm.aiost.entity.CustomInsentient;
import com.pm.aiost.entity.ai.navigation.pathfinder.CustomPathfinder;
import com.pm.aiost.entity.ai.navigation.pathfinder.CustomPathfinderAbstract;

import net.minecraft.server.v1_15_R1.AttributeInstance;
import net.minecraft.server.v1_15_R1.Block;
import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.Blocks;
import net.minecraft.server.v1_15_R1.ChunkCache;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.GenericAttributes;
import net.minecraft.server.v1_15_R1.IBlockData;
import net.minecraft.server.v1_15_R1.MathHelper;
import net.minecraft.server.v1_15_R1.PathEntity;
import net.minecraft.server.v1_15_R1.PathPoint;
import net.minecraft.server.v1_15_R1.PathfinderAbstract;
import net.minecraft.server.v1_15_R1.PathfinderNormal;
import net.minecraft.server.v1_15_R1.SystemUtils;
import net.minecraft.server.v1_15_R1.Vec3D;
import net.minecraft.server.v1_15_R1.World;

public abstract class CustomNavigationAbstract {

	protected final CustomInsentient a;
	protected final EntityLiving a_;
	protected final World b;
	@Nullable
	protected PathEntity c;
	protected double d;
	private final AttributeInstance p;
	protected int e;
	protected int f;
	protected Vec3D g;
	protected Vec3D h;
	protected long i;
	protected long j;
	protected double k;
	protected float l;
	protected boolean m;
	protected long n;
	protected CustomPathfinderAbstract o;
	private BlockPosition q;
	private int r;
	private float s;
	private final CustomPathfinder t;

	public CustomNavigationAbstract(CustomInsentient var0, World var1) {
		this.g = Vec3D.a;
		this.h = Vec3D.a;

		this.l = 0.5F;

		this.s = 1.0F;

		this.a = var0;
		this.a_ = var0.getEntity();
		this.b = var1;
		this.p = var0.getAttributeInstance(GenericAttributes.FOLLOW_RANGE);

		int var2 = MathHelper.floor(this.p.getValue() * 16.0D);
		this.t = a(var2);
	}

	public void g() {
		this.s = 1.0F;
	}

	public void a(float var0) {
		this.s = var0;
	}

	public BlockPosition h() {
		return this.q;
	}

	protected abstract CustomPathfinder a(int paramInt);

	public void a(double var0) {
		this.d = var0;
	}

	public boolean i() {
		return this.m;
	}

	public void j() {
		if (this.b.getTime() - this.n > 20L) {
			if (this.q != null) {
				this.c = null;
				this.c = a(this.q, this.r);
				this.n = this.b.getTime();
				this.m = false;
			}
		} else {
			this.m = true;
		}
	}

	@Nullable
	public final PathEntity a(double var0, double var2, double var4, int var6) {
		return a(new BlockPosition(var0, var2, var4), var6);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Nullable
	public PathEntity a(Stream<BlockPosition> var0, int var1) {
		return a((Set) var0.collect(Collectors.toSet()), 8, false, var1);
	}

	@Nullable
	public PathEntity a(BlockPosition var0, int var1) {
		return a(ImmutableSet.of(var0), 8, false, var1);
	}

	@Nullable
	public PathEntity a(Entity var0, int var1) {
		return a(ImmutableSet.of(new BlockPosition(var0)), 16, true, var1);
	}

	@Nullable
	protected PathEntity a(Set<BlockPosition> var0, int var1, boolean var2, int var3) {
		if (var0.isEmpty()) {
			return null;
		}

		if (this.a.locY() < 0.0D) {
			return null;
		}

		if (!a()) {
			return null;
		}

		if (this.c != null && !this.c.b() && var0.contains(this.q)) {
			return this.c;
		}

		this.b.getMethodProfiler().enter("pathfind");
		float var4 = (float) this.p.getValue();
		BlockPosition var5 = var2 ? (new BlockPosition(this.a_)).up() : new BlockPosition(this.a_);
		int var6 = (int) (var4 + var1);

		ChunkCache var7 = new ChunkCache(this.b, var5.b(-var6, -var6, -var6), var5.b(var6, var6, var6));
		PathEntity var8 = this.t.a(var7, this.a, var0, var4, var3, this.s);
		this.b.getMethodProfiler().exit();

		if (var8 != null && var8.k() != null) {

			this.q = var8.k();
			this.r = var3;
		}

		return var8;
	}

	public boolean a(double var0, double var2, double var4, double var6) {
		return a(a(var0, var2, var4, 1), var6);
	}

	public boolean a(Entity var0, double var1) {
		PathEntity var3 = a(var0, 1);
		return (var3 != null && a(var3, var1));
	}

	public boolean a(@Nullable PathEntity var0, double var1) {
		if (var0 == null) {
			this.c = null;
			return false;
		}
		if (!var0.a(this.c)) {
			this.c = var0;
		}
		if (m()) {
			return false;
		}
		F_();
		if (this.c.e() <= 0) {
			return false;
		}

		this.d = var1;
		Vec3D var3 = b();
		this.f = this.e;
		this.g = var3;
		return true;
	}

	@Nullable
	public PathEntity k() {
		return this.c;
	}

	public void c() {
		this.e++;

		if (this.m) {
			j();
		}

		if (m()) {
			return;
		}

		if (a()) {
			l();
		} else if (this.c != null && this.c.f() < this.c.e()) {
			Vec3D var0 = b();
			Vec3D var1 = this.c.a(this.a_, this.c.f());
			if (var0.y > var1.y && !this.a_.onGround && MathHelper.floor(var0.x) == MathHelper.floor(var1.x)
					&& MathHelper.floor(var0.z) == MathHelper.floor(var1.z)) {
				this.c.c(this.c.f() + 1);
			}
		}

//		PacketDebug.a(this.b, this.a, this.c, this.l);

		if (m()) {
			return;
		}

		Vec3D var0 = this.c.a(this.a_);
		BlockPosition var1 = new BlockPosition(var0);

		this.a.getControllerMove().a(var0.x,
				this.b.getType(var1.down()).isAir() ? var0.y : PathfinderNormal.a(this.b, var1), var0.z, this.d);
	}

	protected void l() {
		Vec3D var0 = b();

		this.l = (this.a.getWidth() > 0.75F) ? (this.a.getWidth() / 2.0F) : (0.75F - this.a.getWidth() / 2.0F);
		Vec3D var1 = this.c.g();
		if (Math.abs(this.a.locX() - var1.x + 0.5D) < this.l && Math.abs(this.a.locZ() - var1.z + 0.5D) < this.l
				&& Math.abs(this.a.locY() - var1.y) < 1.0D) {
			this.c.c(this.c.f() + 1);
		}

		a(var0);
	}

	protected void a(Vec3D var0) {
		if (this.e - this.f > 100) {
			if (var0.distanceSquared(this.g) < 2.25D) {
				o();
			}
			this.f = this.e;
			this.g = var0;
		}

		if (this.c != null && !this.c.b()) {
			Vec3D var1 = this.c.g();

			if (var1.equals(this.h)) {
				this.i += SystemUtils.getMonotonicMillis() - this.j;
			} else {
				this.h = var1;
				double var2 = var0.f(this.h);
				this.k = (this.a_.dt() > 0.0F) ? (var2 / this.a_.dt() * 1000.0D) : 0.0D;
			}

			if (this.k > 0.0D && this.i > this.k * 3.0D) {
				this.h = Vec3D.a;
				this.i = 0L;
				this.k = 0.0D;
				o();
			}
			this.j = SystemUtils.getMonotonicMillis();
		}
	}

	public boolean m() {
		return (this.c == null || this.c.b());
	}

	public boolean n() {
		return !m();
	}

	public void o() {
		this.c = null;
	}

	protected abstract Vec3D b();

	protected abstract boolean a();

	protected boolean p() {
		return (this.a_.az() || this.a_.aH());
	}

	protected void F_() {
		if (this.c == null) {
			return;
		}

		for (int var0 = 0; var0 < this.c.e(); var0++) {
			PathPoint var1 = this.c.a(var0);
			PathPoint var2 = (var0 + 1 < this.c.e()) ? this.c.a(var0 + 1) : null;

			IBlockData var3 = this.b.getType(new BlockPosition(var1.a, var1.b, var1.c));
			Block var4 = var3.getBlock();

			if (var4 == Blocks.CAULDRON) {
				this.c.a(var0, var1.a(var1.a, var1.b + 1, var1.c));
				if (var2 != null && var1.b >= var2.b) {
					this.c.a(var0 + 1, var2.a(var2.a, var1.b + 1, var2.c));
				}
			}
		}
	}

	protected abstract boolean a(Vec3D paramVec3D1, Vec3D paramVec3D2, int paramInt1, int paramInt2, int paramInt3);

	public boolean a(BlockPosition var0) {
		BlockPosition var1 = var0.down();
		return this.b.getType(var1).g(this.b, var1);
	}

	public PathfinderAbstract q() {
		return this.o;
	}

	public void d(boolean var0) {
		this.o.c(var0);
	}

	public boolean r() {
		return this.o.e();
	}

	public void b(BlockPosition var0) {
		if (this.c == null || this.c.b() || this.c.e() == 0) {
			return;
		}

		PathPoint var1 = this.c.c();

		Vec3D var2 = new Vec3D((var1.a + this.a.locX()) / 2.0D, (var1.b + this.a.locY()) / 2.0D,
				(var1.c + this.a.locZ()) / 2.0D);

		if (var0.a(var2, (this.c.e() - this.c.f())))
			j();
	}
}