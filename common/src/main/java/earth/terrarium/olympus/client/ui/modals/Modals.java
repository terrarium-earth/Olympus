package earth.terrarium.olympus.client.ui.modals;

import earth.terrarium.olympus.client.components.Widgets;
import earth.terrarium.olympus.client.components.renderers.WidgetRenderers;
import earth.terrarium.olympus.client.constants.MinecraftColors;
import earth.terrarium.olympus.client.ui.UIConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class Modals {

    private static void closeScreen() {
        Screen screen = Minecraft.getInstance().screen;
        if (screen == null) return;
        screen.onClose();
    }

    public static ActionModal.Builder action() {
        return ActionModal.builder();
    }

    public static ActionModal.Builder delete(Component title, Component description, Runnable action) {
        return ActionModal.builder()
                .withTitle(title)
                .withContent(description)
                .withAction(Widgets.button()
                        .withRenderer(WidgetRenderers.text(UIConstants.CANCEL))
                        .withSize(80, 24)
                        .withCallback(Modals::closeScreen)
                )
                .withAction(Widgets.button()
                        .withRenderer(WidgetRenderers.text(UIConstants.DELETE).withColor(MinecraftColors.WHITE))
                        .withSize(80, 24)
                        .withTexture(UIConstants.DANGER_BUTTON)
                        .withCallback(() -> {
                            action.run();
                            closeScreen();
                        })
                );
    }
}
