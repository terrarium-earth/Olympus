package earth.terrarium.olympus.client.ui;

import net.minecraft.resources.ResourceLocation;

public class UIIcons {

    public static final ResourceLocation MODRINTH = create("modrinth");

    public static final ResourceLocation CHEVRON_DOWN = create("chevron_down");
    public static final ResourceLocation CHEVRON_UP = create("chevron_up");

    public static final ResourceLocation CHECKMARK = create("checkmark");
    public static final ResourceLocation CROSS = create("cross");
    public static final ResourceLocation DASH = create("dash");
    public static final ResourceLocation TRASH = create("trash");
    public static final ResourceLocation EYE_DROPPER = create("eye_dropper");

    private static ResourceLocation create(String name) {
        return UIConstants.id("icons/%s".formatted(name));
    }
}
