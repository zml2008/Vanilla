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
package org.spout.vanilla.protocol.game;

import java.net.InetSocketAddress;

import org.spout.api.protocol.Message;
import org.spout.vanilla.protocol.VanillaProtocol;
import org.spout.vanilla.protocol.game.codec.entity.EntityActionCodec;
import org.spout.vanilla.protocol.game.codec.entity.EntityAnimationCodec;
import org.spout.vanilla.protocol.game.codec.entity.EntityAttachEntityCodec;
import org.spout.vanilla.protocol.game.codec.entity.EntityDestroyCodec;
import org.spout.vanilla.protocol.game.codec.entity.EntityEquipmentCodec;
import org.spout.vanilla.protocol.game.codec.entity.EntityInitializeCodec;
import org.spout.vanilla.protocol.game.codec.entity.EntityItemDataCodec;
import org.spout.vanilla.protocol.game.codec.entity.EntityMetadataCodec;
import org.spout.vanilla.protocol.game.codec.entity.EntityPropertiesCodec;
import org.spout.vanilla.protocol.game.codec.entity.EntityRelativePositionCodec;
import org.spout.vanilla.protocol.game.codec.entity.EntityStatusCodec;
import org.spout.vanilla.protocol.game.codec.entity.EntityTileDataCodec;
import org.spout.vanilla.protocol.game.codec.entity.SteerVehicleCodec;
import org.spout.vanilla.protocol.game.codec.entity.effect.EntityEffectCodec;
import org.spout.vanilla.protocol.game.codec.entity.effect.EntityRemoveEffectCodec;
import org.spout.vanilla.protocol.game.codec.entity.pos.EntityHeadYawCodec;
import org.spout.vanilla.protocol.game.codec.entity.pos.EntityRelativePositionYawCodec;
import org.spout.vanilla.protocol.game.codec.entity.pos.EntityTeleportCodec;
import org.spout.vanilla.protocol.game.codec.entity.pos.EntityVelocityCodec;
import org.spout.vanilla.protocol.game.codec.entity.pos.EntityYawCodec;
import org.spout.vanilla.protocol.game.codec.entity.spawn.EntityExperienceOrbCodec;
import org.spout.vanilla.protocol.game.codec.entity.spawn.EntityMobCodec;
import org.spout.vanilla.protocol.game.codec.entity.spawn.EntityPaintingCodec;
import org.spout.vanilla.protocol.game.codec.entity.spawn.EntitySpawnObjectCodec;
import org.spout.vanilla.protocol.game.codec.entity.spawn.EntityThunderboltCodec;
import org.spout.vanilla.protocol.game.codec.player.PlayerAbilityCodec;
import org.spout.vanilla.protocol.game.codec.player.PlayerBedCodec;
import org.spout.vanilla.protocol.game.codec.player.PlayerBlockPlacementCodec;
import org.spout.vanilla.protocol.game.codec.player.PlayerChatCodec;
import org.spout.vanilla.protocol.game.codec.player.PlayerCollectItemCodec;
import org.spout.vanilla.protocol.game.codec.player.PlayerDiggingCodec;
import org.spout.vanilla.protocol.game.codec.player.PlayerExperienceCodec;
import org.spout.vanilla.protocol.game.codec.player.PlayerGameStateCodec;
import org.spout.vanilla.protocol.game.codec.player.PlayerGroundCodec;
import org.spout.vanilla.protocol.game.codec.player.PlayerHealthCodec;
import org.spout.vanilla.protocol.game.codec.player.PlayerHeldItemChangeCodec;
import org.spout.vanilla.protocol.game.codec.player.PlayerLocaleViewDistanceCodec;
import org.spout.vanilla.protocol.game.codec.player.PlayerStatisticCodec;
import org.spout.vanilla.protocol.game.codec.player.PlayerStatusCodec;
import org.spout.vanilla.protocol.game.codec.player.PlayerTabCompleteCodec;
import org.spout.vanilla.protocol.game.codec.player.PlayerTimeCodec;
import org.spout.vanilla.protocol.game.codec.player.PlayerUseEntityCodec;
import org.spout.vanilla.protocol.game.codec.player.conn.PlayerKickCodec;
import org.spout.vanilla.protocol.game.codec.player.conn.PlayerListCodec;
import org.spout.vanilla.protocol.game.codec.player.conn.PlayerLoginRequestCodec;
import org.spout.vanilla.protocol.game.codec.player.conn.PlayerPingCodec;
import org.spout.vanilla.protocol.game.codec.player.pos.PlayerLookCodec;
import org.spout.vanilla.protocol.game.codec.player.pos.PlayerPositionCodec;
import org.spout.vanilla.protocol.game.codec.player.pos.PlayerPositionLookCodec;
import org.spout.vanilla.protocol.game.codec.player.pos.PlayerRespawnCodec;
import org.spout.vanilla.protocol.game.codec.player.pos.PlayerSpawnCodec;
import org.spout.vanilla.protocol.game.codec.player.pos.PlayerSpawnPositionCodec;
import org.spout.vanilla.protocol.game.codec.scoreboard.ScoreboardDisplayCodec;
import org.spout.vanilla.protocol.game.codec.scoreboard.ScoreboardObjectiveCodec;
import org.spout.vanilla.protocol.game.codec.scoreboard.ScoreboardScoreCodec;
import org.spout.vanilla.protocol.game.codec.scoreboard.ScoreboardTeamCodec;
import org.spout.vanilla.protocol.game.codec.server.ServerPluginCodec;
import org.spout.vanilla.protocol.game.codec.window.WindowClickCodec;
import org.spout.vanilla.protocol.game.codec.window.WindowCloseCodec;
import org.spout.vanilla.protocol.game.codec.window.WindowCreativeActionCodec;
import org.spout.vanilla.protocol.game.codec.window.WindowEnchantItemCodec;
import org.spout.vanilla.protocol.game.codec.window.WindowItemsCodec;
import org.spout.vanilla.protocol.game.codec.window.WindowOpenCodec;
import org.spout.vanilla.protocol.game.codec.window.WindowPropertyCodec;
import org.spout.vanilla.protocol.game.codec.window.WindowSlotCodec;
import org.spout.vanilla.protocol.game.codec.window.WindowTransactionCodec;
import org.spout.vanilla.protocol.game.codec.world.EffectCodec;
import org.spout.vanilla.protocol.game.codec.world.ExplosionCodec;
import org.spout.vanilla.protocol.game.codec.world.ParticleEffectCodec;
import org.spout.vanilla.protocol.game.codec.world.SoundEffectCodec;
import org.spout.vanilla.protocol.game.codec.world.block.BlockActionCodec;
import org.spout.vanilla.protocol.game.codec.world.block.BlockBreakAnimationCodec;
import org.spout.vanilla.protocol.game.codec.world.block.BlockBulkCodec;
import org.spout.vanilla.protocol.game.codec.world.block.BlockChangeCodec;
import org.spout.vanilla.protocol.game.codec.world.block.SignCodec;
import org.spout.vanilla.protocol.game.codec.world.chunk.ChunkBulkCodec;
import org.spout.vanilla.protocol.game.codec.world.chunk.ChunkDataCodec;
import org.spout.vanilla.protocol.game.handler.ServerPluginHandler;
import org.spout.vanilla.protocol.game.handler.entity.EntityActionHandler;
import org.spout.vanilla.protocol.game.handler.entity.EntityAnimationHandler;
import org.spout.vanilla.protocol.game.handler.entity.EntityAttachEntityHandler;
import org.spout.vanilla.protocol.game.handler.entity.EntityDestroyHandler;
import org.spout.vanilla.protocol.game.handler.entity.EntityEffectHandler;
import org.spout.vanilla.protocol.game.handler.entity.EntityEquipmentHandler;
import org.spout.vanilla.protocol.game.handler.entity.EntityExperienceOrbHandler;
import org.spout.vanilla.protocol.game.handler.entity.EntityInitializeHandler;
import org.spout.vanilla.protocol.game.handler.entity.EntityItemDataHandler;
import org.spout.vanilla.protocol.game.handler.entity.EntityMetadataHandler;
import org.spout.vanilla.protocol.game.handler.entity.EntityMobHandler;
import org.spout.vanilla.protocol.game.handler.entity.EntityObjectHandler;
import org.spout.vanilla.protocol.game.handler.entity.EntityPaintingHandler;
import org.spout.vanilla.protocol.game.handler.entity.EntityPropertiesHandler;
import org.spout.vanilla.protocol.game.handler.entity.EntityStatusHandler;
import org.spout.vanilla.protocol.game.handler.entity.EntityThunderboltHandler;
import org.spout.vanilla.protocol.game.handler.entity.EntityTileDataHandler;
import org.spout.vanilla.protocol.game.handler.entity.SteerVehicleHandler;
import org.spout.vanilla.protocol.game.handler.entity.effect.EntityRemoveEffectHandler;
import org.spout.vanilla.protocol.game.handler.entity.pos.EntityHeadYawHandler;
import org.spout.vanilla.protocol.game.handler.entity.pos.EntityRelativePositionHandler;
import org.spout.vanilla.protocol.game.handler.entity.pos.EntityRelativePositionYawHandler;
import org.spout.vanilla.protocol.game.handler.entity.pos.EntityTeleportHandler;
import org.spout.vanilla.protocol.game.handler.entity.pos.EntityVelocityHandler;
import org.spout.vanilla.protocol.game.handler.entity.pos.EntityYawHandler;
import org.spout.vanilla.protocol.game.handler.player.PlayerAbilityHandler;
import org.spout.vanilla.protocol.game.handler.player.PlayerBedHandler;
import org.spout.vanilla.protocol.game.handler.player.PlayerBlockPlacementHandler;
import org.spout.vanilla.protocol.game.handler.player.PlayerChatHandler;
import org.spout.vanilla.protocol.game.handler.player.PlayerCollectItemHandler;
import org.spout.vanilla.protocol.game.handler.player.PlayerDiggingHandler;
import org.spout.vanilla.protocol.game.handler.player.PlayerExperienceHandler;
import org.spout.vanilla.protocol.game.handler.player.PlayerGameStateHandler;
import org.spout.vanilla.protocol.game.handler.player.PlayerGroundHandler;
import org.spout.vanilla.protocol.game.handler.player.PlayerHealthHandler;
import org.spout.vanilla.protocol.game.handler.player.PlayerHeldItemChangeHandler;
import org.spout.vanilla.protocol.game.handler.player.PlayerLocaleViewDistanceHandler;
import org.spout.vanilla.protocol.game.handler.player.PlayerRespawnHandler;
import org.spout.vanilla.protocol.game.handler.player.PlayerStatisticHandler;
import org.spout.vanilla.protocol.game.handler.player.PlayerStatusHandler;
import org.spout.vanilla.protocol.game.handler.player.PlayerTabCompleteHandler;
import org.spout.vanilla.protocol.game.handler.player.PlayerTimeHandler;
import org.spout.vanilla.protocol.game.handler.player.PlayerUseEntityHandler;
import org.spout.vanilla.protocol.game.handler.player.conn.PlayerKickHandler;
import org.spout.vanilla.protocol.game.handler.player.conn.PlayerListHandler;
import org.spout.vanilla.protocol.game.handler.player.conn.PlayerLoginRequestHandler;
import org.spout.vanilla.protocol.game.handler.player.conn.PlayerPingHandler;
import org.spout.vanilla.protocol.game.handler.player.pos.PlayerLookHandler;
import org.spout.vanilla.protocol.game.handler.player.pos.PlayerPositionHandler;
import org.spout.vanilla.protocol.game.handler.player.pos.PlayerPositionLookHandler;
import org.spout.vanilla.protocol.game.handler.player.pos.PlayerSpawnHandler;
import org.spout.vanilla.protocol.game.handler.player.pos.PlayerSpawnPositionHandler;
import org.spout.vanilla.protocol.game.handler.scoreboard.ScoreboardDisplayHandler;
import org.spout.vanilla.protocol.game.handler.scoreboard.ScoreboardObjectiveHandler;
import org.spout.vanilla.protocol.game.handler.scoreboard.ScoreboardScoreHandler;
import org.spout.vanilla.protocol.game.handler.scoreboard.ScoreboardTeamHandler;
import org.spout.vanilla.protocol.game.handler.window.WindowClickHandler;
import org.spout.vanilla.protocol.game.handler.window.WindowCloseHandler;
import org.spout.vanilla.protocol.game.handler.window.WindowCreativeActionHandler;
import org.spout.vanilla.protocol.game.handler.window.WindowEnchantItemHandler;
import org.spout.vanilla.protocol.game.handler.window.WindowItemsHandler;
import org.spout.vanilla.protocol.game.handler.window.WindowOpenHandler;
import org.spout.vanilla.protocol.game.handler.window.WindowPropertyHandler;
import org.spout.vanilla.protocol.game.handler.window.WindowSlotHandler;
import org.spout.vanilla.protocol.game.handler.window.WindowTransactionHandler;
import org.spout.vanilla.protocol.game.handler.world.EffectHandler;
import org.spout.vanilla.protocol.game.handler.world.ExplosionHandler;
import org.spout.vanilla.protocol.game.handler.world.ParticleEffectHandler;
import org.spout.vanilla.protocol.game.handler.world.SoundEffectHandler;
import org.spout.vanilla.protocol.game.handler.world.block.BlockActionHandler;
import org.spout.vanilla.protocol.game.handler.world.block.BlockBreakAnimationHandler;
import org.spout.vanilla.protocol.game.handler.world.block.BlockBulkHandler;
import org.spout.vanilla.protocol.game.handler.world.block.BlockChangeHandler;
import org.spout.vanilla.protocol.game.handler.world.block.SignHandler;
import org.spout.vanilla.protocol.game.handler.world.chunk.ChunkBulkHandler;
import org.spout.vanilla.protocol.game.handler.world.chunk.ChunkDataHandler;
import org.spout.vanilla.protocol.game.msg.player.conn.PlayerKickMessage;
import org.spout.vanilla.protocol.game.plugin.BeaconCodec;
import org.spout.vanilla.protocol.game.plugin.BeaconHandler;
import org.spout.vanilla.protocol.game.plugin.CommandBlockCodec;
import org.spout.vanilla.protocol.game.plugin.CommandBlockHandler;
import org.spout.vanilla.protocol.game.plugin.RegisterPluginChannelCodec;
import org.spout.vanilla.protocol.game.plugin.RegisterPluginChannelMessageHandler;
import org.spout.vanilla.protocol.game.plugin.UnregisterPluginChannelCodec;
import org.spout.vanilla.protocol.game.plugin.UnregisterPluginChannelMessageHandler;
import org.spout.vanilla.protocol.netcache.protocol.ChunkCacheCodec;
import org.spout.vanilla.protocol.netcache.protocol.ChunkCacheHandler;

/**
 * @author zml2008
 */
public class VanillaGameProtocol extends VanillaProtocol {
	public static final VanillaGameProtocol INSTANCE = new VanillaGameProtocol();
	static {
		registerProtocol(INSTANCE);
	}

	private VanillaGameProtocol() {
		super("Game", 512);
		// THIS FORMATTING IS CORRECT NO MATTER WHAT ANY AUTOFORMATTER SAYS. DON'T CHANGE IT
		/* 0x00 */
		registerPacket(PlayerPingCodec.class, new PlayerPingHandler());
		/* 0x01 */
		registerPacket(PlayerLoginRequestCodec.class, new PlayerLoginRequestHandler());
		/* 0x03 */
		registerPacket(PlayerChatCodec.class, new PlayerChatHandler());
		/* 0x04 */
		registerPacket(PlayerTimeCodec.class, new PlayerTimeHandler());
		/* 0x05 */
		registerPacket(EntityEquipmentCodec.class, new EntityEquipmentHandler());
		/* 0x06 */
		registerPacket(PlayerSpawnPositionCodec.class, new PlayerSpawnPositionHandler());
		/* 0x07 */
		registerPacket(PlayerUseEntityCodec.class, new PlayerUseEntityHandler());
		/* 0x08 */
		registerPacket(PlayerHealthCodec.class, new PlayerHealthHandler());
		/* 0x09 */
		registerPacket(PlayerRespawnCodec.class, new PlayerRespawnHandler());
		/* 0x0A */
		registerPacket(PlayerGroundCodec.class, new PlayerGroundHandler());
		/* 0x0B */
		registerPacket(PlayerPositionCodec.class, new PlayerPositionHandler());
		/* 0x0C */
		registerPacket(PlayerLookCodec.class, new PlayerLookHandler());
		/* 0x0D */
		registerPacket(PlayerPositionLookCodec.class, new PlayerPositionLookHandler());
		/* 0x0E */
		registerPacket(PlayerDiggingCodec.class, new PlayerDiggingHandler());
		/* 0x0F */
		registerPacket(PlayerBlockPlacementCodec.class, new PlayerBlockPlacementHandler());
		/* 0x10 */
		registerPacket(PlayerHeldItemChangeCodec.class, new PlayerHeldItemChangeHandler());
		/* 0x11 */
		registerPacket(PlayerBedCodec.class, new PlayerBedHandler());
		/* 0x12 */
		registerPacket(EntityAnimationCodec.class, new EntityAnimationHandler());
		/* 0x13 */
		registerPacket(EntityActionCodec.class, new EntityActionHandler());
		/* 0x14 */
		registerPacket(PlayerSpawnCodec.class, new PlayerSpawnHandler());
		/* 0x15 */ //registerPacket(EntityItemCodec.class, new EntityItemHandler()); // Removed as of 1.4.6
		/* 0x16 */
		registerPacket(PlayerCollectItemCodec.class, new PlayerCollectItemHandler());
		/* 0x17 */
		registerPacket(EntitySpawnObjectCodec.class, new EntityObjectHandler());
		/* 0x18 */
		registerPacket(EntityMobCodec.class, new EntityMobHandler());
		/* 0x19 */
		registerPacket(EntityPaintingCodec.class, new EntityPaintingHandler());
		/* 0x1A */
		registerPacket(EntityExperienceOrbCodec.class, new EntityExperienceOrbHandler());
		/* 0x1B */
		registerPacket(SteerVehicleCodec.class, new SteerVehicleHandler());
		/* 0x1C */
		registerPacket(EntityVelocityCodec.class, new EntityVelocityHandler());
		/* 0x1D */
		registerPacket(EntityDestroyCodec.class, new EntityDestroyHandler());
		/* 0x1E */
		registerPacket(EntityInitializeCodec.class, new EntityInitializeHandler()); //TODO the meaning of this packet is basically that the entity did not move/look since the last such packet. We need to implement this!
		/* 0x1F */
		registerPacket(EntityRelativePositionCodec.class, new EntityRelativePositionHandler());
		/* 0x20 */
		registerPacket(EntityYawCodec.class, new EntityYawHandler()); //TODO rename Entity Look on the minecraft protocol page
		/* 0x21 */
		registerPacket(EntityRelativePositionYawCodec.class, new EntityRelativePositionYawHandler());  //TODO same as above
		/* 0x22 */
		registerPacket(EntityTeleportCodec.class, new EntityTeleportHandler());
		/* 0x23 */
		registerPacket(EntityHeadYawCodec.class, new EntityHeadYawHandler()); //TODO same as above
		/* 0x26 */
		registerPacket(EntityStatusCodec.class, new EntityStatusHandler());
		/* 0x27 */
		registerPacket(EntityAttachEntityCodec.class, new EntityAttachEntityHandler());
		/* 0x28 */
		registerPacket(EntityMetadataCodec.class, new EntityMetadataHandler());
		/* 0x29 */
		registerPacket(EntityEffectCodec.class, new EntityEffectHandler());
		/* 0x2A */
		registerPacket(EntityRemoveEffectCodec.class, new EntityRemoveEffectHandler());
		/* 0x2B */
		registerPacket(PlayerExperienceCodec.class, new PlayerExperienceHandler());
		/* 0x2C */
		registerPacket(EntityPropertiesCodec.class, new EntityPropertiesHandler());
		/* 0x33 */
		registerPacket(ChunkDataCodec.class, new ChunkDataHandler()); //TODO rename on the minecraft protocol page
		/* 0x34 */
		registerPacket(BlockBulkCodec.class, new BlockBulkHandler());
		/* 0x35 */
		registerPacket(BlockChangeCodec.class, new BlockChangeHandler());
		/* 0x36 */
		registerPacket(BlockActionCodec.class, new BlockActionHandler());
		/* 0x37 */
		registerPacket(BlockBreakAnimationCodec.class, new BlockBreakAnimationHandler());
		/* 0x38 */
		registerPacket(ChunkBulkCodec.class, new ChunkBulkHandler());
		/* 0x3C */
		registerPacket(ExplosionCodec.class, new ExplosionHandler());
		/* 0x3D */
		registerPacket(EffectCodec.class, new EffectHandler());
		/* 0x3E */
		registerPacket(SoundEffectCodec.class, new SoundEffectHandler());
		/* 0x3F */
		registerPacket(ParticleEffectCodec.class, new ParticleEffectHandler());
		/* 0x46 */
		registerPacket(PlayerGameStateCodec.class, new PlayerGameStateHandler());
		/* 0x47 */
		registerPacket(EntityThunderboltCodec.class, new EntityThunderboltHandler()); //Minecraft protocol page -> Thunderbolt :/
		/* 0x64 */
		registerPacket(WindowOpenCodec.class, new WindowOpenHandler());
		/* 0x65 */
		registerPacket(WindowCloseCodec.class, new WindowCloseHandler());
		/* 0x66 */
		registerPacket(WindowClickCodec.class, new WindowClickHandler());
		/* 0x67 */
		registerPacket(WindowSlotCodec.class, new WindowSlotHandler());
		/* 0x68 */
		registerPacket(WindowItemsCodec.class, new WindowItemsHandler());
		/* 0x69 */
		registerPacket(WindowPropertyCodec.class, new WindowPropertyHandler()); //Update Window Property on the protocol page!
		/* 0x6A */
		registerPacket(WindowTransactionCodec.class, new WindowTransactionHandler());
		/* 0x6B */
		registerPacket(WindowCreativeActionCodec.class, new WindowCreativeActionHandler());
		/* 0x6C */
		registerPacket(WindowEnchantItemCodec.class, new WindowEnchantItemHandler());
		/* 0x82 */
		registerPacket(SignCodec.class, new SignHandler());
		/* 0x83 */
		registerPacket(EntityItemDataCodec.class, new EntityItemDataHandler());
		/* 0x84 */
		registerPacket(EntityTileDataCodec.class, new EntityTileDataHandler()); //Update Tile Entity...
		/* 0xC8 */
		registerPacket(PlayerStatisticCodec.class, new PlayerStatisticHandler());
		/* 0xC9 */
		registerPacket(PlayerListCodec.class, new PlayerListHandler());
		/* 0xCA */
		registerPacket(PlayerAbilityCodec.class, new PlayerAbilityHandler());
		/* 0xCB */
		registerPacket(PlayerTabCompleteCodec.class, new PlayerTabCompleteHandler());
		/* 0xCC */
		registerPacket(PlayerLocaleViewDistanceCodec.class, new PlayerLocaleViewDistanceHandler());
		/* 0xCD */
		registerPacket(PlayerStatusCodec.class, new PlayerStatusHandler());
		/* 0xCE */
		registerPacket(ScoreboardObjectiveCodec.class, new ScoreboardObjectiveHandler());
		/* 0xCF */
		registerPacket(ScoreboardScoreCodec.class, new ScoreboardScoreHandler());
		/* 0xD0 */
		registerPacket(ScoreboardDisplayCodec.class, new ScoreboardDisplayHandler());
		/* 0xD1 */
		registerPacket(ScoreboardTeamCodec.class, new ScoreboardTeamHandler());
		/* 0xFA */
		registerPacket(ServerPluginCodec.class, new ServerPluginHandler());
		/* 0xFF */
		registerPacket(PlayerKickCodec.class, new PlayerKickHandler());
		/* PacketFA wrapped packets */
		registerPacket(RegisterPluginChannelCodec.class, new RegisterPluginChannelMessageHandler());
		registerPacket(UnregisterPluginChannelCodec.class, new UnregisterPluginChannelMessageHandler());
		registerPacket(ChunkCacheCodec.class, new ChunkCacheHandler());
		registerPacket(CommandBlockCodec.class, new CommandBlockHandler());
		registerPacket(BeaconCodec.class, new BeaconHandler());
	}

	@Override
	public Message getKickMessage(String message) {
		return new PlayerKickMessage(message);
	}

	@Override
	public Message getIntroductionMessage(String playerName, InetSocketAddress addr) {
		throw new IllegalStateException("Player cannot be introduced with game protocol!");
	}
}
