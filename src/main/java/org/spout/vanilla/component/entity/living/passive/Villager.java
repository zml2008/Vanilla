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
package org.spout.vanilla.component.entity.living.passive;

import java.util.HashMap;

import org.spout.api.entity.Player;
import org.spout.api.event.entity.EntityInteractEvent;
import org.spout.api.event.player.PlayerInteractEntityEvent;
import org.spout.api.inventory.Container;

import org.spout.vanilla.component.entity.living.Ageable;
import org.spout.vanilla.component.entity.living.Passive;
import org.spout.vanilla.component.entity.misc.Health;
import org.spout.vanilla.component.entity.misc.MetadataComponent;
import org.spout.vanilla.data.Metadata;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.inventory.player.CraftingInventory;
import org.spout.vanilla.inventory.window.Window;
import org.spout.vanilla.protocol.game.entity.creature.CreatureProtocol;
import org.spout.vanilla.protocol.game.entity.creature.CreatureType;

/**
 * A component that identifies the entity as a Villager.
 */
public class Villager extends Ageable implements Container, Passive {
	private HashMap<Player, Window> viewers = new HashMap<Player, Window>();

	@Override
	public void onAttached() {
		super.onAttached();
		setEntityProtocol(new CreatureProtocol(CreatureType.VILLAGER));

		if (getAttachedCount() == 1) {
			getOwner().add(Health.class).setSpawnHealth(20);
		}

		// Set default villager type of 5
		getOwner().getData().put(VanillaData.ENTITY_CATEGORY, (byte) 5);

		// Add metadata for villager type index
		getOwner().add(MetadataComponent.class).addMeta(Metadata.TYPE_BYTE, 16, VanillaData.ENTITY_CATEGORY);
	}

	@Override
	public void onInteract(final EntityInteractEvent<?> event) {
		if (event instanceof PlayerInteractEntityEvent) {
			final PlayerInteractEntityEvent pie = (PlayerInteractEntityEvent) event;
			final Player player = (Player) pie.getEntity();
			switch (pie.getAction()) {
				case RIGHT_CLICK:
					//TODO Open Window
			}
		}
	}

	@Override
	public CraftingInventory getInventory() {
		return getOwner().getData().get(VanillaData.VILLAGER_INVENTORY);
	}

	/**
	 * The integer value associated with this villager type.
	 *
	 * @return int
	 */
	public int getVillagerTypeID() {
		return getOwner().getData().get(VanillaData.ENTITY_CATEGORY);
	}
}
