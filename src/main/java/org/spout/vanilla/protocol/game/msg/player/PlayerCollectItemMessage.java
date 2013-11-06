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

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.protocol.Message;
import org.spout.api.protocol.proxy.ConnectionInfo;
import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.protocol.game.msg.entity.EntityMessage;
import org.spout.vanilla.protocol.game.msg.entity.EntityMessage;
import org.spout.vanilla.protocol.proxy.VanillaConnectionInfo;

public final class PlayerCollectItemMessage extends EntityMessage {
	private int collector;

	public PlayerCollectItemMessage(int id, int collector) {
		super(id);
		this.collector = collector;
	}

	@Override
	public Message transform(boolean upstream, int connects, ConnectionInfo info, ConnectionInfo auxChannelInfo) {
		super.transform(upstream, connects, info, auxChannelInfo);
		if (collector == ((VanillaConnectionInfo) info).getEntityId()) {
			collector = ((VanillaConnectionInfo) auxChannelInfo).getEntityId();
		} else if (collector == ((VanillaConnectionInfo) auxChannelInfo).getEntityId()) {
			collector = ((VanillaConnectionInfo) info).getEntityId();
		}
		return this;
	}

	public int getCollector() {
		return collector;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("id", this.getEntityId())
				.append("collector", collector)
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
		final PlayerCollectItemMessage other = (PlayerCollectItemMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.getEntityId(), other.getEntityId())
				.append(this.collector, other.collector)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new org.apache.commons.lang3.builder.HashCodeBuilder()
				.append(this.getEntityId())
				.append(this.collector)
				.toHashCode();
	}
}
