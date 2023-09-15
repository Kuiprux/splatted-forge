package com.kuiprux.splatted.client.handler;

import java.util.Random;

import com.kuiprux.splatted.Splatted;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderBlockScreenEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

//@Mod.EventBusSubscriber(modid = Splatted.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class BlockOverlayEventHandler {
	Random r = new Random();

	@SubscribeEvent
	public void onBlockOverlay(RenderBlockScreenEffectEvent event) {
			System.out.println(event.getBlockPos());
			Block
	}
}
