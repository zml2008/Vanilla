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
package org.spout.vanilla.protocol.game.msg.entity.pos;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.util.SpoutToStringStyle;

import org.spout.math.vector.Vector3;
import org.spout.vanilla.protocol.game.msg.entity.EntityMessage;
import org.spout.vanilla.protocol.game.msg.entity.EntityMessage;

public final class EntityVelocityMessage extends EntityMessage {
	private final int velocityX, velocityY, velocityZ;

	public EntityVelocityMessage(int id, Vector3 velocity) {
		this(id, (int) velocity.getX(), (int) velocity.getY(), (int) velocity.getZ());
	}

	public EntityVelocityMessage(int id, int velocityX, int velocityY, int velocityZ) {
		super(id);
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.velocityZ = velocityZ;
	}

	public int getVelocityX() {
		return velocityX;
	}

	public int getVelocityY() {
		return velocityY;
	}

	public int getVelocityZ() {
		return velocityZ;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("id", this.getEntityId())
				.append("velocityX", velocityX)
				.append("velocityY", velocityY)
				.append("velocityZ", velocityZ)
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
		final EntityVelocityMessage other = (EntityVelocityMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.getEntityId(), other.getEntityId())
				.append(this.velocityX, other.velocityX)
				.append(this.velocityY, other.velocityY)
				.append(this.velocityZ, other.velocityZ)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new org.apache.commons.lang3.builder.HashCodeBuilder()
				.append(this.getEntityId())
				.append(this.velocityX)
				.append(this.velocityY)
				.append(this.velocityZ)
				.toHashCode();
	}
}
