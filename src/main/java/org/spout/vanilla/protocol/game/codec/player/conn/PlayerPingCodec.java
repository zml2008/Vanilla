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
package org.spout.vanilla.protocol.game.codec.player.conn;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.spout.api.protocol.MessageCodec;

import org.spout.vanilla.protocol.game.msg.player.conn.PlayerPingMessage;

public final class PlayerPingCodec extends MessageCodec<PlayerPingMessage> {
	public PlayerPingCodec() {
		super(PlayerPingMessage.class, 0x00, 0x00);
	}

	@Override
	public PlayerPingMessage decode(ByteBuf buffer) {
		int id = buffer.readInt();
		return new PlayerPingMessage(id);
	}

	@Override
	public ByteBuf encode(PlayerPingMessage message) {
		ByteBuf buffer = Unpooled.buffer(4);
		buffer.writeInt(message.getPingId());
		return buffer;
	}
}