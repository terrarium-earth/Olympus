package earth.terrarium.olympus.client.components.renderers;

import earth.terrarium.olympus.client.components.base.renderer.WidgetRenderer;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRendererContext;
import earth.terrarium.olympus.client.components.dropdown.DropdownState;
import earth.terrarium.olympus.client.ui.UIIcons;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

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

    public static <T extends AbstractWidget, V> WidgetRenderer<T> dropdown(DropdownState<V> state, BiFunction<@Nullable V, Boolean, @NotNull WidgetRenderer<T>> text) {
        return (graphics, context, partialTick) -> {
            WidgetRenderer<T> textRenderer = text.apply(state.get(), state.getOpenState());
            textRenderer.render(graphics, context, partialTick);
        };
    }

    public static <T extends AbstractWidget> TextWithIconWidgetRenderer<T> dropdownText(Component text, boolean open) {
        TextWithIconWidgetRenderer<T> renderer = new TextWithIconWidgetRenderer<>(text(text), icon(open ? UIIcons.CHEVRON_UP : UIIcons.CHEVRON_DOWN));
        return renderer.withTextLeftIconRight();
    }

    public static <T extends AbstractWidget> TextWithIconWidgetRenderer<T> emptyDropdown(boolean open) {
        TextWithIconWidgetRenderer<T> renderer = new TextWithIconWidgetRenderer<>(text(CommonComponents.ELLIPSIS), icon(open ? UIIcons.CHEVRON_UP : UIIcons.CHEVRON_DOWN));
        return renderer.withTextLeftIconRight();
    }
}
