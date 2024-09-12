package earth.terrarium.olympus.client.components.base.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
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
        return padded(padding, padding, padding, padding, renderer);
    }

    public static <T extends AbstractWidget> WidgetRenderer<T> padded(int left, int top, int right, int bottom, WidgetRenderer<T> renderer) {
        return (graphics, context, partialTick) -> {
            WidgetRendererContext<T> paddedContext = context.copy()
                    .setWidth(context.getWidth() - left - right)
                    .setHeight(context.getHeight() - top - bottom)
                    .setX(context.getX() + left)
                    .setY(context.getY() + top);
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

    public static <T extends AbstractWidget> WidgetRenderer<T> icon(WidgetSprites sprites) {
        return icon(sprites, 0xFFFFFFFF);
    }

    public static <T extends AbstractWidget> WidgetRenderer<T> icon(WidgetSprites sprites, int color) {
        var red = (color >> 16 & 255) / 255.0F;
        var green = (color >> 8 & 255) / 255.0F;
        var blue = (color & 255) / 255.0F;
        var alpha = (color >> 24 & 255) / 255.0F;
        return (graphics, context, partialTick) -> {
            RenderSystem.setShaderColor(red / 3f, green / 3f, blue / 3f, alpha);
            graphics.blitSprite(
                    sprites.get(context.getWidget().isActive(), context.getWidget().isHoveredOrFocused()),
                    context.getX() + 1, context.getY() + 1,
                    context.getWidth(), context.getHeight()
            );

            RenderSystem.setShaderColor(red, green, blue, alpha);
            graphics.blitSprite(
                    sprites.get(context.getWidget().isActive(), context.getWidget().isHoveredOrFocused()),
                    context.getX(), context.getY(),
                    context.getWidth(), context.getHeight()
            );
        };
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
