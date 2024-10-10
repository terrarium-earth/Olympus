package earth.terrarium.olympus.client.shader.builtin;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import earth.terrarium.olympus.client.shader.Shader;
import earth.terrarium.olympus.client.shader.Uniform;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.function.Supplier;

public class RoundedTextureShader extends Shader {

    private static final Supplier<String> VERTEX = () -> """
    #version 150
    
    in vec3 Position;
    in vec2 UV0;
    
    uniform mat4 modelViewMat;
    uniform mat4 projMat;
    
    out vec2 texCoord0;
    
    void main() {
        gl_Position = projMat * modelViewMat * vec4(Position, 1.0);
    
        texCoord0 = UV0;
    }
    """;

    private static final Supplier<String> FRAGMENT = () -> """
    #version 150
    
    uniform sampler2D sampler0;
    uniform vec4 radius;
    uniform vec2 size;
    uniform vec2 center;
    uniform float scaleFactor;
    
    in vec2 texCoord0;
    
    out vec4 fragColor;
    
    // From: https://iquilezles.org/articles/distfunctions2d/
    float sdRoundedBox(vec2 p, vec2 b, vec4 r){
        r.xy = (p.x > 0.0) ? r.xy : r.zw;
        r.x  = (p.y > 0.0) ? r.x  : r.y;
        vec2 q = abs(p)-b+r.x;
        return min(max(q.x,q.y),0.0) + length(max(q,0.0)) - r.x;
    }
    
    void main() {
        vec4 color = texture(sampler0, texCoord0);
        if (color.a == 0.0) {
            discard;
        }
    
        vec2 halfSize = size / 2.0;
        float distance = sdRoundedBox(gl_FragCoord.xy - center, halfSize, radius * scaleFactor);
    
        if (distance > 0.0) {
            discard;
        }
        
        fragColor = color;
    }
    """;

    private static final RoundedTextureShader INSTANCE = Util.make(new RoundedTextureShader(), Shader::compile);

    private Matrix4f modelViewMat;
    private Matrix4f projectionMat;

    private Vector4f radius;
    private Vector2f size;
    private Vector2f center;
    private float scaleFactor;
    private ResourceLocation texture;

    protected RoundedTextureShader() {
        super(VERTEX, FRAGMENT);
    }

    @Override
    protected void addUniforms() {
        addUniform(Uniform.Type.MAT4, "modelViewMat", () -> modelViewMat);
        addUniform(Uniform.Type.MAT4, "projMat", () -> projectionMat);
        addUniform(Uniform.Type.VEC4, "radius", () -> radius);
        addUniform(Uniform.Type.VEC2, "size", () -> size);
        addUniform(Uniform.Type.VEC2, "center", () -> center);
        addUniform(Uniform.Type.FLOAT, "scaleFactor", () -> scaleFactor);
        addUniform(Uniform.Type.TEXTURE0, "sampler0", () -> texture);
    }

    public static boolean use(
            Matrix4f modelViewMat, Matrix4f projectionMat,
            ResourceLocation texture, Vector4f radius,
            Vector2f size, Vector2f center, float scaleFactor
    ) {
        if (!INSTANCE.compiled) return false;
        INSTANCE.modelViewMat = modelViewMat;
        INSTANCE.projectionMat = projectionMat;
        INSTANCE.radius = radius;
        INSTANCE.size = size;
        INSTANCE.center = center;
        INSTANCE.scaleFactor = scaleFactor;
        INSTANCE.texture = texture;
        INSTANCE.enable();
        INSTANCE.uploadUniforms();
        return true;
    }

    public static void unuse() {
        INSTANCE.disable();
    }

    public static boolean blit(
            GuiGraphics graphics,
            int x, int y, int width, int height,
            ResourceLocation texture,
            float u0, float v0, float u1, float v1,
            float radius
    ) {
        Window window = Minecraft.getInstance().getWindow();
        float scale = (float) window.getGuiScale();
        float scaledX = x * scale;
        float scaledY = y * scale;
        float scaledWidth = width * scale;
        float scaledHeight = height * scale;

        float yOffset = (window.getScreenHeight() - scaledHeight) - (scaledY * 2f);

        boolean used = use(
                new Matrix4f(RenderSystem.getModelViewMatrix()),
                new Matrix4f(RenderSystem.getProjectionMatrix()),
                texture,
                new Vector4f(radius, radius, radius, radius),
                new Vector2f(scaledWidth, scaledHeight),
                new Vector2f(scaledX + scaledWidth / 2f, scaledY + scaledHeight / 2f + yOffset),
                scale
        );

        if (!used) return false;

        RenderSystem.enableBlend();

        AbstractTexture textureObj = Minecraft.getInstance().getTextureManager().getTexture(texture);
        RenderSystem.bindTexture(textureObj.getId());

        Matrix4f matrix = graphics.pose().last().pose();
        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.addVertex(matrix, x, y, 0f).setUv(u0, v0);
        buffer.addVertex(matrix, x, y + height, 0f).setUv(u0, v1);
        buffer.addVertex(matrix, x + width, y + height, 0f).setUv(u1, v1);
        buffer.addVertex(matrix, x + width, y, 0f).setUv(u1, v0);
        BufferUploader.draw(buffer.buildOrThrow());

        RenderSystem.disableBlend();

        unuse();
        return true;
    }

    @ApiStatus.Internal
    public static void recompileShader() {
        INSTANCE.recompile();
    }
}