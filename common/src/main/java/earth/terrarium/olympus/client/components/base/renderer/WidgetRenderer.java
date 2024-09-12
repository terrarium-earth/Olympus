package earth.terrarium.olympus.client.components.base.renderer;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;

/**
 * A generic renderer to be used within a widget to render additional content.
 */
public interface WidgetRenderer<T extends AbstractWidget> {

    void render(GuiGraphics graphics, WidgetRendererContext<T> widget, float partialTick);

    default WidgetRenderer<T> withPadding(int padding) {
        return WidgetRenderers.padded(padding, padding, padding, padding, this);
    }

    default WidgetRenderer<T> withPadding(int horizontal, int vertical) {
        return WidgetRenderers.padded(horizontal, vertical, horizontal, vertical, this);
    }

    default WidgetRenderer<T> withPadding(int top, int right, int bottom, int left) {
        return WidgetRenderers.padded(left, top, right, bottom, this);
    }

    static <T extends AbstractWidget> WidgetRenderer<T> empty() {
        return (graphics, context, partialTick) -> {};
    }

}
