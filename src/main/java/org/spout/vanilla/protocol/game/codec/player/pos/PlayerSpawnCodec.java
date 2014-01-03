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
package org.spout.vanilla.protocol.game.codec.player.pos;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.spout.api.protocol.MessageCodec;
import org.spout.api.util.Parameter;

import org.spout.vanilla.protocol.VanillaByteBufUtils;
import org.spout.vanilla.protocol.game.msg.player.pos.PlayerSpawnMessage;

public final class PlayerSpawnCodec extends MessageCodec<PlayerSpawnMessage> {
	public PlayerSpawnCodec() {
		super(PlayerSpawnMessage.class, -1, 0x0C);
	}

	@Override
	public PlayerSpawnMessage decode(ByteBuf buffer) throws IOException {
		int id = VanillaByteBufUtils.readVarInt(buffer);
		UUID playerId = UUID.fromString(VanillaByteBufUtils.readString(buffer));
		String name = VanillaByteBufUtils.readString(buffer);
		int x = buffer.readInt();
		int y = buffer.readInt();
		int z = buffer.readInt();
		int rotation = buffer.readUnsignedByte();
		int pitch = buffer.readUnsignedByte();
		int item = buffer.readUnsignedShort();
		List<Parameter<?>> parameters = VanillaByteBufUtils.readParameters(buffer);
		return new PlayerSpawnMessage(id, playerId, name, x, y, z, rotation, pitch, item, parameters);
	}

	@Override
	public ByteBuf encode(PlayerSpawnMessage message) throws IOException {
		ByteBuf buffer = Unpooled.buffer();
		VanillaByteBufUtils.writeVarInt(buffer, message.getId());
		VanillaByteBufUtils.writeString(buffer, message.getPlayerUid().toString());
		VanillaByteBufUtils.writeString(buffer, message.getName());
		buffer.writeInt(message.getX());
		buffer.writeInt(message.getY());
		buffer.writeInt(message.getZ());
		buffer.writeByte(message.getYaw());
		buffer.writeByte(message.getPitch());
		buffer.writeShort(message.getId());
		VanillaByteBufUtils.writeParameters(buffer, message.getParameters());
		return buffer;
	}
}
