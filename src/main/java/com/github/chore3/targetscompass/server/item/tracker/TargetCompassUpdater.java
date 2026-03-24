package com.github.chore3.targetscompass.server.item.tracker;

import com.github.chore3.targetscompass.common.item.TargetCompassNbt;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;

public class TargetCompassUpdater {
    private TargetCompassUpdater() {}

    public static GlobalPos findNearestTargetPos(ItemStack stack, Player holder, Level level) {
        if (stack == null || stack.isEmpty()) return null;
        String targetTag = TargetCompassNbt.targetTagGet(stack);
        return findNearestTargetPosByTag(holder, level, targetTag);
    }

    public static GlobalPos findNearestTargetPosByTag(Player holder, Level level, String tag) {
        if (tag == null || tag.isEmpty()) return null;

        Map<String, List<Player>> targetMap = TargetMapBuilder.getInstance().getTargetMap();
        List<Player> candidates = targetMap.get(tag);
        if (candidates == null || candidates.isEmpty()) return null;

        Player nearest = null;
        double nearestDistSq = Double.MAX_VALUE;
        for (Player candidate : candidates) {
            if (candidate.getUUID().equals(holder.getUUID())) continue;
            if (!candidate.level().dimension().equals(level.dimension())) continue;
            double distSq = holder.distanceToSqr(candidate);
            if (distSq < nearestDistSq) {
                nearestDistSq = distSq;
                nearest = candidate;
            }
        }

        if (nearest == null) {
            return null;
        }

        return GlobalPos.of(level.dimension(), nearest.blockPosition());
    }
}
