package com.github.chore3.targetscompass;

import com.github.chore3.targetscompass.server.commands.TargetTagCommand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("targetscompass")
public class Targetscompass {
    public static final String MOD_ID = "targetscompass";

    @SuppressWarnings("removal")
    public Targetscompass() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event){
        TargetTagCommand.register(event.getServer().getCommands().getDispatcher());
    }
}
