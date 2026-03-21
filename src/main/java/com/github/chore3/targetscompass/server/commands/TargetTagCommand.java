package com.github.chore3.targetscompass.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class TargetTagCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
    dispatcher.register(Commands.literal("targettag")
        .executes(context -> {
            CommandSourceStack source = context.getSource();
            Player player = source.getPlayerOrException();
            ItemStack itemInHand = player.getMainHandItem();

            if(!itemInHand.isEmpty()){
                source.sendSuccess(() -> Component.literal(itemInHand.getHoverName().getString()), false);
            } else {
                source.sendSuccess(() -> Component.literal("none"), false);
            }
            return 1;
        })
    );
    }
}
