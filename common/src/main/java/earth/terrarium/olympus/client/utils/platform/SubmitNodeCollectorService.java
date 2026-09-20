package earth.terrarium.olympus.client.utils.platform;

import com.teamresourceful.resourcefullib.common.exceptions.NotImplementedException;
import earth.terrarium.olympus.client.pipelines.renderer.PipelineSubmit;
import net.minecraft.client.renderer.SubmitNodeCollector;
import org.jetbrains.annotations.ApiStatus;

@PlatformService
@ApiStatus.Internal
public interface SubmitNodeCollectorService {

    void submit(SubmitNodeCollector collector, PipelineSubmit submit);

    static SubmitNodeCollectorService create() {
        throw new NotImplementedException();
    }

}
