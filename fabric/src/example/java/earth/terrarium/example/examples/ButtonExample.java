package earth.terrarium.example.examples;

import earth.terrarium.example.base.ExampleScreen;
import earth.terrarium.example.base.OlympusExample;
import earth.terrarium.olympus.client.components.buttons.TextButton;
import earth.terrarium.olympus.client.ui.UIConstants;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.network.chat.Component;

@OlympusExample(id = "button", description = "A simple button example")
public class ButtonExample extends ExampleScreen {

    @Override
    protected void init() {
        LinearLayout horizontal = LinearLayout.horizontal().spacing(20);
        horizontal.addChild(TextButton.create(
                100,
                20,
                Component.literal("Enabled"),
                button -> System.out.println("Enabled button clicked!")
        ));
        horizontal.addChild(TextButton.create(
                100,
                20,
                Component.literal("Disabled"),
                button -> System.out.println("Disabled button clicked!")
        )).active = false;
        horizontal.addChild(new TextButton(
                100,
                20,
                0xFFFFFF,
                UIConstants.DANGER_BUTTON,
                Component.literal("Danager"),
                button -> System.out.println("Danger button clicked!")
        ));

        horizontal.arrangeElements();
        FrameLayout.centerInRectangle(horizontal, 0, 0, this.width, this.height);
        horizontal.visitWidgets(this::addRenderableWidget);
    }
}
