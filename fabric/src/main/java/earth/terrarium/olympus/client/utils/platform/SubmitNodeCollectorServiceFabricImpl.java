package earth.terrarium.olympus.client.utils.platform;

import earth.terrarium.olympus.client.pipelines.renderer.PipelineSubmit;
import net.fabricmc.fabric.api.client.rendering.v1.SubmitRenderPhases;
import net.minecraft.client.renderer.SubmitNodeCollector;

public class SubmitNodeCollectorServiceFabricImpl implements SubmitNodeCollectorService{
    @Override
    public void submit(SubmitNodeCollector collector, PipelineSubmit submit) {
        collector.submitCustom(SubmitRenderPhases.ALWAYS_ON_TOP, submit);
    }
}
