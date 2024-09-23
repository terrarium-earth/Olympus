package earth.terrarium.olympus.client.components;

import com.teamresourceful.resourcefullib.common.utils.TriState;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRenderer;
import earth.terrarium.olympus.client.components.buttons.Button;
import earth.terrarium.olympus.client.components.compound.CompoundWidget;
import earth.terrarium.olympus.client.components.compound.radio.RadioBuilder;
import earth.terrarium.olympus.client.components.compound.radio.RadioState;
import earth.terrarium.olympus.client.components.dropdown.DropdownBuilder;
import earth.terrarium.olympus.client.components.dropdown.DropdownState;
import earth.terrarium.olympus.client.components.map.MapRenderer;
import earth.terrarium.olympus.client.components.map.MapWidget;
import earth.terrarium.olympus.client.components.renderers.TristateRenderers;
import earth.terrarium.olympus.client.components.renderers.WidgetRenderers;
import earth.terrarium.olympus.client.constants.MinecraftColors;
import earth.terrarium.olympus.client.ui.UIConstants;
import earth.terrarium.olympus.client.utils.State;
import earth.terrarium.olympus.client.utils.StateUtils;
import earth.terrarium.olympus.client.utils.Translatable;
import earth.terrarium.olympus.client.utils.UIHelper;
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
        return button(UIHelper.emptyConsumer());
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
        return toggle(state, UIHelper.emptyConsumer());
    }

    public static <T> Button dropdown(DropdownState<T> state, List<T> options, Function<T, Component> optionText, Consumer<Button> factory, Consumer<DropdownBuilder<T>> builder) {
        var button = button((btn -> btn.withRenderer(state.withRenderer((value, open) -> value == null ? WidgetRenderers.ellpsisWithChevron(open) : WidgetRenderers.textWithChevron(optionText.apply(value), open)).withPadding(4, 6))));
        factory.accept(button);

        var dropdown = button.withDropdown(state);
        dropdown.withOptions(options).withEntryRenderer(t -> WidgetRenderers.text(optionText.apply(t)).withColor(MinecraftColors.WHITE).withAlignment(0).withPadding(0, 4));

        builder.accept(dropdown);
        return dropdown.build();
    }

    public static <T extends Enum<?>> Button dropdown(DropdownState<T> state, Class<T> clazz, Consumer<DropdownBuilder<T>> dropdownFactory, Consumer<Button> buttonFactory) {
        return dropdown(
                state,
                List.of(clazz.getEnumConstants()),
                Translatable::toComponent,
                buttonFactory,
                dropdownFactory
        );
    }

    public static <T extends Enum<?>> Button dropdown(DropdownState<T> state, Class<T> clazz) {
        return dropdown(state, clazz, UIHelper.emptyConsumer(), UIHelper.emptyConsumer());
    }

    public static MapWidget map(State<MapRenderer> state, Consumer<MapWidget> factory) {
        var map = new MapWidget(state).withRenderDistanceScale();
        factory.accept(map);
        return map;
    }

    public static MapWidget map(State<MapRenderer> state) {
        return map(state, map -> {
        });
    }

    public static <T> CompoundWidget radio(RadioState<T> state, Consumer<RadioBuilder<T>> builder, Consumer<CompoundWidget> factory) {
        RadioBuilder<T> radioBuilder = new RadioBuilder<>(state);
        builder.accept(radioBuilder);
        factory.accept(radioBuilder.build());
        return radioBuilder.build();
    }

    public static CompoundWidget tristate(RadioState<TriState> state, Consumer<RadioBuilder<TriState>> builder, Consumer<CompoundWidget> factory) {
        RadioBuilder<TriState> radioBuilder = new RadioBuilder<>(state);
        radioBuilder.withoutEntrySprites()
                .withRenderer((triState, depressed) -> WidgetRenderers.layered(
                        WidgetRenderers.sprite(depressed ? TristateRenderers.getButtonSprites(triState) : UIConstants.BUTTON),
                        WidgetRenderers.icon(TristateRenderers.getIcon(triState)).withColor(depressed ? MinecraftColors.WHITE : TristateRenderers.getColor(triState)).withCentered(12, 12).withPadding(0, 0, 2, 0)
                ))
                .withOption(TriState.TRUE)
                .withOption(TriState.UNDEFINED)
                .withOption(TriState.FALSE)
                .withSize(60, 20);
        builder.accept(radioBuilder);
        factory.accept(radioBuilder.build());
        return radioBuilder.build();
    }

    public static CompoundWidget tristate(RadioState<TriState> state) {
        return tristate(state, ignored -> {}, ignored -> {});
    }
}
