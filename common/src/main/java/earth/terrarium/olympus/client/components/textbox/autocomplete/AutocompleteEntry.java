package earth.terrarium.olympus.client.components.textbox.autocomplete;

import com.mojang.blaze3d.platform.InputConstants;
import earth.terrarium.olympus.client.components.base.BaseWidget;
import earth.terrarium.olympus.client.ui.UIConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class AutocompleteEntry extends BaseWidget {

    private static final ResourceLocation ENTRY = UIConstants.id("lists/entry/normal");
    private static final ResourceLocation ENTRY_HOVERED = UIConstants.id("lists/entry/hovered");

    protected final String value;
    protected final Runnable action;

    public AutocompleteEntry(int width, int height, String value, Runnable action) {
        super(width, height);
        this.action = action;
        this.value = value;

        withTooltip(Component.nullToEmpty(value));
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        ResourceLocation texture = this.isHovered() ? ENTRY_HOVERED : ENTRY;
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, texture, this.getX(), this.getY(), this.getWidth(), this.getHeight());
        int textOffset = (this.height - 8) / 2;
        graphics.drawString(Minecraft.getInstance().font, this.value, this.getX() + textOffset, this.getY() + textOffset, 0xFEFEFE);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == InputConstants.KEY_RETURN && this.isFocused()) {
            this.action.run();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.action.run();
    }
}
