package earth.terrarium.olympus.mixins;

import earth.terrarium.olympus.client.pipelines.uniforms.RenderPipelineUniformsStorage;
import net.minecraft.client.renderer.RenderBuffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderBuffers.class)
public class RenderBuffersMixin {

    @Inject(method = "endFrame", at = @At(value = "TAIL"))
    private static void endFrame(CallbackInfo ci) {
        RenderPipelineUniformsStorage.endFrame();
    }
}
