package earth.terrarium.olympus.client.pipelines.renderer;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.vertex.VertexFormat;
import earth.terrarium.olympus.client.pipelines.uniforms.RenderPipelineUniforms;
import earth.terrarium.olympus.client.utils.SubmitNodeCollectorHelper;
import java.util.function.Consumer;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.DynamicGpuDataStorage;
import net.minecraft.client.renderer.SubmitNodeCollector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PipelineSubmitBuilder {

    private final RenderPipeline pipeline;

    private final List<UniformEntry<?>> uniforms = new ArrayList<>();
    private TextureSetup textures = TextureSetup.noTexture();
    private int color = -1;
    private Consumer<VertexConsumer> meshBuilder;
    private VertexFormat format;
    private PrimitiveTopology primitiveTopology;

    protected PipelineSubmitBuilder(RenderPipeline pipeline) {
        this.pipeline = pipeline;
    }

    public <T extends RenderPipelineUniforms> PipelineSubmitBuilder uniform(
        Supplier<DynamicGpuDataStorage<T>> storage,
        T uniform) {
        this.uniforms.add(new UniformEntry<>(uniform, storage));
        return this;
    }

    public PipelineSubmitBuilder textures(TextureSetup textures) {
        this.textures = textures;
        return this;
    }

    public PipelineSubmitBuilder color(int color) {
        this.color = color;
        return this;
    }

    public PipelineSubmitBuilder vertices(VertexFormat format, PrimitiveTopology primitiveTopology, Consumer<VertexConsumer> consumer) {
        this.meshBuilder = consumer;
        this.format = format;
        this.primitiveTopology = primitiveTopology;
        return this;
    }

    public PipelineSubmit build() {
        List<Pair<String, GpuBufferSlice>> dynamicUniforms = new ArrayList<>();
        for (UniformEntry<?> entry : this.uniforms) {
            dynamicUniforms.add(Pair.of(entry.uniform.name(), entry.write()));
        }

        if (this.primitiveTopology == null || this.format == null || this.meshBuilder == null) {
            throw new IllegalStateException("Missing vertex information.");
        }

        return new PipelineSubmit(
            this.pipeline, this.meshBuilder, this.format, this.primitiveTopology, this.color, this.textures, pass -> {
            for (var entry : dynamicUniforms) {
                pass.setUniform(entry.getFirst(), entry.getSecond());
            }
        });
    }

    public void submit(SubmitNodeCollector collector) {
        SubmitNodeCollectorHelper.submit(collector, this.build());
    }

    private record UniformEntry<T extends RenderPipelineUniforms>(
        T uniform, Supplier<DynamicGpuDataStorage<T>> storage
    ) {

        public GpuBufferSlice write() {
            return storage.get().writeData(uniform);
        }
    }
}
