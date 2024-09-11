package earth.terrarium.olympus.client.ui;

import net.minecraft.client.gui.components.WidgetSprites;

public class UIIcons {

    public static final WidgetSprites MODRINTH = create("modrinth");

    private static WidgetSprites create(String name) {
        return new WidgetSprites(
                UIConstants.id("icons/%s/normal".formatted(name)),
                UIConstants.id("icons/%s/disabled".formatted(name)),
                UIConstants.id("icons/%s/hovered".formatted(name))
        );
    }
}
