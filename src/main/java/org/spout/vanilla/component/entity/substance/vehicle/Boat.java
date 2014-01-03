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
package org.spout.vanilla.component.entity.substance.vehicle;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.material.block.BlockFace;
import org.spout.api.util.Parameter;

import org.spout.vanilla.component.entity.substance.Substance;
import org.spout.vanilla.event.entity.network.EntityMetaChangeEvent;
import org.spout.vanilla.protocol.VanillaByteBufUtils;
import org.spout.vanilla.protocol.game.entity.object.ObjectEntityProtocol;
import org.spout.vanilla.protocol.game.entity.object.ObjectType;

public class Boat extends Substance {
	private BlockFace direction = BlockFace.NORTH;

	@Override
	public void onTick(float dt) {
	}

	@Override
	public void onAttached() {
		setEntityProtocol(new ObjectEntityProtocol(ObjectType.BOAT));
		super.onAttached();
	}

	public BlockFace getForwardDirection() {
		return direction;
	}

	public void setForwardDirection(BlockFace direction) {
		this.direction = direction;
		List<Parameter<?>> params = new ArrayList<Parameter<?>>();
		params.add(new Parameter<Integer>(Parameter.TYPE_INT, 18, (int) VanillaByteBufUtils.getNativeDirection(direction)));
		getOwner().getNetwork().callProtocolEvent(new EntityMetaChangeEvent(getOwner(), params));
	}
}
