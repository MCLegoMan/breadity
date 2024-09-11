package com.mclegoman.mclm_breadity.client;

import com.mclegoman.mclm_breadity.config.BreadityConfig;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;

public class BreadFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
	private final float scaleX;
	private final float scaleY;
	private final float scaleZ;
	private final HeldItemRenderer heldItemRenderer;
	public BreadFeatureRenderer(FeatureRendererContext<T, M> context, EntityModelLoader loader, HeldItemRenderer heldItemRenderer) {
		this(context, loader, 1.0F, 1.0F, 1.0F, heldItemRenderer);
	}
	public BreadFeatureRenderer(FeatureRendererContext<T, M> context, EntityModelLoader loader, float scaleX, float scaleY, float scaleZ, HeldItemRenderer heldItemRenderer) {
		super(context);
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.scaleZ = scaleZ;
		this.heldItemRenderer = heldItemRenderer;
	}
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		ItemStack itemStack = BreadityConfig.getItem();
		if (!itemStack.isEmpty()) {
			matrices.push();
			matrices.scale(entity.getScale() * this.scaleX, entity.getScale() * this.scaleY, entity.getScale() * this.scaleZ);
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
			this.heldItemRenderer.renderItem(entity, itemStack, ModelTransformationMode.HEAD, false, matrices, vertexConsumers, light);
			matrices.pop();
		}
	}
}
