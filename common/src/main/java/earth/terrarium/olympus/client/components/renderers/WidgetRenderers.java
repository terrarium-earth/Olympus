package earth.terrarium.olympus.client.components.renderers;

import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRenderer;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRendererContext;
import earth.terrarium.olympus.client.constants.MinecraftColors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class WidgetRenderers {

    public static <T extends AbstractWidget> WidgetRenderer<T> padded(int top, int right, int bottom, int left, WidgetRenderer<T> renderer) {
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

    public static <T extends AbstractWidget> IconWidgetRenderer<T> icon(ResourceLocation icon) {
        return icon(icon, Color.DEFAULT);
    }

    public static <T extends AbstractWidget> IconWidgetRenderer<T> icon(ResourceLocation icon, Color color) {
        return new IconWidgetRenderer<>(icon, color);
    }

    public static <T extends AbstractWidget> TextWidgetRenderer<T> text(Component text) {
        return text(text, Minecraft.getInstance().font, MinecraftColors.DARK_GRAY);
    }

    public static <T extends AbstractWidget> TextWidgetRenderer<T> text(Component text, Color color) {
        return text(text, Minecraft.getInstance().font, color);
    }

    public static <T extends AbstractWidget> TextWidgetRenderer<T> text(Component text, Font font, Color color) {
        return new TextWidgetRenderer<>(text, font, color);
    }
}
