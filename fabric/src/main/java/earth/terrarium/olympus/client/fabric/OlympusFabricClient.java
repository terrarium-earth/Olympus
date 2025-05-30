package earth.terrarium.olympus.client.fabric;

import earth.terrarium.olympus.client.images.ImageProviders;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class OlympusFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(ignored -> ImageProviders.tick());
    }
}
