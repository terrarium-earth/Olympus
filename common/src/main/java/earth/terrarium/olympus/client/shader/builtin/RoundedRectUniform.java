package earth.terrarium.olympus.client.shader.builtin;

import earth.terrarium.olympus.client.shader.Shader;
import earth.terrarium.olympus.client.shader.Uniform;
import earth.terrarium.olympus.client.shader.UniformTypes;
import earth.terrarium.olympus.client.shader.Uniforms;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class RoundedRectUniform extends Uniforms {

    public final Uniform<Matrix4f> modelViewMat;
    public final Uniform<Matrix4f> projMat;
    public final Uniform<Vector4f> backgroundColor;
    public final Uniform<Vector4f> borderColor;
    public final Uniform<Vector4f> borderRadius;
    public final Uniform<Float> borderWidth;
    public final Uniform<Vector2f> size;
    public final Uniform<Vector2f> center;
    public final Uniform<Float> scaleFactor;

    public RoundedRectUniform(Shader<RoundedRectUniform> shader) {
        this.modelViewMat = create(shader, UniformTypes.MAT4, "modelViewMat");
        this.projMat = create(shader, UniformTypes.MAT4, "projMat");
        this.backgroundColor = create(shader, UniformTypes.VEC4, "backgroundColor");
        this.borderColor = create(shader, UniformTypes.VEC4, "borderColor");
        this.borderRadius = create(shader, UniformTypes.VEC4, "borderRadius");
        this.borderWidth = create(shader, UniformTypes.FLOAT, "borderWidth");
        this.size = create(shader, UniformTypes.VEC2, "size");
        this.center = create(shader, UniformTypes.VEC2, "center");
        this.scaleFactor = create(shader, UniformTypes.FLOAT, "scaleFactor");
    }
}
