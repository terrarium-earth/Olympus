package earth.terrarium.olympus.client.components.buttons;

import com.teamresourceful.resourcefullib.client.components.CursorWidget;
import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import earth.terrarium.olympus.client.ui.UIConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TextButton extends Button implements CursorWidget {

    protected final WidgetSprites sprites;
    protected final int color;

    public TextButton(int width, int height, int color, WidgetSprites sprites, Component text, OnPress onPress) {
        super(0, 0, width, height, text, onPress, DEFAULT_NARRATION);
        this.color = color;
        this.sprites = sprites;
    }

    public static TextButton create(int width, int height, Component text, OnPress onPress) {
        return new TextButton(width, height, 0x555555, UIConstants.BUTTON, text, onPress);
    }

    public static TextButton create(int width, int height, Component text, Runnable onPress) {
        return new TextButton(width, height, 0x555555, UIConstants.BUTTON, text, (button) -> onPress.run());
    }

    public static TextButton normal(int width, int height, Component text, OnPress onPress) {
        return new TextButton(width, height, 0x555555, UIConstants.BUTTON, text, onPress);
    }

    public static TextButton primary(int width, int height, Component text, OnPress onPress) {
        return new TextButton(width, height, 0xFFFFFF, UIConstants.PRIMARY_BUTTON, text, onPress);
    }

    public static TextButton danger(int width, int height, Component text, OnPress onPress) {
        return new TextButton(width, height, 0xFFFFFF, UIConstants.DANGER_BUTTON, text, onPress);
    }

    public TextButton withTooltip(Component component) {
        this.setTooltip(Tooltip.create(component));
        return this;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        ResourceLocation sprite = this.sprites.get(this.isActive(), this.isHoveredOrFocused());
        graphics.blitSprite(sprite, getX(), getY(), this.width, this.height);

        Font font = Minecraft.getInstance().font;

        int textX = getX() + (this.width - font.width(this.getMessage())) / 2;
        int textY = getY() + 1 + (this.height - font.lineHeight) / 2;

        graphics.drawString(font, this.getMessage(), textX, textY, this.color, false);
    }

    @Override
    public CursorScreen.Cursor getCursor() {
        return !this.isActive() ? CursorScreen.Cursor.DISABLED : CursorScreen.Cursor.POINTER;
    }
}