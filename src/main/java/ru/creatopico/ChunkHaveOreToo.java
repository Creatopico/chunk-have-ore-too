package ru.creatopico;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wispforest.owo.network.OwoNetChannel;
import io.wispforest.owo.registration.reflect.FieldRegistrationHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import ru.creatopico.block.BlockInit;
import ru.creatopico.chunk.ServerState;
import ru.creatopico.command.CommandContainer;
import ru.creatopico.item.ItemInit;
import ru.creatopico.util.ChunkConfig;
import ru.creatopico.util.SoundPacket;


public class ChunkHaveOreToo implements ModInitializer {

	public static final String MODID = "chunk_have_ore_too";
	public static final Logger LOGGER = LoggerFactory.getLogger("chot");
	public static final ChunkConfig CONFIG = ChunkConfig.createAndLoad();

	public static final OwoNetChannel SOUND_CHANNEL = OwoNetChannel.create(new Identifier(MODID, "sounds"));


	@Override
	public void onInitialize() {
		ServerChunkEvents.CHUNK_LOAD.register(ServerState::getChunkStorage);
		ServerChunkEvents.CHUNK_UNLOAD.register(ServerState::getChunkStorage);

		FieldRegistrationHandler.register(ItemInit.class, MODID, false);
		FieldRegistrationHandler.register(BlockInit.class, MODID, false);

		SOUND_CHANNEL.registerClientbound(SoundPacket.class, ((message, access) -> {
			access.player().playSound(new SoundEvent(message.aMinecraftClass()), message.volume(), 100);
		}));

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> CommandContainer.register(dispatcher));
	}


}
