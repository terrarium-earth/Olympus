package earth.terrarium.example.examples;

import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.example.base.ExampleScreen;
import earth.terrarium.example.base.OlympusExample;
import earth.terrarium.olympus.client.components.Widgets;
import earth.terrarium.olympus.client.components.dropdown.DropdownState;
import earth.terrarium.olympus.client.components.renderers.WidgetRenderers;
import earth.terrarium.olympus.client.constants.MinecraftColors;
import earth.terrarium.olympus.client.ui.UIIcons;
import earth.terrarium.olympus.client.components.dropdown.DropdownAlignment;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.HashMap;

@OlympusExample(id = "dropdown", description = "A simple dropdown example")
public class DropdownExample extends ExampleScreen {
    public final DropdownState<Color> state1 = DropdownState.of(MinecraftColors.RED);
    public final DropdownState<Color> state2 = DropdownState.empty();
    public final DropdownState<Color> state3 = DropdownState.empty();

    public final HashMap<DropdownAlignment, DropdownState<Color>> states = new HashMap<>();

    public DropdownExample() {
        states.put(DropdownAlignment.BOTTOM_RIGHT, new DropdownState<>(MinecraftColors.RED));
        states.put(DropdownAlignment.TOP_RIGHT, new DropdownState<>(MinecraftColors.RED));
        states.put(DropdownAlignment.TOP_LEFT, new DropdownState<>(MinecraftColors.RED));
        states.put(DropdownAlignment.BOTTOM_LEFT, new DropdownState<>(MinecraftColors.RED));
        states.put(DropdownAlignment.RIGHT_TOP, new DropdownState<>(MinecraftColors.RED));
        states.put(DropdownAlignment.RIGHT_BOTTOM, new DropdownState<>(MinecraftColors.RED));
        states.put(DropdownAlignment.LEFT_TOP, new DropdownState<>(MinecraftColors.RED));
        states.put(DropdownAlignment.LEFT_BOTTOM, new DropdownState<>(MinecraftColors.RED));
    }

    @Override
    protected void init() {
        super.init();

        LinearLayout horizontal = LinearLayout.horizontal().spacing(20);

        LinearLayout secondary = LinearLayout.horizontal().spacing(20);

        horizontal.addChild(Widgets.button()
                .withRenderer(state1.createRenderer((color, bool) -> color == null ? WidgetRenderers.ellpsisWithChevron(bool) : WidgetRenderers.textWithChevron(Component.literal("Color"), bool).withColor(color).withShadow()).withPadding(4, 6))
                .withSize(100, 24)
                .withDropdown(state1)
                        .withOptions(Arrays.asList(MinecraftColors.COLORS))
                        .withEntryRenderer((color) -> WidgetRenderers.text(Component.literal("Color")).withColor(color))
                .build());

        horizontal.addChild(Widgets.button()
                .withRenderer(state2.createRenderer((color, bool) -> color == null ? WidgetRenderers.ellpsisWithChevron(bool) : WidgetRenderers.textWithChevron(Component.literal("Color"), bool).withColor(color)).withPadding(4, 6))
                .withSize(100, 24)
                .withDropdown(state2)
                .withAlignment(DropdownAlignment.TOP_LEFT)
                .withOptions(Arrays.asList(MinecraftColors.COLORS))
                .withEntryRenderer((color) -> WidgetRenderers.text(Component.literal("Color")).withColor(color).withAlignment(0).withPadding(0, 4))
                .build());

        horizontal.addChild(Widgets.dropdown(
                state3,
                Arrays.asList(MinecraftColors.COLORS),
                color -> Component.literal(color.toString()),
                button -> button.withSize(100, 24),
                (ignored) -> {}
        ));

        for (DropdownAlignment align : DropdownAlignment.values()) {
            var state = states.get(align);
            secondary.addChild(Widgets.button()
                    .withRenderer(state.createRenderer((color, bool) -> {
                        var renderer = WidgetRenderers.icon(bool ? UIIcons.CHEVRON_UP : UIIcons.CHEVRON_DOWN);
                        if (color != null) renderer.withColor(color).withShadow();
                        return renderer;
                    }).withCentered(10, 10))
                    .withSize(20, 20)
                    .withDropdown(state)
                    .withSize(100, 150)
                    .withAlignment(align)
                    .withOptions(Arrays.asList(MinecraftColors.COLORS))
                    .withEntryRenderer((color) -> WidgetRenderers.text(Component.literal("Color")).withColor(color))
                    .build()
            );
        }

        horizontal.arrangeElements();
        secondary.arrangeElements();
        FrameLayout.centerInRectangle(horizontal, 0, 0, this.width, this.height);
        FrameLayout.centerInRectangle(secondary, 0, 0, this.width, this.height);
        secondary.setY(secondary.getY() + 50);
        horizontal.visitWidgets(this::addRenderableWidget);
        secondary.visitWidgets(this::addRenderableWidget);
    }
}
