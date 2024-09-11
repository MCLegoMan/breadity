/*
    Breadity
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Breadity
    Licence: GNU LGPLv3
*/

package com.mclegoman.mclm_breadity.mixin.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.render.entity.feature.EyesFeatureRenderer.class)
public abstract class EyesFeatureRendererMixin<T extends Entity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
	public EyesFeatureRendererMixin(FeatureRendererContext<T, M> context) {
		super(context);
	}
	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void breadity$render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch, CallbackInfo ci) {
		ci.cancel();
	}
}