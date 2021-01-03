package com.kneelawk.marionette.client.mixin;

import com.kneelawk.marionette.client.ClientGlobalSignals;
import net.minecraft.client.gui.screen.SplashScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceReloadMonitor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SplashScreen.class)
public class SplashScreenMixin {
    private boolean marionette_signalCalled = false;

    @Shadow
    @Final
    private ResourceReloadMonitor reloadMonitor;

    @Shadow
    @Final
    private boolean reloading;

    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!marionette_signalCalled && reloadMonitor.isApplyStageComplete() && !reloading) {
            marionette_signalCalled = true;
            ClientGlobalSignals.signalSplashScreen();
        }
    }
}
