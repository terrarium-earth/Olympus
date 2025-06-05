package earth.terrarium.olympus.client.pipelines;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import earth.terrarium.olympus.client.pipelines.renderer.PipelineRenderer;
import earth.terrarium.olympus.client.pipelines.uniforms.RoundedTextureUniform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class RoundedTexture {

    public static final RenderPipeline PIPELINE = RenderPipeline.builder()
            .withLocation(ResourceLocation.fromNamespaceAndPath("olympus", "rounded_tex"))
            .withSampler("Sampler0")
            .withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER)
            .withUniform("Projection", UniformType.UNIFORM_BUFFER)
            .withUniform(RoundedTextureUniform.NAME, UniformType.UNIFORM_BUFFER)
            .withBlend(BlendFunction.TRANSLUCENT)
            .withFragmentShader(ResourceLocation.fromNamespaceAndPath("olympus", "core/rounded_tex"))
            .withVertexShader(ResourceLocation.fromNamespaceAndPath("olympus", "core/rounded_tex"))
            .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
            .build();

    public static void drawRelative(
            GuiGraphics graphics,
            int x, int y, int width, int height,
            ResourceLocation texture,
            float u0, float v0, float u1, float v1,
            float radius, int color
    ) {
        var pose = graphics.pose();

        pose.pushMatrix();

        var xOffset = pose.m20();
        var yOffset = pose.m21();

        pose.translate(-xOffset, -yOffset);

        draw(graphics, (int) (x + xOffset), (int) (y + yOffset), width, height, texture, u0, v0, u1, v1, radius, color);

        pose.popMatrix();
    }

    public static void draw(
            GuiGraphics graphics,
            int x, int y, int width, int height,
            ResourceLocation texture,
            float u0, float v0, float u1, float v1,
            float radius
    ) {
        draw(graphics, x, y, width, height, texture, u0, v0, u1, v1, radius, -1);
    }

    public static void draw(
            GuiGraphics graphics,
            int x, int y, int width, int height,
            ResourceLocation texture,
            float u0, float v0, float u1, float v1,
            float radius, int color
    ) {
        Minecraft mc = Minecraft.getInstance();
        Window window = mc.getWindow();
        float scale = (float) window.getGuiScale();
        float scaledX = x * scale;
        float scaledY = y * scale;
        float scaledWidth = width * scale;
        float scaledHeight = height * scale;

        float yOffset = (window.getHeight() - scaledHeight) - (scaledY * 2f);

        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        buffer.addVertexWith2DPose(graphics.pose(), x, y, 0f).setUv(u0, v0).setColor(color);
        buffer.addVertexWith2DPose(graphics.pose(), x, y + height, 0f).setUv(u0, v1).setColor(color);
        buffer.addVertexWith2DPose(graphics.pose(), x + width, y + height, 0f).setUv(u1, v1).setColor(color);
        buffer.addVertexWith2DPose(graphics.pose(), x + width, y, 0f).setUv(u1, v0).setColor(color);

        RenderSystem.setShaderTexture(0, mc.getTextureManager().getTexture(texture).getTextureView());

        PipelineRenderer.builder(PIPELINE, buffer.buildOrThrow())
                .uniform(RoundedTextureUniform.STORAGE, RoundedTextureUniform.of(
                        new Vector4f(radius),
                        new Vector2f(scaledWidth - (radius * 2f * scale), scaledHeight - (radius * 2f * scale)),
                        new Vector2f(scaledX + scaledWidth / 2f, scaledY + scaledHeight / 2f + yOffset),
                        scale
                ))
                .color(color)
                .draw();
    }
}
