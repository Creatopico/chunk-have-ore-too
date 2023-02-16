package ru.creatopico.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Timer;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import ru.creatopico.ChunkHaveOreToo;
import ru.creatopico.chunk.ChunkPit;
import ru.creatopico.chunk.ServerState;

public class OreViewerScheduler{

	protected Timer timer = new Timer();

	protected HashMap<ServerPlayerEntity, Boolean> playerStatus = new HashMap<>();

	public void query(ServerPlayerEntity entity, World world, boolean selected) {
		if (!selected) {
			playerStatus.remove(entity);
			return;
		}

		if(!playerStatus.containsKey(entity) || playerStatus.get(entity))
			tick(entity, world);
	}

	protected void tick(ServerPlayerEntity entity, World world) {
		playerStatus.put(entity, false);

		List<Vec2I> chunksWithOre = computeChunks(entity, world);
		Optional<Vec2I> nearest = chunksWithOre.stream().min((a, b) -> (a.x - b.x) + (a.z - b.z));

		int delay;
		if (nearest.isPresent()) {
			int max = Math.max(nearest.get().x, nearest.get().z);
			int volume = (ChunkHaveOreToo.CONFIG.stepOreDetectSoundDelay() - max) * 5;
			ChunkHaveOreToo.SOUND_CHANNEL.serverHandle(entity).send(new SoundPacket(volume, NetConstants.ORE_DETECT_SOUND));
			delay = ChunkHaveOreToo.CONFIG.minOreDetectSoundDelay() + max * ChunkHaveOreToo.CONFIG.stepOreDetectSoundDelay();
		}
		else delay = ChunkHaveOreToo.CONFIG.noOreDetectSoundDelay();

		timer.schedule(new OreViewerTimerTask(playerStatus, entity), delay);
	}

	protected List<Vec2I> computeChunks(ServerPlayerEntity entity, World world) {
		ServerWorld serverWorld = world.getServer().getWorld(world.getRegistryKey());

		List<Vec2I> chunksWithOre = new LinkedList<>();
		int distance = ChunkHaveOreToo.CONFIG.oreViewerDetectDistance();

		for (int x = -distance; x < distance + 1; x++)
			for (int z = -distance; z < distance + 1; z++) {
				Vec2I pos = new Vec2I(entity.getChunkPos().x + x, entity.getChunkPos().z + z);
				ChunkPit storage = ServerState.getChunkStorage(serverWorld, pos);
				if (storage != null && storage.getOreLeft() > 0)
					chunksWithOre.add(pos);
			}

		chunksWithOre.forEach((chunkPos) -> {
			chunkPos.x = Math.abs(chunkPos.x - entity.getChunkPos().x);
			chunkPos.z = Math.abs(chunkPos.z - entity.getChunkPos().z);
		});

		return chunksWithOre;
	}

}
