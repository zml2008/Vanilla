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
package org.spout.vanilla.protocol.game.msg.scoreboard;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.protocol.game.msg.VanillaMainChannelMessage;
import org.spout.vanilla.protocol.game.msg.VanillaMainChannelMessage;

public class ScoreboardObjectiveMessage extends VanillaMainChannelMessage {
	public static final byte ACTION_CREATE = 0;
	public static final byte ACTION_REMOVE = 1;
	public static final byte ACTION_UPDATE = 2;
	private final String name, display;
	private final byte action;

	public ScoreboardObjectiveMessage(String name, String display, byte action) {
		this.name = name;
		this.display = display;
		this.action = action;
	}

	@Override
	public boolean isAsync() {
		return true;
	}

	public String getName() {
		return name;
	}

	public String getDisplay() {
		return display;
	}

	public byte getAction() {
		return action;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ScoreboardObjectiveMessage other = (ScoreboardObjectiveMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.name, other.name)
				.append(this.display, other.display)
				.append(this.action, other.action)
				.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("name", this.name)
				.append("display", this.display)
				.append("action", this.action)
				.toString();
	}

	@Override
	public int hashCode() {
		return new org.apache.commons.lang3.builder.HashCodeBuilder()
				.append(this.name)
				.append(this.display)
				.append(this.action)
				.toHashCode();
	}
}
