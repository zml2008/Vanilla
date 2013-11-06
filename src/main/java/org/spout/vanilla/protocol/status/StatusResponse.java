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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import org.spout.api.Server;
import org.spout.api.entity.Player;
import org.spout.api.protocol.ByteBufferChannelProcessor;
import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.data.configuration.VanillaConfiguration;

/**
 * Status response object. Designed to be encoded with Gson
 */
public class StatusResponse {
	public static class VersionInfo {
		private String pluginVersion;
		private int protocolVersion;

		public static VersionInfo fromPlugin(VanillaPlugin plugin) {
			final VersionInfo info = new VersionInfo();
			info.pluginVersion = plugin.getDescription().getVersion();
			info.protocolVersion = Integer.parseInt(plugin.getDescription().getData("protocol"));
			return new VersionInfo();
		}

		public String getPluginVersion() {
			return pluginVersion;
		}

		public int getProtocolVersion() {
			return protocolVersion;
		}
	}
	public static class PlayerEntry {
		private String name;
		private String id;

		public PlayerEntry(String name, String id) {
			this.name = name;
			this.id = id;
		}

		public PlayerEntry() {
		}

		public String getName() {
			return name;
		}

		public String getId() {
			return id;
		}
	}
	public static class PlayerInfo {
		private int max;
		private int online;
		private List<PlayerEntry> players;

		public static PlayerInfo fromPlugin(VanillaPlugin plugin) {
			PlayerInfo info = new PlayerInfo();
			if (plugin.getEngine() instanceof Server) {
				Server serv = (Server) plugin.getEngine();
				info.max = serv.getMaxPlayers();
				Player[] online = serv.getOnlinePlayers();
				info.online = online.length;
				info.players = new ArrayList<>();
				for (int i = 0; i < Math.min(5, online.length); ++i) {
					info.players.add(new PlayerEntry(online[i].getName(), online[i].getUID().toString()));
				}
			}
			return info;
		}

		public int getMax() {
			return max;
		}

		public int getOnline() {
			return online;
		}

		public List<PlayerEntry> getPlayers() {
			if (players == null) {
				return Collections.emptyList();
			}
			return Collections.unmodifiableList(players);
		}
	}
	private VersionInfo version;
	private PlayerInfo players;
	public String description;
	public String favicon;

	public static StatusResponse fromPlugin(VanillaPlugin plugin) {
		StatusResponse response = new StatusResponse();
		response.version = VersionInfo.fromPlugin(plugin);
		response.players = PlayerInfo.fromPlugin(plugin);
		response.description = VanillaConfiguration.MOTD.getString();
		response.favicon = ""; //"data:image/png;base64,";
		return response;
	}

	public VersionInfo getVersion() {
		return version;
	}

	public PlayerInfo getPlayers() {
		return players;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFavicon() {
		return favicon;
	}

	public void setFavicon(BufferedImage img) throws IOException {
		ByteBuf buffer = Unpooled.buffer(128 * 1024);
		// Probably not the most memory-efficient method, but it should work
		ImageIO.write(img, "png", new ByteBufOutputStream(buffer));
		ByteBuf encoded = Base64.encode(buffer, false);
		this.favicon = "data:image/png;base64," + new String(encoded.array());
	}
}
