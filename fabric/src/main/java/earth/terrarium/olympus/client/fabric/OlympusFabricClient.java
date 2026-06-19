package earth.terrarium.olympus.client.fabric;

import earth.terrarium.olympus.client.images.ImageProviders;
import earth.terrarium.olympus.client.pipelines.pips.RoundedRectanglePIPRenderer;
import earth.terrarium.olympus.client.pipelines.pips.RoundedTexturePIPRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.PictureInPictureRendererRegistry;

public class OlympusFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(ignored -> ImageProviders.tick());
        PictureInPictureRendererRegistry.register(_ -> new RoundedTexturePIPRenderer());
        PictureInPictureRendererRegistry.register(_ -> new RoundedRectanglePIPRenderer());
    }
}
