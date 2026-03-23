package com.github.chore3.targetscompass.client.event;

import com.github.chore3.targetscompass.Targetscompass;
import com.github.chore3.targetscompass.client.renderer.TargetCompassOverridePos;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Targetscompass.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(
                    Items.COMPASS,
                    new ResourceLocation("angle"),
                    new CompassItemPropertyFunction(new TargetCompassOverridePos())
            );
        });
    }
}