package ru.creatopico.util;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.TimerTask;

public class OreViewerTimerTask extends TimerTask {

	protected final ServerPlayerEntity entity;
	protected final  HashMap<ServerPlayerEntity, Boolean> map;
	public OreViewerTimerTask(HashMap<ServerPlayerEntity, Boolean> map,  ServerPlayerEntity entity) {
		this.entity = entity;
		this.map = map;
	}

	@Override
	public void run() {
		if (map.containsKey(entity))
			map.put(entity, true);
	}
}
