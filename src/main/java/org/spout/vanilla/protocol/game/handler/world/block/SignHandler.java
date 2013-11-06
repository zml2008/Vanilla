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
package org.spout.vanilla.protocol.game.handler.world.block;

import org.spout.api.entity.Player;
import org.spout.api.event.cause.PlayerCause;
import org.spout.api.protocol.ClientSession;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.ServerSession;
import org.spout.api.protocol.reposition.RepositionManager;

import org.spout.vanilla.component.block.material.Sign;
import org.spout.vanilla.protocol.game.msg.world.block.SignMessage;
import org.spout.vanilla.protocol.game.msg.world.block.SignMessage;

public class SignHandler extends MessageHandler<SignMessage> {
	@Override
	public void handleServer(ServerSession session, SignMessage message) {
		Player player = session.getPlayer();
		RepositionManager rmInverse = player.getNetwork().getRepositionManager().getInverse();

		int x = rmInverse.convertX(message.getX());
		int y = rmInverse.convertY(message.getY());
		int z = rmInverse.convertZ(message.getZ());

		Sign sign = player.getWorld().getBlock(x, y, z).get(Sign.class);
		if (sign == null) {
			return;
		}

		String[] text = message.getMessage();
		if (text.length != 4) {
			return;
		}

		sign.setText(text, new PlayerCause(player));
	}

	@Override
	public void handleClient(ClientSession session, SignMessage message) {
		Player player = session.getPlayer();
		RepositionManager rm = player.getNetwork().getRepositionManager();

		int x = rm.convertX(message.getX());
		int y = rm.convertY(message.getY());
		int z = rm.convertZ(message.getZ());

		Sign sign = player.getWorld().getBlock(x, y, z).get(Sign.class);
		if (sign == null) {
			return;
		}

		String[] text = message.getMessage();
		if (text.length != 4) {
			return;
		}

		sign.setText(text, new PlayerCause(player));
	}
}
