package earth.terrarium.olympus.client.shader;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.logging.LogUtils;
import net.minecraft.Util;
import org.lwjgl.opengl.GL20;
import org.slf4j.Logger;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Shader<T extends Uniforms> {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Supplier<String> vertexShader;
    private final Supplier<String> fragmentShader;
    private final Function<Shader<T>, T> uniformsFactory;

    private boolean compiled = false;
    private T uniforms;
    int program;

    private Shader(Supplier<String> vertexShader, Supplier<String> fragmentShader, Function<Shader<T>, T> uniformsFactory) {
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
        this.uniformsFactory = uniformsFactory;
    }

    public void recompile() {
        this.compiled = false;
        this.compile();
    }

    public void compile() {
        if (this.compiled) return;
        if (this.vertexShader.get().isEmpty() || this.fragmentShader.get().isEmpty()) return;

        int vertexShaderId = GlStateManager.glCreateShader(GL20.GL_VERTEX_SHADER);
        GlStateManager.glShaderSource(vertexShaderId, List.of(this.vertexShader.get()));
        GlStateManager.glCompileShader(vertexShaderId);
        if (GlStateManager.glGetShaderi(vertexShaderId, GL20.GL_COMPILE_STATUS) == 0) {
            LOGGER.error("Failed to compile vertex shader: {}", GL20.glGetShaderInfoLog(vertexShaderId));
            return;
        }

        int fragmentShaderId = GlStateManager.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GlStateManager.glShaderSource(fragmentShaderId, List.of(this.fragmentShader.get()));
        GlStateManager.glCompileShader(fragmentShaderId);
        if (GlStateManager.glGetShaderi(fragmentShaderId, GL20.GL_COMPILE_STATUS) == 0) {
            LOGGER.error("Failed to compile fragment shader: {}", GL20.glGetShaderInfoLog(fragmentShaderId));
            return;
        }

        this.program = GlStateManager.glCreateProgram();
        GlStateManager.glAttachShader(this.program, vertexShaderId);
        GlStateManager.glAttachShader(this.program, fragmentShaderId);
        GlStateManager.glLinkProgram(this.program);
        if (GlStateManager.glGetProgrami(this.program, GL20.GL_LINK_STATUS) == 0) {
            LOGGER.error("Failed to link program: {}", GL20.glGetProgramInfoLog(this.program));
            return;
        }

        GlStateManager.glDeleteShader(vertexShaderId);
        GlStateManager.glDeleteShader(fragmentShaderId);

        this.uniforms = this.uniformsFactory.apply(this);

        this.compiled = true;
    }

    public T uniforms() {
        return this.uniforms;
    }

    public void use(Runnable action) {
        if (!this.compiled) throw new IllegalStateException("Shader not compiled");
        GlStateManager._glUseProgram(this.program);
        this.uniforms.upload();
        action.run();
        GlStateManager._glUseProgram(0);
    }

    public static <T extends Uniforms> Shader<T> make(Supplier<String> vertexShader, Supplier<String> fragmentShader, Function<Shader<T>, T> uniformsFactory) {
        return Util.make(new Shader<>(vertexShader, fragmentShader, uniformsFactory), Shader::compile);
    }
}