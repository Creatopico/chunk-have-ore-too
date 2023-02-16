package ru.creatopico.chunk;

import java.util.HashMap;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.WorldChunk;
import ru.creatopico.util.ChunkInfo;
import ru.creatopico.util.Vec2I;

public class ServerState extends PersistentState {

	public final static String CHUNK_ORE_STORAGE_KEY = "chunk_ore_storage";
	public final static String CHUNK_POS_X_KEY = "x";
	public final static String CHUNK_POS_Z_KEY = "z";

	public final static String CHUNK_ORE_KEY = "pit";

	public final HashMap<Vec2I, ChunkPit> chunksStorage = new HashMap<>();

	public static HashMap<String, ServerState> worldStates = new HashMap<>();

	private static ServerState getServerState(ServerWorld world) {
		String key = ChunkInfo.buildKey(world.getRegistryKey().getValue().toShortTranslationKey());

		if (worldStates.containsKey(key))
			return worldStates.get(key);

		PersistentStateManager persistentStateManager = world.getPersistentStateManager();

		ServerState serverState = persistentStateManager.getOrCreate(
				ServerState::createFromNbt,
				ServerState::new,
				key);

		serverState.markDirty();

		worldStates.put(key, serverState);
		return serverState;
	}

	public static int getChunkCountWithOre() {
		return worldStates
				.values()
				.stream()
				.map((x) -> x.chunksStorage.values().size()).reduce(Integer::sum)
				.orElse(0);
	}

	public static ChunkPit getChunkStorage(ServerWorld world, WorldChunk chunk) {
		ServerState state = getServerState(world);
		var biome =  chunk.getWorld().getBiome(chunk.getPos().getStartPos()).getKey();
		if (chunk.isEmpty())
			throw new NullPointerException("Chunk biome registry key is null!");
		return state.loadOrGenerate(new Vec2I(chunk.getPos()), biome.get());
	}

	public static ChunkPit getChunkStorage(ServerWorld world, ChunkPos pos) {
		ServerState state = getServerState(world);
		return state.load(new Vec2I(pos));
	}

	public static ChunkPit getChunkStorage(ServerWorld world, Vec2I pos) {
		ServerState state = getServerState(world);
		return state.load(pos);
	}

	public static ServerState getServerState(ChunkInfo info) {
		return worldStates.get(info.world);
	}

	public ChunkPit getChunkStorage(Vec2I pos) {
		return chunksStorage.get(pos);
	}

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        chunksStorage.forEach((pos, storage) -> {
            NbtCompound chunkNbt = new NbtCompound();
            chunkNbt.putInt(CHUNK_ORE_KEY, storage.getOreLeft());
            chunkNbt.putInt(CHUNK_POS_X_KEY, pos.x);
            chunkNbt.putInt(CHUNK_POS_Z_KEY, pos.z);
			nbt.put(pos.x + "/" + pos.z, chunkNbt);
        });

        return nbt;
    }

    private static ServerState createFromNbt(NbtCompound nbt) {
        ServerState serverState = new ServerState();
		nbt.getKeys().forEach(key -> {
            ChunkPit storage = new ChunkPit();
            NbtCompound storageData = nbt.getCompound(key);

            storage.setOreLeft(storageData.getInt(CHUNK_ORE_KEY));
            Vec2I pos = new Vec2I(storageData.getInt(CHUNK_POS_X_KEY), storageData.getInt(CHUNK_POS_Z_KEY));

            serverState.chunksStorage.put(pos, storage);
        });

        return serverState;
    }

	public ChunkPit load(Vec2I pos) {
        return chunksStorage.get(pos);
    }

	public ChunkPit loadOrGenerate(Vec2I pos, RegistryKey<Biome> biome) {
		if (chunksStorage.containsKey(pos))
			return chunksStorage.get(pos);

		ChunkPit storage = ChunkPitGenerator.generate(biome);
		chunksStorage.put(pos, storage);
		return storage;
	}


}
