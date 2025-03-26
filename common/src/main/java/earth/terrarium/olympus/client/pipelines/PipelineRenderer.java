package earth.terrarium.olympus.client.pipelines;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;

import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.Consumer;

public class PipelineRenderer {

    public static void draw(RenderPipeline pipeline, MeshData mesh, Consumer<RenderPass> options) {
        GpuDevice device = RenderSystem.getDevice();

        GpuBuffer vertexBuffer = pipeline.getVertexFormat().uploadImmediateVertexBuffer(mesh.vertexBuffer());
        GpuBuffer indexBuffer;
        VertexFormat.IndexType indexType;

        if (mesh.indexBuffer() == null) {
            var buffer = RenderSystem.getSequentialBuffer(mesh.drawState().mode());
            indexBuffer = buffer.getBuffer(mesh.drawState().indexCount());
            indexType = buffer.type();
        } else {
            indexBuffer = pipeline.getVertexFormat().uploadImmediateIndexBuffer(mesh.indexBuffer());
            indexType = mesh.drawState().indexType();
        }

        var target = Minecraft.getInstance().getMainRenderTarget();

        try (mesh; var pass = device.createCommandEncoder().createRenderPass(
                target.getColorTexture(),
                OptionalInt.empty(),
                target.useDepth ? target.getDepthTexture() : null,
                OptionalDouble.empty()
        )) {
            options.accept(pass);

            pass.setPipeline(pipeline);
            pass.setVertexBuffer(0, vertexBuffer);
            if (RenderSystem.SCISSOR_STATE.isEnabled()) pass.enableScissor(RenderSystem.SCISSOR_STATE);

            for (int i = 0; i < 12; i++) {
                var texture = RenderSystem.getShaderTexture(i);
                if (texture == null) continue;
                pass.bindSampler("Sampler" + i, texture);
            }

            pass.setIndexBuffer(indexBuffer, indexType);
            pass.drawIndexed(0, mesh.drawState().indexCount());
        }
    }
}
