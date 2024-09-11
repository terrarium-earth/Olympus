package earth.terrarium.olympus.client.ui.context;

import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import earth.terrarium.olympus.client.components.base.BaseWidget;
import earth.terrarium.olympus.client.ui.UIConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class DividerWidget extends BaseWidget {

    public DividerWidget() {
        super(0, 10);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.blitSprite(
            UIConstants.CONTEXT_DIVIDER,
            this.getX(),
            this.getY(),
            this.getWidth(),
            this.getHeight()
        );
    }
}
