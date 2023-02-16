package ru.creatopico.util;

import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.Config;
import ru.creatopico.ChunkHaveOreToo;

@Modmenu(modId = ChunkHaveOreToo.MODID)
@Config(name = "ChunkConfig", wrapperName = "ChunkConfig")
public class ConfigModel {
	public String playerMessageMask = "Осталось $value ед. руды";
	public int oreViewerDetectDistance = 3;
	public int stepOreDetectSoundDelay = 200;
	public int minOreDetectSoundDelay = 100;

	public int noOreDetectSoundDelay = 1000;

	public int defaultOreInChunk = 0;
}
