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
package org.spout.vanilla.protocol.login.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.spout.api.protocol.MessageCodec;

import org.spout.vanilla.protocol.VanillaByteBufUtils;
import org.spout.vanilla.protocol.login.msg.EncryptionKeyRequestMessage;

public final class EncryptionKeyRequestCodec extends MessageCodec<EncryptionKeyRequestMessage> {
	public EncryptionKeyRequestCodec() {
		super(EncryptionKeyRequestMessage.class, 0x00, 0x01);
	}

	@Override
	public EncryptionKeyRequestMessage decode(ByteBuf buffer) {
		String sessionId = VanillaByteBufUtils.readString(buffer);
		int length = buffer.readShort() & 0xFFFF;
		byte[] publicKey = new byte[length];
		buffer.readBytes(publicKey);
		int tokenLength = buffer.readShort() & 0xFFFF;
		byte[] token = new byte[tokenLength];
		buffer.readBytes(token);
		return new EncryptionKeyRequestMessage(sessionId, false, publicKey, token);
	}

	@Override
	public ByteBuf encode(EncryptionKeyRequestMessage message) {
		ByteBuf buffer = Unpooled.buffer();
		VanillaByteBufUtils.writeString(buffer, message.getSessionId());
		byte[] publicKey = message.getSecretArray();
		buffer.writeShort((short) publicKey.length);
		buffer.writeBytes(publicKey);
		buffer.writeShort((short) message.getVerifyTokenArray().length);
		buffer.writeBytes(message.getVerifyTokenArray());
		return buffer;
	}
}
