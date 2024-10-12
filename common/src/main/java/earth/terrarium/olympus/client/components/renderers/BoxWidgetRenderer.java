package earth.terrarium.olympus.client.components.renderers;

import com.teamresourceful.resourcefullib.common.color.Color;
import com.teamresourceful.resourcefullib.common.color.ConstantColors;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRenderer;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRendererContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;

public class BoxWidgetRenderer<T extends AbstractWidget> implements WidgetRenderer<T>, ColorableWidget {
    protected Color color = ConstantColors.white;
    protected boolean drawShadow = false;

    @Override
    public void render(GuiGraphics graphics, WidgetRendererContext<T> widget, float partialTick) {
        graphics.fill(widget.getX(), widget.getY(), widget.getX() + widget.getWidth(), widget.getY() + widget.getHeight(), this.color.getValue());

        if (drawShadow) {
            var red = (int) ((color.getFloatRed() / 3f) * 255);
            var green = (int) ((color.getFloatGreen() / 3f) * 255);
            var blue = (int) ((color.getFloatBlue() / 3f) * 255);
            var color = new Color(red, green, blue, this.color.getIntAlpha());
            graphics.fill(widget.getX() + 1, widget.getY() + 1, widget.getX() + widget.getWidth() + 1, widget.getY() + widget.getHeight() + 1, color.getValue());
        }
    }

    @Override
    public BoxWidgetRenderer<T> withColor(Color color) {
        this.color = color;
        return this;
    }

    @Override
    public BoxWidgetRenderer<T> withShadow() {
        this.drawShadow = true;
        return this;
    }
}
