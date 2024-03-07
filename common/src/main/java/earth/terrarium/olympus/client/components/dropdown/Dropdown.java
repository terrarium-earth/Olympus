package earth.terrarium.olympus.client.components.dropdown;

import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import earth.terrarium.olympus.client.components.base.BaseWidget;
import earth.terrarium.olympus.client.ui.UIConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.function.Consumer;

public class Dropdown<T> extends BaseWidget {

    private static final ResourceLocation CHEVRON_DOWN = new ResourceLocation(UIConstants.MOD_ID, "dropdown/chevron_down");
    private static final ResourceLocation CHEVRON_UP = new ResourceLocation(UIConstants.MOD_ID, "dropdown/chevron_up");

    public static final int SELECTED = 0x505050;
    public static final int COLOR = 0xFEFEFE;

    private final Dropdown<?> parent;
    private final Map<T, Component> options;
    private final Consumer<T> onSelect;
    private T selected;

    public Dropdown(Dropdown<T> old, int width, int height, Map<T, Component> options, T selected) {
        this(old, width, height, options, selected, value -> {});
    }

    public Dropdown(Dropdown<T> old, int width, int height, Map<T, Component> options, T selected, Consumer<T> onSelect) {
        super(width, height);

        this.parent = old;
        this.options = options;
        this.onSelect = onSelect;
        this.selected = old != null ? old.selected : selected;
    }

    public boolean isDropdownOpen() {
        return Minecraft.getInstance().screen instanceof DropdownScreen<?> screen && screen.isOriginator(this);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        Font font = Minecraft.getInstance().font;

        ResourceLocation sprite = UIConstants.BUTTON.get(this.active, this.isHovered());
        graphics.blitSprite(sprite, this.getX(), this.getY(), this.getWidth(), this.getHeight());

        int textOffset = (this.height - 8) / 2;

        graphics.drawString(font, getText(this.selected), this.getX() + textOffset, this.getY() + textOffset - 1, SELECTED, false);

        int chevronOffset = (this.height - 16) / 2;

        ResourceLocation chevron = this.isDropdownOpen() ? CHEVRON_UP : CHEVRON_DOWN;
        graphics.blitSprite(chevron, this.getX() + this.width - chevronOffset - 16, this.getY() + chevronOffset, 16, 16);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        DropdownScreen<T> screen = new DropdownScreen<>(Minecraft.getInstance().screen, this);
        Minecraft.getInstance().setScreen(screen);
    }

    public Component getText(T value) {
        return this.options.getOrDefault(value, CommonComponents.ELLIPSIS);
    }

    public Map<T, Component> options() {
        return this.options;
    }

    public T selected() {
        return this.selected;
    }

    public void select(T option) {
        this.selected = option;
        this.onSelect.accept(option);
    }

    @Override
    public CursorScreen.Cursor getCursor() {
        return this.isActive() ? CursorScreen.Cursor.POINTER : CursorScreen.Cursor.DISABLED;
    }

    public boolean is(Dropdown<?> dropdown) {
        return this == dropdown || this.parent != null && this.parent.is(dropdown);
    }
}
