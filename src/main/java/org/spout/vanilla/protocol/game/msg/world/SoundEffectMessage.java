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
package org.spout.vanilla.protocol.game.msg.world;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.protocol.reposition.RepositionManager;
import org.spout.api.util.SpoutToStringStyle;

import org.spout.math.vector.Vector3;
import org.spout.vanilla.protocol.game.msg.VanillaMainChannelMessage;
import org.spout.vanilla.protocol.game.msg.VanillaMainChannelMessage;

public class SoundEffectMessage extends VanillaMainChannelMessage {
	private final float x, y, z, volume, pitch;
	private final String soundName;

	public SoundEffectMessage(String soundName, Vector3 position, float volume, float pitch, RepositionManager rm) {
		this(soundName, position.getX(), position.getY(), position.getZ(), volume, pitch, rm);
	}

	public SoundEffectMessage(String soundName, float x, float y, float z, float volume, float pitch, RepositionManager rm) {
		this.x = rm.convertX(x);
		this.y = rm.convertY(y);
		this.z = rm.convertZ(z);
		this.soundName = soundName;
		this.volume = volume;
		this.pitch = pitch;
	}

	public float getX() {
		return this.x;
	}

	public float getY() {
		return this.y;
	}

	public float getZ() {
		return this.z;
	}

	public String getSoundName() {
		return this.soundName;
	}

	public float getVolume() {
		return this.volume;
	}

	public float getPitch() {
		return this.pitch;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("sound", this.soundName)
				.append("x", this.x)
				.append("y", this.y)
				.append("z", this.z)
				.append("volume", this.volume)
				.append("pitch", this.pitch)
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
		final SoundEffectMessage other = (SoundEffectMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.soundName, other.soundName)
				.append(this.x, other.x)
				.append(this.y, other.y)
				.append(this.z, other.z)
				.append(this.volume, other.volume)
				.append(this.pitch, other.pitch)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new org.apache.commons.lang3.builder.HashCodeBuilder()
				.append(this.soundName)
				.append(this.x)
				.append(this.y)
				.append(this.z)
				.append(this.volume)
				.append(this.pitch)
				.toHashCode();
	}
}
