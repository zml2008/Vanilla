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

import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.ServerSession;

import org.spout.vanilla.component.block.material.Beacon;
import org.spout.vanilla.component.entity.inventory.WindowHolder;
import org.spout.vanilla.data.effect.EntityEffectType;
import org.spout.vanilla.inventory.block.BeaconInventory;
import org.spout.vanilla.inventory.util.InventoryConverter;
import org.spout.vanilla.inventory.window.Window;
import org.spout.vanilla.inventory.window.block.BeaconWindow;

public class BeaconHandler extends MessageHandler<BeaconMessage> {
	@Override
	public void handleServer(ServerSession session, BeaconMessage msg) {
		Window window = session.getPlayer().get(WindowHolder.class).getActiveWindow();
		if (!(window instanceof BeaconWindow)) {
			throw new IllegalStateException("Player tried to change Beacon but does not have an opened Beacon.");
		}
		Beacon beacon = ((BeaconWindow) window).getBeacon();
		beacon.setPrimaryEffect(EntityEffectType.get(msg.getPrimaryEffect()));
		beacon.setSecondaryEffect(EntityEffectType.get(msg.getSecondaryEffect()));
		for (InventoryConverter converter : window.getInventoryConverters()) {
			if (converter.getInventory() instanceof BeaconInventory) {
				converter.getInventory().clear();
			}
		}
	}
}
