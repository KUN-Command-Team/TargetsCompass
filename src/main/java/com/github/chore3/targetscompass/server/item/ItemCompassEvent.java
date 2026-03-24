package com.github.chore3.targetscompass.server.item;

import com.github.chore3.targetscompass.common.item.TargetCompassNbt;
import com.github.chore3.targetscompass.common.network.SyncTargetTagPosS2CPacket;
import com.github.chore3.targetscompass.common.network.TargetCompassNetwork;
import com.github.chore3.targetscompass.server.item.tracker.TargetCompassUpdater;
import com.github.chore3.targetscompass.server.item.tracker.TargetMapBuilder;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = "targetscompass", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ItemCompassEvent {
    // 共有のターゲット一覧を更新する
    private static final int REBUILD_INTERVAL = 5;

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        long gameTime = server.overworld().getGameTime();
        if (gameTime % REBUILD_INTERVAL == 0) {
            TargetMapBuilder.getInstance().rebuild(server);
        }
    }

    // 各プレイヤーのコンパスが向く法学を更新する
    private static final int PACKET_SEND_INTERVAL = 20;
    private static final Map<ServerPlayer, Long> lastPacketSendTime = new WeakHashMap<>();

    private static final Map<ServerPlayer, Map<String, GlobalPos>> lastSentPos = new WeakHashMap<>();
    private static final int TARGET_SYNC_DISTANCE_BLOCKS = 2;
    private static final int TARGET_SYNC_DISTANCE_SQ = TARGET_SYNC_DISTANCE_BLOCKS * TARGET_SYNC_DISTANCE_BLOCKS;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.player.level().isClientSide()) return;

        ServerPlayer player = (ServerPlayer) event.player;
        long currentTime = player.level().getGameTime();
        Long lastTime = lastPacketSendTime.getOrDefault(player, 0L);
        if (currentTime - lastTime < PACKET_SEND_INTERVAL) {
            return;
        }
        lastPacketSendTime.put(player, currentTime);

        Map<String, GlobalPos> nearestTargetPosByTag = new HashMap<>();
        Map<String, GlobalPos> lastSentPosMap = lastSentPos.getOrDefault(player, new HashMap<>());
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.isEmpty() || !stack.is(Items.COMPASS)) continue;

            String targetTag = TargetCompassNbt.targetTagGet(stack);
            if (targetTag == null || targetTag.isEmpty()) continue;

            if (!nearestTargetPosByTag.containsKey(targetTag)) {
                nearestTargetPosByTag.put(targetTag,
                TargetCompassUpdater.findNearestTargetPos(stack, player, player.level()));
            }

            GlobalPos nearestTargetPos = nearestTargetPosByTag.get(targetTag);
            if (shouldSync(nearestTargetPos, lastSentPosMap.get(targetTag))) {
                TargetCompassNetwork.CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new SyncTargetTagPosS2CPacket(targetTag, nearestTargetPos)
                );
                lastSentPosMap.put(targetTag, nearestTargetPos);
            }
        }
        lastSentPos.put(player, lastSentPosMap);
    }

    private static boolean shouldSync(@Nullable GlobalPos currentPos, @Nullable GlobalPos lastSentPos) {
        if (lastSentPos == null) return currentPos != null;
        if (currentPos == null) return true;
        if (!currentPos.dimension().equals(lastSentPos.dimension())) return true;
        return currentPos.pos().distSqr(lastSentPos.pos()) >= TARGET_SYNC_DISTANCE_SQ;
    }
}