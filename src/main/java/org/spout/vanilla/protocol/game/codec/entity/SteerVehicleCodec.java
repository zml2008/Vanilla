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
package org.spout.vanilla.protocol.game.codec.entity;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.spout.api.protocol.MessageCodec;

import org.spout.vanilla.protocol.game.msg.entity.SteerVehicleMessage;

public class SteerVehicleCodec extends MessageCodec<SteerVehicleMessage> {
	public SteerVehicleCodec() {
		super(SteerVehicleMessage.class, 0x1B);
	}

	@Override
	public SteerVehicleMessage decode(ByteBuf buffer) throws IOException {
		float sideways = buffer.readFloat();
		float forward = buffer.readFloat();
		boolean jump = buffer.readByte() != 0;
		boolean unmount = buffer.readByte() != 0;
		return new SteerVehicleMessage(sideways, forward, jump, unmount);
	}

	@Override
	public ByteBuf encode(SteerVehicleMessage message) throws IOException {
		ByteBuf buffer = Unpooled.buffer(11);
		buffer.writeFloat(message.getSideways());
		buffer.writeFloat(message.getForward());
		buffer.writeByte(message.isJumping() ? 1 : 0);
		buffer.writeByte(message.isUnmount() ? 1 : 0);
		return buffer;
	}
}
