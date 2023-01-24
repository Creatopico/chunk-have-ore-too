package ru.creatopico;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.ChunkPos;
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

    public HashMap<Vec2I, ChunkOreStorage> cacheOreStorage = new HashMap<>();
    public HashMap<ChunkPos, ChunkOreStorage> chunksStorages = new HashMap<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        ExampleMod.LOGGER.info("Write nbt");
        NbtCompound chunksStoragesNbt = new NbtCompound();

        chunksStorages.forEach((pos, storage) -> {
            NbtCompound chunkNbt = new NbtCompound();
            chunkNbt.putInt("ore_left", storage.oreLeft);
            chunkNbt.putInt(CHUNK_POS_X_KEY, pos.x);
            chunkNbt.putInt(CHUNK_POS_Z_KEY, pos.z);
            chunksStoragesNbt.put("chunk_" + pos.x + "_" + pos.z, chunkNbt);
        });

        cacheOreStorage.forEach((pos, storage) -> {
            NbtCompound chunkNbt = new NbtCompound();
            chunkNbt.putInt("ore_left", storage.oreLeft);
            chunkNbt.putInt(CHUNK_POS_X_KEY, pos.x);
            chunkNbt.putInt(CHUNK_POS_Z_KEY, pos.z);
            chunksStoragesNbt.put("chunk_" + pos.x + "_" + pos.z, chunkNbt);
        });

        nbt.put(CHUNK_ORE_STORAGE_KEY, chunksStoragesNbt);
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

    public static ServerState getServerState(MinecraftServer server, RegistryKey<World> world) {
        PersistentStateManager persistentStateManager = server.getWorld(world).getPersistentStateManager();

        ServerState serverState = persistentStateManager.getOrCreate(
                ServerState::createFromNbt,
                ServerState::new,
                "chunk_have_ore_too");

        serverState.markDirty();

        return serverState;
    }

    public static ChunkOreStorage getChunkOreStorageState(WorldChunk chunk) {
        ServerState serverState = getServerState(chunk.getWorld().getServer(), chunk.getWorld().getRegistryKey());
        ChunkOreStorage storage = serverState.chunksStorages.computeIfAbsent(
                chunk.getPos(),
                serverState::loadFromCacheOrGenerate
        );

        return storage;
    }

    public ChunkOreStorage loadFromCacheOrGenerate(ChunkPos pos) {
        Vec2I vecPos = new Vec2I(pos);
        if (cacheOreStorage.containsKey(vecPos))
            return cacheOreStorage.remove(vecPos);

        return new ChunkOreStorage();
    }
}
