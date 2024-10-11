package earth.terrarium.olympus.client.shader;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

public final class UniformTypes {

    public static final UniformType<Float> FLOAT = GL20::glUniform1f;
    public static final UniformType<Integer> INT = GL20::glUniform1i;
    public static final UniformType<Boolean> BOOL = (id, value) -> GL20.glUniform1i(id, value ? 1 : 0);
    public static final UniformType<Vector2f> VEC2 = (id, value) -> GL20.glUniform2f(id, value.x, value.y);
    public static final UniformType<Vector3f> VEC3 = (id, value) -> GL20.glUniform3f(id, value.x, value.y, value.z);
    public static final UniformType<Vector4f> VEC4 = (id, value) -> GL20.glUniform4f(id, value.x, value.y, value.z, value.w);
    public static final UniformType<Matrix4f> MAT4 = (id, value) -> GL20.glUniformMatrix4fv(id, false, value.get(new float[16]));
    public static final UniformType<ResourceLocation> TEXTURE0 = (id, value) -> {
        int i = GlStateManager._getActiveTexture();
        TextureManager manager = Minecraft.getInstance().getTextureManager();
        GL20.glUniform1i(id, 0);
        RenderSystem.activeTexture(GL13.GL_TEXTURE0);
        RenderSystem.bindTexture(manager.getTexture(value).getId());
        RenderSystem.activeTexture(i);
    };

}
