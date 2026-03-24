package com.github.chore3.targetscompass.server.item.tracker;

import com.github.chore3.targetscompass.common.item.TargetCompassNbt;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.*;

public final class TargetMapBuilder {
    private static final TargetMapBuilder INSTANCE = new TargetMapBuilder();

    private final Map<String, List<Player>> targetMap = new HashMap<>();

    private TargetMapBuilder() {}

    public static TargetMapBuilder getInstance() {
        return INSTANCE;
    }

    public Map<String, List<Player>> getTargetMap() {
        return Collections.unmodifiableMap(targetMap);
    }

    public void rebuild(MinecraftServer server){
        if (server == null) return;
        targetMap.clear();
        tagUpdate(server);
        playerUpdate(server);
    }
    
    private void tagUpdate(MinecraftServer server){
        PlayerList list = server.getPlayerList();
        for (ServerPlayer player : list.getPlayers()){
            Set<String> tags = getPlayerCompassTargetTags(player);
            if (tags.isEmpty()) continue;
            for(String tag : tags){
                if(targetMap.containsKey(tag)) continue;
                targetMap.put(tag, new ArrayList<>());
            }
        }
    }

    private void playerUpdate(MinecraftServer server){
        PlayerList list = server.getPlayerList();
        for (ServerPlayer player : list.getPlayers()){
            for (String tag : targetMap.keySet()){
                if(player.getTags().contains(tag)){
                    targetMap.get(tag).add(player);
                }
            }
        }
    }

    private Set<String> getPlayerCompassTargetTags(ServerPlayer player) {
        Set<String> compassTargetTags = new HashSet<>();
        if (player == null) return compassTargetTags;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.isEmpty() || !stack.is(Items.COMPASS)) continue;
            String targetTag = TargetCompassNbt.targetTagGet(stack);
            if (targetTag == null || targetTag.isEmpty()) continue;
            compassTargetTags.add(targetTag);
        }
        return compassTargetTags;
    }
}
