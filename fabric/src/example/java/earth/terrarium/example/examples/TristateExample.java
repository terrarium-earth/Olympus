package earth.terrarium.example.examples;

import com.teamresourceful.resourcefullib.common.utils.TriState;
import earth.terrarium.example.base.ExampleScreen;
import earth.terrarium.example.base.OlympusExample;
import earth.terrarium.olympus.client.components.Widgets;
import earth.terrarium.olympus.client.components.base.BaseWidget;
import earth.terrarium.olympus.client.utils.State;
import net.minecraft.client.gui.layouts.FrameLayout;

@OlympusExample(id = "tristate", description = "A simple tristate example")
public class TristateExample extends ExampleScreen {

    private final State<TriState> state = State.of(TriState.UNDEFINED);

    @Override
    protected void init() {
        BaseWidget button = addRenderableWidget(Widgets.tristate(state)
                .withSize(100, 20)
        );

        FrameLayout.centerInRectangle(button, 0, 0, this.width, this.height);
    }
}
