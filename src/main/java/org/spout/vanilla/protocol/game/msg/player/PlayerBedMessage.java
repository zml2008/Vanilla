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
package org.spout.vanilla.protocol.game.msg.player;

import org.spout.api.entity.Entity;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.protocol.reposition.RepositionManager;

import org.spout.vanilla.protocol.game.msg.entity.EntityMessage;
import org.spout.vanilla.protocol.game.msg.entity.EntityMessage;

public final class PlayerBedMessage extends EntityMessage {
	private final int used, x, y, z;

	public PlayerBedMessage(int id, int used, int x, int y, int z, RepositionManager rm) {
		super(id);
		this.used = used;
		this.x = rm.convertX(x);
		this.y = rm.convertY(y);
		this.z = rm.convertZ(z);
	}

	public PlayerBedMessage(Entity entity, Block head, RepositionManager rm) {
		this(entity.getId(), 0, head.getX(), head.getY(), head.getZ(), rm);
	}

	public int getUsed() {
		return used;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	@Override
	public String toString() {
		return "UseBedMessage{id=" + this.getEntityId() + ",used=" + used + ",x=" + x + ",y=" + y + ",z=" + z + "}";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PlayerBedMessage other = (PlayerBedMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.getEntityId(), other.getEntityId())
				.append(this.used, other.used)
				.append(this.x, other.x)
				.append(this.y, other.y)
				.append(this.z, other.z)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new org.apache.commons.lang3.builder.HashCodeBuilder()
				.append(this.getEntityId())
				.append(this.used)
				.append(this.x)
				.append(this.y)
				.append(this.z)
				.toHashCode();
	}
}
