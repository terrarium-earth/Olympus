package earth.terrarium.olympus.client.utils;

import com.mojang.blaze3d.textures.GpuTextureView;
import earth.terrarium.olympus.mixins.PipRendererAccessor;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public class PipRendererHelper {
    public static @Nullable GpuTextureView getTextureView(PictureInPictureRenderer<?> owner) {
        return ((PipRendererAccessor) owner).olympus$textureView();
    }

}
