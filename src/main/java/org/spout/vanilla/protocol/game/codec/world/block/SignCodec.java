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
package org.spout.vanilla.protocol.game.codec.world.block;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.spout.api.protocol.MessageCodec;
import org.spout.api.protocol.reposition.NullRepositionManager;

import org.spout.vanilla.protocol.VanillaByteBufUtils;
import org.spout.vanilla.protocol.game.msg.world.block.SignMessage;
import org.spout.vanilla.protocol.game.msg.world.block.SignMessage;

public final class SignCodec extends MessageCodec<SignMessage> {
	public SignCodec() {
		super(SignMessage.class, 0x82);
	}

	@Override
	public SignMessage decode(ByteBuf buffer) throws IOException {
		int x = buffer.readInt();
		int y = buffer.readShort();
		int z = buffer.readInt();
		String[] message = new String[4];
		for (int i = 0; i < message.length; i++) {
			String line = VanillaByteBufUtils.readString(buffer);
			if (line == null) {
				line = "";
			}
			message[i] = line;
		}
		return new SignMessage(x, y, z, message, NullRepositionManager.getInstance());
	}

	@Override
	public ByteBuf encode(SignMessage message) throws IOException {
		String[] lines = message.getMessage();

		ByteBuf buffer = Unpooled.buffer();
		buffer.writeInt(message.getX());
		buffer.writeShort(message.getY());
		buffer.writeInt(message.getZ());
		for (String line : lines) {
			if (line == null) {
				line = "";
			}
			VanillaByteBufUtils.writeString(buffer, line);
		}
		return buffer;
	}
}
