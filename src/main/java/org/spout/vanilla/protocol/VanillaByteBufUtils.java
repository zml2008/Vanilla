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
package org.spout.vanilla.protocol;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;

import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.util.Parameter;

import org.spout.math.GenericMath;
import org.spout.nbt.CompoundMap;
import org.spout.vanilla.material.VanillaMaterials;

import static org.spout.api.util.ByteBufUtils.readCompound;
import static org.spout.api.util.ByteBufUtils.writeCompound;

public final class VanillaByteBufUtils {
	public static final Charset UTF_8_CHARSET = Charset.forName("UTF-8");
	/**
	 * Writes a list of parameters (e.g. mob metadata) to the buffer.
	 *
	 * @param buf The buffer.
	 * @param parameters The parameters.
	 */
	@SuppressWarnings ("unchecked")
	public static void writeParameters(ByteBuf buf, List<Parameter<?>> parameters) {
		for (Parameter<?> parameter : parameters) {
			int type = parameter.getType();
			int index = parameter.getIndex();
			if (index > 0x1F) {
				throw new IllegalArgumentException("Index has a maximum of 0x1F!");
			}

			buf.writeByte(type << 5 | index & 0x1F);

			switch (type) {
				case Parameter.TYPE_BYTE:
					buf.writeByte(((Parameter<Byte>) parameter).getValue());
					break;
				case Parameter.TYPE_SHORT:
					buf.writeShort(((Parameter<Short>) parameter).getValue());
					break;
				case Parameter.TYPE_INT:
					buf.writeInt(((Parameter<Integer>) parameter).getValue());
					break;
				case Parameter.TYPE_FLOAT:
					buf.writeFloat(((Parameter<Float>) parameter).getValue());
					break;
				case Parameter.TYPE_STRING:
					writeString(buf, ((Parameter<String>) parameter).getValue());
					break;
				case Parameter.TYPE_ITEM:
					ItemStack item = ((Parameter<ItemStack>) parameter).getValue();
					writeItemStack(buf, item);
					break;
			}
		}

		buf.writeByte(127);
	}

	/**
	 * Reads a list of parameters from the buffer.
	 *
	 * @param buf The buffer.
	 * @return The parameters.
	 */
	public static List<Parameter<?>> readParameters(ByteBuf buf) throws IOException {
		List<Parameter<?>> parameters = new ArrayList<Parameter<?>>();

		for (int b = buf.readUnsignedByte(); b != 127; b = buf.readUnsignedByte()) {
			int type = (b & 0xE0) >> 5;
			int index = b & 0x1F;

			switch (type) {
				case Parameter.TYPE_BYTE:
					parameters.add(new Parameter<Byte>(type, index, buf.readByte()));
					break;
				case Parameter.TYPE_SHORT:
					parameters.add(new Parameter<Short>(type, index, buf.readShort()));
					break;
				case Parameter.TYPE_INT:
					parameters.add(new Parameter<Integer>(type, index, buf.readInt()));
					break;
				case Parameter.TYPE_FLOAT:
					parameters.add(new Parameter<Float>(type, index, buf.readFloat()));
					break;
				case Parameter.TYPE_STRING:
					parameters.add(new Parameter<String>(type, index, readString(buf)));
					break;
				case Parameter.TYPE_ITEM:
					parameters.add(new Parameter<ItemStack>(type, index, readItemStack(buf)));
					break;
			}
		}

		return parameters;
	}

	private static final byte VARINT_MORE_FLAG = (byte) (1 << 7);
	public static int readVarInt(ByteBuf buffer) {
		int ret = 0;
		short read;
		byte offset = 0;
		do {
			read = buffer.readUnsignedByte();
			ret = ret | ((read & ~VARINT_MORE_FLAG) << offset);
			offset += 7;
		} while (((read >> 7) & 1) != 0);
		return ret;
	}

	public static void writeVarInt(ByteBuf buffer, int num) {
		while (num != 0) {
			short write = (short) (num & ~VARINT_MORE_FLAG);
			num >>= 7;
			if (num != 0) {
				write |= VARINT_MORE_FLAG;
			}
			buffer.writeByte(write);
		}
	}

	/**
	 * Writes a string to the buffer.
	 *
	 * @param buf The buffer.
	 * @param str The string.
	 * @throws IllegalArgumentException if the string is too long <em>after</em> it is encoded.
	 */
	public static void writeString(ByteBuf buf, String str) {
		writeVarInt(buf, str.length());
		buf.writeBytes(str.getBytes(UTF_8_CHARSET));
	}

	/**
	 * Reads a string from the buffer.
	 *
	 * @param buf The buffer.
	 * @return The string.
	 */
	public static String readString(ByteBuf buf) {
		int len = (int) readVarInt(buf);

		byte[] chars = new byte[len];
		buf.readBytes(chars);

		return new String(chars, UTF_8_CHARSET);
	}

	/**
	 * Gets the size of a String represented in the formats read and written by
	 * {@link #writeString(ByteBuf, String)} and {@link #readString(ByteBuf)}.
	 *
	 * @param str to get the written length of
	 * @return String length
	 */
	public static int getMinStringLength(String str) {
		return str.length() + 1;
	}

	public static ItemStack readItemStack(ByteBuf buffer) throws IOException {
		short id = buffer.readShort();
		if (id < 0) {
			return null;
		} else {
			Material material = VanillaMaterials.getMaterial(id);
			if (material == null) {
				throw new IOException("Unknown material with id of " + id);
			}
			int count = buffer.readUnsignedByte();
			int damage = buffer.readUnsignedShort();
			CompoundMap nbtData = readCompound(buffer);
			return new ItemStack(material, damage, count).setNBTData(nbtData);
		}
	}

	public static void writeItemStack(ByteBuf buffer, ItemStack item) {
		short id = item == null ? (short) -1 : VanillaMaterials.getMinecraftId(item.getMaterial());
		buffer.writeShort(id);
		if (id != -1) {
			buffer.writeByte(item.getAmount());
			buffer.writeShort(item.getData());
			writeCompound(buffer, item.getNBTData());
		}
	}

	public static byte getNativeDirection(BlockFace face) {
		switch (face) {
			case SOUTH:
				return 3;
			case WEST:
				return 0;
			case NORTH:
				return 1;
			case EAST:
				return 2;
			default:
				return -1;
		}
	}

	public static int protocolifyPosition(float pos) {
		return GenericMath.floor(pos * 32);
	}

	public static float deProtocolifyPosition(int pos) {
		return pos / 32F;
	}

	public static int protocolifyPitch(float pitch) {
		return GenericMath.wrapByte(GenericMath.floor((pitch / 360) * 256));
	}

	public static float deProtocolifyPitch(int pitch) {
		return 360f * (pitch / 256f);
	}

	public static int protocolifyYaw(float yaw) {
		return protocolifyPitch(-yaw);
	}

	public static float deProtocolifyYaw(int yaw) {
		return -deProtocolifyPitch(yaw);
	}

	private VanillaByteBufUtils() {
	}

}
