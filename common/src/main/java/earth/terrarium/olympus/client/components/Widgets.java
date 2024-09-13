package earth.terrarium.olympus.client.components;

import earth.terrarium.olympus.client.components.base.renderer.WidgetRenderer;
import earth.terrarium.olympus.client.components.renderers.WidgetRenderers;
import earth.terrarium.olympus.client.components.buttons.Button;
import earth.terrarium.olympus.client.ui.UIConstants;
import earth.terrarium.olympus.client.utils.State;
import earth.terrarium.olympus.client.utils.StateUtils;

import java.util.function.Consumer;

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
}
