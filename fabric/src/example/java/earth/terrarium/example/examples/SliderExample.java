package earth.terrarium.example.examples;

import earth.terrarium.example.base.ExampleScreen;
import earth.terrarium.example.base.OlympusExample;
import earth.terrarium.olympus.client.components.Widgets;
import earth.terrarium.olympus.client.layouts.Layouts;
import earth.terrarium.olympus.client.utils.State;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.layouts.FrameLayout;

@OlympusExample(id = "slider", description = "A simple slider example")
public class SliderExample extends ExampleScreen {

    private final State<Integer> sliderValue = State.of(50);

    @Override
    protected void init() {
        var layout = Layouts.column()
                .withGap(20)
                .withChild(Widgets
                        .slider(this.sliderValue, 0, 100)
                        .withSize(200, 8)
                );

        layout.arrangeElements();
        FrameLayout.centerInRectangle(layout, 0, 0, this.width, this.height);
        layout.visitWidgets(this::addRenderableWidget);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        graphics.text(font, "Slider Value: " + this.sliderValue.get(), 10, 10, -1);
    }
}
