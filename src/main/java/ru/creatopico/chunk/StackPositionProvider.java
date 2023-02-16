package ru.creatopico.chunk;

import net.minecraft.item.ItemStack;
import ru.creatopico.util.ChunkInfo;

import java.util.HashMap;

public abstract class StackPositionProvider {
	private final static HashMap<ItemStack, ChunkInfo> stackToEntityMap = new HashMap<>();

	public static ChunkInfo get(ItemStack stack) {
		return stackToEntityMap.remove(stack);
	}

	public static void put(ItemStack stack, ChunkInfo info) {
		stackToEntityMap.put(stack, info);
	}

}
