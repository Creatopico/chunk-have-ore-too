package ru.creatopico;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class ExampleMod implements ModInitializer {

	public static final String MODID = "chunk_have_ore_too";
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("chot");
	public static HashMap<ItemStack, Entity> stackToEntityMap = new HashMap();

	@Override
	public void onInitialize() {



		ServerChunkEvents.CHUNK_LOAD.register((serverWorld, chunk) -> ServerStateManager.uploadChunk(chunk));

		ServerChunkEvents.CHUNK_UNLOAD.register((serverWorld, chunk) -> ServerStateManager.uploadChunk(chunk));

		LOGGER.info("Hello Fabric world!");
	}
}
