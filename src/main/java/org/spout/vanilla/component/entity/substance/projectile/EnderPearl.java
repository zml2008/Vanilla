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
package org.spout.vanilla.component.entity.substance.projectile;

import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.component.entity.misc.Health;
import org.spout.vanilla.component.entity.substance.Substance;
import org.spout.vanilla.protocol.game.entity.object.ObjectEntityProtocol;
import org.spout.vanilla.protocol.game.entity.object.ObjectType;
import org.spout.vanilla.util.PlayerUtil;

public class EnderPearl extends Substance implements Projectile {
	private Entity shooter;

	@Override
	public void onAttached() {
		setEntityProtocol(new ObjectEntityProtocol(ObjectType.ENDER_PEARL));
		super.onAttached();
	}

	@Override
	public Entity getShooter() {
		return shooter;
	}

	@Override
	public void setShooter(Entity shooter) {
		this.shooter = shooter;
	}

	/**
	 * Called when the entity collides with a block.
	 *
	 * @param colliderPoint The point where this entity collided with the material
	 * @param collidedPoint The point where the material was collided with the entity
	 * @param block The block this entity collided with
	 */
	public void onCollided(Point colliderPoint, Point collidedPoint, Block block) {
		if (getShooter() != null && getShooter() instanceof Player) {
			Health health = getShooter().get(Health.class);
			if (health != null && !health.isDead()) {
				if (!PlayerUtil.isCreativePlayer(getShooter())) {
					health.damage(5);
				}
				((Player) getShooter()).getPhysics().setPosition(block.translate(BlockFace.TOP).getPosition());
			}
		}
		getOwner().remove();
	}
}
