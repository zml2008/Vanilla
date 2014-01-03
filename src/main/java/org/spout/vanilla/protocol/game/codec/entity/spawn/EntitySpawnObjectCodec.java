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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.spout.api.protocol.MessageCodec;
import org.spout.api.protocol.reposition.NullRepositionManager;

import org.spout.vanilla.protocol.VanillaByteBufUtils;
import org.spout.vanilla.protocol.game.msg.entity.spawn.EntityObjectMessage;

public final class EntitySpawnObjectCodec extends MessageCodec<EntityObjectMessage> {
	public EntitySpawnObjectCodec() {
		super(EntityObjectMessage.class, -1, 0x0E);
	}

	@Override
	public EntityObjectMessage decode(ByteBuf buffer) throws IOException {
		//TODO: There's 2 new bytes. Currently unknown according to wiki.vg 17/12/2012
		int entityId = VanillaByteBufUtils.readVarInt(buffer);
		byte type = buffer.readByte();
		int x = buffer.readInt();
		int y = buffer.readInt();
		int z = buffer.readInt();
		byte yaw = buffer.readByte();
		byte pitch = buffer.readByte();
		int throwerId = buffer.readInt();
		if (throwerId > 0) {
			short speedX = buffer.readShort();
			short speedY = buffer.readShort();
			short speedZ = buffer.readShort();
			return new EntityObjectMessage(entityId, type, x, y, z, throwerId, speedX, speedY, speedZ, yaw, pitch, NullRepositionManager.getInstance());
		}
		return new EntityObjectMessage(entityId, type, x, y, z, throwerId, yaw, pitch, NullRepositionManager.getInstance());
	}

	@Override
	public ByteBuf encode(EntityObjectMessage message) throws IOException {
		ByteBuf buffer = Unpooled.buffer(message.getThrowerId() > 0 ? 29 : 23);
		VanillaByteBufUtils.writeVarInt(buffer, message.getEntityId());
		buffer.writeByte(message.getType());
		buffer.writeInt(message.getX());
		buffer.writeInt(message.getY());
		buffer.writeInt(message.getZ());
		buffer.writeByte(message.getYaw());
		buffer.writeByte(message.getPitch());
		int throwerId = message.getThrowerId();
		buffer.writeInt(throwerId);
		if (throwerId > 0) {
			buffer.writeShort(message.getSpeedX());
			buffer.writeShort(message.getSpeedY());
			buffer.writeShort(message.getSpeedZ());
		}
		return buffer;
	}
}
