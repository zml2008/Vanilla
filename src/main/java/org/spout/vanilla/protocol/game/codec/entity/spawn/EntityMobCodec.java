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
package org.spout.vanilla.protocol.game.codec.entity.spawn;

import java.io.IOException;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.spout.api.protocol.MessageCodec;
import org.spout.api.protocol.reposition.NullRepositionManager;
import org.spout.api.util.Parameter;

import org.spout.vanilla.protocol.VanillaByteBufUtils;
import org.spout.vanilla.protocol.game.msg.entity.spawn.EntityMobMessage;

public final class EntityMobCodec extends MessageCodec<EntityMobMessage> {
	public EntityMobCodec() {
		super(EntityMobMessage.class, -1, 0x0F);
	}

	@Override
	public EntityMobMessage decode(ByteBuf buffer) throws IOException {
		int id = VanillaByteBufUtils.readVarInt(buffer);
		int type = buffer.readUnsignedByte();
		int x = buffer.readInt();
		int y = buffer.readInt();
		int z = buffer.readInt();
		int yaw = buffer.readUnsignedByte();
		int pitch = buffer.readUnsignedByte();
		int headYaw = buffer.readUnsignedByte();
		short velocityZ = buffer.readShort();
		short velocityX = buffer.readShort();
		short velocityY = buffer.readShort();
		List<Parameter<?>> parameters = VanillaByteBufUtils.readParameters(buffer);
		return new EntityMobMessage(id, type, x, y, z, yaw, pitch, headYaw, velocityZ, velocityX, velocityY, parameters, NullRepositionManager.getInstance());
	}

	@Override
	public ByteBuf encode(EntityMobMessage message) throws IOException {
		ByteBuf buffer = Unpooled.buffer();
		VanillaByteBufUtils.writeVarInt(buffer, message.getEntityId());
		buffer.writeByte(message.getType());
		buffer.writeInt(message.getX());
		buffer.writeInt(message.getY());
		buffer.writeInt(message.getZ());
		buffer.writeByte(message.getYaw());
		buffer.writeByte(message.getPitch());
		buffer.writeByte(message.getHeadYaw());
		buffer.writeShort(message.getVelocityZ());
		buffer.writeShort(message.getVelocityX());
		buffer.writeShort(message.getVelocityY());
		VanillaByteBufUtils.writeParameters(buffer, message.getParameters());
		return buffer;
	}
}
