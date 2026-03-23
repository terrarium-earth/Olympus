package earth.terrarium.olympus.client.utils.platform;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;

public class GuiGraphicsServiceNeoForgeImpl implements GuiGraphicsService {

    @Override
    public void submitPip(GuiGraphicsExtractor graphics, PictureInPictureRenderState state) {
        graphics.submitPictureInPictureRenderState(state);
    }

    @Override
    public void submitElement(GuiGraphicsExtractor graphics, GuiElementRenderState state) {
        graphics.submitGuiElementRenderState(state);
    }

    @Override
    public ScreenRectangle getLastScissor(GuiGraphicsExtractor graphics) {
        return graphics.peekScissorStack();
    }
}
