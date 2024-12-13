package com.smushytaco.colored_lightning.mixins;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.smushytaco.colored_lightning.ColoredLightning;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LightningEntityRenderer;
import net.minecraft.client.render.entity.state.LightningEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.random.Random;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(LightningEntityRenderer.class)
public class LightningEntityRendererColor {
    @Unique
    private Random random = null;
    @Unique
    private float currentRed = 0.0F;
    @Unique
    private float currentGreen = 0.0F;
    @Unique
    private float currentBlue = 0.0F;
    @Inject(method = "render(Lnet/minecraft/client/render/entity/state/LightningEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"))
    private void hookRenderOne(LightningEntityRenderState lightningEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        random = Random.create(lightningEntityRenderState.seed);
        if (ColoredLightning.INSTANCE.getConfig().getEnableColoredLightning() && !ColoredLightning.INSTANCE.getConfig().getChangeColorForEachSegment()) {
            currentRed = 0.5F + random.nextFloat() * 0.5F;
            currentGreen = 0.5F + random.nextFloat() * 0.5F;
            currentBlue = 0.5F + random.nextFloat() * 0.5F;
        }
    }
    @WrapOperation(method = "render(Lnet/minecraft/client/render/entity/state/LightningEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LightningEntityRenderer;drawBranch(Lorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumer;FFIFFFFFFFZZZZ)V"))
    private void hookRenderTwo(Matrix4f matrix, VertexConsumer buffer, float x1, float z1, int y, float x2, float z2, float red, float green, float blue, float offset2, float offset1, boolean shiftEast1, boolean shiftSouth1, boolean shiftEast2, boolean shiftSouth2, Operation<Void> original) {
        if (!ColoredLightning.INSTANCE.getConfig().getEnableColoredLightning()) {
            original.call(matrix, buffer, x1, z1, y, x2, z2, red, green, blue, offset2, offset1, shiftEast1, shiftSouth1, shiftEast2, shiftSouth2);
            return;
        }
        if (ColoredLightning.INSTANCE.getConfig().getChangeColorForEachSegment() && !shiftEast1 && !shiftSouth1 && shiftEast2 && !shiftSouth2) {
            currentRed = 0.5F + random.nextFloat() * 0.5F;
            currentGreen = 0.5F + random.nextFloat() * 0.5F;
            currentBlue = 0.5F + random.nextFloat() * 0.5F;
        }
        original.call(matrix, buffer, x1, z1, y, x2, z2, currentRed, currentGreen, currentBlue, offset2, offset1, shiftEast1, shiftSouth1, shiftEast2, shiftSouth2);
    }
}