package earth.terrarium.olympus.client.pipelines;

import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.renderpearl.api.pipeline.UniformType;
import earth.terrarium.olympus.client.pipelines.pips.RoundedTexturePIPRenderer;
import earth.terrarium.olympus.client.pipelines.uniforms.RoundedTextureUniform;
import earth.terrarium.olympus.client.utils.GuiGraphicsHelper;
import earth.terrarium.olympus.client.utils.TextureUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

public class RoundedTexture {

    public static final BindGroupLayout LAYOUT =  BindGroupLayout.builder()
            .withUniform("Sampler0", UniformType.COMBINED_IMAGE_SAMPLER)
            .withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER)
            .withUniform("Projection", UniformType.UNIFORM_BUFFER)
            .withUniform(RoundedTextureUniform.NAME, UniformType.UNIFORM_BUFFER)
            .build();

    public static final RenderPipeline PIPELINE = RenderPipeline.builder()
            .withLocation(Identifier.fromNamespaceAndPath("olympus", "rounded_tex"))
            .withBindGroupLayout(LAYOUT)
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
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
