package earth.terrarium.example.examples;

import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.example.base.ExampleScreen;
import earth.terrarium.example.base.OlympusExample;
import earth.terrarium.olympus.client.components.Widgets;
import earth.terrarium.olympus.client.layouts.Layouts;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.network.chat.Component;

@OlympusExample(id = "text", description = "A simple text example" )
public class TextExample extends ExampleScreen {
    public static final String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam";

    @Override
    protected void init() {
        super.init();

        var entries = Layouts.column();

        // rainbow, clipped, with shadow
        entries.withChild(Widgets.text(Component.literal("Hello, World!"), textWidget -> {
            textWidget.withColor(Color.RAINBOW);
            textWidget.withShadow();
            textWidget.setWidth(20);
        }));

        // rainbow, left aligned
        entries.withChild(Widgets.text(Component.literal("Hello, World!"), textWidget -> {
            textWidget.withColor(Color.RAINBOW);
        }));

        // rainbow, right aligned, width 100
        entries.withChild(Widgets.text(Component.literal("Hello, World!"), textWidget -> {
            textWidget.withColor(Color.RAINBOW);
            textWidget.withRightAlignment();
            textWidget.setWidth(100);
        }));

        entries.withChild(Widgets.multilineText(Component.literal(text), 100));

        entries.withChild(Widgets.multilineText(Component.literal(text), 100, widget -> {
            widget.scale(0.8f);
        }));


        entries.withChild(Widgets.multilineText(Component.literal(text), 100, widget -> {
            widget.scale(0.5f);
        }));

        entries.build(this::addRenderableWidget);
        FrameLayout.centerInRectangle(entries, 0, 0, this.width, this.height);
    }
}
