package earth.terrarium.olympus.client.ui.context;

import earth.terrarium.olympus.client.components.base.BaseWidget;
import earth.terrarium.olympus.client.ui.UIConstants;
import net.minecraft.client.gui.GuiGraphics;

public class DividerWidget extends BaseWidget {

    public DividerWidget() {
        super(0, 2);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.blitSprite(UIConstants.LIST_ENTRY.get(true, false), this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
}
