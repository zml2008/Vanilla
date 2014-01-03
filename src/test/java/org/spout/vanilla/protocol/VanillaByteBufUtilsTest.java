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
package org.spout.vanilla.protocol;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.junit.Test;

import org.spout.api.inventory.ItemStack;
import org.spout.api.util.Parameter;

import org.spout.vanilla.EngineFaker;
import org.spout.vanilla.material.VanillaMaterials;

import static org.junit.Assert.assertEquals;
import static org.spout.vanilla.protocol.VanillaByteBufUtils.*;

public class VanillaByteBufUtilsTest {
	public static final List<Parameter<?>> TEST_PARAMS = new ArrayList<Parameter<?>>();

	static {
		EngineFaker.setupEngine();

		TEST_PARAMS.add(new Parameter<Byte>(Parameter.TYPE_BYTE, 1, (byte) 33));
		TEST_PARAMS.add(new Parameter<Short>(Parameter.TYPE_SHORT, 2, (short) 333));
		TEST_PARAMS.add(new Parameter<Integer>(Parameter.TYPE_INT, 3, 22));
		TEST_PARAMS.add(new Parameter<Float>(Parameter.TYPE_FLOAT, 4, 1.23F));
		TEST_PARAMS.add(new Parameter<String>(Parameter.TYPE_STRING, 5, "Hello World"));
		TEST_PARAMS.add(new Parameter<ItemStack>(Parameter.TYPE_ITEM, 6, new ItemStack(VanillaMaterials.BEDROCK, 5)));
	}

	@Test
	public void testParameters() throws Exception {
		ByteBuf buf = Unpooled.buffer();
		writeParameters(buf, TEST_PARAMS);
		assertEquals(TEST_PARAMS, readParameters(buf));
	}

	private static final String TEST_STRING = "This is a test String \u007Aawith symbols";

	@Test
	public void testString() throws Exception {
		ByteBuf buf = Unpooled.buffer();
		writeString(buf, TEST_STRING);
		assertEquals(TEST_STRING, readString(buf));
	}

	private static final int[] TEST_VALS = new int[] {0, 1, 1024, 123456, Integer.MIN_VALUE, Integer.MAX_VALUE};
	@Test
	public void testVarInt() {
		ByteBuf buf = Unpooled.buffer();
		for (int i : TEST_VALS) {
			buf.clear();
			writeVarInt(buf, i);
			assertEquals(i, readVarInt(buf));
		}

	}
}
