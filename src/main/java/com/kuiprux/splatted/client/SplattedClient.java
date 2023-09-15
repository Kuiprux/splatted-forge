package com.kuiprux.splatted.client;

import java.util.Random;

import com.kuiprux.splatted.Splatted;
import com.kuiprux.splatted.client.projectile.FiredInkModel;
import com.kuiprux.splatted.client.projectile.FiredInkRenderer;
import com.kuiprux.splatted.client.projectile.SplattedProjectilesClient;
import com.kuiprux.splatted.projectile.SplattedProjectiles;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderBlockScreenEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Splatted.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class SplattedClient {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
    	 event.registerLayerDefinition(SplattedProjectilesClient.MODEL_FIRED_INK_LAYER, FiredInkModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
    	event.registerEntityRenderer(SplattedProjectiles.FIRED_INK.get(), FiredInkRenderer::new);
    }
    
    Random r = new Random();

	@SubscribeEvent
	public void onBlockOverlay(RenderBlockScreenEffectEvent event) {
			System.out.println(event.getBlockPos());
	}
}
