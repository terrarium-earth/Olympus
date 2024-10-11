package earth.terrarium.olympus.client.shader.builtin;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import earth.terrarium.olympus.client.shader.Shader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.function.Supplier;

public final class RoundedRectShader {

    private static final Supplier<String> VERTEX = () -> """
            #version 150
            
            in vec3 Position;
            
            uniform mat4 modelViewMat;
            uniform mat4 projMat;
            
            void main() {
                gl_Position = projMat * modelViewMat * vec4(Position, 1.0);
            }
            """;

    private static final Supplier<String> FRAGMENT = () -> """
            #version 150
            
            uniform mat4 modelViewMat;
            uniform mat4 projMat;
            uniform vec4 backgroundColor;
            uniform vec4 borderColor;
            uniform vec4 borderRadius;
            uniform float borderWidth;
            uniform vec2 size;
            uniform vec2 center;
            uniform float scaleFactor;
            
            out vec4 fragColor;
            
            // From: https://iquilezles.org/articles/distfunctions2d/
            float sdRoundedBox(vec2 p, vec2 b, vec4 r){
                r.xy = (p.x > 0.0) ? r.xy : r.zw;
                r.x  = (p.y > 0.0) ? r.x  : r.y;
                vec2 q = abs(p)-b+r.x;
                return min(max(q.x,q.y),0.0) + length(max(q,0.0)) - r.x;
            }
            
            void main() {
                vec4 color = backgroundColor;
                if (color.a == 0.0) {
                    discard;
                }
            
                vec2 halfSize = size / 2.0;
                float distance = sdRoundedBox(gl_FragCoord.xy - center, halfSize, borderRadius * scaleFactor);
                float smoothed = min(1.0 - distance, color.a);
                float border = min(1.0 - smoothstep(borderWidth, borderWidth, abs(distance)), borderColor.a);
            
                if (border > 0.0) {
                    fragColor = borderColor * vec4(1.0, 1.0, 1.0, border);
                } else {
                    fragColor = color * vec4(1.0, 1.0, 1.0, smoothed);
                }
            }
            """;

    public static final Shader<RoundedRectUniform> SHADER = Shader.make(VERTEX, FRAGMENT, RoundedRectUniform::new);

    public static void fill(
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

        float yOffset = (window.getScreenHeight() - scaledHeight) - (scaledY * 2f);

        var uniforms = SHADER.uniforms();
        uniforms.modelViewMat.set(new Matrix4f(RenderSystem.getModelViewMatrix()));
        uniforms.projMat.set(new Matrix4f(RenderSystem.getProjectionMatrix()));
        uniforms.backgroundColor.set(new Vector4f(
                (backgroundColor >> 16 & 0xFF) / 255f,
                (backgroundColor >> 8 & 0xFF) / 255f,
                (backgroundColor & 0xFF) / 255f,
                (backgroundColor >> 24 & 0xFF) / 255f
        ));
        uniforms.borderColor.set(new Vector4f(
                (borderColor >> 16 & 0xFF) / 255f,
                (borderColor >> 8 & 0xFF) / 255f,
                (borderColor & 0xFF) / 255f,
                (borderColor >> 24 & 0xFF) / 255f
        ));
        uniforms.borderRadius.set(new Vector4f(borderRadius, borderRadius, borderRadius, borderRadius));
        uniforms.borderWidth.set((float) borderWidth);
        uniforms.size.set(new Vector2f(scaledWidth - (borderWidth * 2f * scale), scaledHeight - (borderWidth * 2f * scale)));
        uniforms.center.set(new Vector2f(scaledX + scaledWidth / 2f, scaledY + scaledHeight / 2f + yOffset));
        uniforms.scaleFactor.set(scale);

        SHADER.use(() -> {
            RenderSystem.enableBlend();

            Matrix4f matrix = graphics.pose().last().pose();
            BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
            buffer.addVertex(matrix, x, y, 0f);
            buffer.addVertex(matrix, x, y + height, 0f);
            buffer.addVertex(matrix, x + width, y + height, 0f);
            buffer.addVertex(matrix, x + width, y, 0f);
            BufferUploader.draw(buffer.buildOrThrow());

            RenderSystem.disableBlend();
        });
    }

}