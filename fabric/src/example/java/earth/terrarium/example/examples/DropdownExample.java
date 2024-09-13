package earth.terrarium.example.examples;

import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.example.base.ExampleScreen;
import earth.terrarium.example.base.OlympusExample;
import earth.terrarium.olympus.client.components.Widgets;
import earth.terrarium.olympus.client.components.buttons.Button;
import earth.terrarium.olympus.client.components.renderers.WidgetRenderers;
import earth.terrarium.olympus.client.constants.MinecraftColors;
import earth.terrarium.olympus.client.utils.State;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

@OlympusExample(id = "dropdown", description = "A simple dropdown example")
public class DropdownExample extends ExampleScreen {
    public final State<Button> buttonState = State.of(null);
    public final State<Color> colorState = State.of(MinecraftColors.RED);
    public final State<Boolean> openState = State.of(false);

    @Override
    protected void init() {
        super.init();

        LinearLayout horizontal = LinearLayout.horizontal().spacing(20);

        horizontal.addChild(Widgets.button()
                .withRenderer(WidgetRenderers.dropdown(openState, colorState, font, MinecraftColors.DARK_GRAY, (color) -> {
                    return color == null ? CommonComponents.ELLIPSIS : Component.literal(color.toString());
                }).withPadding(4))
                .withSize(100, 20)
                .withDropdown(buttonState, colorState, openState)
                        .withOptions(Arrays.asList(MinecraftColors.COLORS))
                        .withEntryRenderer((color) -> WidgetRenderers.text(Component.literal("Color")).withColor(color))
                .build());

        horizontal.arrangeElements();
        FrameLayout.centerInRectangle(horizontal, 0, 0, this.width, this.height);
        horizontal.visitWidgets(this::addRenderableWidget);
    }
}
