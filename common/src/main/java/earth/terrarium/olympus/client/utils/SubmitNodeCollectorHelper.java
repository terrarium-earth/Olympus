package earth.terrarium.olympus.client.utils;

import earth.terrarium.olympus.client.pipelines.renderer.PipelineSubmit;
import earth.terrarium.olympus.client.utils.platform.SubmitNodeCollectorService;
import net.minecraft.client.renderer.SubmitNodeCollector;

public class SubmitNodeCollectorHelper {

    public static final SubmitNodeCollectorService INSTANCE = SubmitNodeCollectorService.create();

    public static void submit(SubmitNodeCollector collector, PipelineSubmit submit) {
        INSTANCE.submit(collector, submit);
    }
}
