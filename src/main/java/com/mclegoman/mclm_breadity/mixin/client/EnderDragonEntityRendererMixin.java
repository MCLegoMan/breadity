/*
    Breadity
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Breadity
    Licence: GNU LGPLv3
*/

package com.mclegoman.mclm_breadity.mixin.client;

import com.mclegoman.mclm_breadity.config.BreadityConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EnderDragonEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderDragonEntityRenderer.class)
public abstract class EnderDragonEntityRendererMixin extends EntityRenderer<EnderDragonEntity> {
	protected EnderDragonEntityRendererMixin(EntityRendererFactory.Context ctx) {
		super(ctx);
	}
	@Inject(method = "render(Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
	private void breadity$render(EnderDragonEntity entity, float f, float g, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		ItemStack itemStack = BreadityConfig.getItem();
		if (!itemStack.isEmpty()) {
			matrices.push();
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-(float)entity.getSegmentProperties(7, g)[0]));
			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(((float)(entity.getSegmentProperties(5, g)[1] - entity.getSegmentProperties(10, g)[1])) * 10.0F));
			matrices.scale(entity.getScale() * 16.0F, entity.getScale() * 16.0F, entity.getScale() * 16.0F);
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
			MinecraftClient.getInstance().getItemRenderer().renderItem(entity, itemStack, ModelTransformationMode.HEAD, false, matrices, vertexConsumers, entity.getWorld(), light, OverlayTexture.DEFAULT_UV, entity.getId() + ModelTransformationMode.HEAD.ordinal());
			matrices.pop();
		}
		ci.cancel();
	}
}