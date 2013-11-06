/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.protocol.game.msg.world.chunk;

import java.util.Arrays;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.protocol.reposition.RepositionManager;
import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.protocol.game.msg.VanillaBlockDataChannelMessage;

public final class ChunkBulkMessage extends VanillaBlockDataChannelMessage {
	private final int[] x;
	private final int[] z;
	private final boolean[][] addData;
	private final byte[][][] data;
	private final byte[][] biomeData;

	public ChunkBulkMessage(int[] x, int[] z, boolean[][] hasAdditionalData, byte[][][] data, byte[][] biomeData, RepositionManager rm) {
		int l = x.length;
		if (l != z.length || l != hasAdditionalData.length || l != data.length || l != biomeData.length) {
			throw new IllegalArgumentException("The lengths of all bulk data arrays must be equal");
		}
		this.x = Arrays.copyOf(x, x.length);
		this.z = Arrays.copyOf(z, z.length);
		for (int i = 0; i < x.length; i++) {
			x[i] = rm.convertChunkX(x[i]);
			z[i] = rm.convertChunkZ(z[i]);
		}
		this.addData = hasAdditionalData;
		this.data = data;
		this.biomeData = biomeData;
	}

	public int[] getX() {
		return x;
	}

	public int[] getZ() {
		return z;
	}

	public boolean[][] hasAdditionalData() {
		return addData;
	}

	public byte[][][] getData() {
		return data;
	}

	public byte[][] getBiomeData() {
		return biomeData;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("x", this.x)
				.append("z", this.z)
				.append("hasAdditionalData", this.addData)
				.append("data", this.data, false)
				.append("biomeData", this.data, false)
				.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ChunkBulkMessage other = (ChunkBulkMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.x, other.x)
				.append(this.z, other.z)
				.append(this.addData, other.addData)
				.append(this.data, other.data)
				.append(this.biomeData, other.biomeData)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new org.apache.commons.lang3.builder.HashCodeBuilder()
				.append(this.x)
				.append(this.z)
				.append(this.addData)
				.append(this.data)
				.append(this.biomeData)
				.toHashCode();
	}
}
