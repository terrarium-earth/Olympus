package earth.terrarium.olympus.client.components.renderers;

import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRenderer;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRendererContext;
import earth.terrarium.olympus.client.utils.UIHelper;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class TextWidgetRenderer<T extends AbstractWidget> implements WidgetRenderer<T> {

    private final Component text;
    private final Font font;
    private final Color color;
    private boolean drawShadow = false;

    TextWidgetRenderer(Component text, Font font, Color color) {
        this.text = text;
        this.font = font;
        this.color = color;
    }

    @Override
    public void render(GuiGraphics graphics, WidgetRendererContext<T> context, float partialTick) {
        int textWidth = this.font.width(this.text);
        int centerX = context.getX() + context.getWidth() / 2;
        int centerY = context.getY() + context.getHeight() / 2 - font.lineHeight / 2 - 1;
        double seconds = (double) Util.getMillis() / 1000.0;
        if (textWidth > context.getWidth()) {
            int overhang = textWidth - context.getWidth();
            double e = Math.max((double) overhang * 0.5, 3.0);
            double f = Math.sin(Mth.HALF_PI * Math.cos(Mth.TWO_PI * seconds / e)) / 2.0 + 0.5;
            double g = Mth.lerp(f, 0.0, overhang);
            graphics.enableScissor(context.getLeft(), context.getTop(), context.getRight(), context.getBottom());
            graphics.drawString(this.font, this.text, context.getX() - (int) g, centerY, UIHelper.getEnsureAlpha(color), this.drawShadow);
            graphics.disableScissor();
        } else {
            graphics.drawString(this.font, this.text, centerX - textWidth / 2, centerY, UIHelper.getEnsureAlpha(color), this.drawShadow);
        }
    }

    public TextWidgetRenderer<T> withShadow() {
        this.drawShadow = true;
        return this;
    }
}
