package earth.terrarium.olympus.client.fabric;

import earth.terrarium.olympus.client.images.ImageProviders;
import earth.terrarium.olympus.client.pipelines.RoundedRectangle;
import earth.terrarium.olympus.client.pipelines.RoundedTexture;
import earth.terrarium.olympus.client.pipelines.pips.RoundedRectanglePIPRenderer;
import earth.terrarium.olympus.client.pipelines.pips.RoundedTexturePIPRenderer;
import earth.terrarium.olympus.client.pipelines.renderer.PipelineSubmitFeatureRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.FeatureRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.PictureInPictureRendererRegistry;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.registries.BuiltInRegistries;

public class OlympusFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(ignored -> ImageProviders.tick());
        PictureInPictureRendererRegistry.register(_ -> new RoundedTexturePIPRenderer());
        PictureInPictureRendererRegistry.register(_ -> new RoundedRectanglePIPRenderer());
        FeatureRendererRegistry.register(PipelineSubmitFeatureRenderer.TYPE, PipelineSubmitFeatureRenderer::new);

        RenderPipelines.register(RoundedTexture.PIPELINE);
        RenderPipelines.register(RoundedRectangle.PIPELINE);
    }
}
