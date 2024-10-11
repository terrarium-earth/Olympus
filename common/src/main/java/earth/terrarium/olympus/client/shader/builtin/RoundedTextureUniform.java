package earth.terrarium.olympus.client.shader.builtin;

import earth.terrarium.olympus.client.shader.Shader;
import earth.terrarium.olympus.client.shader.Uniform;
import earth.terrarium.olympus.client.shader.UniformTypes;
import earth.terrarium.olympus.client.shader.Uniforms;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class RoundedTextureUniform extends Uniforms {

    public final Uniform<Matrix4f> modelViewMat;
    public final Uniform<Matrix4f> projMat;
    public final Uniform<Vector4f> radius;
    public final Uniform<Vector2f> size;
    public final Uniform<Vector2f> center;
    public final Uniform<Float> scaleFactor;
    public final Uniform<ResourceLocation> texture;

    public RoundedTextureUniform(Shader<RoundedTextureUniform> shader) {
        this.modelViewMat = create(shader, UniformTypes.MAT4, "modelViewMat");
        this.projMat = create(shader, UniformTypes.MAT4, "projMat");
        this.radius = create(shader, UniformTypes.VEC4, "radius");
        this.size = create(shader, UniformTypes.VEC2, "size");
        this.center = create(shader, UniformTypes.VEC2, "center");
        this.scaleFactor = create(shader, UniformTypes.FLOAT, "scaleFactor");
        this.texture = create(shader, UniformTypes.TEXTURE0, "sampler0");
    }
}
