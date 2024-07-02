package earth.terrarium.olympus.client.components.dropdown;

import earth.terrarium.olympus.client.components.base.ListWidget;
import earth.terrarium.olympus.client.ui.Overlay;
import earth.terrarium.olympus.client.ui.UIConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

public class DropdownScreen<T> extends Overlay {

    protected final Dropdown<T> dropdown;

    protected DropdownScreen(Screen background, Dropdown<T> dropdown) {
        super(background);
        this.dropdown = dropdown;
    }

    public int x() {
        return this.dropdown.getX();
    }

    public int y() {
        int y = this.dropdown.getY() + this.dropdown.getHeight();
        if (y + this.height() > this.background.height) {
            y = this.dropdown.getY() - this.height();
        }
        return y;
    }

    public int width() {
        return this.dropdown.getWidth();
    }

    public int height() {
        return Math.min(24 * 5, this.dropdown.options().size() * this.dropdown.getEntryHeight()) + 3;
    }

    @Override
    protected void init() {
        ListWidget list = new ListWidget(this.width() - 3, this.height() - 3);
        list.setPosition(this.x() + 1, this.y() + 2);

        this.dropdown.initEntries(list, width() - 3, this::onClose);

        addRenderableWidget(list);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!super.mouseClicked(mouseX, mouseY, button)) {
            this.onClose();
        }
        return true;
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        dropdown.renderEntriesBackground(graphics, this.x(), this.y(), this.width(), this.height(), mouseX, mouseY, partialTick);
    }

    public boolean isOriginator(Dropdown<?> dropdown) {
        return this.dropdown.is(dropdown);
    }
}
