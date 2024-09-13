package earth.terrarium.example.examples;

import earth.terrarium.example.base.ExampleScreen;
import earth.terrarium.example.base.OlympusExample;
import earth.terrarium.olympus.client.components.Widgets;
import earth.terrarium.olympus.client.components.base.BaseWidget;
import earth.terrarium.olympus.client.components.renderers.WidgetRenderers;
import earth.terrarium.olympus.client.ui.modals.DeleteConfirmModal;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.network.chat.Component;

@OlympusExample(id = "modal", description = "A simple modal example")
public class ModalExample extends ExampleScreen {

    @Override
    protected void init() {
        BaseWidget button = addRenderableWidget(Widgets.button()
                .withCallback(() -> DeleteConfirmModal.open(
                        Component.literal("Delete item"),
                        Component.literal("Are you sure you want to delete this item?"),
                        () -> System.out.println("Item deleted!")
                ))
                .withRenderer(WidgetRenderers.text(Component.literal("Open modal")))
                .withSize(100, 20)
        );

        FrameLayout.centerInRectangle(button, 0, 0, this.width, this.height);
    }
}
