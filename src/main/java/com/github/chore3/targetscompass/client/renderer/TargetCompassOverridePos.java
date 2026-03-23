package com.github.chore3.targetscompass.client.renderer;

import com.github.chore3.targetscompass.common.item.TargetCompassNbt;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TargetCompassOverridePos implements CompassItemPropertyFunction.CompassTarget {
    @Override
    public GlobalPos getPos(@NotNull ClientLevel level, ItemStack stack, @NotNull Entity entity) {
        if (stack.isEmpty()) return null;

        BlockPos pos = TargetCompassNbt.nearestTargetPosGet(stack);
        if (pos == null) return null;

        return GlobalPos.of(level.dimension(), pos);
    }
}
