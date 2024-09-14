package earth.terrarium.olympus.client.components;

import earth.terrarium.olympus.client.components.base.renderer.WidgetRenderer;
import earth.terrarium.olympus.client.components.dropdown.DropdownBuilder;
import earth.terrarium.olympus.client.components.dropdown.DropdownState;
import earth.terrarium.olympus.client.components.renderers.WidgetRenderers;
import earth.terrarium.olympus.client.components.buttons.Button;
import earth.terrarium.olympus.client.constants.MinecraftColors;
import earth.terrarium.olympus.client.ui.UIConstants;
import earth.terrarium.olympus.client.utils.State;
import earth.terrarium.olympus.client.utils.StateUtils;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public final class Widgets {

    public static Button button(Consumer<Button> factory) {
        Button button = new Button();
        factory.accept(button);
        return button;
    }

    public static Button button() {
        return button(button -> {
        });
    }

    public static Button toggle(State<Boolean> state, Consumer<Button> factory) {
        WidgetRenderer<Button> onRenderer = WidgetRenderers.sprite(UIConstants.SWITCH_ON);
        WidgetRenderer<Button> offRenderer = WidgetRenderers.sprite(UIConstants.SWITCH);
        Consumer<Button> switchFactory = button -> button
                .withCallback(StateUtils.booleanToggle(state))
                .withTexture(null)
                .withRenderer((graphics, context, partialTick) ->
                        (state.get() ? onRenderer : offRenderer).render(graphics, context, partialTick)
                );
        return button(switchFactory.andThen(factory));
    }

    public static Button toggle(State<Boolean> state) {
        return toggle(state, button -> {
        });
    }

    public static <T> Button dropdown(DropdownState<T> state, List<T> options, Function<T, Component> optionText, Consumer<Button> factory, Consumer<DropdownBuilder<T>> builder) {
        var button = button(factory.andThen(btn -> btn.withRenderer(state.withRenderer((value, open) -> value == null ? WidgetRenderers.ellpsisWithChevron(open) : WidgetRenderers.textWithChevron(optionText.apply(value), open)).withPadding(4, 6))));

        var dropdown = button.withDropdown(state);
        dropdown.withOptions(options).withEntryRenderer(t -> WidgetRenderers.text(optionText.apply(t)).withColor(MinecraftColors.WHITE).withAlignment(0).withPadding(0, 4));

        builder.accept(dropdown);
        return dropdown.build();
    }
}
