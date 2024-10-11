package earth.terrarium.olympus.client.shader;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Uniforms implements Iterable<Uniform<?>> {

    private final List<Uniform<?>> uniforms = new ArrayList<>();

    protected <T> Uniform<T> create(Shader<?> shader, UniformType<T> type, String name) {
        Uniform<T> uniform = new Uniform<>(shader, type, name);
        this.uniforms.add(uniform);
        return uniform;
    }

    @Override
    public @NotNull Iterator<Uniform<?>> iterator() {
        return uniforms.iterator();
    }

    public void upload() {
        for (Uniform<?> uniform : this) {
            uniform.upload();
        }
    }
}
