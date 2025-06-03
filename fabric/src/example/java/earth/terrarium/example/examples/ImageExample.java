package earth.terrarium.example.examples;

import earth.terrarium.example.base.ExampleScreen;
import earth.terrarium.example.base.OlympusExample;
import earth.terrarium.olympus.client.components.Widgets;
import earth.terrarium.olympus.client.components.renderers.WidgetRenderers;
import earth.terrarium.olympus.client.images.BuiltinImageProviders;
import earth.terrarium.olympus.client.layouts.Layouts;
import earth.terrarium.olympus.client.utils.State;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.net.URI;

@OlympusExample(id = "image", description = "External image loading example")
public class ImageExample extends ExampleScreen {

    private final State<URI> image = State.empty();

    @Override
    protected void init() {
        super.init();

        var state = State.of("");

        var layout = Layouts.row()
                .withGap(20)
                .withChild(Widgets.textInput(state).withSize(100, 20))
                .withChild(Widgets.button()
                        .withRenderer(WidgetRenderers.text(Component.literal("Load Image")))
                        .withSize(100, 20)
                        .withCallback(() -> {
                            try {
                                image.set(URI.create(state.get()));
                            } catch (Exception ignored) {}
                        })
                )
                .build(this::addRenderableWidget);

        FrameLayout.alignInRectangle(layout, this.getRectangle(), 0.5f, 1f);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float f) {
        super.render(graphics, mouseX, mouseY, f);

        var uri = image.get();
        if (uri == null) return;

        var minx = this.width / 4f;
        var maxx = this.width * 3f / 4f;

        var miny = 10f;
        var maxy = this.height / 2f;


        Matrix4f matrix = graphics.pose().last().pose();
        graphics.drawSpecial(source -> {
            var buffer = source.getBuffer(RenderType.guiTextured(BuiltinImageProviders.URL.get(uri)));
            buffer.addVertex(matrix, minx, miny, 0).setColor(-1).setUv(0, 0);
            buffer.addVertex(matrix, minx, maxy, 0).setColor(-1).setUv(0, 1);
            buffer.addVertex(matrix, maxx, maxy, 0).setColor(-1).setUv(1, 1);
            buffer.addVertex(matrix, maxx, miny, 0).setColor(-1).setUv(1, 0);
        });
    }
}
