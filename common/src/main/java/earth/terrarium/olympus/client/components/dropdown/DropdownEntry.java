package earth.terrarium.olympus.client.components.dropdown;

import earth.terrarium.olympus.client.components.base.BaseWidget;
import earth.terrarium.olympus.client.components.base.ListWidget;
import earth.terrarium.olympus.client.ui.UIConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class DropdownEntry<T> extends BaseWidget {
    public static final int COLOR = 0xFEFEFE;

    protected final Dropdown<T> dropdown;
    protected final T value;
    protected final Runnable action;

    public DropdownEntry(int width, int height, Dropdown<T> dropdown, T value, Runnable action) {
        super(width, height);
        this.action = action;
        this.dropdown = dropdown;
        this.value = value;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        Component text = this.dropdown.getText(this.value);
        int textOffset = (this.height - 8) / 2;
        graphics.drawString(Minecraft.getInstance().font, text, this.getX() + textOffset, this.getY() + textOffset, getFontColor());
    }

    protected int getFontColor() {
        return COLOR;
    }

    protected void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.blitSprite(UIConstants.LIST_ENTRY.get(true, isHovered()), this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.action.run();
    }
}
