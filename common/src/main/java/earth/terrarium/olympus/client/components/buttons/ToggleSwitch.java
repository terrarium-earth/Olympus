package earth.terrarium.olympus.client.components.buttons;

import com.teamresourceful.resourcefullib.client.components.CursorWidget;
import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import earth.terrarium.olympus.client.ui.UIConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BooleanSupplier;

public class ToggleSwitch extends Button implements CursorWidget {

    private final BooleanSupplier value;

    public ToggleSwitch(int width, int height, OnPress onPress, BooleanSupplier value) {
        super(0, 0, width, height, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
        this.value = value;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        WidgetSprites sprites = this.value.getAsBoolean() ? UIConstants.SWITCH_ON : UIConstants.SWITCH;
        ResourceLocation sprite = sprites.get(this.isActive(), this.isHoveredOrFocused());
        graphics.blitSprite(sprite, getX(), getY(), this.width, this.height);
    }

    @Override
    public CursorScreen.Cursor getCursor() {
        return !this.isActive() ? CursorScreen.Cursor.DISABLED : CursorScreen.Cursor.POINTER;
    }
}
