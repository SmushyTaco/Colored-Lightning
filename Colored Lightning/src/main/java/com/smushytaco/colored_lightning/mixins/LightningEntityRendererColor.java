package com.smushytaco.colored_lightning.mixins;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.smushytaco.colored_lightning.ColoredLightning;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraft.client.renderer.entity.state.LightningBoltRenderState;
import net.minecraft.util.RandomSource;
import org.joml.Matrix4fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(LightningBoltRenderer.class)
public class LightningEntityRendererColor {
    @Inject(method = "lambda$submit$0", at = @At("HEAD"))
    private static void hookRenderOne(LightningBoltRenderState state, float[] xOffs, float finalXOff, float[] zOffs, float finalZOff, PoseStack.Pose pose, VertexConsumer buffer, CallbackInfo ci, @Share("random") LocalRef<RandomSource> randomLocalRef, @Share("currentRed") LocalFloatRef currentRedRef, @Share("currentGreen") LocalFloatRef currentGreenRef, @Share("currentBlue") LocalFloatRef currentBlueRef) {
        randomLocalRef.set(RandomSource.create(state.seed));
        currentRedRef.set(0.0F);
        currentGreenRef.set(0.0F);
        currentBlueRef.set(0.0F);
        if (ColoredLightning.INSTANCE.getConfig().getEnableColoredLightning() && !ColoredLightning.INSTANCE.getConfig().getChangeColorForEachSegment()) {
            currentRedRef.set(0.5F + randomLocalRef.get().nextFloat() * 0.5F);
            currentGreenRef.set(0.5F + randomLocalRef.get().nextFloat() * 0.5F);
            currentBlueRef.set(0.5F + randomLocalRef.get().nextFloat() * 0.5F);
        }
    }
    @WrapOperation(method = "lambda$submit$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LightningBoltRenderer;quad(Lorg/joml/Matrix4fc;Lcom/mojang/blaze3d/vertex/VertexConsumer;FFIFFFFFFFZZZZ)V"))
    private static void hookRenderTwo(Matrix4fc pose, VertexConsumer buffer, float xo0, float zo0, int h, float xo1, float zo1, float boltRed, float boltGreen, float boltBlue, float rr1, float rr2, boolean px1, boolean pz1, boolean px2, boolean pz2, Operation<Void> original, @Share("random") LocalRef<RandomSource> randomLocalRef, @Share("currentRed") LocalFloatRef currentRedRef, @Share("currentGreen") LocalFloatRef currentGreenRef, @Share("currentBlue") LocalFloatRef currentBlueRef) {
        if (!ColoredLightning.INSTANCE.getConfig().getEnableColoredLightning()) {
            original.call(pose, buffer, xo0, zo0, h, xo1, zo1, boltRed, boltGreen, boltBlue, rr1, rr2, px1, pz1, px2, pz2);
            return;
        }
        if (ColoredLightning.INSTANCE.getConfig().getChangeColorForEachSegment() && !px1 && !pz1 && px2 && !pz2) {
            currentRedRef.set(0.5F + randomLocalRef.get().nextFloat() * 0.5F);
            currentGreenRef.set(0.5F + randomLocalRef.get().nextFloat() * 0.5F);
            currentBlueRef.set(0.5F + randomLocalRef.get().nextFloat() * 0.5F);
        }
        original.call(pose, buffer, xo0, zo0, h, xo1, zo1, currentRedRef.get(), currentGreenRef.get(), currentBlueRef.get(), rr1, rr2, px1, pz1, px2, pz2);
    }
}