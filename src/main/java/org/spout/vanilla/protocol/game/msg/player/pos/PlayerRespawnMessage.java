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
package org.spout.vanilla.protocol.game.msg.player.pos;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.protocol.game.msg.VanillaMainChannelMessage;
import org.spout.vanilla.protocol.game.msg.VanillaMainChannelMessage;

public final class PlayerRespawnMessage extends VanillaMainChannelMessage {
	private final byte difficulty, mode;
	private final int worldHeight, dimension;
	private final String worldType;

	public PlayerRespawnMessage(int dimension, byte difficulty, byte mode, int worldHeight, String worldType) {
		this.dimension = dimension;
		this.difficulty = difficulty;
		this.mode = mode;
		this.worldHeight = worldHeight;
		this.worldType = worldType;
	}

	public PlayerRespawnMessage() {
		this(0, (byte) 0, (byte) 0, 0, "");
	}

	public int getDimension() {
		return dimension;
	}

	public byte getDifficulty() {
		return difficulty;
	}

	public byte getGameMode() {
		return mode;
	}

	public int getWorldHeight() {
		return worldHeight;
	}

	public String getWorldType() {
		return worldType;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("difficulty", difficulty)
				.append("mode", mode)
				.append("worldHeight", worldHeight)
				.append("dimension", dimension)
				.append("worldType", worldType)
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
		final PlayerRespawnMessage other = (PlayerRespawnMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.difficulty, other.difficulty)
				.append(this.mode, other.mode)
				.append(this.worldHeight, other.worldHeight)
				.append(this.dimension, other.dimension)
				.append(this.worldType, other.worldType)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new org.apache.commons.lang3.builder.HashCodeBuilder()
				.append(this.difficulty)
				.append(this.mode)
				.append(this.worldHeight)
				.append(this.dimension)
				.append(worldType)
				.toHashCode();
	}
}
