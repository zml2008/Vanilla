package org.spout.vanilla.component.entity.minecart.type;

import org.spout.api.entity.Player;
import org.spout.api.event.entity.EntityInteractEvent;
import org.spout.api.event.player.PlayerInteractEntityEvent;
import org.spout.api.inventory.ItemStack;
import org.spout.api.map.DefaultedKey;
import org.spout.api.map.DefaultedKeyFactory;
import org.spout.api.material.BlockMaterial;
import org.spout.vanilla.component.entity.inventory.WindowHolder;
import org.spout.vanilla.component.entity.minecart.MinecartType;
import org.spout.vanilla.component.entity.misc.DeathDrops;
import org.spout.vanilla.inventory.block.ChestInventory;
import org.spout.vanilla.inventory.window.block.chest.ChestWindow;
import org.spout.vanilla.material.VanillaMaterials;

public class StorageMinecartType extends MinecartType {
	public static final DefaultedKey<ChestInventory> CHEST_INVENTORY = new DefaultedKeyFactory<ChestInventory>("chest_inventory", ChestInventory.class);

	@Override
	public void onAttached() {
		super.onAttached();
		if (getAttachedCount() == 1) {
			getOwner().add(DeathDrops.class).addDrop(new ItemStack(VanillaMaterials.CHEST, 1));
		}
	}

	public ChestInventory getInventory() {
		return this.getData().get(CHEST_INVENTORY);
	}

	@Override
	public void onInteract(final EntityInteractEvent<?> event) {
		if (event instanceof PlayerInteractEntityEvent) {
			final PlayerInteractEntityEvent pie = (PlayerInteractEntityEvent) event;
			final Player player = (Player) pie.getEntity();
			switch (pie.getAction()) {
				case RIGHT_CLICK:
					player.add(WindowHolder.class).openWindow(new ChestWindow(player, getInventory(), "Minecart"));
			}
		}
		super.onInteract(event);
	}

	@Override
	public void onDestroy() {
		for (ItemStack stack : (ItemStack[]) getInventory().toArray()) {
			if (stack != null) {
				getOwner().get(DeathDrops.class).addDrop(stack);
			}
		}
		super.onDestroy();
	}

	@Override
	public BlockMaterial getDefaultDisplayedBlock() {
		return VanillaMaterials.CHEST;
	}
}