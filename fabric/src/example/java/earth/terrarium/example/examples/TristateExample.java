package earth.terrarium.example.examples;

import earth.terrarium.example.base.ExampleScreen;
import earth.terrarium.example.base.OlympusExample;
import earth.terrarium.olympus.client.components.Widgets;
import earth.terrarium.olympus.client.components.compound.radio.RadioState;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.util.TriState;

@OlympusExample(id = "tristate", description = "A simple tristate example")
public class TristateExample extends ExampleScreen {

    private final RadioState<TriState> state = RadioState.of(TriState.DEFAULT, 1);

    @Override
    protected void init() {
        var tristateRadio = addRenderableWidget(Widgets.tristate(state));

        FrameLayout.centerInRectangle(tristateRadio, 0, 0, this.width, this.height);
    }
}
