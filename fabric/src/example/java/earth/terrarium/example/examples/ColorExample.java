package earth.terrarium.example.examples;

import com.teamresourceful.resourcefullib.common.color.Color;
import com.teamresourceful.resourcefullib.common.color.ConstantColors;
import earth.terrarium.example.base.ExampleScreen;
import earth.terrarium.example.base.OlympusExample;
import earth.terrarium.olympus.client.components.Widgets;
import earth.terrarium.olympus.client.utils.State;
import net.minecraft.client.gui.layouts.FrameLayout;
import org.apache.commons.lang3.function.Consumers;

@OlympusExample(id = "color", description = "A simple color example")
public class ColorExample extends ExampleScreen {
    public final State<Color> color = State.of(ConstantColors.wheat);
    public final Color[] presets = new Color[] { ConstantColors.red, ConstantColors.green, ConstantColors.blue };

    @Override
    protected void init() {
        var colorPicker = Widgets.colorPicker(color, Consumers.nop(), Consumers.nop());

        this.addRenderableWidget(colorPicker);
        FrameLayout.alignInRectangle(colorPicker, 0, 0, this.width, this.height, 0.5f, 0f);
    }
}
