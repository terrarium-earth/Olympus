package earth.terrarium.olympus.client.pipelines;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.teamresourceful.resourcefullib.client.CloseablePoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class RoundedRectangle {

    public static final RenderPipeline PIPELINE = RenderPipeline.builder()
            .withLocation(ResourceLocation.fromNamespaceAndPath("olympus", "rounded_rect"))
            .withUniform("ModelViewMat", UniformType.MATRIX4X4)
            .withUniform("ProjMat", UniformType.MATRIX4X4)
            .withUniform("ColorModulator", UniformType.VEC4)
            .withUniform("borderColor", UniformType.VEC4)
            .withUniform("borderRadius", UniformType.VEC4)
            .withUniform("borderWidth", UniformType.FLOAT)
            .withUniform("size", UniformType.VEC2)
            .withUniform("center", UniformType.VEC2)
            .withUniform("scaleFactor", UniformType.FLOAT)
            .withBlend(BlendFunction.TRANSLUCENT)
            .withFragmentShader(ResourceLocation.fromNamespaceAndPath("olympus", "core/rounded_rect"))
            .withVertexShader(ResourceLocation.fromNamespaceAndPath("olympus", "core/rounded_rect"))
            .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
            .build();

    public static void drawRelative(
            GuiGraphics graphics,
            int x, int y, int width, int height,
            int backgroundColor, int borderColor,
            float borderRadius, int borderWidth
    ) {
        var xOffset = graphics.pose().last().pose().m30();
        var yOffset = graphics.pose().last().pose().m31();

        try (var stack = new CloseablePoseStack(graphics)) {
            stack.translate(-xOffset, -yOffset, 0);
            draw(
                    graphics,
                    (int) (x + xOffset), (int) (y + yOffset),
                    width, height,
                    backgroundColor, borderColor,
                    borderRadius, borderWidth
            );
        }
    }

    public static void draw(
            GuiGraphics graphics,
            int x, int y, int width, int height,
            int backgroundColor, int borderColor,
            float borderRadius, int borderWidth
    ) {
        Window window = Minecraft.getInstance().getWindow();
        float scale = (float) window.getGuiScale();
        float scaledX = x * scale;
        float scaledY = y * scale;
        float scaledWidth = width * scale;
        float scaledHeight = height * scale;

        float yOffset = (window.getHeight() - scaledHeight) - (scaledY * 2f);


        Matrix4f matrix = graphics.pose().last().pose();
        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        buffer.addVertex(matrix, x, y, 0f).setColor(backgroundColor);
        buffer.addVertex(matrix, x, y + height, 0f).setColor(backgroundColor);
        buffer.addVertex(matrix, x + width, y + height, 0f).setColor(backgroundColor);
        buffer.addVertex(matrix, x + width, y, 0f).setColor(backgroundColor);

        PipelineRenderer.draw(PIPELINE, buffer.buildOrThrow(), pass -> {
            pass.setUniform("borderColor",
                    (borderColor >> 16 & 0xFF) / 255f,
                    (borderColor >> 8 & 0xFF) / 255f,
                    (borderColor & 0xFF) / 255f,
                    (borderColor >> 24 & 0xFF) / 255f
            );
            pass.setUniform("borderRadius", borderRadius, borderRadius, borderRadius, borderRadius);
            pass.setUniform("borderWidth", (float) borderWidth);
            pass.setUniform("size", scaledWidth - (borderWidth * 2f * scale), scaledHeight - (borderWidth * 2f * scale));
            pass.setUniform("center", scaledX + scaledWidth / 2f, scaledY + scaledHeight / 2f + yOffset);
            pass.setUniform("scaleFactor", scale);
        });
    }
}
