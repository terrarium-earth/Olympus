package earth.terrarium.olympus.neoforge;

import earth.terrarium.olympus.Olympus;
import earth.terrarium.olympus.client.neoforge.OlympusClientNeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(Olympus.MOD_ID)
public class OlympusNeoForge {

    public OlympusNeoForge(IEventBus bus) {
        Olympus.init();
        if (FMLEnvironment.dist.isClient()) {
            OlympusClientNeoForge.init(bus);
        }
    }
}
