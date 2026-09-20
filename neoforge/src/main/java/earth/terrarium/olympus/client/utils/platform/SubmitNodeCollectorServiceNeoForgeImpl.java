package earth.terrarium.olympus.client.utils.platform;

import earth.terrarium.olympus.client.pipelines.renderer.PipelineSubmit;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.neoforged.neoforge.client.submit.RenderPhaseKeys;

public class SubmitNodeCollectorServiceNeoForgeImpl implements SubmitNodeCollectorService{
    @Override
    public void submit(SubmitNodeCollector collector, PipelineSubmit submit) {
        collector.submitSpecial(RenderPhaseKeys.ALWAYS_ON_TOP, submit);
    }
}
