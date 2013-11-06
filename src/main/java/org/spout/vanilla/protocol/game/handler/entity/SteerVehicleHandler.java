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
package org.spout.vanilla.protocol.game.handler.entity;

import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.ServerSession;

import org.spout.vanilla.component.entity.living.Human;
import org.spout.vanilla.protocol.game.msg.entity.SteerVehicleMessage;

public final class SteerVehicleHandler extends MessageHandler<SteerVehicleMessage> {
	public void handleServer(ServerSession session, SteerVehicleMessage message) {
		Human player = session.getPlayer().get(Human.class);
		if (!player.isRiding()) {
			return;
		}
		if (message.isUnmount()) {
			player.setRiding(false);
			// TODO: detach the player from the entity it's riding
			return;
		}

		if (message.isJumping()) {
			player.setJumping(true);
			// TODO: set the ridden entity as jumping also if possible
		}
		// TODO: handle movement onto the entity
	}
}
