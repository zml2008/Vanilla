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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.commons.lang3.tuple.Pair;
import org.spout.api.Platform;
import org.spout.api.Spout;
import org.spout.api.command.CommandArguments;
import org.spout.api.component.entity.PlayerNetworkComponent;
import org.spout.api.exception.ArgumentParseException;
import org.spout.api.exception.UnknownPacketException;
import org.spout.api.map.DefaultedKey;
import org.spout.api.map.DefaultedKeyImpl;
import org.spout.api.protocol.ClientSession;
import org.spout.api.protocol.CodecLookupService;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.MessageCodec;
import org.spout.api.protocol.Protocol;
import org.spout.api.protocol.ServerSession;
import org.spout.api.util.Named;
import org.spout.vanilla.ChatStyle;
import org.spout.vanilla.component.entity.player.VanillaPlayerNetworkComponent;
import org.spout.vanilla.protocol.game.msg.ServerPluginMessage;
import org.spout.vanilla.protocol.game.msg.player.PlayerChatMessage;
import org.spout.vanilla.protocol.game.plugin.RegisterPluginChannelMessage;
import org.spout.vanilla.protocol.login.VanillaLoginProtocol;
import org.spout.vanilla.protocol.netcache.ChunkNetCache;
import org.spout.vanilla.protocol.status.VanillaStatusProtocol;

public abstract class VanillaProtocol extends Protocol {
	private static final Map<Integer, VanillaProtocol> PROTOCOL_STATES = new HashMap<>();
	static {
		PROTOCOL_STATES.put(1, VanillaStatusProtocol.INSTANCE);
		PROTOCOL_STATES.put(2, VanillaLoginProtocol.INSTANCE);
	}
	public static final DefaultedKey<String> SESSION_ID = new DefaultedKeyImpl<String>("sessionid", "0000000000000000");
	public static final DefaultedKey<String> HANDSHAKE_USERNAME = new DefaultedKeyImpl<String>("handshake_username", "");
	public static final DefaultedKey<Long> LOGIN_TIME = new DefaultedKeyImpl<Long>("handshake_time", -1L);
	public static final DefaultedKey<ChunkNetCache> CHUNK_NET_CACHE = new DefaultedKeyImpl<ChunkNetCache>("chunk_net_cache", (ChunkNetCache) null);
	public static final DefaultedKey<ArrayList<String>> REGISTERED_CUSTOM_PACKETS = new DefaultedKey<ArrayList<String>>() {
		private final List<String> defaultRestricted = Arrays.asList("REGISTER", "UNREGISTER");

		public ArrayList<String> getDefaultValue() {
			return new ArrayList<String>(defaultRestricted);
		}

		public String getKeyString() {
			return "registeredPluginChannels";
		}
	};
	public static final int DEFAULT_PORT = 25565;

	public static VanillaProtocol getProtocolForState(int id) {
		return PROTOCOL_STATES.get(id);
	}

	protected VanillaProtocol(String name, int codecSize) {
		super(name == null ? "Vanilla" : ("Vanilla-" + name), DEFAULT_PORT, codecSize);
	}

	@Override
	public Message getCommandMessage(String command, CommandArguments args) {
		try {
			if (command.equals("kick")) {
				return getKickMessage(args.popRemainingStrings("message"));
			} else if (command.equals("say")) {
				return new PlayerChatMessage(args.popRemainingStrings("message") + "\u00a7r"); // The reset text is a workaround for a change in 1.3 -- Remove if fixed
			} else {
				return new PlayerChatMessage('/' + command + ' ' + args.popRemainingStrings("message"));
			}
		} catch (ArgumentParseException ex) {
			return new PlayerChatMessage(ChatStyle.RED + ex.getMessage());
		}
	}

	@Override
	@SuppressWarnings ("unchecked")
	public <T extends Message> Message getWrappedMessage(T dynamicMessage) throws IOException {
		MessageCodec<T> codec = (MessageCodec<T>) getCodecLookupService().find(dynamicMessage.getClass());
		ByteBuf buffer = codec.encode(Spout.getPlatform() == Platform.CLIENT, dynamicMessage);

		return new ServerPluginMessage(getName(codec), buffer.array());
	}

	@Override
	public MessageCodec<?> readHeader(ByteBuf buf) throws IOException {
		int len = VanillaByteBufUtils.readVarInt(buf);
		if (len == 0xFE) {
			System.out.println("Possible old-style ping");
		}
		/*if (!buf.isReadable(len)) {
			throw new IOException("Not enough data provided to read next packet");
		}*/
		int opcode = VanillaByteBufUtils.readVarInt(buf);
		MessageCodec<?> codec = getCodecLookupService().find(opcode, Spout.getPlatform() == Platform.CLIENT ? CodecLookupService.ProtocolSide.CLIENT : CodecLookupService.ProtocolSide.SERVER);
		System.out.println("Reading a " + codec);
		if (codec == null) {
			throw new UnknownPacketException(opcode);
		}
		return codec;
	}

	@Override
	public ByteBuf writeHeader(MessageCodec<?> codec, ByteBuf data) {
		ByteBuf buffer = Unpooled.buffer();
		ByteBuf opcodeBuf = Unpooled.buffer();
		VanillaByteBufUtils.writeVarInt(opcodeBuf, codec.getOutgoingOpcode());
		VanillaByteBufUtils.writeVarInt(buffer, data.readableBytes() + opcodeBuf.readableBytes());
		System.out.println("Writing a " + codec);

		buffer.writeBytes(opcodeBuf);
		return buffer;
	}



	public static MessageCodec<?> getCodec(String name, Protocol activeProtocol) {
		for (Pair<Integer, String> item : activeProtocol.getDynamicallyRegisteredPackets()) {
			MessageCodec<?> codec = activeProtocol.getCodecLookupService().find(item.getLeft());
			if (getName(codec).equalsIgnoreCase(name)) {
				return codec;
			}
		}
		return null;
	}

	public static String getName(MessageCodec<?> codec) {
		if (codec instanceof Named) {
			return ((Named) codec).getName();
		} else {
			return "SPOUT:" + codec.getOutgoingOpcode();
		}
	}

	@Override
	public Class<? extends PlayerNetworkComponent> getServerNetworkComponent(ServerSession session) {
		return VanillaPlayerNetworkComponent.class;
	}

	@Override
	public Class<? extends PlayerNetworkComponent> getClientNetworkComponent(ClientSession session) {
		return VanillaPlayerNetworkComponent.class;
	}

	@Override
	public void initializeServerSession(ServerSession session) {
		List<MessageCodec<?>> dynamicCodecList = new ArrayList<MessageCodec<?>>();
		for (Pair<Integer, String> item : getDynamicallyRegisteredPackets()) {
			MessageCodec<?> codec = getCodecLookupService().find(item.getLeft());
			if (codec != null) {
				dynamicCodecList.add(codec);
			} else {
				throw new IllegalStateException("Dynamic packet class" + item.getRight() + " claims to be registered but is not in our CodecLookupService!");
			}
		}

		session.send(new RegisterPluginChannelMessage(dynamicCodecList));
	}

	@Override
	public void initializeClientSession(ClientSession session) {
		List<MessageCodec<?>> dynamicCodecList = new ArrayList<MessageCodec<?>>();
		for (Pair<Integer, String> item : getDynamicallyRegisteredPackets()) {
			MessageCodec<?> codec = getCodecLookupService().find(item.getLeft());
			if (codec != null) {
				dynamicCodecList.add(codec);
			} else {
				throw new IllegalStateException("Dynamic packet class" + item.getRight() + " claims to be registered but is not in our CodecLookupService!");
			}
		}
		session.send(new RegisterPluginChannelMessage(dynamicCodecList));
	}
}
