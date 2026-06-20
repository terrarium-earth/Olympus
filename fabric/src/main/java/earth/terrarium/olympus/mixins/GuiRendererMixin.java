package earth.terrarium.olympus.mixins;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import earth.terrarium.olympus.client.fabric.PictureInPictureHandler;
import earth.terrarium.olympus.client.fabric.PictureInPicturePool;
import earth.terrarium.olympus.client.pipelines.pips.OlympusPictureInPictureRenderState;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiRenderer.class)
public class GuiRendererMixin {

    @Shadow @Final
    private GuiRenderState renderState;
    @Shadow @Final private FeatureRenderDispatcher featureRenderDispatcher;
    @Unique private PictureInPictureHandler pipHandler = null;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initPictureInPictureHandler(CallbackInfo ci) {
        this.pipHandler = new PictureInPictureHandler();
    }

    @WrapMethod(method = "preparePictureInPictureState")
    private void fixPipState(PictureInPictureRenderState state, int scale, Operation<Void> original) {
        if (this.pipHandler != null && state instanceof OlympusPictureInPictureRenderState<?> olympusState) {
            PictureInPicturePool<PictureInPictureRenderState> pool = this.pipHandler.getPool(olympusState);
            if (pool != null) {
                pool.prepare(state, this.renderState, this.featureRenderDispatcher, scale);
                return;
            }
        }

        original.call(state, scale);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void endPictureInPictureStates(CallbackInfo ci) {
        if (this.pipHandler == null) return;
        this.pipHandler.end();
    }

    @Inject(method = "close", at = @At("TAIL"))
    private void closePictureInPictureHandler(CallbackInfo ci) {
        if (this.pipHandler == null) return;
        this.pipHandler.close();
    }
}
