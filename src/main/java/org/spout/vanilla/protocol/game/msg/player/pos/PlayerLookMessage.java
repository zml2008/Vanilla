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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.util.SpoutToStringStyle;

import org.spout.math.imaginary.Quaternion;
import org.spout.vanilla.protocol.game.msg.VanillaMainChannelMessage;

public final class PlayerLookMessage extends VanillaMainChannelMessage {
	private final float yaw, pitch, roll;
	private final boolean onGround;

	public PlayerLookMessage(float yaw, float pitch, boolean onGround) {
		this.yaw = yaw;
		this.pitch = pitch;
		this.roll = 0.0f; // No roll supported
		this.onGround = onGround;
	}

	public Quaternion getRotation() {
		return Quaternion.fromAxesAnglesDeg(this.pitch, this.yaw, this.roll);
	}

	public float getYaw() {
		return yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public float getRoll() {
		return roll;
	}

	public boolean isOnGround() {
		return onGround;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("yaw", yaw)
				.append("pitch", pitch)
				.append("roll", roll)
				.append("onGround", onGround)
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
		final PlayerLookMessage other = (PlayerLookMessage) obj;
		return new EqualsBuilder()
				.append(this.yaw, other.yaw)
				.append(this.pitch, other.pitch)
				.append(this.roll, other.roll)
				.append(this.onGround, other.onGround)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(this.yaw)
				.append(this.pitch)
				.append(this.roll)
				.append(this.onGround)
				.toHashCode();
	}
}
