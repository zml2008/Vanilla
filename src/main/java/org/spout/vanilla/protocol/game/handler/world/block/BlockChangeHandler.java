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
import org.spout.api.geo.World;
import org.spout.api.material.BlockMaterial;
import org.spout.api.protocol.ClientSession;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.reposition.RepositionManager;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.game.msg.world.block.BlockChangeMessage;
import org.spout.vanilla.protocol.game.msg.world.block.BlockChangeMessage;

public class BlockChangeHandler extends MessageHandler<BlockChangeMessage> {
	@Override
	public void handleClient(ClientSession session, BlockChangeMessage message) {
		Player player = session.getPlayer();
		World world = player.getWorld();
		RepositionManager rm = player.getNetwork().getRepositionManager();

		int x = rm.convertX(message.getX());
		int y = rm.convertY(message.getY());
		int z = rm.convertZ(message.getZ());

		short type = message.getType();
		int data = message.getMetadata();

		BlockMaterial material = (BlockMaterial) VanillaMaterials.getMaterial(type, (short) data);
		world.getChunkFromBlock(x, y, z).getBlock(x, y, z).setMaterial(material);
	}
}
