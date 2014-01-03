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

import org.spout.vanilla.protocol.VanillaByteBufUtils;
import org.spout.vanilla.protocol.game.msg.player.conn.PlayerJoinGameMessage;

public final class PlayerJoinGameCodec extends MessageCodec<PlayerJoinGameMessage> {
	public PlayerJoinGameCodec() {
		super(PlayerJoinGameMessage.class, -1, 0x01);
	}

	@Override
	public PlayerJoinGameMessage decodeFromServer(ByteBuf buffer) {
		int id = buffer.readInt();
		short mode = buffer.readUnsignedByte();
		byte dimension = buffer.readByte();
		short difficulty = buffer.readUnsignedByte();
		short maxPlayers = buffer.readUnsignedByte();
		final String worldType = VanillaByteBufUtils.readString(buffer);
		return new PlayerJoinGameMessage(id, worldType, mode, dimension, difficulty, maxPlayers);
	}

	/* This is needed for tests to complete. It is not actually used.
	 * See the commented-out code below this function */
	/*@Override
	public PlayerJoinGameMessage decodeFromClient(ByteBuf buffer) {
		int id = buffer.readInt();
		String worldType = VanillaByteBufUtils.readString(buffer);
		byte mode = buffer.readByte();
		byte dimension = buffer.readByte();
		byte difficulty = buffer.readByte();
		buffer.readUnsignedByte(); //not used?
		short maxPlayers = buffer.readUnsignedByte();
		return new PlayerJoinGameMessage(id, worldType, mode, dimension, difficulty, maxPlayers);
	}*/

	@Override
	public ByteBuf encodeToClient(PlayerJoinGameMessage message) {
		ByteBuf buffer = Unpooled.buffer();
		buffer.writeInt(message.getId());
		buffer.writeByte(message.getGameMode());
		buffer.writeByte(message.getDimension());
		buffer.writeByte(message.getDifficulty());
		buffer.writeByte(message.getMaxPlayers());
		VanillaByteBufUtils.writeString(buffer, message.getWorldType());
		return buffer;
	}

	/*@Override
	public ByteBuf encodeToServer(PlayerJoinGameMessage message) {
		PlayerJoinGameMessage server = message;
		ByteBuf buffer = Unpooled.buffer();
		buffer.writeInt(server.getId());
		VanillaByteBufUtils.writeString(buffer, server.getWorldType());
		buffer.writeByte(server.getGameMode());
		buffer.writeByte(server.getDimension());
		buffer.writeByte(server.getDifficulty());
		buffer.writeByte(0);
		buffer.writeByte(server.getMaxPlayers());
		return buffer;
	}*/
}
