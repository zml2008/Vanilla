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

import org.spout.api.protocol.CodecLookupService;
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
import org.spout.vanilla.protocol.game.codec.player.conn.PlayerJoinGameCodec;
import org.spout.vanilla.protocol.game.codec.player.conn.PlayerKickCodec;
import org.spout.vanilla.protocol.game.codec.player.conn.PlayerListCodec;
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
import org.spout.vanilla.protocol.game.handler.player.conn.PlayerJoinGameHandler;
import org.spout.vanilla.protocol.game.handler.player.conn.PlayerKickHandler;
import org.spout.vanilla.protocol.game.handler.player.conn.PlayerListHandler;
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
		registerPacket(PlayerPingCodec.class, new PlayerPingHandler());
		registerPacket(PlayerJoinGameCodec.class, new PlayerJoinGameHandler(), CodecLookupService.ProtocolSide.CLIENT);
		registerPacket(PlayerChatCodec.class, new PlayerChatHandler());
		registerPacket(PlayerTimeCodec.class, new PlayerTimeHandler(), CodecLookupService.ProtocolSide.CLIENT);
		registerPacket(EntityEquipmentCodec.class, new EntityEquipmentHandler());
		registerPacket(PlayerSpawnPositionCodec.class, new PlayerSpawnPositionHandler());
		//registerPacket(PlayerUseEntityCodec.class, new PlayerUseEntityHandler(), CodecLookupService.ProtocolSide.SERVER);
		registerPacket(PlayerHealthCodec.class, new PlayerHealthHandler());
		registerPacket(PlayerRespawnCodec.class, new PlayerRespawnHandler());
		//registerPacket(PlayerGroundCodec.class, new PlayerGroundHandler(), CodecLookupService.ProtocolSide.SERVER);
		//registerPacket(PlayerPositionCodec.class, new PlayerPositionHandler(), CodecLookupService.ProtocolSide.SERVER);
		//registerPacket(PlayerLookCodec.class, new PlayerLookHandler(), CodecLookupService.ProtocolSide.SERVER);
		registerPacket(PlayerPositionLookCodec.class, new PlayerPositionLookHandler());
		//registerPacket(PlayerDiggingCodec.class, new PlayerDiggingHandler(), CodecLookupService.ProtocolSide.SERVER);
		//registerPacket(PlayerBlockPlacementCodec.class, new PlayerBlockPlacementHandler(), CodecLookupService.ProtocolSide.SERVER);
		registerPacket(PlayerHeldItemChangeCodec.class, new PlayerHeldItemChangeHandler());
		registerPacket(PlayerBedCodec.class, new PlayerBedHandler());
		registerPacket(EntityAnimationCodec.class, new EntityAnimationHandler());
		registerPacket(PlayerSpawnCodec.class, new PlayerSpawnHandler(), CodecLookupService.ProtocolSide.CLIENT);
		//registerPacket(EntityActionCodec.class, new EntityActionHandler());
		 //registerPacket(EntityItemCodec.class, new EntityItemHandler()); // Removed as of 1.4.6
		registerPacket(PlayerCollectItemCodec.class, new PlayerCollectItemHandler());
		registerPacket(EntitySpawnObjectCodec.class, new EntityObjectHandler());
		registerPacket(EntityMobCodec.class, new EntityMobHandler());
		registerPacket(EntityPaintingCodec.class, new EntityPaintingHandler());
		registerPacket(EntityExperienceOrbCodec.class, new EntityExperienceOrbHandler());
		//registerPacket(SteerVehicleCodec.class, new SteerVehicleHandler());
		registerPacket(EntityVelocityCodec.class, new EntityVelocityHandler());
		registerPacket(EntityDestroyCodec.class, new EntityDestroyHandler());
		registerPacket(EntityInitializeCodec.class, new EntityInitializeHandler()); //TODO the meaning of this packet is basically that the entity did not move/look since the last such packet. We need to implement this!
		registerPacket(EntityRelativePositionCodec.class, new EntityRelativePositionHandler());
		registerPacket(EntityYawCodec.class, new EntityYawHandler()); //TODO rename Entity Look on the minecraft protocol page
		registerPacket(EntityRelativePositionYawCodec.class, new EntityRelativePositionYawHandler());  //TODO same as above
		registerPacket(EntityTeleportCodec.class, new EntityTeleportHandler());
		registerPacket(EntityHeadYawCodec.class, new EntityHeadYawHandler()); //TODO same as above
		registerPacket(EntityStatusCodec.class, new EntityStatusHandler());
		registerPacket(EntityAttachEntityCodec.class, new EntityAttachEntityHandler());
		registerPacket(EntityMetadataCodec.class, new EntityMetadataHandler());
		registerPacket(EntityEffectCodec.class, new EntityEffectHandler());
		registerPacket(EntityRemoveEffectCodec.class, new EntityRemoveEffectHandler());
		registerPacket(PlayerExperienceCodec.class, new PlayerExperienceHandler());
		registerPacket(EntityPropertiesCodec.class, new EntityPropertiesHandler());
		registerPacket(ChunkDataCodec.class, new ChunkDataHandler()); //TODO rename on the minecraft protocol page
		registerPacket(BlockBulkCodec.class, new BlockBulkHandler());
		registerPacket(BlockChangeCodec.class, new BlockChangeHandler());
		registerPacket(BlockActionCodec.class, new BlockActionHandler());
		registerPacket(BlockBreakAnimationCodec.class, new BlockBreakAnimationHandler());
		registerPacket(ChunkBulkCodec.class, new ChunkBulkHandler());
		registerPacket(ExplosionCodec.class, new ExplosionHandler());
		registerPacket(EffectCodec.class, new EffectHandler());
		registerPacket(SoundEffectCodec.class, new SoundEffectHandler());
		registerPacket(ParticleEffectCodec.class, new ParticleEffectHandler());
		registerPacket(PlayerGameStateCodec.class, new PlayerGameStateHandler());
		registerPacket(EntityThunderboltCodec.class, new EntityThunderboltHandler()); //Minecraft protocol page -> Thunderbolt :/
		registerPacket(WindowOpenCodec.class, new WindowOpenHandler());
		registerPacket(WindowCloseCodec.class, new WindowCloseHandler());
		registerPacket(WindowClickCodec.class, new WindowClickHandler());
		registerPacket(WindowSlotCodec.class, new WindowSlotHandler());
		registerPacket(WindowItemsCodec.class, new WindowItemsHandler());
		registerPacket(WindowPropertyCodec.class, new WindowPropertyHandler()); //Update Window Property on the protocol page!
		registerPacket(WindowTransactionCodec.class, new WindowTransactionHandler());
		registerPacket(WindowCreativeActionCodec.class, new WindowCreativeActionHandler());
		registerPacket(WindowEnchantItemCodec.class, new WindowEnchantItemHandler());
		registerPacket(SignCodec.class, new SignHandler());
		registerPacket(EntityItemDataCodec.class, new EntityItemDataHandler());
		registerPacket(EntityTileDataCodec.class, new EntityTileDataHandler()); //Update Tile Entity...
		registerPacket(PlayerStatisticCodec.class, new PlayerStatisticHandler());
		registerPacket(PlayerListCodec.class, new PlayerListHandler());
		registerPacket(PlayerAbilityCodec.class, new PlayerAbilityHandler());
		registerPacket(PlayerTabCompleteCodec.class, new PlayerTabCompleteHandler());
		registerPacket(PlayerLocaleViewDistanceCodec.class, new PlayerLocaleViewDistanceHandler());
		registerPacket(PlayerStatusCodec.class, new PlayerStatusHandler());
		registerPacket(ScoreboardObjectiveCodec.class, new ScoreboardObjectiveHandler());
		registerPacket(ScoreboardScoreCodec.class, new ScoreboardScoreHandler());
		registerPacket(ScoreboardDisplayCodec.class, new ScoreboardDisplayHandler());
		registerPacket(ScoreboardTeamCodec.class, new ScoreboardTeamHandler());
		registerPacket(ServerPluginCodec.class, new ServerPluginHandler());
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
