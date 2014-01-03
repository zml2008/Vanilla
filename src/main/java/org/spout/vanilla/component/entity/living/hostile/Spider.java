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
package org.spout.vanilla.component.entity.living.hostile;

import java.util.Random;

import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.component.entity.living.Aggressive;
import org.spout.vanilla.component.entity.living.Hostile;
import org.spout.vanilla.component.entity.living.Living;
import org.spout.vanilla.component.entity.misc.Damage;
import org.spout.vanilla.component.entity.misc.DeathDrops;
import org.spout.vanilla.component.entity.misc.Health;
import org.spout.vanilla.component.entity.misc.MetadataComponent;
import org.spout.vanilla.data.Difficulty;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.game.entity.creature.CreatureProtocol;
import org.spout.vanilla.protocol.game.entity.creature.CreatureType;

/**
 * A component that identifies the entity as a Spider.
 */
public class Spider extends Living implements Hostile, Aggressive {
	@Override
	public void onAttached() {
		super.onAttached();
		setEntityProtocol(new CreatureProtocol(CreatureType.SPIDER));
		DeathDrops dropComponent = getOwner().add(DeathDrops.class);
		Random random = getRandom();
		dropComponent.addDrop(new ItemStack(VanillaMaterials.STRING, random.nextInt(2)));
		dropComponent.addDrop(new ItemStack(VanillaMaterials.SPIDER_EYE, random.nextInt(1)));
		dropComponent.addXpDrop((short) 5);
		if (getAttachedCount() == 1) {
			getOwner().add(Health.class).setSpawnHealth(16);
		}
		Damage damage = getOwner().add(Damage.class);
		damage.getDamageLevel(Difficulty.EASY).setAmount(2);
		damage.getDamageLevel(Difficulty.NORMAL).setAmount(2);
		damage.getDamageLevel(Difficulty.HARD).setAmount(3);
		damage.getDamageLevel(Difficulty.HARDCORE).setAmount(damage.getDamageLevel(Difficulty.HARD).getAmount());

		// Add metadata for aggressiveness state
		getOwner().add(MetadataComponent.class).addBoolMeta(16, VanillaData.AGGRESSIVE);
	}

	@Override
	public boolean isAggressive() {
		return getData().get(VanillaData.AGGRESSIVE);
	}

	@Override
	public void setAggressive(boolean aggro) {
		getData().put(VanillaData.AGGRESSIVE, aggro);
	}
}
