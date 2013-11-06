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
package org.spout.vanilla.protocol.game.plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.protocol.MessageCodec;
import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.protocol.VanillaProtocol;
import org.spout.vanilla.protocol.game.msg.VanillaMainChannelMessage;

public class RegisterPluginChannelMessage extends VanillaMainChannelMessage {
	private final List<String> types;

	public RegisterPluginChannelMessage(Collection<MessageCodec<?>> codecs) {
		List<String> types = new ArrayList<String>();
		for (MessageCodec<?> codec : codecs) {
			types.add(VanillaProtocol.getName(codec));
		}
		this.types = Collections.unmodifiableList(types);
	}

	public RegisterPluginChannelMessage(List<String> types) {
		this.types = Collections.unmodifiableList(types);
	}

	public List<String> getTypes() {
		return types;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("types", types)
				.toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(39, 45)
				.append(types)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RegisterPluginChannelMessage) {
			final RegisterPluginChannelMessage other = (RegisterPluginChannelMessage) obj;
			return new EqualsBuilder()
					.append(types, other.types)
					.isEquals();
		} else {
			return false;
		}
	}
}
