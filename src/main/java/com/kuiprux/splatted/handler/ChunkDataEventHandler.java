package com.kuiprux.splatted.handler;

import com.kuiprux.splatted.Splatted;

import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Splatted.MOD_ID, bus = Bus.FORGE, value = Dist.DEDICATED_SERVER)
public class ChunkDataEventHandler {

	@SubscribeEvent
	public void onLevelTick(LevelTickEvent event) {
		InkSavedData.get((ServerLevel) event.level).onLevelTick();
	}
}