package earth.terrarium.olympus.client.components.renderers;

import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRenderer;
import earth.terrarium.olympus.client.constants.MinecraftColors;
import earth.terrarium.olympus.client.ui.UIConstants;
import earth.terrarium.olympus.client.ui.UIIcons;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.TriState;

public class TristateRenderers {

    public static Component getText(TriState state) {
        return switch (state) {
            case TRUE -> Component.translatable("olympus.ui.tristate.true");
            case FALSE -> Component.translatable("olympus.ui.tristate.false");
            case DEFAULT -> Component.translatable("olympus.ui.tristate.undefined");
        };
    }

    @Deprecated
    public static Component getText(com.teamresourceful.resourcefullib.common.utils.TriState state) {
        return getText(into(state));
    }

    public static Identifier getIcon(TriState state) {
        return switch (state) {
            case TRUE -> UIIcons.CHECKMARK;
            case FALSE -> UIIcons.CROSS;
            case DEFAULT -> UIIcons.DASH;
        };
    }

    @Deprecated
    public static Identifier getIcon(com.teamresourceful.resourcefullib.common.utils.TriState state) {
        return getIcon(into(state));
    }

    public static Color getColor(TriState state) {
        return switch (state) {
            case TRUE -> MinecraftColors.DARK_GREEN;
            case FALSE -> MinecraftColors.RED;
            case DEFAULT -> MinecraftColors.DARK_GRAY;
        };
    }

    @Deprecated
    public static Color getColor(com.teamresourceful.resourcefullib.common.utils.TriState state) {
        return getColor(into(state));
    }

    public static WidgetSprites getButtonSprites(TriState state) {
        return switch (state) {
            case TRUE -> UIConstants.PRIMARY_BUTTON;
            case FALSE -> UIConstants.DANGER_BUTTON;
            case DEFAULT -> UIConstants.DARK_BUTTON;
        };
    }

    @Deprecated
    public static WidgetSprites getButtonSprites(com.teamresourceful.resourcefullib.common.utils.TriState state) {
        return getButtonSprites(into(state));
    }

    public static <T extends AbstractWidget> WidgetRenderer<T> iconWithText(TriState state) {
        return WidgetRenderers.<T>textWithIcon(getText(state), getIcon(state))
                .withShadow()
                .withTextLeftIconLeft()
                .withIconSize(12)
                .withGap(6)
                .withColor(getColor(state));
    }

    @Deprecated
    public static <T extends AbstractWidget> WidgetRenderer<T> iconWithText(com.teamresourceful.resourcefullib.common.utils.TriState state) {
        return iconWithText(into(state));
    }

    public static <T extends AbstractWidget> WidgetRenderer<T> icon(TriState state) {
        return WidgetRenderers.<T>icon(getIcon(state))
                .withShadow()
                .withColor(getColor(state))
                .withCentered(12, 12);
    }

    @Deprecated
    public static <T extends AbstractWidget> WidgetRenderer<T> icon(com.teamresourceful.resourcefullib.common.utils.TriState state) {
        return icon(into(state));
    }

    private static TriState into(com.teamresourceful.resourcefullib.common.utils.TriState state) {
        return switch (state) {
            case TRUE -> TriState.TRUE;
            case FALSE -> TriState.FALSE;
            case UNDEFINED -> TriState.DEFAULT;
        };
    }
}
