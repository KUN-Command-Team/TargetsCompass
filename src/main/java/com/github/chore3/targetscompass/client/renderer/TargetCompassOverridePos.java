package com.github.chore3.targetscompass.client.renderer;

import com.github.chore3.targetscompass.common.item.TargetCompassNbt;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class TargetCompassOverridePos implements CompassItemPropertyFunction.CompassTarget {
    @Override
    public GlobalPos getPos(@NotNull ClientLevel level, ItemStack stack, @NotNull Entity entity) {
        if (stack.isEmpty()) return null;
        if (TargetCompassNbt.targetTagGet(stack) != null) {
            BlockPos pos = TargetCompassNbt.nearestTargetPosGet(stack);
            if (pos == null) return null;
            return GlobalPos.of(level.dimension(), pos);
        }

        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("LodestonePos")) {
            Optional<ResourceKey<Level>> lodestoneDim = Level.RESOURCE_KEY_CODEC
                    .parse(NbtOps.INSTANCE, tag.get("LodestoneDimension"))
                    .result();
            if (lodestoneDim.isPresent()) {
                BlockPos lodestonePos = NbtUtils.readBlockPos(tag.getCompound("LodestonePos"));
                return GlobalPos.of(lodestoneDim.get(), lodestonePos);
            }
            return null;
        }

        return GlobalPos.of(Level.OVERWORLD, level.getSharedSpawnPos());
    }
}
