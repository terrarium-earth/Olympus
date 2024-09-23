package earth.terrarium.example.examples;

import com.teamresourceful.resourcefullib.common.utils.TriState;
import earth.terrarium.example.base.ExampleScreen;
import earth.terrarium.example.base.OlympusExample;
import earth.terrarium.olympus.client.components.Widgets;
import earth.terrarium.olympus.client.components.buttons.Button;
import earth.terrarium.olympus.client.components.compound.CompoundWidget;
import earth.terrarium.olympus.client.components.compound.radio.RadioState;
import earth.terrarium.olympus.client.components.dropdown.DropdownState;
import earth.terrarium.olympus.client.components.renderers.TristateRenderers;
import earth.terrarium.olympus.client.layouts.Layouts;
import net.minecraft.client.gui.layouts.FrameLayout;

@OlympusExample(id = "tristate", description = "A simple tristate example")
public class TristateExample extends ExampleScreen {

    private final RadioState<TriState> state = RadioState.of(TriState.UNDEFINED, 1);

    @Override
    protected void init() {
        CompoundWidget tristateRadio = addRenderableWidget(Widgets.tristate(state));

        FrameLayout.centerInRectangle(tristateRadio, 0, 0, this.width, this.height);
    }
}
