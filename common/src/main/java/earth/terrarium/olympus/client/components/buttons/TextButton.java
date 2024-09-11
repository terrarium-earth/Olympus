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
import org.jetbrains.annotations.ApiStatus;

public class TextButton extends Button implements CursorWidget {
    protected WidgetSprites sprites;
    protected int color;
    protected int hoverColor;

    public TextButton(int width, int height, int color, int hoverColor, WidgetSprites sprites, Component text, OnPress onPress) {
        super(0, 0, width, height, text, onPress, DEFAULT_NARRATION);
        this.color = color;
        this.sprites = sprites;
        this.hoverColor = hoverColor;
    }

    public TextButton(int width, int height, int color, WidgetSprites sprites, Component text, OnPress onPress) {
        this(width, height, color, color, sprites, text, onPress);
    }

    public static TextButton create(OnPress onPress) {
        return normal(0, 0, UIConstants.LOADING, onPress);
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

    public TextButton withColor(int color) {
        this.color = color;
        return this;
    }

    public TextButton withHoverColor(int hoverColor) {
        this.hoverColor = hoverColor;
        return this;
    }

    public TextButton withSprites(WidgetSprites sprites) {
        this.sprites = sprites;
        return this;
    }

    public TextButton danger() {
        this.color = 0xFFFFFF;
        this.hoverColor = 0xFFFFFF;
        this.sprites = UIConstants.DANGER_BUTTON;
        return this;
    }

    public TextButton primary() {
        this.color = 0xFFFFFF;
        this.hoverColor = 0xFFFFFF;
        this.sprites = UIConstants.PRIMARY_BUTTON;
        return this;
    }

    public TextButton withText(Component text) {
        this.setMessage(text);
        return this;
    }

    public TextButton withSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public TextButton withPosition(int x, int y) {
        this.setPosition(x, y);
        return this;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        ResourceLocation sprite = this.sprites.get(this.isActive(), this.isHoveredOrFocused());
        graphics.blitSprite(sprite, getX(), getY(), this.width, this.height);

        Font font = Minecraft.getInstance().font;

        int textX = getX() + (this.width - font.width(this.getMessage())) / 2;
        int textY = getY() + 1 + (this.height - font.lineHeight) / 2;

        graphics.drawString(font, this.getMessage(), textX, textY, this.isHovered() ? this.hoverColor : this.color, false);
    }

    @Override
    public CursorScreen.Cursor getCursor() {
        return !this.isActive() ? CursorScreen.Cursor.DISABLED : CursorScreen.Cursor.POINTER;
    }
}