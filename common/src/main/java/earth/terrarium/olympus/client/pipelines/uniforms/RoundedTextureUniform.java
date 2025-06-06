package earth.terrarium.olympus.client.pipelines.uniforms;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import net.minecraft.client.renderer.DynamicUniformStorage;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.nio.ByteBuffer;
import java.util.function.Supplier;

public record RoundedTextureUniform(
        Vector4f radius,
        Vector2f size,
        Vector2f center,
        float scaleFactor
) implements RenderPipelineUniforms {

    public static final String NAME = "RoundedTextureUniform";
    public static final Supplier<DynamicUniformStorage<RoundedTextureUniform>> STORAGE = Suppliers.memoize(() -> new DynamicUniformStorage<>(
            "Rounded Texture UBO",
            new Std140SizeCalculator().putVec4().putVec2().putVec2().putFloat().get(),
            2
    ));

    public static RoundedTextureUniform of(Vector4f radius, Vector2f size, Vector2f center, float scaleFactor) {
        return new RoundedTextureUniform(radius, size, center, scaleFactor);
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public void write(ByteBuffer buffer) {
        Std140Builder.intoBuffer(buffer)
                .putVec4(radius)
                .putVec2(size)
                .putVec2(center)
                .putFloat(scaleFactor)
                .get();
    }
}
