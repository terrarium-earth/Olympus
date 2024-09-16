package earth.terrarium.example.examples;

import earth.terrarium.example.base.ExampleScreen;
import earth.terrarium.example.base.OlympusExample;
import earth.terrarium.olympus.client.components.map.MapRenderer;
import earth.terrarium.olympus.client.components.map.MapWidget;
import earth.terrarium.olympus.client.utils.State;
import net.minecraft.client.gui.layouts.FrameLayout;

@OlympusExample(id = "maps", description = "A simple example of using maps")
public class MapExample extends ExampleScreen {
    public State<MapRenderer> mapState = MapWidget.emptyState();

    @Override
    protected void init() {
        var map = MapWidget.create(mapState, 100);
        FrameLayout.centerInRectangle(map, 0, 0, this.width, this.height);
        this.addRenderableWidget(map);
    }
}
