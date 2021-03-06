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
package org.spout.vanilla.event.entity;

import java.util.Set;

import org.spout.api.entity.Entity;
import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;
import org.spout.api.event.entity.AbstractEntityEvent;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;

/**
 * Event which is called when an Entity explodes
 */
public class EntityExplodeEvent extends AbstractEntityEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private Set<Block> blocks;
	private Point epicenter;
	private float yield;
	private boolean incendiary;

	public EntityExplodeEvent(Entity e) {
		super(e);
	}

	/**
	 * Gets a set of blocks that explode during this event.
	 */
	public Set<Block> getBlocks() {
		return blocks;
	}

	/**
	 * Sets which blocks to explode.
	 */
	public void setBlocks(Set<Block> blocks) {
		this.blocks = blocks;
	}

	/**
	 * Gets the epicenter of the explosion.
	 *
	 * @return epicenter of explosion.
	 */
	public Point getEpicenter() {
		return epicenter;
	}

	/**
	 * Sets the epicenter of the explosion.
	 */
	public void setEpicenter(Point epicenter) {
		this.epicenter = epicenter;
	}

	/**
	 * Whether or not the explosion is incendiary.
	 *
	 * @return true if incendiary.
	 */
	public boolean isIncendiary() {
		return incendiary;
	}

	/**
	 * Sets whether or not the explosion is incendiary.
	 */
	public void setIncendiary(boolean incendiary) {
		this.incendiary = incendiary;
	}

	/**
	 * Gets the yield of the explosion.
	 *
	 * @return yield of explosion.
	 */
	public float getYield() {
		return yield;
	}

	/**
	 * Sets the yield of the explosion.
	 */
	public void setYield(float yield) {
		this.yield = yield;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		super.setCancelled(cancelled);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
