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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.spout.api.protocol.MessageCodec;

import org.spout.vanilla.protocol.VanillaByteBufUtils;

public final class PlayerHandshakeCodec extends MessageCodec<PlayerHandshakeMessage> {
	public PlayerHandshakeCodec() {
		super(PlayerHandshakeMessage.class, 0x00);
	}

	@Override
	public PlayerHandshakeMessage decode(ByteBuf buffer) {
		int protoVersion = (int) VanillaByteBufUtils.readVarInt(buffer);
		String hostname = VanillaByteBufUtils.readString(buffer);
		int port = buffer.readUnsignedShort();
		int nextState = (int) VanillaByteBufUtils.readVarInt(buffer);
		return new PlayerHandshakeMessage(protoVersion, hostname, port, nextState);
	}

	@Override
	public ByteBuf encode(PlayerHandshakeMessage message) {
		ByteBuf buffer = Unpooled.buffer();
		VanillaByteBufUtils.writeVarInt(buffer, message.getProtocolVersion());
		VanillaByteBufUtils.writeString(buffer, message.getHostname());
		buffer.writeShort(message.getPort());
		VanillaByteBufUtils.writeVarInt(buffer, message.getNextState());
		return buffer;
	}
}
