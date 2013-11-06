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
package org.spout.vanilla.protocol.status;

import java.net.InetSocketAddress;

import org.spout.api.protocol.CodecLookupService;
import org.spout.api.protocol.Message;
import org.spout.vanilla.protocol.VanillaProtocol;
import org.spout.vanilla.protocol.status.codec.PingCodec;
import org.spout.vanilla.protocol.status.codec.RequestCodec;
import org.spout.vanilla.protocol.status.codec.ResponseCodec;
import org.spout.vanilla.protocol.status.handler.PingHandler;
import org.spout.vanilla.protocol.status.handler.RequestHandler;
import org.spout.vanilla.protocol.status.msg.RequestMessage;

public class VanillaStatusProtocol extends VanillaProtocol {
	public static final VanillaStatusProtocol INSTANCE = new VanillaStatusProtocol();
	protected VanillaStatusProtocol() {
		super("Status", 2);
		registerPacket(RequestCodec.class, new RequestHandler(), CodecLookupService.ProtocolSide.SERVER);
		registerPacket(ResponseCodec.class, null, CodecLookupService.ProtocolSide.CLIENT);
		registerPacket(PingCodec.class, new PingHandler());
	}

	public Message getKickMessage(String message) {
		return null;
	}

	public Message getIntroductionMessage(String playerName, InetSocketAddress addr) {
		return new RequestMessage();
	}
}
