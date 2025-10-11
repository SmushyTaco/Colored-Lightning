package com.smushytaco.colored_lightning.mixins;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.smushytaco.colored_lightning.ColoredLightning;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.LightningEntityRenderer;
import net.minecraft.client.render.entity.state.LightningEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.random.Random;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(LightningEntityRenderer.class)
public class LightningEntityRendererColor {
    @Inject(method = "method_72989", at = @At("HEAD"))
    private static void hookRenderOne(LightningEntityRenderState lightningEntityRenderState, float[] fs, float f, float[] gs, float g, MatrixStack.Entry entry, VertexConsumer vertexConsumer, CallbackInfo ci, @Share("random") LocalRef<Random> randomLocalRef, @Share("currentRed") LocalFloatRef currentRedRef, @Share("currentGreen") LocalFloatRef currentGreenRef, @Share("currentBlue") LocalFloatRef currentBlueRef) {
        randomLocalRef.set(Random.create(lightningEntityRenderState.seed));
        currentRedRef.set(0.0F);
        currentGreenRef.set(0.0F);
        currentBlueRef.set(0.0F);
        if (ColoredLightning.INSTANCE.getConfig().getEnableColoredLightning() && !ColoredLightning.INSTANCE.getConfig().getChangeColorForEachSegment()) {
            currentRedRef.set(0.5F + randomLocalRef.get().nextFloat() * 0.5F);
            currentGreenRef.set(0.5F + randomLocalRef.get().nextFloat() * 0.5F);
            currentBlueRef.set(0.5F + randomLocalRef.get().nextFloat() * 0.5F);
        }
    }
    @WrapOperation(method = "method_72989", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LightningEntityRenderer;drawBranch(Lorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumer;FFIFFFFFFFZZZZ)V"))
    private static void hookRenderTwo(Matrix4f matrix, VertexConsumer buffer, float x1, float z1, int y, float x2, float z2, float red, float green, float blue, float offset2, float offset1, boolean shiftEast1, boolean shiftSouth1, boolean shiftEast2, boolean shiftSouth2, Operation<Void> original, @Share("random") LocalRef<Random> randomLocalRef, @Share("currentRed") LocalFloatRef currentRedRef, @Share("currentGreen") LocalFloatRef currentGreenRef, @Share("currentBlue") LocalFloatRef currentBlueRef) {
        if (!ColoredLightning.INSTANCE.getConfig().getEnableColoredLightning()) {
            original.call(matrix, buffer, x1, z1, y, x2, z2, red, green, blue, offset2, offset1, shiftEast1, shiftSouth1, shiftEast2, shiftSouth2);
            return;
        }
        if (ColoredLightning.INSTANCE.getConfig().getChangeColorForEachSegment() && !shiftEast1 && !shiftSouth1 && shiftEast2 && !shiftSouth2) {
            currentRedRef.set(0.5F + randomLocalRef.get().nextFloat() * 0.5F);
            currentGreenRef.set(0.5F + randomLocalRef.get().nextFloat() * 0.5F);
            currentBlueRef.set(0.5F + randomLocalRef.get().nextFloat() * 0.5F);
        }
        original.call(matrix, buffer, x1, z1, y, x2, z2, currentRedRef.get(), currentGreenRef.get(), currentBlueRef.get(), offset2, offset1, shiftEast1, shiftSouth1, shiftEast2, shiftSouth2);
    }
}