package ru.creatopico.chunk;

import java.util.HashMap;
import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public abstract class ChunkPitGenerator {

	private final static Random random = new Random();

	private final static HashMap<RegistryKey<Biome>, Supplier<ChunkPit>> biomeMap = new HashMap<>();

	static {
		biomeMap.put(null, ChunkPitGenerator::empty);
		biomeMap.put(BiomeKeys.STONY_PEAKS, ChunkPitGenerator::stonyPeak);
		biomeMap.put(BiomeKeys.SNOWY_SLOPES, ChunkPitGenerator::snowySlopes);
	}

	public static ChunkPit generate(RegistryKey<Biome> biome) {
		if (biomeMap.containsKey(biome))
			return biomeMap.get(biome).get();
		else return biomeMap.get(null).get();
	}

	private static ChunkPit empty() {
		ChunkPit pit = new ChunkPit();
		if(random.nextDouble() > 0.0006)
			return pit;

		double oreRandom = Math.abs(random.nextGaussian()) * 30000;

		int oreCount = 1000 + (int) oreRandom;
		pit.addOre(oreCount);
		return new ChunkPit();
	}

	private static ChunkPit stonyPeak() {
		ChunkPit pit = new ChunkPit();
		if(random.nextDouble() > 0.0015)
			return pit;

		double oreRandom = Math.abs(random.nextGaussian()) * 500000;
		int oreCount = 10000 + (int) oreRandom;

		pit.addOre(oreCount);
		return pit;
	}
	private static ChunkPit snowySlopes() {
		ChunkPit pit = new ChunkPit();
		if(random.nextDouble() > 0.001)
			return pit;

		double oreRandom = Math.abs(random.nextGaussian()) * 100000;
		int oreCount = 10000 + (int) oreRandom;

		pit.addOre(oreCount);
		return pit;
	}
}
