package ru.creatopico.util;

import net.minecraft.world.World;
import ru.creatopico.chunk.ServerState;

public class ChunkInfo {
	public Vec2I pos;
	public String world;

	public ChunkInfo(Vec2I pos, World world) {
		this.pos = pos;
		this.world = buildKey(world.getRegistryKey().getValue().toShortTranslationKey());
	}

	public static String buildKey(String key) {
		return ServerState.CHUNK_ORE_STORAGE_KEY + "_" + key;
	}
}
