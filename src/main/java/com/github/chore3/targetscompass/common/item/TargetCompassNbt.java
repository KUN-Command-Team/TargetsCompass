package com.github.chore3.targetscompass.common.item;

import com.github.chore3.targetscompass.Targetscompass;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class TargetCompassNbt {
    public static final String TARGET_TAG_KEY = Targetscompass.MOD_ID + ":TargetTag";
    public static final String NEAREST_TARGET_POS_KEY = Targetscompass.MOD_ID + ":NearestTargetPos";

    private TargetCompassNbt(){}

    public static void targetTagSet(ItemStack stack, String targetTag){
        if (stack == null || stack.isEmpty()) return;
        stack.getOrCreateTag().putString(TARGET_TAG_KEY, targetTag);
    }

    public static String targetTagGet(ItemStack stack){
        if (stack == null || stack.isEmpty()) return null;
        CompoundTag tag = stack.getTag();
        if (tag == null) return null;
        return tag.getString(TARGET_TAG_KEY);
    }

    public static void targetTagClear(ItemStack stack){
        if (stack == null || stack.isEmpty()) return;
        CompoundTag tag = stack.getTag();
        if (tag == null) return;
        tag.remove(TARGET_TAG_KEY);
    }
}
