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
package org.spout.vanilla.protocol.game.codec.world.chunk;

import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.protocol.MessageCodec;
import org.spout.api.protocol.Session;
import org.spout.api.protocol.reposition.NullRepositionManager;

import org.spout.vanilla.protocol.VanillaProtocol;
import org.spout.vanilla.protocol.game.msg.world.chunk.ChunkDataMessage;

public final class ChunkDataCodec extends MessageCodec<ChunkDataMessage> {
	private static final int COMPRESSION_LEVEL = Deflater.BEST_SPEED;
	private static final int MAX_SECTIONS = 16;
	private final byte[] UNLOAD_COMPRESSED = {0x78, (byte) 0x9C, 0x63, 0x64, 0x1C, (byte) 0xD9, 0x00, 0x00, (byte) 0x81, (byte) 0x80, 0x01, 0x01}; //Fake compressed data, client expects this when unloading

	public ChunkDataCodec() {
		super(ChunkDataMessage.class, 0x33);
	}

	@Override
	public ChunkDataMessage decode(ByteBuf buffer) throws IOException {
		int x = buffer.readInt();
		int z = buffer.readInt();
		boolean contiguous = buffer.readByte() == 1;

		short primaryBitMap = buffer.readShort();
		short addBitMap = buffer.readShort();
		int compressedSize = buffer.readInt();
		byte[] compressedData = new byte[compressedSize];
		buffer.readBytes(compressedData);

		boolean[] hasAdditionalData = new boolean[MAX_SECTIONS];
		byte[][] data = new byte[MAX_SECTIONS][];

		int size = 0;
		for (int i = 0; i < MAX_SECTIONS; ++i) {
			if ((primaryBitMap & 1 << i) != 0) { // This chunk exists! Let's initialize the data for it.
				int sectionSize = Chunk.BLOCKS.HALF_VOLUME * 5;
				if ((addBitMap & 1 << i) != 0) {
					hasAdditionalData[i] = true;
					sectionSize += Chunk.BLOCKS.HALF_VOLUME;
				}

				data[i] = new byte[sectionSize];
				size += sectionSize;
			}
		}

		if (contiguous) {
			size += Chunk.BLOCKS.AREA;
		}

		byte[] uncompressedData = new byte[size];

		Inflater inflater = new Inflater();
		inflater.setInput(compressedData);
		inflater.getRemaining();
		int index = 0;
		try {
			while (index < uncompressedData.length) {
				int uncompressed = inflater.inflate(uncompressedData, index, uncompressedData.length - index);
				index += uncompressed;
				if (uncompressed == 0) {
					break;
				}
			}
			if (index != uncompressedData.length) {
				throw new IOException("Not all bytes uncompressed.");
			}
		} catch (DataFormatException e) {
			e.printStackTrace();
			throw new IOException("Bad compressed data.");
		} finally {
			inflater.end();
		}

		size = 0;
		// TODO - fix this total hack
		size = readSectionData(uncompressedData, size, data, 0, 4096);
		size = readSectionData(uncompressedData, size, data, 2 * 2048, 2048);
		size = readSectionData(uncompressedData, size, data, 3 * 2048, 2048);
		size = readSectionData(uncompressedData, size, data, 4 * 2048, 2048);

		/*size = 0;
		for (byte[] sectionData : data) {
			if (sectionData != null && sectionData.length + size < uncompressedData.length) {
				System.arraycopy(uncompressedData, size, sectionData, 0, sectionData.length);
				size += sectionData.length;
			}
		}*/
		byte[] biomeData = new byte[Chunk.BLOCKS.AREA];

		if (contiguous) {
			System.arraycopy(uncompressedData, size, biomeData, 0, biomeData.length);
			size += biomeData.length;
		}

		return new ChunkDataMessage(x, z, contiguous, hasAdditionalData, data, biomeData, null, NullRepositionManager.getInstance());
	}

	@Override
	public ByteBuf encode(ChunkDataMessage message) throws IOException {
		ByteBuf buffer = Unpooled.buffer();

		buffer.writeInt(message.getX());
		buffer.writeInt(message.getZ());
		buffer.writeByte(message.isContiguous() ? 1 : 0);
		if (message.shouldUnload()) {
			buffer.writeShort(0);
			buffer.writeShort(0);
			buffer.writeInt(UNLOAD_COMPRESSED.length);
			buffer.writeBytes(UNLOAD_COMPRESSED);
			return buffer;
		}
		short sectionsSentBitmap = 0;
		short additionalDataBitMap = 0;

		byte[][] data = message.getData();

		int uncompressedSize = 0;
		for (int i = 0; i < MAX_SECTIONS; ++i) {
			if (data[i] != null) { // This chunk exists! Let's initialize the data for it.
				sectionsSentBitmap |= 1 << i;
				if (message.hasAdditionalData()[i]) {
					additionalDataBitMap |= 1 << i;
				}
				uncompressedSize += data[i].length;
			}
		}

		if (message.isContiguous()) {
			uncompressedSize += message.getBiomeData().length;
		}

		buffer.writeShort(sectionsSentBitmap);
		buffer.writeShort(additionalDataBitMap);
		byte[] uncompressedData = new byte[uncompressedSize];
		int index = 0;

		// TODO - fix this total hack
		index = writeSectionData(data, 0, uncompressedData, index, 4096);
		index = writeSectionData(data, 2 * 2048, uncompressedData, index, 2048);
		index = writeSectionData(data, 3 * 2048, uncompressedData, index, 2048);
		index = writeSectionData(data, 4 * 2048, uncompressedData, index, 2048);

		if (message.isContiguous()) {
			System.arraycopy(message.getBiomeData(), 0, uncompressedData, index, message.getBiomeData().length);
			index += message.getBiomeData().length;
		}

		Session session = message.getSession();
		if (session != null) {
			uncompressedData = message.getSession().getDataMap().get(VanillaProtocol.CHUNK_NET_CACHE).handle(uncompressedData);
		}

		byte[] compressedData = new byte[uncompressedSize >> 2];

		Deflater deflater = new Deflater(COMPRESSION_LEVEL);
		deflater.setInput(uncompressedData);
		deflater.finish();

		int compressed;
		try {
			compressed = deflater.deflate(compressedData);
			if (compressed == 0) {
				throw new IOException("No compressed data found");
			} else if (compressed == compressedData.length) {
				boolean done = false;
				while (!done) {
					byte[] newCompressedData = new byte[1 + compressedData.length + (compressedData.length >> 1)];
					System.arraycopy(compressedData, 0, newCompressedData, 0, compressedData.length);
					compressedData = newCompressedData;
					compressed += deflater.deflate(compressedData, compressed, compressedData.length - compressed);
					done = compressed < compressedData.length;
				}
			}
		} finally {
			deflater.end();
		}

		buffer.writeInt(compressed);
		buffer.writeBytes(compressedData, 0, compressed);

		return buffer;
	}

	private int readSectionData(byte[] data, int off, byte[][] target, int targetOff, int len) {
		for (byte[] sectionTarget : target) {
			if (sectionTarget != null) {
				for (int i = targetOff; i < targetOff + len && i < sectionTarget.length; ++i) {
					sectionTarget[i] = data[off++];
				}
			}
		}
		return off;
	}

	private int writeSectionData(byte[][] data, int off, byte[] target, int targetOff, int len) {
		for (byte[] sectionData : data) {
			if (sectionData != null) {
				int j = off;
				for (int i = 0; i < len; i++) {
					target[targetOff++] = sectionData[j++];
				}
			}
		}
		return targetOff;
	}
}
