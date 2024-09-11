package earth.terrarium.olympus.client.components.dropdown;

import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import earth.terrarium.olympus.client.components.base.BaseWidget;
import earth.terrarium.olympus.client.components.base.ListWidget;
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
    public static final int SELECTED = 0x505050;

    protected final Dropdown<?> parent;
    protected final Map<T, Component> options;
    protected final Consumer<T> onSelect;
    protected T selected;

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

        renderButton(graphics, mouseX, mouseY, partialTick);

        int textOffset = (this.height - 8) / 2;

        graphics.drawString(font, getText(this.selected), this.getX() + textOffset, this.getY() + textOffset - 1, getFontColor(), false);

        int chevronOffset = (this.height - 16) / 2;

        graphics.blitSprite(getChevronTexture(), this.getX() + this.width - chevronOffset - 16, this.getY() + chevronOffset, 16, 16);
    }

    protected void renderButton(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        ResourceLocation sprite = UIConstants.BUTTON.get(this.active, this.isHovered());
        graphics.blitSprite(sprite, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    protected ResourceLocation getChevronTexture() {
        return this.isDropdownOpen() ? UIConstants.CHEVRON_UP : UIConstants.CHEVRON_DOWN;
    }

    protected int getFontColor() {
        return SELECTED;
    }

    public int getEntryHeight() {
        return 24;
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

    public void select(T selected) {
        this.selected = selected;
        this.onSelect.accept(selected);
    }

    @Override
    public CursorScreen.Cursor getCursor() {
        return this.isActive() ? CursorScreen.Cursor.POINTER : CursorScreen.Cursor.DISABLED;
    }

    public boolean is(Dropdown<?> dropdown) {
        return this == dropdown || this.parent != null && this.parent.is(dropdown);
    }

    public void initEntries(ListWidget list, int width, Runnable action) {
        for (var entry : options.entrySet()) {
            T value = entry.getKey();
            list.add(new DropdownEntry<>(width, getEntryHeight(), this, value, () -> {
                select(value);
                action.run();
            }));
        }
    }

    public void renderEntriesBackground(GuiGraphics graphics, int x, int y, int width, int height, int mouseX, int mouseY, float partialTick) {
        graphics.blitSprite(UIConstants.LIST_BG, x, y, width, height);
    }
}
