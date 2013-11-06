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
package org.spout.vanilla.protocol.game.handler.window;

import org.spout.api.entity.Player;
import org.spout.api.inventory.Slot;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.ServerSession;

import org.spout.vanilla.component.entity.inventory.WindowHolder;
import org.spout.vanilla.component.entity.living.Human;
import org.spout.vanilla.inventory.window.ClickArguments;
import org.spout.vanilla.inventory.window.Window;
import org.spout.vanilla.protocol.game.msg.window.WindowCreativeActionMessage;
import org.spout.vanilla.protocol.game.msg.window.WindowCreativeActionMessage;

public class WindowCreativeActionHandler extends MessageHandler<WindowCreativeActionMessage> {
	@Override
	public void handleServer(ServerSession session, WindowCreativeActionMessage message) {
		Player holder = session.getPlayer();
		Human human = holder.get(Human.class);
		if (human == null) {
			return;
		}
		if (human.isSurvival()) {
			holder.kick("Attempted to use the creative inventory while on survival.");
			return;
		}

		Window window = holder.get(WindowHolder.class).getActiveWindow();
		if (message.get() == null) {
			//Taking item from existing slot
			window.setCursorItem(null);
			ClickArguments args = window.getClickArguments(message.getSlot(), ClickArguments.ClickAction.LEFT_CLICK);
			if (args != null) {
				window.onClick(args);
			}
		} else if (message.getSlot() == -1) {
			window.setCursorItem(message.get());
			window.onOutsideClick();
		} else {
			Slot entry = window.getSlot(message.getSlot());
			if (entry != null) {
				window.onCreativeClick(entry.getInventory(), entry.getIndex(), message.get());
			}
		}
	}
}
