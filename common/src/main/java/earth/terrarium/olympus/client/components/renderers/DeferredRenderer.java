package earth.terrarium.olympus.client.components.renderers;

import earth.terrarium.olympus.client.components.base.renderer.WidgetRenderer;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRendererContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;

import java.util.function.Supplier;

public class DeferredRenderer implements WidgetRenderer<AbstractWidget> {

    private final Supplier<WidgetRenderer<AbstractWidget>> rendererSupplier;

    private DeferredRenderer(Supplier<WidgetRenderer<AbstractWidget>> rendererSupplier) {
        this.rendererSupplier = rendererSupplier;
    }

    @Override
    public void render(GuiGraphics graphics, WidgetRendererContext<AbstractWidget> widget, float partialTick) {
        rendererSupplier.get().render(graphics, widget, partialTick);
    }
}
