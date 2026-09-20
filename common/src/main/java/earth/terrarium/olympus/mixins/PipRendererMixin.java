package earth.terrarium.olympus.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import earth.terrarium.olympus.client.utils.PipPipelineHelper;
import java.util.Objects;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PictureInPictureRenderer.class)
public class PipRendererMixin implements PipPipelineHelper {

    @ModifyExpressionValue(method = "blitTexture", at = @At(
        value = "FIELD",
        target = "Lnet/minecraft/client/renderer/RenderPipelines;GUI_TEXTURED_PREMULTIPLIED_ALPHA:Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;"
    ))
    RenderPipeline blitTexture(RenderPipeline original) {
        return Objects.requireNonNullElse(olympus$pipelineOverride(), original);
    }

}
