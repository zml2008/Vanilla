/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
 * Vanilla is licensed under the SpoutDev License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.world.generator.normal.object.tree;

import org.spout.vanilla.world.generator.normal.object.tree.TreeObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;
import org.spout.api.util.BlockIterator;

import org.spout.vanilla.material.VanillaMaterials;

public class BigTreeObject extends TreeObject {
	private float trunkHeightMultiplier = 0.618f;
	private byte trunkHeight;
	private float leafAmount = 1;
	private byte leafDistanceLimit = 5;
	private float widthScale = 1;
	private float branchSlope = 0.381f;

	public BigTreeObject(TreeType type) {
		this(null, type);
	}

	public BigTreeObject(Random random, TreeType type) {
		super(random, (byte) 5, (byte) 12, type.getMetadata());
		overridable.add(VanillaMaterials.AIR);
		overridable.add(VanillaMaterials.LEAVES);
	}

	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		final BlockMaterial under = w.getBlockMaterial(x, y - 1, z);
		if (under != VanillaMaterials.GRASS && under != VanillaMaterials.DIRT) {
			return false;
		}
		final Point base = new Point(w, x, y, z);
		final byte availableSpace = getAvailableBlockSpace(base, base.add(0, totalHeight - 1, 0));
		if (availableSpace > baseHeight || availableSpace == -1) {
			if (availableSpace != -1) {
				totalHeight = availableSpace;
			}
			return true;
		}
		return false;
	}

	@Override
	public void placeObject(World w, int x, int y, int z) {
		trunkHeight = (byte) (totalHeight * trunkHeightMultiplier);
		final List<PointBase> leaves = getLeafGroupPoints(w, x, y, z);
		for (PointBase leafGroup : leaves) {
			final int groupX = leafGroup.getBlockX();
			final int groupY = leafGroup.getBlockY();
			final int groupZ = leafGroup.getBlockZ();
			for (int yy = groupY; yy < groupY + leafDistanceLimit; yy++) {
				generateGroupLayer(w, groupX, yy, groupZ, getLeafGroupLayerSize((byte) (yy - groupY)));
			}
		}
		final BlockIterator trunk = new BlockIterator(new Point(w, x, y, z), new Point(w, x, y + trunkHeight, z));
		while (trunk.hasNext()) {
			trunk.next().setMaterial(VanillaMaterials.LOG, logMetadata);
		}
		generateBranches(w, x, y, z, leaves);
	}

	private List<PointBase> getLeafGroupPoints(World world, int x, int y, int z) {
		byte groupsPerLayer = (byte) (1.382 + Math.pow(leafAmount * totalHeight / 13, 2));

		if (groupsPerLayer == 0) {
			groupsPerLayer = 1;
		}

		final int trunkTopY = y + trunkHeight;
		final List<PointBase> groups = new ArrayList<PointBase>();
		int groupY = y + totalHeight - leafDistanceLimit;
		groups.add(new PointBase(world, x, groupY, z, trunkTopY));

		for (byte currentLayer = (byte) (totalHeight - leafDistanceLimit); currentLayer >= 0; currentLayer--) {

			final float layerSize = getRoughLayerSize(currentLayer);

			if (layerSize < 0) {
				groupY--;
				continue;
			}

			for (byte count = 0; count < groupsPerLayer; count++) {
				final float randomAngle = (float) (Math.PI * 2f * random.nextFloat());
				final float scale = widthScale * layerSize * (random.nextFloat() + 0.328f);
				final int groupX = (int) (scale * Math.sin(randomAngle) + x + 0.5);
				final int groupZ = (int) (scale * Math.cos(randomAngle) + z + 0.5);
				final Point group = new Point(world, groupX, groupY, groupZ);
				if (getAvailableBlockSpace(group, group.add(0, leafDistanceLimit, 0)) != -1) {
					continue;
				}
				final float horizontalDistanceToTrunk = (float) Math.sqrt(Math.pow(x - groupX, 2) + Math.pow(z - groupZ, 2));
				final float verticalDistanceToTrunk = horizontalDistanceToTrunk * branchSlope;
				final int base;
				final int yDiff = (int) (groupY - verticalDistanceToTrunk);
				if (yDiff > trunkTopY) {
					base = trunkTopY;
				} else {
					base = yDiff;
				}
				if (getAvailableBlockSpace(new Point(world, x, base, z), group) == -1) {
					groups.add(new PointBase(group, base));
				}
			}
			groupY--;
		}
		return groups;
	}

	private byte getLeafGroupLayerSize(byte y) {
		if (y >= 0 && y < leafDistanceLimit) {
			return (byte) (y != 0 && y != leafDistanceLimit - 1 ? 3 : 2);
		}
		return -1;
	}

	private void generateGroupLayer(World world, int x, int y, int z, byte size) {
		for (int xx = x - size; xx <= x + size; xx++) {
			for (int zz = z - size; zz <= z + size; zz++) {
				final float disk = (float) Math.sqrt(Math.pow(Math.abs(x - xx) + 0.5, 2)
						+ Math.pow(Math.abs(z - zz) + 0.5, 2));
				if (disk <= size) {
					if (overridable.contains(world.getBlockMaterial(xx, y, zz))) {
						world.setBlockMaterial(xx, y, zz, VanillaMaterials.LEAVES, leavesMetadata, world);
					}
				}
			}
		}
	}

	private float getRoughLayerSize(byte layer) {
		if (layer < totalHeight / 3f) {
			return -1f;
		} else if (layer == totalHeight / 2f) {
			return totalHeight / 4f;
		} else if (layer >= totalHeight || layer <= 0) {
			return 0;
		} else {
			return (float) Math.sqrt(Math.pow(totalHeight / 2, 2) - Math.pow(layer - totalHeight / 2, 2)) / 2;
		}
	}

	private void generateBranches(World world, int x, int y, int z, List<PointBase> groups) {
		for (PointBase group : groups) {
			final int base = group.getBase();
			if (base - y >= totalHeight * 0.2) {
				final BlockIterator branch = new BlockIterator(new Point(world, x, base, z), group);
				while (branch.hasNext()) {
					branch.next().setMaterial(VanillaMaterials.LOG, logMetadata);
				}
			}
		}
	}

	private byte getAvailableBlockSpace(Point from, Point to) {
		byte count = 0;
		final BlockIterator iter = new BlockIterator(from, to);
		while (iter.hasNext()) {
			if (!overridable.contains(iter.next().getMaterial())) {
				return count;
			}
			count++;
		}
		return -1;
	}

	private static class PointBase extends Point {
		private final int base;

		public PointBase(Point point, int base) {
			super(point);
			this.base = base;
		}

		public PointBase(World world, float x, float y, float z, int base) {
			super(world, x, y, z);
			this.base = base;
		}

		public int getBase() {
			return base;
		}
	}

	public void setBranchSlope(float branchSlope) {
		this.branchSlope = branchSlope;
	}

	public void setLeafAmount(float leafAmount) {
		this.leafAmount = leafAmount;
	}

	public void setLeafDistanceLimit(byte leafDistanceLimit) {
		this.leafDistanceLimit = leafDistanceLimit;
	}

	public void setTrunkHeight(byte trunkHeight) {
		this.trunkHeight = trunkHeight;
	}

	public void setTrunkHeightMultiplier(float trunkHeightMultiplier) {
		this.trunkHeightMultiplier = trunkHeightMultiplier;
	}

	public void setWidthScale(float widthScale) {
		this.widthScale = widthScale;
	}
}