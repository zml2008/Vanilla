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
package org.spout.vanilla.protocol.game.entity.object;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.reposition.RepositionManager;

import org.spout.vanilla.protocol.game.entity.BasicEntityProtocol;
import org.spout.vanilla.protocol.game.entity.BasicEntityProtocol;
import org.spout.vanilla.protocol.game.msg.entity.EntityMetadataMessage;
import org.spout.vanilla.protocol.game.msg.entity.spawn.EntityObjectMessage;
import org.spout.vanilla.protocol.game.msg.entity.EntityMetadataMessage;
import org.spout.vanilla.protocol.game.msg.entity.spawn.EntityObjectMessage;

public class ObjectEntityProtocol extends BasicEntityProtocol {
	public ObjectEntityProtocol(ObjectType type) {
		super(type.getId());
	}

	@Override
	public List<Message> getSpawnMessages(Entity entity, RepositionManager rm) {
		List<Message> messages = new ArrayList<>();
		messages.add(new EntityObjectMessage(entity, (byte) typeId, rm));
		messages.add(new EntityMetadataMessage(entity.getId(), getSpawnParameters(entity)));
		return messages;
	}
}
