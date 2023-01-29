package ru.creatopico;

import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.chunk.WorldChunk;

public class ServerStateManager {
	public static HashMap<String, ServerState> worldStates = new HashMap<>();

	public static ServerState getServerState(ServerWorld world) {
		String key = "chunk_have_ore_too_" + world.getRegistryKey().getValue().toShortTranslationKey();
		if (worldStates.containsKey(key))
			return worldStates.get(key);

		PersistentStateManager persistentStateManager = world.getPersistentStateManager();

		ServerState serverState = persistentStateManager.getOrCreate(
				ServerState::createFromNbt,
				ServerState::new,
				key);

		serverState.markDirty();

		return serverState;
	}

	public static ChunkOreStorage uploadChunk(WorldChunk chunk) {
		ServerWorld world = chunk.getWorld().getServer().getWorld(chunk.getWorld().getRegistryKey());
		ServerState serverState = getServerState(world);

		ChunkOreStorage storage = serverState.chunksStorages.computeIfAbsent(
				new Vec2I(chunk.getPos()),
				serverState::loadFromCacheOrGenerate
		);

		return storage;
	}

	public static ChunkOreStorage getChunkStorage(Entity entity) {
		ServerWorld world = entity.getEntityWorld().getServer().getWorld(entity.world.getRegistryKey());
		return getServerState(world).getChunkStorage(entity.getPos());
	}
}
