/*
    Breadity
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Breadity
    Licence: GNU LGPLv3
*/

package com.mclegoman.mclm_breadity.mixin.client;

import com.mclegoman.mclm_breadity.client.BreadFeatureRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements FeatureRendererContext<T, M> {
	@Shadow protected abstract boolean addFeature(FeatureRenderer<T, M> feature);

	@Shadow @Final protected List<FeatureRenderer<T, M>> features;

	@Shadow protected abstract void scale(T entity, MatrixStack matrices, float amount);

	@Shadow protected abstract void setupTransforms(T entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta, float scale);

	@Shadow protected abstract float getAnimationProgress(T entity, float tickDelta);

	@Shadow protected M model;

	@Shadow protected abstract float getHandSwingProgress(T entity, float tickDelta);

	protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
		super(ctx);
	}
	@Inject(method = "<init>", at = @At("TAIL"))
	private void breadity$init(EntityRendererFactory.Context context, EntityModel model, float shadowRadius, CallbackInfo ci) {
		this.addFeature(new BreadFeatureRenderer<>((LivingEntityRenderer) (Object) this, context.getModelLoader(), context.getHeldItemRenderer()));
	}
	@Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
	private void breadity$render(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		matrixStack.push();
		this.model.handSwingProgress = this.getHandSwingProgress(livingEntity, g);
		this.model.riding = livingEntity.hasVehicle();
		this.model.child = livingEntity.isBaby();
		float h = MathHelper.lerpAngleDegrees(g, livingEntity.prevBodyYaw, livingEntity.bodyYaw);
		float j = MathHelper.lerpAngleDegrees(g, livingEntity.prevHeadYaw, livingEntity.headYaw);
		float k = j - h;
		float l;
		if (livingEntity.hasVehicle()) {
			Entity var11 = livingEntity.getVehicle();
			if (var11 instanceof LivingEntity) {
				LivingEntity livingEntity2 = (LivingEntity)var11;
				h = MathHelper.lerpAngleDegrees(g, livingEntity2.prevBodyYaw, livingEntity2.bodyYaw);
				k = j - h;
				l = MathHelper.wrapDegrees(k);
				if (l < -85.0F) {
					l = -85.0F;
				}

				if (l >= 85.0F) {
					l = 85.0F;
				}

				h = j - l;
				if (l * l > 2500.0F) {
					h += l * 0.2F;
				}

				k = j - h;
			}
		}

		float m = MathHelper.lerp(g, livingEntity.prevPitch, livingEntity.getPitch());
		if (LivingEntityRenderer.shouldFlipUpsideDown(livingEntity)) {
			m *= -1.0F;
			k *= -1.0F;
		}

		k = MathHelper.wrapDegrees(k);
		float n;
		if (livingEntity.isInPose(EntityPose.SLEEPING)) {
			Direction direction = livingEntity.getSleepingDirection();
			if (direction != null) {
				n = livingEntity.getEyeHeight(EntityPose.STANDING) - 0.1F;
				matrixStack.translate((float)(-direction.getOffsetX()) * n, 0.0F, (float)(-direction.getOffsetZ()) * n);
			}
		}

		l = livingEntity.getScale();
		matrixStack.scale(l, l, l);
		n = this.getAnimationProgress(livingEntity, g);
		this.setupTransforms(livingEntity, matrixStack, n, h, g, l);
		matrixStack.scale(-1.0F, -1.0F, 1.0F);
		this.scale(livingEntity, matrixStack, g);
		matrixStack.translate(0.0F, -1.501F, 0.0F);
		float o = 0.0F;
		float p = 0.0F;
		if (!livingEntity.hasVehicle() && livingEntity.isAlive()) {
			o = livingEntity.limbAnimator.getSpeed(g);
			p = livingEntity.limbAnimator.getPos(g);
			if (livingEntity.isBaby()) {
				p *= 3.0F;
			}

			if (o > 1.0F) {
				o = 1.0F;
			}
		}

		if (!livingEntity.isSpectator()) {
			Iterator var25 = this.features.iterator();

			while(var25.hasNext()) {
				FeatureRenderer<T, M> featureRenderer = (FeatureRenderer)var25.next();
				featureRenderer.render(matrixStack, vertexConsumerProvider, i, livingEntity, p, o, g, n, k, m);
			}
		}

		matrixStack.pop();
		super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
		ci.cancel();
	}
}