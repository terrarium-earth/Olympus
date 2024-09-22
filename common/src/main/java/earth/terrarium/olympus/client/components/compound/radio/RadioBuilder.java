package earth.terrarium.olympus.client.components.compound.radio;

import earth.terrarium.olympus.client.components.Widgets;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRenderer;
import earth.terrarium.olympus.client.components.buttons.Button;
import earth.terrarium.olympus.client.components.compound.CompoundWidget;
import earth.terrarium.olympus.client.components.renderers.WidgetRenderers;
import earth.terrarium.olympus.client.layouts.Layouts;
import earth.terrarium.olympus.client.ui.UIConstants;
import earth.terrarium.olympus.client.utils.State;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.CommonComponents;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class RadioBuilder<T> {
    private final List<T> options = new ArrayList<>();
    private final RadioState<T> state;
    private BiFunction<T, Boolean, WidgetRenderer<Button>> entryRenderer = (t, ignored) -> WidgetRenderers.text(CommonComponents.ELLIPSIS);
    private BiFunction<T, Boolean, WidgetSprites> entrySprites = (t, ignored) -> UIConstants.BUTTON;
    private Consumer<T> action = t -> {};

    private int width = 120;
    private int height = 20;
    private int gap = 0;

    public RadioBuilder(RadioState<T> state) {
        this.state = state;
    }

    public RadioBuilder<T> withOptions(List<T> options) {
        this.options.addAll(options);
        return this;
    }

    public RadioBuilder<T> withOption(T value) {
        this.options.add(value);
        return this;
    }

    public RadioBuilder<T> withRenderer(BiFunction<T, Boolean, WidgetRenderer<Button>> renderer) {
        this.entryRenderer = renderer;
        return this;
    }

    public RadioBuilder<T> withEntrySprites(WidgetSprites sprites, WidgetSprites depressedSprites) {
        this.entrySprites = (ignored, depressed) -> depressed ? depressedSprites : sprites;
        return this;
    }

    public RadioBuilder<T> withEntrySprites(WidgetSprites sprites) {
        this.entrySprites = (ignored, depressed) -> sprites;
        return this;
    }

    public RadioBuilder<T> withEntrySprites(BiFunction<T, Boolean, WidgetSprites> sprites) {
        this.entrySprites = sprites;
        return this;
    }

    public RadioBuilder<T> withCallback(Consumer<T> action) {
        this.action = action;
        return this;
    }

    public RadioBuilder<T> withSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public CompoundWidget build() {
        return new CompoundWidget().withContents(layout -> {
            var radioGroup = Layouts.row().withGap(gap);
            for (int index = 0; index < options.size(); index++) {
                T option = options.get(index);
                int finalIndex = index;
                var button = Widgets.button()
                        .withSize((width - gap * (options.size() - 1)) / options.size(), height)
                        .withRenderer((graphics, widget, partialTick) -> entryRenderer.apply(option, state.getIndex() == finalIndex).render(graphics, widget, partialTick))
                        .withTexture(entrySprites.apply(option, state.getIndex() == index));
                button.withCallback(() -> {
                    if (state.getIndex() != finalIndex) {
                        state.setIndex(finalIndex);
                        state.set(option);
                        action.accept(option);
                    } else {
                        state.setIndex(-1);
                        state.set(null);
                        action.accept(null);
                    }
                    button.withTexture(entrySprites.apply(option, state.getIndex() == finalIndex));
                });
                radioGroup.withChild(button);
            }
            layout.addChild(radioGroup);
        });
    }
}
