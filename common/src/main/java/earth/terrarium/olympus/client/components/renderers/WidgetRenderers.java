package earth.terrarium.olympus.client.components.renderers;

import com.teamresourceful.resourcefullib.common.utils.TriState;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRenderer;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRendererContext;
import earth.terrarium.olympus.client.constants.MinecraftColors;
import earth.terrarium.olympus.client.ui.UIIcons;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class WidgetRenderers {

    public static <T extends AbstractWidget> WidgetRenderer<T> center(int width, int height, WidgetRenderer<T> renderer) {
        return (graphics, context, partialTick) -> {
            int x = context.getX() + (context.getWidth() - width) / 2;
            int y = context.getY() + (context.getHeight() - height) / 2;
            WidgetRendererContext<T> centeredContext = context.copy()
                    .setWidth(width)
                    .setHeight(height)
                    .setX(x)
                    .setY(y);
            renderer.render(graphics, centeredContext, partialTick);
        };
    }

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

    public static <T extends AbstractWidget> TextWithIconWidgetRenderer<T> textWithIcon(Component text, ResourceLocation icon) {
        return new TextWithIconWidgetRenderer<>(text(text), icon(icon));
    }

    public static <T extends AbstractWidget> TextWithIconWidgetRenderer<T> textWithChevron(Component text, boolean open) {
        return WidgetRenderers.<T>textWithIcon(text, open ? UIIcons.CHEVRON_DOWN : UIIcons.CHEVRON_UP)
                .withTextLeftIconRight();
    }

    public static <T extends AbstractWidget> TextWithIconWidgetRenderer<T> ellpsisWithChevron(boolean open) {
        return textWithChevron(CommonComponents.ELLIPSIS, open);
    }

    public static <T extends AbstractWidget> TextWithIconWidgetRenderer<T> tristate(TriState state) {
        Component text = switch (state) {
            case TRUE -> Component.translatable("olympus.ui.tristate.true");
            case FALSE -> Component.translatable("olympus.ui.tristate.false");
            case UNDEFINED -> Component.translatable("olympus.ui.tristate.undefined");
        };
        ResourceLocation icon = switch (state) {
            case TRUE -> UIIcons.CHECKMARK;
            case FALSE -> UIIcons.CROSS;
            case UNDEFINED -> UIIcons.DASH;
        };
        return WidgetRenderers.<T>textWithIcon(text, icon)
                .withShadow()
                .withTextLeftIconLeft()
                .withIconSize(12)
                .withGap(6)
                .withColor(switch (state) {
                    case TRUE -> MinecraftColors.GREEN;
                    case FALSE -> MinecraftColors.RED;
                    case UNDEFINED -> MinecraftColors.GRAY;
                });
    }
}
