package com.github.chore3.targetscompass.common.network;

import com.github.chore3.targetscompass.Targetscompass;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class TargetCompassNetwork {
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(Targetscompass.MOD_ID, "main"),
        () -> "1", "1"::equals, "1"::equals
    );

    public static void register() {
        CHANNEL.registerMessage(
            0,
            SyncTargetTagPosS2CPacket.class,
            SyncTargetTagPosS2CPacket::encode,
            SyncTargetTagPosS2CPacket::decode,
            SyncTargetTagPosS2CPacket::handle,
            Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
    }
}