package earth.terrarium.olympus.client.pipelines;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import earth.terrarium.olympus.client.pipelines.pips.RoundedRectanglePIPRenderer;
import earth.terrarium.olympus.client.pipelines.uniforms.RoundedRectangleUniform;
import earth.terrarium.olympus.client.utils.GuiGraphicsHelper;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

public class RoundedRectangle {

    public static final BindGroupLayout LAYOUT = BindGroupLayout.builder()
            .withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER)
            .withUniform("Projection", UniformType.UNIFORM_BUFFER)
            .withUniform(RoundedRectangleUniform.NAME, UniformType.UNIFORM_BUFFER)
            .build();

    public static final RenderPipeline PIPELINE = RenderPipeline.builder()
            .withLocation(Identifier.fromNamespaceAndPath("olympus", "rounded_rect"))
            .withBindGroupLayout(LAYOUT)
            .withFragmentShader(Identifier.fromNamespaceAndPath("olympus", "core/rounded_rect"))
            .withVertexShader(Identifier.fromNamespaceAndPath("olympus", "core/rounded_rect"))
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
            .build();

    public static void draw(
            GuiGraphicsExtractor graphics,
            int x, int y, int width, int height,
            int backgroundColor, int borderColor,
            float borderRadius, int borderWidth
    ) {
        GuiGraphicsHelper.submitPip(graphics, new RoundedRectanglePIPRenderer.State(
                graphics,
                x, y, width, height,
                backgroundColor,
                borderColor, borderColor, borderColor, borderColor,
                (int) borderRadius, borderWidth
        ));
    }

    public static void draw(
            GuiGraphicsExtractor graphics,
            int x, int y, int width, int height,
            int backgroundColor,
            int borderColorTopLeft, int borderColorTopRight,
            int borderColorBottomLeft, int borderColorBottomRight,
            int borderRadius, int borderWidth
    ) {
        GuiGraphicsHelper.submitPip(graphics, new RoundedRectanglePIPRenderer.State(
                graphics,
                x, y, width, height,
                backgroundColor,
                borderColorTopLeft, borderColorTopRight, borderColorBottomLeft, borderColorBottomRight,
                borderRadius, borderWidth
        ));
    }
}
