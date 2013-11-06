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
package org.spout.vanilla.protocol.game.plugin;

import java.util.Arrays;
import java.util.Iterator;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import org.spout.api.protocol.MessageCodec;
import org.spout.api.util.Named;

public class UnregisterPluginChannelCodec extends MessageCodec<UnregisterPluginChannelMessage> implements Named {
	public UnregisterPluginChannelCodec(int opcode) {
		super(UnregisterPluginChannelMessage.class, opcode);
	}

	@Override
	public ByteBuf encode(UnregisterPluginChannelMessage message) {
		ByteBuf buffer = Unpooled.buffer();
		for (Iterator<String> i = message.getTypes().iterator(); i.hasNext(); ) {
			buffer.writeBytes(i.next().getBytes(CharsetUtil.UTF_8));
			if (i.hasNext()) {
				buffer.writeByte(0);
			}
		}
		return buffer;
	}

	@Override
	public UnregisterPluginChannelMessage decode(ByteBuf buffer) {
		byte[] strData = new byte[buffer.readableBytes()];
		buffer.readBytes(strData);
		String str = new String(strData, CharsetUtil.UTF_8);
		return new UnregisterPluginChannelMessage(Arrays.asList(str.split("\0")));
	}

	public String getName() {
		return "UNREGISTER";
	}
}
