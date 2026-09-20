package earth.terrarium.olympus.client.neoforge;

import earth.terrarium.olympus.client.images.ImageProviders;
import earth.terrarium.olympus.client.pipelines.RoundedRectangle;
import earth.terrarium.olympus.client.pipelines.RoundedTexture;
import earth.terrarium.olympus.client.pipelines.pips.RoundedRectanglePIPRenderer;
import earth.terrarium.olympus.client.pipelines.pips.RoundedTexturePIPRenderer;
import earth.terrarium.olympus.client.pipelines.renderer.PipelineSubmitFeatureRenderer;
import earth.terrarium.olympus.client.ui.UIConstants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterFeatureRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterPictureInPictureRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = UIConstants.MOD_ID, dist = Dist.CLIENT)
public class OlympusNeoForgeClient {

    public OlympusNeoForgeClient(IEventBus bus) {
        NeoForge.EVENT_BUS.addListener((ClientTickEvent.Pre ignored) -> ImageProviders.tick());
        NeoForge.EVENT_BUS.addListener((RegisterFeatureRenderersEvent event) -> {
            event.register(PipelineSubmitFeatureRenderer.TYPE, new PipelineSubmitFeatureRenderer());
        });
        bus.addListener((RegisterPictureInPictureRenderersEvent event) -> {
            event.register(RoundedRectanglePIPRenderer.State.class, RoundedRectanglePIPRenderer::new);
            event.register(RoundedTexturePIPRenderer.State.class, RoundedTexturePIPRenderer::new);
        });
        bus.addListener((RegisterRenderPipelinesEvent event) -> {
            event.registerPipeline(RoundedTexture.PIPELINE);
            event.registerPipeline(RoundedRectangle.PIPELINE);
        });
    }
}
