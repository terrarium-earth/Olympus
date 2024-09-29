package earth.terrarium.example.examples;

import earth.terrarium.example.base.ExampleScreen;
import earth.terrarium.example.base.OlympusExample;
import earth.terrarium.olympus.client.components.Widgets;
import earth.terrarium.olympus.client.components.base.ListWidget;
import earth.terrarium.olympus.client.components.renderers.WidgetRenderers;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.network.chat.Component;

@OlympusExample(id = "list", description = "A simple list example")
public class ListExample extends ExampleScreen {
    @Override
    protected void init() {
        super.init();

        var list = new ListWidget(100, 100);

        for (int i = 0; i < 20; i++) {
            list.add(Widgets.button(button -> {
                button.withSize(100, 20);
                button.withRenderer(WidgetRenderers.text(Component.literal("Button")));
            }));
        }

        addRenderableWidget(list);

        FrameLayout.centerInRectangle(list, 0, 0, this.width, this.height);
    }
}