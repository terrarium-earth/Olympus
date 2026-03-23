package earth.terrarium.olympus.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import earth.terrarium.olympus.client.pipelines.uniforms.RenderPipelineUniformsStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderSystem.class)
public class RenderSystemMixin {

    @Inject(method = "flipFrame", at = @At(value = "TAIL"))
    private static void endFrame(CallbackInfo ci) {
        RenderPipelineUniformsStorage.endFrame();
    }
}
