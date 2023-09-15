package com.kuiprux.splatted.client.projectile;

import com.kuiprux.splatted.Splatted;
import com.kuiprux.splatted.projectile.FiredInk;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class FiredInkRenderer extends EntityRenderer<FiredInk> {
	
	private final FiredInkModel model;

	public FiredInkRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.model = new FiredInkModel(context.bakeLayer(SplattedProjectilesClient.MODEL_FIRED_INK_LAYER));
	}
	
    @Override
    public void render(FiredInk entity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(g, entity.yRotO, entity.getYRot()) - 90.0f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(g, entity.xRotO, entity.getXRot()) + 90.0f));
        int color = entity.getColor();
        int blue = color & 0xFF;
        int green = color >> 8 & 0xFF;
        int red = color >> 16 & 0xFF;
        this.model.renderToBuffer(poseStack, multiBufferSource.getBuffer(this.model.renderType(this.getTextureLocation(entity))), i, OverlayTexture.NO_OVERLAY, red, green, blue, 0xFF);
        poseStack.popPose();
        super.render(entity, f, g, poseStack, multiBufferSource, i);
    }

	@Override
	public ResourceLocation getTextureLocation(FiredInk var1) {
		return new ResourceLocation(Splatted.MOD_ID, "textures/white.png");
	}

}