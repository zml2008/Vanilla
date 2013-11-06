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
package org.spout.vanilla.protocol.login;

import java.net.InetSocketAddress;

import org.spout.api.protocol.CodecLookupService;
import org.spout.api.protocol.Message;
import org.spout.vanilla.protocol.VanillaProtocol;
import org.spout.vanilla.protocol.login.codec.DisconnectCodec;
import org.spout.vanilla.protocol.login.codec.EncryptionKeyRequestCodec;
import org.spout.vanilla.protocol.login.codec.EncryptionKeyResponseCodec;
import org.spout.vanilla.protocol.login.codec.LoginStartCodec;
import org.spout.vanilla.protocol.login.codec.LoginSuccessCodec;
import org.spout.vanilla.protocol.login.handler.EncryptionKeyRequestHandler;
import org.spout.vanilla.protocol.login.handler.EncryptionKeyResponseHandler;
import org.spout.vanilla.protocol.login.handler.LoginStartHandler;
import org.spout.vanilla.protocol.login.msg.DisconnectMessage;
import org.spout.vanilla.protocol.login.msg.LoginStartMessage;

public class VanillaLoginProtocol extends VanillaProtocol {
	public static final VanillaLoginProtocol INSTANCE = new VanillaLoginProtocol();
	protected VanillaLoginProtocol() {
		super("Login", 3);
		registerPacket(DisconnectCodec.class, null, CodecLookupService.ProtocolSide.CLIENT);
		registerPacket(LoginStartCodec.class, new LoginStartHandler(), CodecLookupService.ProtocolSide.SERVER);
		registerPacket(EncryptionKeyRequestCodec.class, new EncryptionKeyRequestHandler(), CodecLookupService.ProtocolSide.CLIENT);
		registerPacket(EncryptionKeyResponseCodec.class, new EncryptionKeyResponseHandler(), CodecLookupService.ProtocolSide.SERVER);
		registerPacket(LoginSuccessCodec.class, null, CodecLookupService.ProtocolSide.CLIENT);
	}

	public Message getKickMessage(String message) {
		return new DisconnectMessage(message);
	}

	public Message getIntroductionMessage(String playerName, InetSocketAddress addr) {
		return new LoginStartMessage(playerName);
	}
}
