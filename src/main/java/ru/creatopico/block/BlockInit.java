package ru.creatopico.block;

import io.wispforest.owo.registration.reflect.BlockRegistryContainer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class BlockInit implements BlockRegistryContainer {

	public static final OreViewerBlock BLOCK_ORE_DETECTOR = new OreViewerBlock(FabricBlockSettings.of(Material.METAL).strength(4.0f));

	@Override
	public BlockItem createBlockItem(Block block, String identifier) {
			return new BlockItem(block, new Item.Settings().group(ItemGroup.TOOLS));
	}
}

