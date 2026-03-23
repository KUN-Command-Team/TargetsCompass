package com.github.chore3.targetscompass.server.item;

import com.github.chore3.targetscompass.common.item.TargetCompassNbt;
import com.github.chore3.targetscompass.server.item.tracker.TargetMapBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber(modid = "targetscompass", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ItemCompassEvent {
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

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.player.level().isClientSide()) return;
        Player player = event.player;

        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.isEmpty() || !stack.is(Items.COMPASS)) continue;
            String targetTag = TargetCompassNbt.targetTagGet(stack);
            if (targetTag == null || targetTag.isEmpty()) continue;
            if (TargetCompassNbt.targetTagGet(stack) == null) continue;
        }
    }
}