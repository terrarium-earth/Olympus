package earth.terrarium.olympus.client.pipelines;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import earth.terrarium.olympus.client.pipelines.renderer.PipelineRenderer;
import earth.terrarium.olympus.client.pipelines.uniforms.RoundedRectangleUniform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class RoundedRectangle {

    public static final RenderPipeline PIPELINE = RenderPipeline.builder()
            .withLocation(ResourceLocation.fromNamespaceAndPath("olympus", "rounded_rect"))
            .withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER)
            .withUniform("Projection", UniformType.UNIFORM_BUFFER)
            .withUniform(RoundedRectangleUniform.NAME, UniformType.UNIFORM_BUFFER)
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
        var pose = graphics.pose();

        pose.pushMatrix();

        var xOffset = pose.m20();
        var yOffset = pose.m21();

        pose.translate(-xOffset, -yOffset);

        draw(graphics, (int) (x + xOffset), (int) (y + yOffset), width, height, backgroundColor, borderColor, borderRadius, borderWidth);

        pose.popMatrix();
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


        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        buffer.addVertexWith2DPose(graphics.pose(), x, y, 0f).setColor(backgroundColor);
        buffer.addVertexWith2DPose(graphics.pose(), x, y + height, 0f).setColor(backgroundColor);
        buffer.addVertexWith2DPose(graphics.pose(), x + width, y + height, 0f).setColor(backgroundColor);
        buffer.addVertexWith2DPose(graphics.pose(), x + width, y, 0f).setColor(backgroundColor);

        PipelineRenderer.builder(PIPELINE, buffer.buildOrThrow())
                .uniform(RoundedRectangleUniform.STORAGE, RoundedRectangleUniform.of(
                        new Vector4f(
                                (borderColor >> 16 & 0xFF) / 255f,
                                (borderColor >> 8 & 0xFF) / 255f,
                                (borderColor & 0xFF) / 255f,
                                (borderColor >> 24 & 0xFF) / 255f
                        ),
                        new Vector4f(borderRadius),
                        (float) borderWidth,
                        new Vector2f(scaledWidth - (borderWidth * 2f * scale), scaledHeight - (borderWidth * 2f * scale)),
                        new Vector2f(scaledX + scaledWidth / 2f, scaledY + scaledHeight / 2f + yOffset),
                        scale
                ))
                .draw();
    }
}
