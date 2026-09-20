package earth.terrarium.olympus.client.utils;

import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface PipPipelineHelper {

    default RenderPipeline olympus$pipelineOverride() {
        return null;
    }

}
