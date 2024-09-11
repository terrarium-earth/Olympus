package earth.terrarium.example.examples;

import earth.terrarium.example.base.ExampleScreen;
import earth.terrarium.example.base.OlympusExample;
import earth.terrarium.olympus.client.components.buttons.TextButton;
import earth.terrarium.olympus.client.ui.context.ContextMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.network.chat.Component;

@OlympusExample(id = "context", description = "A simple context menu example")
public class ContextExample extends ExampleScreen {

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 1) {
            ContextMenu.open(menu -> {
                menu.button(Component.literal("Action 1"), (btn) -> System.out.println("Action 1 clicked!"));
                menu.dangerButton(Component.literal("Action 2"), (btn) -> System.out.println("Action 2 clicked!"));
                menu.primaryButton(Component.literal("Action 3"), (btn) -> System.out.println("Action 3 clicked!"));
                menu.divider();
                menu.button(Component.literal("Action 4"), (btn) -> System.out.println("Action 4 clicked!"));
            });
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
