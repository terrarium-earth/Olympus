package earth.terrarium.olympus.client.pipelines.pips;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import earth.terrarium.olympus.client.pipelines.RoundedRectangle;
import earth.terrarium.olympus.client.pipelines.renderer.PipelineRenderer;
import earth.terrarium.olympus.client.pipelines.uniforms.RoundedRectangleUniform;
import earth.terrarium.olympus.client.utils.GuiGraphicsHelper;
import earth.terrarium.olympus.client.utils.PipRendererHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.gui.BlitRenderState;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3x2f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.function.Function;

public class RoundedRectanglePIPRenderer extends PictureInPictureRenderer<RoundedRectanglePIPRenderer.State> {

    private State lastState;

    public RoundedRectanglePIPRenderer(MultiBufferSource.BufferSource bufferSource) {
        super(bufferSource);
    }

    @Override
    public @NotNull Class<State> getRenderStateClass() {
        return RoundedRectanglePIPRenderer.State.class;
    }

    @Override
    protected boolean textureIsReadyToBlit(State state) {
        return this.lastState != null && this.lastState.equals(state);
    }

    protected void blitTexture(RoundedTexturePIPRenderer.State renderState, GuiRenderState guiRenderState) {
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
    protected void renderToTexture(State state, PoseStack stack) {
        var bounds = state.bounds;

        float scale = (float) Minecraft.getInstance().getWindow().getGuiScale();
        float scaledWidth = (bounds.width() - state.borderWidth() * 2) * scale;
        float scaledHeight = (bounds.height() - state.borderWidth() * 2) * scale;

        var buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        buffer.addVertex(0f, 0f, 0f).setColor(state.color());
        buffer.addVertex(0f, scaledHeight, 0f).setColor(state.color());
        buffer.addVertex(scaledWidth, scaledHeight, 0f).setColor(state.color());
        buffer.addVertex(scaledWidth, 0f, 0f).setColor(state.color());

        PipelineRenderer.builder(RoundedRectangle.PIPELINE, buffer.buildOrThrow())
                .uniform(RoundedRectangleUniform.STORAGE, RoundedRectangleUniform.of(
                        new Vector4f(
                                ARGB.redFloat(state.borderColorTopLeft()),
                                ARGB.greenFloat(state.borderColorTopLeft()),
                                ARGB.blueFloat(state.borderColorTopLeft()),
                                ARGB.alphaFloat(state.borderColorTopLeft())
                        ),
                        new Vector4f(
                                ARGB.redFloat(state.borderColorTopRight()),
                                ARGB.greenFloat(state.borderColorTopRight()),
                                ARGB.blueFloat(state.borderColorTopRight()),
                                ARGB.alphaFloat(state.borderColorTopRight())
                        ),
                        new Vector4f(
                                ARGB.redFloat(state.borderColorBottomLeft()),
                                ARGB.greenFloat(state.borderColorBottomLeft()),
                                ARGB.blueFloat(state.borderColorBottomLeft()),
                                ARGB.alphaFloat(state.borderColorBottomLeft())
                        ),
                        new Vector4f(
                                ARGB.redFloat(state.borderColorBottomRight()),
                                ARGB.greenFloat(state.borderColorBottomRight()),
                                ARGB.blueFloat(state.borderColorBottomRight()),
                                ARGB.alphaFloat(state.borderColorBottomRight())
                        ),
                        new Vector4f(state.borderRadius()),
                        state.borderWidth(),
                        new Vector2f(scaledWidth - state.borderWidth() * 2, scaledHeight - state.borderWidth() * 2),
                        new Vector2f(scaledWidth / 2f, scaledHeight / 2f),
                        scale
                ))
                .draw();

        this.lastState = state;
    }

    @Override
    protected @NotNull String getTextureLabel() {
        return "olympus_rounded_rectangle";
    }

    public record State(
            int x0, int y0, int x1, int y1,
            int color,
            int borderColorTopLeft, int borderColorTopRight,
            int borderColorBottomLeft, int borderColorBottomRight,
            int borderRadius, int borderWidth,
            Matrix3x2f pose, ScreenRectangle scissorArea, ScreenRectangle bounds
    ) implements OlympusPictureInPictureRenderState<State> {

        public State(
                GuiGraphicsExtractor graphics,
                int x, int y, int width, int height,
                int color,
                int borderColorTopLeft, int borderColorTopRight,
                int borderColorBottomLeft, int borderColorBottomRight,
                int borderRadius, int borderWidth
        ) {
            this(
                    x, y, x + width, y + height,
                    color, borderColorTopLeft, borderColorTopRight, borderColorBottomLeft, borderColorBottomRight,
                    borderRadius, borderWidth,
                    new Matrix3x2f(graphics.pose()), GuiGraphicsHelper.getLastScissor(graphics),
                    OlympusPictureInPictureRenderState.getRelativeBounds(
                            graphics,
                            x, y,
                            x + width + borderWidth * 2, y + height + borderWidth * 2
                    )
            );
        }

        @Override
        public float scale() {
            return 1f;
        }

        @Override
        public Function<MultiBufferSource.BufferSource, PictureInPictureRenderer<State>> getFactory() {
            return RoundedRectanglePIPRenderer::new;
        }
    }
}
