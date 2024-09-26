package earth.terrarium.example.examples;

import earth.terrarium.example.base.ExampleScreen;
import earth.terrarium.example.base.OlympusExample;
import earth.terrarium.olympus.client.components.Widgets;
import earth.terrarium.olympus.client.components.base.ListWidget;
import earth.terrarium.olympus.client.components.compound.CompoundWidget;
import earth.terrarium.olympus.client.components.compound.radio.RadioState;
import earth.terrarium.olympus.client.components.renderers.WidgetRenderers;
import earth.terrarium.olympus.client.layouts.Layouts;
import earth.terrarium.olympus.client.utils.State;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.network.chat.Component;

@OlympusExample(id = "list", description = "A simple list example")
public class ListExmaple extends ExampleScreen {

    @Override
    protected void init() {
        super.init();

        var list = new ListWidget(200, 100);
        list.add(Widgets.button(button -> {
            button.withSize(100, 20);
            button.withRenderer(WidgetRenderers.text(Component.literal("Button 1")));
        }));
        list.add(Widgets.tristate(RadioState.empty()));
        list.add(Widgets.tristate(RadioState.empty()));
        list.add(Widgets.tristate(RadioState.empty()));
        for (int i = 0; i < 4; i++) {
            list.add(Widgets.compound(compoundWidget -> compoundWidget.withContents(contents -> {
                contents.addChild(new StringWidget(Component.literal(" WOAAHHH"), font), LayoutSettings::alignHorizontallyLeft);
                contents.addChild(Widgets.tristate(RadioState.empty()), LayoutSettings::alignHorizontallyRight);
            })));
        }
        list.add(Widgets.text(State.of(""), textBox -> {
            textBox.withSize(200, 20);
        }));

        addRenderableWidget(list);
        addRenderableWidget(Widgets.text(State.of("List Example"), textBox -> {
            textBox.withSize(200, 20);
        }));
        FrameLayout.centerInRectangle(list, 0, 0, this.width, this.height);
    }
}
