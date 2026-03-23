package earth.terrarium.example.base;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;

public abstract class ExampleScreen extends Screen {

    public ExampleScreen() {
        super(CommonComponents.EMPTY);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
