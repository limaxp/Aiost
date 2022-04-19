package com.pm.aiost.misc.utils.worldEdit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;

public abstract class Brush {

	private static final List<Brush> LIST = new ArrayList<Brush>();

	public static enum BrushMode {
		
		// TODO overlay, melt, lift, smooth, drain

		SET, BREAK, FILL, REPLACE
	}

	public static final Brush CUBE = new Brush("Cube") {

		@Override
		public void run(BrushData data, boolean opposite) {
			Block block = data.block;
			int xRadius = data.xRadius;
			int yRadius = data.yRadius;
			int zRadius = data.zRadius;
			switch (data.mode) {
			case SET:
				if (opposite)
					WorldEdit.breakBlocks(block, xRadius, yRadius, zRadius);
				else
					WorldEdit.setBlocks(block, xRadius, yRadius, zRadius, data.type);
				break;

			case BREAK:
				if (opposite)
					WorldEdit.fillBlocks(block, xRadius, yRadius, zRadius, data.type);
				else
					WorldEdit.breakBlocks(block, xRadius, yRadius, zRadius);
				break;

			case FILL:
				if (opposite)
					WorldEdit.breakBlocks(block, xRadius, yRadius, zRadius);
				else
					WorldEdit.fillBlocks(block, xRadius, yRadius, zRadius, data.type);
				break;

			case REPLACE:
				if (opposite)
					WorldEdit.replaceBlocks(block, xRadius, yRadius, zRadius, data.type, data.type2);
				else
					WorldEdit.replaceBlocks(block, xRadius, yRadius, zRadius, data.type2, data.type);
				break;

			default:
				break;
			}
		}
	};

	public static final Brush SPHERE = new Brush("Sphere") {

		@Override
		public void run(BrushData data, boolean opposite) {
			Block block = data.block;
			int xRadius = data.xRadius;
			int yRadius = data.yRadius;
			int zRadius = data.zRadius;
			switch (data.mode) {
			case SET:
				if (opposite)
					WorldEdit.breakSphere(block, xRadius, yRadius, zRadius);
				else
					WorldEdit.setSphere(block, xRadius, yRadius, zRadius, data.type);
				break;

			case BREAK:
				if (opposite)
					WorldEdit.fillSphere(block, xRadius, yRadius, zRadius, data.type);
				else
					WorldEdit.breakSphere(block, xRadius, yRadius, zRadius);
				break;

			case FILL:
				if (opposite)
					WorldEdit.breakSphere(block, xRadius, yRadius, zRadius);
				else
					WorldEdit.fillSphere(block, xRadius, yRadius, zRadius, data.type);
				break;

			case REPLACE:
				if (opposite)
					WorldEdit.replaceSphere(block, xRadius, yRadius, zRadius, data.type, data.type2);
				else
					WorldEdit.replaceSphere(block, xRadius, yRadius, zRadius, data.type2, data.type);
				break;

			default:
				break;
			}
		}
	};

	public static final Brush HOLLOW_SPHERE = new Brush("Hollow sphere") {

		@Override
		public void run(BrushData data, boolean opposite) {
			Block block = data.block;
			int xRadius = data.xRadius;
			int yRadius = data.yRadius;
			int zRadius = data.zRadius;
			switch (data.mode) {
			case SET:
				if (opposite)
					WorldEdit.breakHollowSphere(block, xRadius, yRadius, zRadius);
				else
					WorldEdit.setHollowSphere(block, xRadius, yRadius, zRadius, data.type);
				break;

			case BREAK:
				if (opposite)
					WorldEdit.fillHollowSphere(block, xRadius, yRadius, zRadius, data.type);
				else
					WorldEdit.breakHollowSphere(block, xRadius, yRadius, zRadius);
				break;

			case FILL:
				if (opposite)
					WorldEdit.breakHollowSphere(block, xRadius, yRadius, zRadius);
				else
					WorldEdit.fillHollowSphere(block, xRadius, yRadius, zRadius, data.type);
				break;

			case REPLACE:
				if (opposite)
					WorldEdit.replaceHollowSphere(block, xRadius, yRadius, zRadius, data.type, data.type2);
				else
					WorldEdit.replaceHollowSphere(block, xRadius, yRadius, zRadius, data.type2, data.type);
				break;

			default:
				break;
			}
		}
	};

	public static final Brush PLANE = new Brush("Plane") {

		@Override
		public void run(BrushData data, boolean opposite) {
			Block block = data.block;
			int xRadius = data.xRadius;
			int yRadius = data.yRadius;
			int zRadius = data.zRadius;
			switch (data.mode) {
			case SET:
				if (opposite)
					WorldEdit.breakPlane(block, xRadius, yRadius, zRadius, data.face);
				else
					WorldEdit.setPlane(block, xRadius, yRadius, zRadius, data.face, data.type);
				break;

			case BREAK:
				if (opposite)
					WorldEdit.fillPlane(block, xRadius, yRadius, zRadius, data.face, data.type);
				else
					WorldEdit.breakPlane(block, xRadius, yRadius, zRadius, data.face);
				break;

			case FILL:
				if (opposite)
					WorldEdit.breakPlane(block, xRadius, yRadius, zRadius, data.face);
				else
					WorldEdit.fillPlane(block, xRadius, yRadius, zRadius, data.face, data.type);
				break;

			case REPLACE:
				if (opposite)
					WorldEdit.replacePlane(block, xRadius, yRadius, zRadius, data.face, data.type, data.type2);
				else
					WorldEdit.replacePlane(block, xRadius, yRadius, zRadius, data.face, data.type2, data.type);
				break;

			default:
				break;
			}
		}
	};

	public final String name;

	public Brush(String name) {
		this.name = name;
		LIST.add(this);
	}

	public abstract void run(BrushData data, boolean opposite);

	public static List<Brush> values() {
		return LIST;
	}
}
