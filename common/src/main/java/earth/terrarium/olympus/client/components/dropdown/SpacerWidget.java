package earth.terrarium.olympus.client.components.dropdown;

import earth.terrarium.olympus.client.components.base.BaseWidget;
import earth.terrarium.olympus.client.ui.UIConstants;
import net.minecraft.client.gui.GuiGraphics;

public class SpacerWidget extends BaseWidget {

    public SpacerWidget() {
        super(0, 2);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.blitSprite(UIConstants.LIST_ENTRY.get(true, false), this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
}
