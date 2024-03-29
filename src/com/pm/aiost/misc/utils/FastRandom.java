package com.pm.aiost.misc.utils;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public final class FastRandom extends Random {

	private static final long serialVersionUID = 4275191338771720961L;
	private long seed;
	private final long gamma;
	private static final AtomicLong defaultGen;

	public FastRandom() {
		final long andAdd = FastRandom.defaultGen.getAndAdd(4354685564936845354L);
		this.seed = mix64(andAdd);
		this.gamma = mixGamma(andAdd - 7046029254386353131L);
	}

	public FastRandom(long seed) {
		final long andAdd = FastRandom.defaultGen.getAndAdd(seed);
		this.seed = mix64(andAdd);
		this.gamma = mixGamma(andAdd - 7046029254386353131L);
	}

	private static int mix32(long n) {
		n = (n ^ n >>> 33) * 7109453100751455733L;
		return (int) ((n ^ n >>> 28) * -3808689974395783757L >>> 32);
	}

	private static long mix64(long n) {
		n = (n ^ n >>> 30) * -4658895280553007687L;
		n = (n ^ n >>> 27) * -7723592293110705685L;
		return n ^ n >>> 31;
	}

	private static long mixGamma(long n) {
		n = (n ^ n >>> 33) * -49064778989728563L;
		n = (n ^ n >>> 33) * -4265267296055464877L;
		n = ((n ^ n >>> 33) | 0x1L);
		return (Long.bitCount(n ^ n >>> 1) < 24) ? (n ^ 0xAAAAAAAAAAAAAAAAL) : n;
	}

	private long nextSeed() {
		return this.seed += this.gamma;
	}

	public int nextInt() {
		return mix32(nextSeed());
	}

	public int nextInt(final int bound) {
		if (bound < 0) {
			throw new IllegalArgumentException("bound must be positive");
		}
		final int i = nextInt();
		return (i < 0 ? -i : i) % bound;
	}

	public int nextInt(final int min, final int max) {
		if (min >= max) {
			throw new IllegalArgumentException("bound must be greater than origin");
		}
		final int i = nextInt();
		return (i < 0 ? -i : i) % (max - min) + min;
	}

	public long nextLong() {
		return mix64(this.nextSeed());
	}

	public long nextLong(final long bound) {
		if (bound <= 0L) {
			throw new IllegalArgumentException("bound must be positive");
		}
		return nextLong() % bound;
	}

	public long nextLong(final long min, final long max) {
		if (min >= max) {
			throw new IllegalArgumentException("bound must be greater than origin");
		}
		final long l = nextLong();
		return (l < 0 ? -l : l) % (max - min) + min;
	}

	public double nextDouble() {
		return (mix64(this.nextSeed()) >>> 11) * 1.1102230246251565E-16;
	}

	public double nextDouble(final double bound) {
		if (bound <= 0.0) {
			throw new IllegalArgumentException("bound must be positive");
		}
		return nextDouble() * bound;
	}

	public double nextDouble(final double min, final double max) {
		if (min >= max) {
			throw new IllegalArgumentException("bound must be greater than origin");
		}
		return nextDouble() * (max - min) + min;
	}

	public float nextFloat() {
		return (this.nextInt() >>> 8) * 5.960464477539063e-08f;
	}

	public float nextFloat(final float bound) {
		if (bound <= 0.0f) {
			throw new IllegalArgumentException("bound must be positive");
		}
		return nextFloat() * bound;
	}

	public float nextFloat(final float min, final float max) {
		if (min == max) {
			return min;
		}
		if (min >= max) {
			throw new IllegalArgumentException("bound must be greater than origin");
		}
		return nextFloat() * (max - min) + min;
	}

	public boolean nextBoolean() {
		return nextInt() < 0;
	}

	static {
		defaultGen = new AtomicLong(mix64(System.currentTimeMillis()) ^ mix64(System.nanoTime()));
	}
}
