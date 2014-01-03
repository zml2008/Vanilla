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
package org.spout.vanilla.component.entity.substance;

import org.spout.vanilla.component.entity.misc.MetadataComponent;
import org.spout.vanilla.data.Metadata;
import org.spout.vanilla.protocol.game.entity.object.ObjectEntityProtocol;
import org.spout.vanilla.protocol.game.entity.object.ObjectType;

/**
 * A component that identifies the entity as an EnderCrystal.
 */
public class EnderCrystal extends Substance {
	private int health = 5; //Note: health doesn't seem to be used, it's here for the protocol

	@Override
	public void onAttached() {
		setEntityProtocol(new ObjectEntityProtocol(ObjectType.ENDER_CRYSTAL));
		super.onAttached();

		// Add metadata value for the health status of this crystal
		getOwner().add(MetadataComponent.class).addMeta(new Metadata<Integer>(Metadata.TYPE_INT, 8) {
			@Override
			public Integer getValue() {
				return getHealth();
			}

			@Override
			public void setValue(Integer value) {
				setHealth(value);
			}
		});
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
}
