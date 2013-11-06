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

import org.spout.vanilla.protocol.game.msg.world.block.BlockBulkMessage;

public final class BlockBulkCodec extends MessageCodec<BlockBulkMessage> {
	public BlockBulkCodec() {
		super(BlockBulkMessage.class, 0x34);
	}

	@Override
	public BlockBulkMessage decode(ByteBuf buffer) throws IOException {
		int chunkX = buffer.readInt();
		int chunkZ = buffer.readInt();
		int changes = buffer.readUnsignedShort();
		int dataLength = buffer.readInt();
		if (dataLength != changes << 2) {
			throw new IllegalStateException("data length and record count mismatch");
		}

		short[] coordinates = new short[changes * 3];
		short[] types = new short[changes];
		byte[] metadata = new byte[changes];

		int coordinateIndex = 0;

		for (int i = 0; i < changes; i++) {
			int record = buffer.readInt();
			coordinates[coordinateIndex++] = (short) ((record >> 28) & 0x0F);
			coordinates[coordinateIndex++] = (short) ((record >> 16) & 0xFF);
			coordinates[coordinateIndex++] = (short) ((record >> 24) & 0x0F);
			types[i] = (short) ((record >> 4) & 0xFFF);
			metadata[i] = (byte) ((record >> 0) & 0xF);
		}

		return new BlockBulkMessage(chunkX, chunkZ, coordinates, types, metadata, NullRepositionManager.getInstance());
	}

	@Override
	public ByteBuf encode(BlockBulkMessage message) throws IOException {
		ByteBuf buffer = Unpooled.buffer(10 + message.getChanges() * 4);
		buffer.writeInt(message.getChunkX());
		buffer.writeInt(message.getChunkZ());
		buffer.writeShort(message.getChanges());
		buffer.writeInt(message.getChanges() << 2);

		int changes = message.getChanges();
		byte[] metadata = message.getMetadata();
		short[] types = message.getTypes();
		short[] coordinates = message.getCoordinates();

		int coordinateIndex = 0;

		for (int i = 0; i < changes; i++) {
			int record = metadata[i] & 0xF;
			record |= (types[i] & 0xFFF) << 4;
			record |= (coordinates[coordinateIndex++] & 0xF) << 28;
			record |= (coordinates[coordinateIndex++] & 0xFF) << 16;
			record |= (coordinates[coordinateIndex++] & 0xF) << 24;
			buffer.writeInt(record);
		}

		return buffer;
	}
}
