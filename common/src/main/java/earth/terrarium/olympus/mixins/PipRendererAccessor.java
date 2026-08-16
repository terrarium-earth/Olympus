package earth.terrarium.olympus.mixins;

import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PictureInPictureRenderer.class)
public interface PipRendererAccessor {

    @Accessor("textureView")
    GpuTextureView olympus$textureView();

}
