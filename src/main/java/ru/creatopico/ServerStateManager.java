package ru.creatopico;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.chunk.WorldChunk;

public class ServerStateManager extends PersistentState {


	public static HashMap<String, ServerState> worldStates = new HashMap<>();

	public static ServerState getServerState(ServerWorld world) {
		String key =  buildKey(world.getRegistryKey().getValue().toShortTranslationKey());
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

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {

		worldStates.forEach ((tag, state) -> {
			NbtCompound dimState = new NbtCompound();
			state.writeNbt(dimState);
			nbt.put(buildKey(tag), dimState);
		});

		return nbt;
	}

	public static ServerStateManager createFromNbt(NbtCompound nbt) {
		ServerStateManager stateManager = new ServerStateManager();

		Set<String> compounds = nbt.getKeys();

		compounds = compounds
				.stream()
				.filter(x -> x.contains(ServerState.CHUNK_ORE_STORAGE_KEY))
				.collect(Collectors.toSet());

		compounds
				.stream()
				.forEach((dimensionKey) ->
						worldStates.put(dimensionKey, ServerState.createFromNbt(nbt.getCompound(dimensionKey)))
				);

		return stateManager;
	}

	private static String buildKey(String key) {
		return ServerState.chunksStorages + "_" + key;
	}
}
