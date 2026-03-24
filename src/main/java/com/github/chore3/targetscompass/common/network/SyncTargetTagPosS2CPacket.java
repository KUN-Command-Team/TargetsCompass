package com.github.chore3.targetscompass.common.network;

import com.github.chore3.targetscompass.client.cache.ClientTagTargetPosCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class SyncTargetTagPosS2CPacket {
    private final String tag;
    @Nullable
    private final GlobalPos pos;

    public SyncTargetTagPosS2CPacket(String tag, @Nullable GlobalPos pos) {
        this.tag = tag;
        this.pos = pos;
    }

    public static void encode(SyncTargetTagPosS2CPacket packet, FriendlyByteBuf buf){
        buf.writeUtf(packet.tag);
        buf.writeBoolean(packet.pos != null);
        if (packet.pos != null) {
            buf.writeResourceLocation(packet.pos.dimension().location());
            buf.writeBlockPos(packet.pos.pos());
        }
    }

    public static SyncTargetTagPosS2CPacket decode(FriendlyByteBuf buf){
        String tag = buf.readUtf();
        GlobalPos globalPos = null;
        if (buf.readBoolean()){
            ResourceLocation dimLoc = buf.readResourceLocation();
            ResourceKey<Level> dim = ResourceKey.create(Registries.DIMENSION, dimLoc);
            BlockPos pos = buf.readBlockPos();
            globalPos = GlobalPos.of(dim, pos);
        }
        return new SyncTargetTagPosS2CPacket(tag, globalPos);
    }

    public static void handle(SyncTargetTagPosS2CPacket packet, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() ->
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                ClientTagTargetPosCache.getInstance().put(packet.tag, packet.pos)
            )
        );
        ctx.get().setPacketHandled(true);
    }
}
