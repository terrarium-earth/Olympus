package earth.terrarium.olympus.client.ui;

import net.minecraft.resources.ResourceLocation;

public class UIIcons {

    public static final ResourceLocation MODRINTH = create("modrinth");

    public static final ResourceLocation CHEVRON_DOWN = create("chevron_down");
    public static final ResourceLocation CHEVRON_UP = create("chevron_up");


    private static ResourceLocation create(String name) {
        return UIConstants.id("icons/%s".formatted(name));
    }
}
