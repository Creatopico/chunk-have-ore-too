package ru.creatopico.command;


import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import ru.creatopico.chunk.ServerState;
public abstract class CommandContainer {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
		var chot = literal("chot").requires(source -> source.hasPermissionLevel(4));

		dispatcher.register(chot.then(literal("add").then(argument("count", IntegerArgumentType.integer()).executes(
						source -> {
							ServerState
									.getChunkStorage(source.getSource().getWorld(), source.getSource().getEntity().getChunkPos())
									.addOre(getInteger(source, "count"));
							return 1;
						}
				))));

		dispatcher.register(chot.then(literal("set").then(argument("count", IntegerArgumentType.integer()).executes(
				source -> {
					ServerState
							.getChunkStorage(source.getSource().getWorld(), source.getSource().getEntity().getChunkPos())
							.setOreLeft(getInteger(source, "count"));
					return 1;
				}
		))));

		dispatcher.register(chot.then(literal("take").then(argument("count", IntegerArgumentType.integer()).executes(
				source -> {
					ServerState
							.getChunkStorage(source.getSource().getWorld(), source.getSource().getEntity().getChunkPos())
							.tryGetOre(getInteger(source, "count"));
					return 1;
				}
		))));

		dispatcher.register(chot.then(literal("take").then(argument("count", IntegerArgumentType.integer()).executes(
				source -> {
					ServerState
							.getChunkStorage(source.getSource().getWorld(), source.getSource().getEntity().getChunkPos())
							.tryGetOre(getInteger(source, "count"));
					return 1;
				}
		))));

		dispatcher.register(chot.then(literal("count").executes(
				source -> {
					int chunkCount = ServerState.getChunkCountWithOre();
					source.getSource().getPlayer().sendMessage(Text.literal("All chunks with ore " + chunkCount), true);
					return 1;
				}
		)));
	}
}
