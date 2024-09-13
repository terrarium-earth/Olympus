package earth.terrarium.olympus.client.components.renderers;

import com.teamresourceful.resourcefullib.client.scissor.GuiCloseableScissor;
import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRenderer;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRendererContext;
import earth.terrarium.olympus.client.ui.UIConstants;
import earth.terrarium.olympus.client.utils.State;
import earth.terrarium.olympus.client.utils.UIHelper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

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
        return new IconWidgetRenderer<>(icon);
    }

    public static <T extends AbstractWidget> TextWidgetRenderer<T> text(Component text) {
        return new TextWidgetRenderer<>(text);
    }

    public static <T extends AbstractWidget, V> WidgetRenderer<T> dropdown(State<Boolean> isOpen, State<@Nullable V> value, Font font, Color color, Function<@Nullable V, @NotNull Component> text) {
        return (graphics, context, partialTick) -> {
            try (var ignored = new GuiCloseableScissor(graphics, context.getX(), context.getY(), context.getWidth(), context.getHeight())) {
                int textY = context.getY() + context.getHeight() / 2 - font.lineHeight / 2;
                graphics.drawString(font, text.apply(value.get()), context.getX(), textY, UIHelper.getEnsureAlpha(color));

                graphics.setColor(color.getFloatRed(), color.getFloatBlue(), color.getFloatGreen(), color.getFloatAlpha() == 0 ? 1 : color.getFloatAlpha());
                var size = Math.min(16, context.getHeight());
                int chevY = context.getY() + context.getHeight() / 2 - size / 2;
                graphics.blitSprite(isOpen.get() ? UIConstants.CHEVRON_DOWN : UIConstants.CHEVRON_UP, context.getX() + context.getWidth() - size, chevY, size, size);
            }
        };
    }
}
