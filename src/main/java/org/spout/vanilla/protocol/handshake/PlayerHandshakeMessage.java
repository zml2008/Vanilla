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
package org.spout.vanilla.protocol.handshake;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.protocol.Protocol;
import org.spout.api.protocol.ProtocolChangeMessage;
import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.protocol.VanillaProtocol;
import org.spout.vanilla.protocol.game.msg.VanillaMainChannelMessage;

public final class PlayerHandshakeMessage extends VanillaMainChannelMessage implements ProtocolChangeMessage {
	private final int protoVersion;
	private final String hostname;
	private final int port;
	private final int nextState;

	public PlayerHandshakeMessage(int protoVersion, String hostname, int port, int nextState) {
		this.protoVersion = protoVersion;
		this.hostname = hostname;
		this.port = port;
		this.nextState = nextState;
	}

	public int getProtocolVersion() {
		return protoVersion;
	}

	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}

	public int getNextState() {
		return nextState;
	}

	@Override
	public boolean requiresPlayer() {
		return false;
	}

	@Override
	public boolean isAsync() {
		return true;
	}

	@Override
	public Protocol getNextProtocol() {
		return VanillaProtocol.getProtocolForState(getNextState());
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("protocol version", protoVersion)
				.append("hostname", hostname)
				.append("port", port)
				.append("nextState", nextState)
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
		final PlayerHandshakeMessage other = (PlayerHandshakeMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.protoVersion, other.protoVersion)
				.append(this.hostname, other.hostname)
				.append(this.port, other.port)
				.append(this.nextState, other.nextState)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new org.apache.commons.lang3.builder.HashCodeBuilder()
				.append(this.protoVersion)
				.append(this.hostname)
				.append(this.port)
				.append(this.nextState)
				.toHashCode();
	}
}
