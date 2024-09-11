package earth.terrarium.olympus.client.components.base.renderer;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class WidgetRenderers {

    private static double getSeconds() {
        return (double) Util.getMillis() / 1000.0;
    }

    public static <T extends AbstractWidget> WidgetRenderer<T> padded(int padding, WidgetRenderer<T> renderer) {
        return (graphics, context, partialTick) -> {
            WidgetRendererContext<T> paddedContext = context.copy()
                    .setWidth(context.getWidth() - padding * 2)
                    .setHeight(context.getHeight() - padding * 2)
                    .setX(context.getX() + padding)
                    .setY(context.getY() + padding);
            renderer.render(graphics, paddedContext, partialTick);
        };
    }

    public static <T extends AbstractWidget> WidgetRenderer<T> sprite(WidgetSprites sprites) {
        return (graphics, context, partialTick) -> graphics.blitSprite(
                sprites.get(context.getWidget().isActive(), context.getWidget().isHoveredOrFocused()),
                context.getX(), context.getY(),
                context.getWidth(), context.getHeight()
        );
    }

    public static <T extends AbstractWidget> WidgetRenderer<T> text(Component text, int color) {
        return text(text, Minecraft.getInstance().font, color);
    }

    public static <T extends AbstractWidget> WidgetRenderer<T> text(Component text, Font font, int color) {
        return (graphics, context, partialTick) -> {
            int textWidth = font.width(text);
            int centerX = context.getX() + context.getWidth() / 2;
            int centerY = context.getY() + context.getHeight() / 2 - font.lineHeight / 2;
            if (textWidth > context.getWidth()) {
                int overhang = textWidth - context.getWidth();
                double e = Math.max((double) overhang * 0.5, 3.0);
                double f = Math.sin(1.5707963267948966 * Math.cos(6.283185307179586 * getSeconds() / e)) / 2.0 + 0.5;
                double g = Mth.lerp(f, 0.0, overhang);
                graphics.enableScissor(context.getLeft(), context.getTop(), context.getRight(), context.getBottom());
                graphics.drawString(font, text, context.getX() - (int) g, centerY, color);
                graphics.disableScissor();
            } else {
                graphics.drawCenteredString(font, text, centerX, centerY, color);
            }
        };
    }
}
