package earth.terrarium.olympus.neoforge;

import earth.terrarium.olympus.client.images.ImageProviders;
import earth.terrarium.olympus.client.ui.UIConstants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = UIConstants.MOD_ID, dist = Dist.CLIENT)
public class OlympusNeoForgeClient {

    public OlympusNeoForgeClient() {
        NeoForge.EVENT_BUS.addListener((ClientTickEvent.Pre ignored) -> ImageProviders.tick());
    }
}
