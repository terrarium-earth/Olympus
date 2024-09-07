package earth.terrarium.olympus.client.components.contextmenu;

import earth.terrarium.olympus.client.ui.UIConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.LinkedHashMap;
import java.util.Map;

public class ContextMenu extends AbstractWidget {
    private final Map<Component, Runnable> options = new LinkedHashMap<>();

    public ContextMenu() {
        super(0, 0, 0, 0, CommonComponents.EMPTY);
        this.visible = false;
        this.active = false;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBg(graphics);
        int y = this.getY() + 5;
        var font = Minecraft.getInstance().font;
        for (Map.Entry<Component, Runnable> entry : this.options.entrySet()) {
            int width = Minecraft.getInstance().font.width(entry.getKey());
            y += renderEntry(graphics, font, mouseX, mouseY, entry.getKey(), y, width);
        }
    }

    protected void renderBg(GuiGraphics graphics) {
        graphics.blitSprite(UIConstants.LIST_BG, this.getX(), this.getY(), this.width, this.height);
    }

    protected int renderEntry(GuiGraphics graphics, Font font, int mouseX, int mouseY, Component text, int y, int width) {
        var height = font.lineHeight + 4;
        var hovered = mouseX >= getX() && mouseX <= getX() + width && mouseY >= y && mouseY <= y + height;
        graphics.blitSprite(UIConstants.LIST_ENTRY.get(hovered, false), getX(), y, width, height);
        graphics.drawString(Minecraft.getInstance().font, text, getX() + 2, y + 2, 0xFFFFFFFF);
        return height + 5;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int y = this.getY() + 5;
            for (Map.Entry<Component, Runnable> entry : this.options.entrySet()) {
                int x = this.getX() + 5;
                int width = Minecraft.getInstance().font.width(entry.getKey());
                if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + Minecraft.getInstance().font.lineHeight) {
                    entry.getValue().run();
                    this.visible = false;
                    return true;
                }
                y += Minecraft.getInstance().font.lineHeight;
            }
            close();
        }
        return false;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return isActive();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    public ContextMenu start(double x, double y) {
        this.options.clear();
        this.setX((int) x);
        this.setY((int) y);
        return this;
    }

    public ContextMenu addOption(Component component, Runnable runnable) {
        this.options.put(component, runnable);
        return this;
    }

    public ContextMenu open() {
        int longest = 0;
        for (Component component : this.options.keySet()) {
            int width = Minecraft.getInstance().font.width(component);
            if (width > longest) {
                longest = width;
            }
        }
        this.width = longest + 4;
        this.height = this.options.size() * (Minecraft.getInstance().font.lineHeight + 4);
        this.visible = true;
        this.active = true;
        return this;
    }

    public ContextMenu close() {
        this.visible = false;
        this.active = false;
        return this;
    }
}
