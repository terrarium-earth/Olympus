package earth.terrarium.olympus.client.shader;

import com.mojang.blaze3d.platform.GlStateManager;

public class Uniform<T> {

    private final int id;
    private final UniformType<T> type;
    private T value = null;

    private T lastValue;

    public Uniform(Shader<?> shader, UniformType<T> type, String name) {
        this.id = GlStateManager._glGetUniformLocation(shader.program, name);
        this.type = type;
    }

    public void upload() {
        if (this.id == -1) return;
        if (this.value == null) return;
        if (this.type == null) return;
        if (this.value.equals(this.lastValue)) return;
        this.lastValue = this.value;
        this.type.upload(this.id, this.value);
    }

    public void set(T value) {
        this.value = value;
    }
}