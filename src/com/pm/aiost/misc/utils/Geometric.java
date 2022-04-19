package com.pm.aiost.misc.utils;

public class Geometric {

	public static double[] sphere(double radius, int upSize, int sideSize) {
		return sphere(radius, radius, upSize, sideSize);
	}

	public static double[] sphere(double radius, double height, int upSize, int sideSize) {
		double[] coordinates = new double[(upSize * (sideSize * 2)) * 3];
		int i = 0;
		for (double phi = 0; phi < Math.PI; phi += Math.PI / upSize) {
			double y = height * Math.cos(phi);
			for (double theta = 0; theta < Math.PI * 2; theta += Math.PI / sideSize) {
				coordinates[i] = radius * Math.cos(theta) * Math.sin(phi);
				coordinates[i + 1] = y;
				coordinates[i + 2] = radius * Math.sin(theta) * Math.sin(phi);
				i += 3;
			}
		}
		return coordinates;
	}

	public static double[] sphereStep(double radius, int sideSize, double phi) {
		return sphereStep(radius, radius, sideSize, phi);
	}

	public static double[] sphereStep(double radius, double height, int sideSize, double phi) {
		double[] coordinates = new double[(1 + (sideSize * 2)) * 3];
		int i = 0;
		double y = height * Math.cos(phi);
		for (double theta = 0; theta <= Math.PI * 2; theta += Math.PI / sideSize) {
			coordinates[i] = radius * Math.cos(theta) * Math.sin(phi);
			coordinates[i + 1] = y;
			coordinates[i + 2] = radius * Math.sin(theta) * Math.sin(phi);
			i += 3;
		}
		return coordinates;
	}

	public static double[] halfSphere(double radius, int upSize, int sideSize) {
		return halfSphere(radius, radius, upSize, sideSize);
	}

	public static double[] halfSphere(double radius, double height, int upSize, int sideSize) {
		double[] coordinates = new double[(upSize * sideSize) * 3];
		int i = 0;
		for (double phi = 0; phi < Math.PI; phi += Math.PI / upSize) {
			double y = height * Math.cos(phi);
			for (double theta = 0; theta < Math.PI; theta += Math.PI / sideSize) {
				coordinates[i] = radius * Math.cos(theta) * Math.sin(phi);
				coordinates[i + 1] = y;
				coordinates[i + 2] = radius * Math.sin(theta) * Math.sin(phi);
				i += 3;
			}
		}
		return coordinates;
	}

	public static double[] horizontalRing(double radius, int size) {
		double[] coordinates = new double[(1 + (size * 2)) * 2];
		int i = 0;
		for (double phi = 0; phi < Math.PI * 2; phi += Math.PI / size) {
			coordinates[i] = radius * Math.cos(phi); // x
			coordinates[i + 1] = radius * Math.sin(phi); // z
			i += 2;
		}
		return coordinates;
	}

	public static double[] horizontalRingStep(double radius, int size, double phi) {
		double[] coordinates = new double[2];
		coordinates[0] = radius * Math.cos(phi); // x
		coordinates[1] = radius * Math.sin(phi); // z
		return coordinates;
	}

	public static double[] verticalRing(double radius, int size) {
		double[] coordinates = new double[(1 + (size * 2)) * 3];
		int i = 0;
		for (double phi = 0; phi < Math.PI * 2; phi += Math.PI / size) {
			coordinates[i] = radius * Math.sin(phi);
			coordinates[i + 1] = radius * Math.cos(phi);
			coordinates[i + 2] = radius * Math.sin(phi);
			i += 3;
		}
		return coordinates;
	}

	public static double[] verticalRing(double radius, int size, double yaw) {
		double[] coordinates = new double[(1 + (size * 2)) * 3];
		int i = 0;
		for (double phi = 0; phi < Math.PI * 2; phi += Math.PI / size) {
			coordinates[i] = radius * Math.cos(yaw) * Math.sin(phi);
			coordinates[i + 1] = radius * Math.cos(phi);
			coordinates[i + 2] = radius * Math.sin(yaw) * Math.sin(phi);
			i += 3;
		}
		return coordinates;
	}

	public static double[] verticalRingStep(double radius, int size, double phi) {
		double[] coordinates = new double[3];
		coordinates[0] = radius * Math.sin(phi);
		coordinates[1] = radius * Math.cos(phi);
		coordinates[2] = radius * Math.sin(phi);
		return coordinates;
	}

	public static double[] doubleRing(double radius, int size) {
		double[] coordinates = new double[(1 + (size * 2)) * 3];
		int i = 0;
		for (double phi = 0; phi < Math.PI * 2; phi += Math.PI / size) {
			coordinates[i] = radius * Math.cos(phi);
			coordinates[i + 1] = Math.cos(phi) / 4;
			coordinates[i + 2] = radius * Math.sin(phi);
			i += 3;
		}
		return coordinates;
	}

	public static double[] doubleRingStep(double radius, double phi) {
		double[] coordinates = new double[3];
		coordinates[0] = radius * Math.cos(phi);
		coordinates[1] = Math.sin(phi) / 4;
		coordinates[2] = radius * Math.sin(phi);
		return coordinates;
	}

	public static double[] beam(double range, double yaw, double pitch) {
		double[] coordinates = new double[(int) (3 + 10 * range * 3)];
		double xzLen = Math.cos(pitch);
		double x = xzLen * Math.cos(yaw);
		double y = Math.sin(pitch);
		double z = xzLen * Math.sin(-yaw);
		int i = 0;
		for (double d = 0; d < range; d += 0.1) {
			coordinates[i] = x + d;
			coordinates[i + 1] = y + d;
			coordinates[i + 2] = z + d;
			i += 3;
		}
		return coordinates;
	}

	public static double[] wings(double radius, int size) {
		double[] coordinates = new double[(1 + (size * 2)) * 3];
		int i = 0;
		for (double phi = 0; phi <= Math.PI * 2; phi += Math.PI / size) {
			coordinates[i] = radius * Math.cos(phi);
			coordinates[i + 1] = radius * Math.sin(phi * 2) * Math.cos(phi);
			coordinates[i + 2] = radius * Math.cos(phi);
			i += 3;
		}
		return coordinates;
	}
}
