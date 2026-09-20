package earth.terrarium.olympus.client.pipelines.uniforms;

import net.minecraft.client.renderer.DynamicGpuDataStorage;

import java.nio.ByteBuffer;

public interface RenderPipelineUniforms extends DynamicGpuDataStorage.DynamicGpuData {

    String name();

    @Override
    void write(ByteBuffer buffer);
}
