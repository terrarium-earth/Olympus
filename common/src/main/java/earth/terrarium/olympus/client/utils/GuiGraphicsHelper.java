package earth.terrarium.olympus.client.utils;

import earth.terrarium.olympus.client.utils.platform.GuiGraphicsService;
import java.util.Objects;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public class GuiGraphicsHelper {

    private static final GuiGraphicsService SERVICE = GuiGraphicsService.create();

    public static void submitPip(GuiGraphicsExtractor graphics, PictureInPictureRenderState state) {
        SERVICE.submitPip(graphics, state);
    }

    public static void submitElement(GuiGraphicsExtractor graphics, GuiElementRenderState state) {
        SERVICE.submitElement(graphics, state);
    }

    public static @Nullable ScreenRectangle getLastScissor(GuiGraphicsExtractor graphics) {
        return SERVICE.getLastScissor(graphics);
    }

    public static boolean enableScissor(GuiGraphicsExtractor graphicsExtractor, int x0, int y0, int x1, int y1) {
        var currentScissorArea = Objects.requireNonNullElseGet(getLastScissor(graphicsExtractor), () -> new ScreenRectangle(0, 0, graphicsExtractor.guiWidth(), graphicsExtractor.guiHeight()));

        // same logic as ScreenRectangle#intersection
        int left = Math.max(currentScissorArea.left(), x0);
        int top = Math.max(currentScissorArea.top(), y0);
        int right = Math.min(currentScissorArea.right(), x1);
        int bottom = Math.min(currentScissorArea.bottom(), y1);

        if (left < right && top < bottom) {
            graphicsExtractor.enableScissor(x0, y0, x1, y1);
            return true;
        }

        return false;
    }
}
