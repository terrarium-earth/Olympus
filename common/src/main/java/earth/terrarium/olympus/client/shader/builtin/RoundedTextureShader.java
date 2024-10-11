package earth.terrarium.olympus.client.shader.builtin;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import earth.terrarium.olympus.client.shader.Shader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.function.Supplier;

public class RoundedTextureShader {

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

    public static final Shader<RoundedTextureUniform> SHADER = Shader.make(VERTEX, FRAGMENT, RoundedTextureUniform::new);

    public static void blit(
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

        var uniforms = SHADER.uniforms();
        uniforms.modelViewMat.set(new Matrix4f(RenderSystem.getModelViewMatrix()));
        uniforms.projMat.set(new Matrix4f(RenderSystem.getProjectionMatrix()));
        uniforms.radius.set(new Vector4f(radius, radius, radius, radius));
        uniforms.size.set(new Vector2f(scaledWidth, scaledHeight));
        uniforms.center.set(new Vector2f(scaledX + scaledWidth / 2f, scaledY + scaledHeight / 2f + yOffset));
        uniforms.scaleFactor.set(scale);
        uniforms.texture.set(texture);

        SHADER.use(() -> {
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
        });
    }
}