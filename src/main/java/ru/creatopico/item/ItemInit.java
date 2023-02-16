package ru.creatopico.item;

import io.wispforest.owo.registration.reflect.ItemRegistryContainer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

public class ItemInit implements ItemRegistryContainer {

	public static final OreViewer ORE_DETECTOR = new OreViewer(new FabricItemSettings());

}
