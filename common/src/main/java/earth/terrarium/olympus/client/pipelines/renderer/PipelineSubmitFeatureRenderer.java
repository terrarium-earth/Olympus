package earth.terrarium.olympus.client.pipelines.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import java.util.ArrayList;
import net.minecraft.client.renderer.StagedVertexBuffer;
import net.minecraft.client.renderer.feature.FeatureFrameContext;
import net.minecraft.client.renderer.feature.FeatureRenderer;
import net.minecraft.client.renderer.feature.FeatureRendererType;
import net.minecraft.client.renderer.oit.OitStage;
import net.minecraft.util.ARGB;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class PipelineSubmitFeatureRenderer implements FeatureRenderer<PipelineSubmit> {
    public static FeatureRendererType<PipelineSubmit> TYPE = FeatureRendererType.create("olympus:pipeline_renderer");
    private final List<Group> draws = new ArrayList<>();

    private static GpuBufferSlice getDynamicUniforms(int color) {
        return RenderSystem.getDynamicUniforms().writeTransform(
            RenderSystem.getModelViewMatrixCopy(),
            new Vector4f(ARGB.redFloat(color), ARGB.greenFloat(color), ARGB.blueFloat(color), ARGB.alphaFloat(color)),
            new Vector3f(),
            new Matrix4f());
    }

    @Override
    public void prepareGroup(
        @NonNull FeatureFrameContext context,
        @NonNull List<PipelineSubmit> pipelineSubmitNodes,
        boolean strictlyOrdered) {
        var stagedVertexBuffer = context.stagedVertexBuffer();
        for (var pipelineSubmitNode : pipelineSubmitNodes) {

            var draw = stagedVertexBuffer.appendDraw(pipelineSubmitNode.format(), pipelineSubmitNode.primitiveTopology());

            var builder = stagedVertexBuffer.getVertexBuilder(draw);

            pipelineSubmitNode.meshBuilder().accept(builder);

            draws.add(new Group(draw, pipelineSubmitNode));
        }
    }

    @Override
    public void executeGroup(
        @NonNull FeatureFrameContext context,
        @Nullable OitStage stage,
        @NonNull RenderPass pass,
        int groupIndex,
        List<PipelineSubmit> pipelineSubmitNodes,
        boolean strictlyOrdered) {
        var stagedVertexBuffer = context.stagedVertexBuffer();

        for (var group : this.draws) {
            pass.pushDebugGroup(() -> "Pipeline submit " + group.submit.pipeline().getLocation());
            var submit = group.submit;
            var executeInfo = stagedVertexBuffer.getExecuteInfo(group.draw);

            pass.setPipeline(RenderSystem.getCompiledPipeline(submit.pipeline()));

            var scissor = RenderSystem.getScissorStateForRenderTypeDraws();
            if (scissor.enabled()) {
                pass.enableScissor(scissor.x(), scissor.y(), scissor.width(), scissor.height());
            }
            var textures = submit.textures();

            if (textures.texure0() != null) {
                pass.setUniform("Sampler0", textures.texure0(), textures.sampler0());
            }
            if (textures.texure1() != null) {
                pass.setUniform("Sampler1", textures.texure1(), textures.sampler1());
            }
            if (textures.texure2() != null) {
                pass.setUniform("Sampler2", textures.texure2(), textures.sampler2());
            }

            var uniforms = getDynamicUniforms(submit.color());

            RenderSystem.bindDefaultUniforms(pass);
            pass.setUniform("DynamicTransforms", uniforms);

            submit.options().accept(pass);

            pass.setVertexBuffer(0, executeInfo.vertexBuffer().slice());
            pass.setIndexBuffer(executeInfo.indexBuffer(), executeInfo.indexType());

            pass.drawIndexed(executeInfo.indexCount(), 1, executeInfo.firstIndex(), executeInfo.baseVertex(), 0);

            pass.popDebugGroup();
        }
    }

    record Group(StagedVertexBuffer.Draw draw, PipelineSubmit submit) {

    }

    @Override
    public void finishExecute(FeatureFrameContext context) {
        this.draws.clear();
    }
}
