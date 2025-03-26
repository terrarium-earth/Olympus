package earth.terrarium.olympus.client.pipelines;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class RoundedTexture {

    public static final RenderPipeline PIPELINE = RenderPipeline.builder()
            .withLocation(ResourceLocation.fromNamespaceAndPath("olympus", "rounded_tex"))
            .withSampler("Sampler0")
            .withUniform("ModelViewMat", UniformType.MATRIX4X4)
            .withUniform("ProjMat", UniformType.MATRIX4X4)
            .withUniform("ColorModulator", UniformType.VEC4)
            .withUniform("radius", UniformType.VEC4)
            .withUniform("size", UniformType.VEC2)
            .withUniform("center", UniformType.VEC2)
            .withUniform("scaleFactor", UniformType.FLOAT)
            .withBlend(BlendFunction.TRANSLUCENT)
            .withFragmentShader(ResourceLocation.fromNamespaceAndPath("olympus", "core/rounded_tex"))
            .withVertexShader(ResourceLocation.fromNamespaceAndPath("olympus", "core/rounded_tex"))
            .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
            .build();

    public static void draw(
            GuiGraphics graphics,
            int x, int y, int width, int height,
            ResourceLocation texture,
            float u0, float v0, float u1, float v1,
            float radius
    ) {
        Minecraft mc = Minecraft.getInstance();
        Window window = mc.getWindow();
        float scale = (float) window.getGuiScale();
        float scaledX = x * scale;
        float scaledY = y * scale;
        float scaledWidth = width * scale;
        float scaledHeight = height * scale;

        float yOffset = (window.getHeight() - scaledHeight) - (scaledY * 2f);

        var colors = RenderSystem.getShaderColor();

        Matrix4f matrix = graphics.pose().last().pose();
        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        buffer.addVertex(matrix, x, y, 0f).setUv(u0, v0).setColor(colors[0], colors[1], colors[2], colors[3]);
        buffer.addVertex(matrix, x, y + height, 0f).setUv(u0, v1).setColor(colors[0], colors[1], colors[2], colors[3]);
        buffer.addVertex(matrix, x + width, y + height, 0f).setUv(u1, v1).setColor(colors[0], colors[1], colors[2], colors[3]);
        buffer.addVertex(matrix, x + width, y, 0f).setUv(u1, v0).setColor(colors[0], colors[1], colors[2], colors[3]);

        GpuTexture gpuTexture = mc.getTextureManager().getTexture(texture).getTexture();

        RenderSystem.setShaderTexture(0, gpuTexture);

        PipelineRenderer.draw(PIPELINE, buffer.buildOrThrow(), pass -> {
            pass.bindSampler("Sampler0", gpuTexture);
            pass.setUniform("radius", radius, radius, radius, radius);
            pass.setUniform("size", scaledWidth, scaledHeight);
            pass.setUniform("center", scaledX + scaledWidth / 2f, scaledY + scaledHeight / 2f + yOffset);
            pass.setUniform("scaleFactor", scale);
        });
    }
}
