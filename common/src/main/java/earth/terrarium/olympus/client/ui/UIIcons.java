package earth.terrarium.olympus.client.ui;

import net.minecraft.resources.ResourceLocation;

public class UIIcons {

    public static final ResourceLocation MODRINTH = create("modrinth");

    private static ResourceLocation create(String name) {
        return UIConstants.id("icons/%s".formatted(name));
    }
}
