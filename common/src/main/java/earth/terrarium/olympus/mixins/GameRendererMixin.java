package earth.terrarium.olympus.mixins;

import earth.terrarium.olympus.client.pipelines.uniforms.RenderPipelineUniformsStorage;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderBuffers;endFrame()V"))
    private void endFrame(CallbackInfo ci) {
        RenderPipelineUniformsStorage.endFrame();
    }
}
