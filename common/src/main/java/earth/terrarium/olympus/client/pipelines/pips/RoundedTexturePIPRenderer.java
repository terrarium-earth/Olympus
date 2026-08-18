package earth.terrarium.olympus.client.pipelines.pips;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import earth.terrarium.olympus.client.pipelines.RoundedRectangle;
import earth.terrarium.olympus.client.pipelines.RoundedTexture;
import earth.terrarium.olympus.client.pipelines.renderer.PipelineRenderer;
import earth.terrarium.olympus.client.pipelines.uniforms.RoundedRectangleUniform;
import earth.terrarium.olympus.client.pipelines.uniforms.RoundedTextureUniform;
import earth.terrarium.olympus.client.utils.GuiGraphicsHelper;
import earth.terrarium.olympus.client.utils.PipRendererHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.gui.BlitRenderState;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3x2f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.jspecify.annotations.NonNull;

import java.util.function.Supplier;

public class RoundedTexturePIPRenderer extends PictureInPictureRenderer<RoundedTexturePIPRenderer.@NotNull State> {

    private State lastState;

    @Override
    public @NotNull Class<State> getRenderStateClass() {
        return RoundedTexturePIPRenderer.State.class;
    }

    @Override
    protected boolean textureIsReadyToBlit(State state) {
        return this.lastState != null && this.lastState.equals(state);
    }

    protected void blitTexture(State renderState, GuiRenderState guiRenderState) {
        guiRenderState.addBlitToCurrentLayer(new BlitRenderState(
            RenderPipelines.GUI_TEXTURED,
            TextureSetup.singleTexture(PipRendererHelper.getTextureView(this), RenderSystem.getSamplerCache().getRepeat(FilterMode.NEAREST)),
            renderState.pose(),
            renderState.x0(),
            renderState.y0(),
            renderState.x1(),
            renderState.y1(),
            0.0F,
            1.0F,
            1.0F,
            0.0F,
            -1,
            renderState.scissorArea(),
            null)
        );
    }


    @Override
    protected void renderToTexture(@NotNull State state, @NonNull PoseStack poseStack, @NonNull SubmitNodeCollector submitNodeCollector) {
        var bounds = state.bounds;

        float scale = (float) Minecraft.getInstance().getWindow().getGuiScale();
        float scaledWidth = bounds.width() * scale;
        float scaledHeight = bounds.height() * scale;

        try (var byteBufferBuilder = ByteBufferBuilder.exactlySized(DefaultVertexFormat.POSITION_TEX_COLOR.getVertexSize() * 4)) {
            var bufferBuilder = new BufferBuilder(byteBufferBuilder, PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            bufferBuilder.addVertex(0f, 0f, 0f).setUv(state.u0(), state.v0()).setColor(-1);
            bufferBuilder.addVertex(0f, scaledHeight, 0f).setUv(state.u0(), state.v1()).setColor(-1);
            bufferBuilder.addVertex(scaledWidth, scaledHeight, 0f).setUv(state.u1(), state.v1()).setColor(-1);
            bufferBuilder.addVertex(scaledWidth, 0f, 0f).setUv(state.u1(), state.v0()).setColor(-1);

            PipelineRenderer.builder(RoundedTexture.PIPELINE, bufferBuilder.buildOrThrow())
                .uniform(RoundedTextureUniform.STORAGE, RoundedTextureUniform.of(
                    new Vector4f(state.borderRadius()),
                    new Vector2f(scaledWidth, scaledHeight),
                    new Vector2f(scaledWidth / 2f, scaledHeight / 2f),
                    scale
                ))
                .textures(state.texture())
                .color(state.color())
                .draw();
        }

        this.lastState = state;
    }

    @Override
    protected @NotNull String getTextureLabel() {
        return "olympus_rounded_texture";
    }

    public record State(
        int x0, int y0, int x1, int y1,
        float u0, float v0, float u1, float v1,
        TextureSetup texture, int color,
        int borderRadius,
        Matrix3x2f pose, ScreenRectangle scissorArea, ScreenRectangle bounds
    ) implements OlympusPictureInPictureRenderState<State> {

        public State(
            GuiGraphicsExtractor graphics,
            int x, int y, int width, int height,
            float u0, float v0, float u1, float v1,
            TextureSetup texture, int color, int borderRadius
        ) {
            this(
                x, y, x + width, y + height,
                u0, v0, u1, v1,
                texture, color, borderRadius,
                new Matrix3x2f(graphics.pose()), GuiGraphicsHelper.getLastScissor(graphics),
                OlympusPictureInPictureRenderState.getRelativeBounds(graphics, x, y, x + width, y + height)
            );
        }

        @Override
        public float scale() {
            return 1f;
        }

        @Override
        public Supplier<PictureInPictureRenderer<State>> getFactory() {
            return RoundedTexturePIPRenderer::new;
        }
    }
}
