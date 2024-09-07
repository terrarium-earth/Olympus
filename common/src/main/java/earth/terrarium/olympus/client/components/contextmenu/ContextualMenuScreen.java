package earth.terrarium.olympus.client.components.contextmenu;

import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface ContextualMenuScreen {

    @Nullable
    ContextMenu getContextMenu();

    static Optional<ContextMenu> getMenu() {
        if (Minecraft.getInstance().screen instanceof ContextualMenuScreen screen) {
            return Optional.ofNullable(screen.getContextMenu());
        }
        return Optional.empty();
    }
}
