package earth.terrarium.olympus.client.components.buttons.dropdown;

import earth.terrarium.olympus.client.components.base.renderer.WidgetRenderer;
import earth.terrarium.olympus.client.components.buttons.Button;
import earth.terrarium.olympus.client.ui.UIConstants;
import earth.terrarium.olympus.client.ui.context.ContextMenu;
import earth.terrarium.olympus.client.utils.State;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DropdownBuilder<T> {
    private final Button button;
    private final State<T> state;
    private final State<Boolean> openState;
    private final List<T> options = new ArrayList<>();

    private ContextAlignment alignment = ContextAlignment.BOTTOM_LEFT;
    private ResourceLocation background = UIConstants.LIST_BG;
    private WidgetSprites entrySprites = UIConstants.LIST_ENTRY;
    private Function<T, WidgetRenderer<? super Button>> entryRenderer;

    private int width = 100;
    private int height = 150;
    private int entryHeight = 20;

    public DropdownBuilder(Button button, State<T> state, State<Boolean> openState) {
        this.button = button;
        this.state = state;
        this.openState = openState;
        this.width = button.getWidth();
    }

    public DropdownBuilder<T> withOptions(List<T> options) {
        this.options.addAll(options);
        return this;
    }

    public DropdownBuilder<T> withOption(T value) {
        this.options.add(value);
        return this;
    }

    public DropdownBuilder<T> withTexture(ResourceLocation sprite) {
        this.background = sprite;
        return this;
    }

    public DropdownBuilder<T> withAlignment(ContextAlignment alignment) {
        return this;
    }

    public DropdownBuilder<T> withEntrySprites(WidgetSprites sprites) {
        this.entrySprites = sprites;
        return this;
    }

    public DropdownBuilder<T> withEntryRenderer(Function<T, WidgetRenderer<? super Button>> renderer) {
        this.entryRenderer = renderer;
        return this;
    }

    public DropdownBuilder<T> withSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public DropdownBuilder<T> withEntryHeight(int height) {
        this.entryHeight = height;
        return this;
    }

    public Button build() {
        return button.withCallback(() -> {
            var pos = alignment.getPos(this.button, width, height);
            ContextMenu.open(pos.x, pos.y);
        });
    }
}
