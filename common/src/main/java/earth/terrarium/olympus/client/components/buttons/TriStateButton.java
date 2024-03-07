package earth.terrarium.olympus.client.components.buttons;

import com.teamresourceful.resourcefullib.client.components.CursorWidget;
import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import com.teamresourceful.resourcefullib.common.utils.TriState;
import earth.terrarium.olympus.client.ui.UIConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class TriStateButton extends Button implements CursorWidget {

    private final Supplier<TriState> value;

    public TriStateButton(int width, int height, OnPress onPress, Supplier<TriState> value) {
        super(0, 0, width, height, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
        this.value = value;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        TriState state = this.value.get();
        WidgetSprites sprites = switch (state) {
            case UNDEFINED -> UIConstants.BUTTON;
            case TRUE -> UIConstants.PRIMARY_BUTTON;
            case FALSE -> UIConstants.DANGER_BUTTON;
        };

        ResourceLocation sprite = sprites.get(this.isActive(), this.isHoveredOrFocused());
        graphics.blitSprite(sprite, getX(), getY(), this.width, this.height);

        Font font = Minecraft.getInstance().font;

        String icon = switch (state) {
            case UNDEFINED -> "/";
            case TRUE -> "✔";
            case FALSE -> "❌";
        };

        int textX = getX() + (this.width - font.width(icon)) / 2;
        int textY = getY() + (this.height - font.lineHeight) / 2;

        graphics.drawString(font, icon, textX, textY, state.isUndefined() ? 0x555555 : 0xFFFFFF, false);
    }

    @Override
    public CursorScreen.Cursor getCursor() {
        return !this.isActive() ? CursorScreen.Cursor.DISABLED : CursorScreen.Cursor.POINTER;
    }
}
