package earth.terrarium.olympus.client.utils;

import earth.terrarium.olympus.client.utils.platform.GuiGraphicsService;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class GuiGraphicsHelper {

    private static final GuiGraphicsService SERVICE = GuiGraphicsService.create();

    public static void submitPip(GuiGraphicsExtractor graphics, PictureInPictureRenderState state) {
        SERVICE.submitPip(graphics, state);
    }

    public static void submitElement(GuiGraphicsExtractor graphics, GuiElementRenderState state) {
        SERVICE.submitElement(graphics, state);
    }

    public static ScreenRectangle getLastScissor(GuiGraphicsExtractor graphics) {
        return SERVICE.getLastScissor(graphics);
    }
}
