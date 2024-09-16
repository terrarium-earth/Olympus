package earth.terrarium.example.examples;

import com.teamresourceful.resourcefullib.common.utils.TriState;
import earth.terrarium.example.base.ExampleScreen;
import earth.terrarium.example.base.OlympusExample;
import earth.terrarium.olympus.client.components.Widgets;
import earth.terrarium.olympus.client.components.buttons.Button;
import earth.terrarium.olympus.client.components.dropdown.DropdownState;
import earth.terrarium.olympus.client.components.renderers.TristateRenderers;
import earth.terrarium.olympus.client.layouts.Layouts;
import net.minecraft.client.gui.layouts.FrameLayout;

@OlympusExample(id = "tristate", description = "A simple tristate example")
public class TristateExample extends ExampleScreen {

    private final DropdownState<TriState> state1 = DropdownState.of(TriState.UNDEFINED);
    private final DropdownState<TriState> state2 = DropdownState.of(TriState.UNDEFINED);

    @Override
    protected void init() {
        Button iconOnly = Widgets.tristate(
                state1,
                button -> button.withRenderer(
                        state1.withRenderer(state -> TristateRenderers.icon(state).withPadding(0, 0, 4, 0))
                )
        ).withSize(20, 20);
        Button iconWithText = Widgets.tristate(state2).withSize(100, 20);

        FrameLayout.centerInRectangle(
                Layouts.row().withGap(20).withChildren(iconOnly, iconWithText).build(this::addRenderableWidget),
                0, 0, this.width, this.height
        );
    }
}
