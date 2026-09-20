package earth.terrarium.olympus.client.utils.platform;

import com.teamresourceful.resourcefullib.common.exceptions.NotImplementedException;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@PlatformService
@ApiStatus.Internal
public interface GuiGraphicsService {

    void submitPip(GuiGraphicsExtractor graphics, PictureInPictureRenderState state);
    void submitElement(GuiGraphicsExtractor graphics, GuiElementRenderState state);
    @Nullable ScreenRectangle getLastScissor(GuiGraphicsExtractor graphics);

    static GuiGraphicsService create() {
        throw new NotImplementedException();
    }
}
