package earth.terrarium.olympus.client.utils.platform;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;

public class GuiGraphicsServiceFabricImpl implements GuiGraphicsService {

    @Override
    public void submitPip(GuiGraphicsExtractor graphics, PictureInPictureRenderState state) {
        graphics.guiRenderState.addPicturesInPictureState(state);
    }

    @Override
    public void submitElement(GuiGraphicsExtractor graphics, GuiElementRenderState state) {
        graphics.guiRenderState.addGuiElement(state);
    }

    @Override
    public ScreenRectangle getLastScissor(GuiGraphicsExtractor graphics) {
        return graphics.scissorStack.peek();
    }
}
