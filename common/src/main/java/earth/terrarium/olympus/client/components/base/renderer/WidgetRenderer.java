package earth.terrarium.olympus.client.components.base.renderer;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;

/**
 * A generic renderer to be used within a widget to render additional content.
 */
public interface WidgetRenderer<T extends AbstractWidget> {

    void render(GuiGraphics graphics, WidgetRendererContext<T> widget, float partialTick);

    default WidgetRenderer<T> withPadding(int padding) {
        return withPadding(padding, padding, padding, padding);
    }

    default WidgetRenderer<T> withPadding(int left, int top, int right, int bottom) {
        return WidgetRenderers.padded(left, top, right, bottom, this);
    }

    static <T extends AbstractWidget> WidgetRenderer<T> empty() {
        return (graphics, context, partialTick) -> {};
    }

}
