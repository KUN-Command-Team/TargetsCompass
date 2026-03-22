package com.github.chore3.targetscompass.server.commands;

import com.github.chore3.targetscompass.common.item.TargetCompassNbt;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class TargetTagCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        var targetTag = Commands.literal("targettag")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("name", StringArgumentType.word())
                .executes(context -> applyTargetTag(context, StringArgumentType.getString(context, "name"))));
        dispatcher.register(targetTag);
    }

    private static int applyTargetTag(CommandContext<CommandSourceStack> context, String message) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        Player player = source.getPlayerOrException();
        ItemStack itemInHand = player.getMainHandItem();
        TargetCompassNbt.targetTagSet(itemInHand, message);
        return 1;
    }
}
