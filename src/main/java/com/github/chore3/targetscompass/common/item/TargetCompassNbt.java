package com.github.chore3.targetscompass.common.item;

import com.github.chore3.targetscompass.Targetscompass;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class TargetCompassNbt {
    public static final String ROOT_KEY = Targetscompass.MOD_ID;
    public static final String TARGET_TAG_KEY = "TargetTag";

    private TargetCompassNbt(){}

    private static CompoundTag getOrCreateRootTag(ItemStack stack) {
        return stack.getOrCreateTag().getCompound(ROOT_KEY).isEmpty()
                ? createRootTag(stack)
                : stack.getOrCreateTag().getCompound(ROOT_KEY);
    }

    private static CompoundTag createRootTag(ItemStack stack) {
        CompoundTag root = new CompoundTag();
        stack.getOrCreateTag().put(ROOT_KEY, root);
        return root;
    }

    private static CompoundTag getRootTag(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return null;
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(ROOT_KEY, 10)) return null; // 10 \= CompoundTag
        return tag.getCompound(ROOT_KEY);
    }

    // targetTag
    public static void targetTagSet(ItemStack stack, String targetTag){
        if (stack == null || stack.isEmpty()) return;
        CompoundTag root = getOrCreateRootTag(stack);
        root.putString(TARGET_TAG_KEY, targetTag);
        stack.getOrCreateTag().put(ROOT_KEY, root);
    }

    public static String targetTagGet(ItemStack stack){
        CompoundTag root = getRootTag(stack);
        if (root == null || !root.contains(TARGET_TAG_KEY)) return null;
        return root.getString(TARGET_TAG_KEY);
    }
}
