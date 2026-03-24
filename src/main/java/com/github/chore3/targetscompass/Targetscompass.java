package com.github.chore3.targetscompass;

import com.github.chore3.targetscompass.common.network.TargetCompassNetwork;
import com.github.chore3.targetscompass.server.commands.TargetTagCommand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("targetscompass")
public class Targetscompass {
    public static final String MOD_ID = "targetscompass";

    @SuppressWarnings("removal")
    public Targetscompass() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        TargetCompassNetwork.register();
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event){
        TargetTagCommand.register(event.getServer().getCommands().getDispatcher());
    }
}
