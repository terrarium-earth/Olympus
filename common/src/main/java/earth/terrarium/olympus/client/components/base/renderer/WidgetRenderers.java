package earth.terrarium.olympus.client.components.base.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.olympus.client.utils.UIHelper;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class WidgetRenderers {

    private static double getSeconds() {
        return (double) Util.getMillis() / 1000.0;
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

    public static <T extends AbstractWidget> WidgetRenderer<T> icon(ResourceLocation icon) {
        return icon(icon, Color.DEFAULT);
    }

    public static <T extends AbstractWidget> WidgetRenderer<T> icon(ResourceLocation icon, Color color) {
        return (graphics, context, partialTick) -> {
            float red = color.getFloatRed();
            float green = color.getFloatGreen();
            float blue = color.getFloatBlue();
            float alpha = color.getFloatAlpha();
            if (alpha == 0f) alpha = 1f;

            RenderSystem.setShaderColor(red / 3f, green / 3f, blue / 3f, alpha);
            graphics.blitSprite(
                    icon,
                    context.getX() + 1, context.getY() + 1,
                    context.getWidth(), context.getHeight()
            );

            RenderSystem.setShaderColor(red, green, blue, alpha);
            graphics.blitSprite(
                    icon,
                    context.getX(), context.getY(),
                    context.getWidth(), context.getHeight()
            );
        };
    }

    public static <T extends AbstractWidget> WidgetRenderer<T> text(Component text) {
        return text(text, Minecraft.getInstance().font, Color.DEFAULT);
    }

    public static <T extends AbstractWidget> WidgetRenderer<T> text(Component text, Color color) {
        return text(text, Minecraft.getInstance().font, color);
    }

    public static <T extends AbstractWidget> WidgetRenderer<T> text(Component text, Font font, Color color) {
        return (graphics, context, partialTick) -> {
            int textWidth = font.width(text);
            int centerX = context.getX() + context.getWidth() / 2;
            int centerY = context.getY() + context.getHeight() / 2 - font.lineHeight / 2;
            if (textWidth > context.getWidth()) {
                int overhang = textWidth - context.getWidth();
                double e = Math.max((double) overhang * 0.5, 3.0);
                double f = Math.sin(Mth.HALF_PI * Math.cos(Mth.TWO_PI * getSeconds() / e)) / 2.0 + 0.5;
                double g = Mth.lerp(f, 0.0, overhang);
                graphics.enableScissor(context.getLeft(), context.getTop(), context.getRight(), context.getBottom());
                graphics.drawString(font, text, context.getX() - (int) g, centerY, UIHelper.getEnsureAlpha(color));
                graphics.disableScissor();
            } else {
                graphics.drawCenteredString(font, text, centerX, centerY, UIHelper.getEnsureAlpha(color));
            }
        };
    }
}
