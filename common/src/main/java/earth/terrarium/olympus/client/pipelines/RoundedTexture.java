package earth.terrarium.olympus.client.pipelines;

import com.mojang.blaze3d.PrimitiveTopology;import com.mojang.blaze3d.pipeline.BindGroupLayout;import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import earth.terrarium.olympus.client.pipelines.pips.RoundedTexturePIPRenderer;
import earth.terrarium.olympus.client.pipelines.uniforms.RoundedTextureUniform;
import earth.terrarium.olympus.client.utils.GuiGraphicsHelper;
import earth.terrarium.olympus.client.utils.TextureUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;import javax.naming.Binding;

public class RoundedTexture {

    public static final BindGroupLayout LAYOUT =  BindGroupLayout.builder()
            .withSampler("Sampler0")
            .withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER)
            .withUniform("Projection", UniformType.UNIFORM_BUFFER)
            .withUniform(RoundedTextureUniform.NAME, UniformType.UNIFORM_BUFFER)
            .build();

    public static final RenderPipeline PIPELINE = RenderPipeline.builder()
            .withLocation(Identifier.fromNamespaceAndPath("olympus", "rounded_tex"))
            .withBindGroupLayout(LAYOUT)
            .withFragmentShader(Identifier.fromNamespaceAndPath("olympus", "core/rounded_tex"))
            .withVertexShader(Identifier.fromNamespaceAndPath("olympus", "core/rounded_tex"))
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR)
            .build();

    public static void draw(
            GuiGraphicsExtractor graphics,
            int x, int y, int width, int height,
            Identifier texture,
            float u0, float v0, float u1, float v1,
            float radius
    ) {
        draw(graphics, x, y, width, height, texture, u0, v0, u1, v1, radius, 0xFFFFFFFF);
    }

    public static void draw(
            GuiGraphicsExtractor graphics,
            int x, int y, int width, int height,
            Identifier texture,
            float u0, float v0, float u1, float v1,
            float radius, int color
    ) {
        GuiGraphicsHelper.submitPip(graphics, new RoundedTexturePIPRenderer.State(
                graphics,
                x, y, width,height,
                u0, v0, u1, v1,
                TextureUtils.single(texture), color, (int) radius
        ));
    }
}
