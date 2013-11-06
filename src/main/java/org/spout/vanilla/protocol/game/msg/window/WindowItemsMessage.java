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
package org.spout.vanilla.protocol.game.msg.window;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.inventory.ItemStack;
import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.inventory.window.AbstractWindow;

public final class WindowItemsMessage extends WindowMessage {
	private final ItemStack[] items;

	public WindowItemsMessage(AbstractWindow window, ItemStack[] items) {
		this(window.getId(), items);
	}

	public WindowItemsMessage(int windowInstanceId, ItemStack[] items) {
		super(windowInstanceId);
		this.items = items;
	}

	public ItemStack[] getItems() {
		return items;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("id", this.getWindowInstanceId())
				.append("items", this.items, true)
				.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final WindowItemsMessage other = (WindowItemsMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.getWindowInstanceId(), other.getWindowInstanceId())
				.append(this.items, other.items)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new org.apache.commons.lang3.builder.HashCodeBuilder()
				.append(this.getWindowInstanceId())
				.append(this.items)
				.toHashCode();
	}
}
