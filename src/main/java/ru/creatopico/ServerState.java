package ru.creatopico;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;


import java.util.HashMap;

public class ServerState extends PersistentState {

	public final static String CHUNK_ORE_STORAGE_KEY = "chunk_ore_storage";
	public final static String CHUNK_POS_X_KEY = "chunk_pos_x";
	public final static String CHUNK_POS_Z_KEY = "chunk_pos_z";

	public static final HashMap<Vec2I, ChunkOreStorage> cacheOreStorage = new HashMap<>();
	public static final HashMap<Vec2I, ChunkOreStorage> chunksStorages = new HashMap<>();

	public ChunkOreStorage getChunkStorage(Vec3d pos) {
		int x = (int) (pos.x - pos.x % 16);
		int z = (int) (pos.z - pos.z % 16);

		return chunksStorages.get(new Vec2I(x, z));
	}

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {

        chunksStorages.forEach((pos, storage) -> {
            NbtCompound chunkNbt = new NbtCompound();
            chunkNbt.putInt("ore_left", storage.oreLeft);
            chunkNbt.putInt(CHUNK_POS_X_KEY, pos.x);
            chunkNbt.putInt(CHUNK_POS_Z_KEY, pos.z);
			nbt.put("chunk_" + pos.x + "_" + pos.z, chunkNbt);
        });

        cacheOreStorage.forEach((pos, storage) -> {
            NbtCompound chunkNbt = new NbtCompound();
            chunkNbt.putInt("ore_left", storage.oreLeft);
            chunkNbt.putInt(CHUNK_POS_X_KEY, pos.x);
            chunkNbt.putInt(CHUNK_POS_Z_KEY, pos.z);
			nbt.put("chunk_" + pos.x + "_" + pos.z, chunkNbt);
        });

        return nbt;
    }

    public static ServerState createFromNbt(NbtCompound nbt) {
        ExampleMod.LOGGER.info("Read nbt");
        ServerState serverState = new ServerState();
        NbtCompound chunkStorageNbt = nbt.getCompound(CHUNK_ORE_STORAGE_KEY);

        chunkStorageNbt.getKeys().forEach(key -> {

            ChunkOreStorage storage = new ChunkOreStorage();

            NbtCompound storageData = chunkStorageNbt.getCompound(key);

            storage.oreLeft = storageData.getInt(ChunkOreStorage.ORE_LEFT_KEY);
            Vec2I pos = new Vec2I(storageData.getInt(CHUNK_POS_X_KEY), storageData.getInt(CHUNK_POS_Z_KEY));

            serverState.cacheOreStorage.put(pos, storage);
        });

        return serverState;
    }



	public ChunkOreStorage loadFromCacheOrGenerate(Vec2I pos) {
        if (cacheOreStorage.containsKey(pos))
            return cacheOreStorage.remove(pos);

        return new ChunkOreStorage();
    }
}
