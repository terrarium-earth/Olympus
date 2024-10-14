package earth.terrarium.olympus.client.ui;

import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("unused")
public class UIConstants {
    
    public static final String MOD_ID = "olympus";

    public static final ResourceLocation MODAL = id("modal/modal");
    public static final ResourceLocation MODAL_HEADER = id("modal/modal_header");
    public static final ResourceLocation MODAL_FOOTER = id("modal/modal_footer");
    public static final ResourceLocation MODAL_INSET = id("modal/modal_inset");

    public static final ResourceLocation SCROLLBAR = id("lists/scroll/bar");
    public static final ResourceLocation SCROLLBAR_THUMB = id("lists/scroll/thumb");

    public static final ResourceLocation LIST_BG = id("lists/background");

    public static final ResourceLocation CONTEXT_DIVIDER = id("context/divider");

    public static final WidgetSprites TEXTBOX = new WidgetSprites(
        id("textbox/normal"),
        id("textbox/disabled"),
        id("textbox/hovered")
    );

    public static final WidgetSprites LIST_ENTRY = new WidgetSprites(
        id("lists/entry/normal"),
        id("lists/entry/normal"),
        id("lists/entry/hovered")
    );

    public static final WidgetSprites MODAL_CLOSE = new WidgetSprites(
        id("modal/buttons/close/normal"),
        id("modal/buttons/close/normal"),
        id("modal/buttons/close/hovered")
    );

    public static final WidgetSprites MODAL_SAVE = new WidgetSprites(
        id("modal/buttons/save/normal"),
        id("modal/buttons/save/normal"),
        id("modal/buttons/save/hovered")
    );

    public static final WidgetSprites MODAL_REFRESH = new WidgetSprites(
        id("modal/buttons/refresh/normal"),
        id("modal/buttons/refresh/normal"),
        id("modal/buttons/refresh/hovered")
    );

    public static final WidgetSprites BUTTON = new WidgetSprites(
        id("buttons/normal"),
        id("buttons/disabled"),
        id("buttons/hovered")
    );

    public static final WidgetSprites DANGER_BUTTON = new WidgetSprites(
        id("buttons/danger/normal"),
        id("buttons/disabled"),
        id("buttons/danger/hovered")
    );

    public static final WidgetSprites PRIMARY_BUTTON = new WidgetSprites(
        id("buttons/primary/normal"),
        id("buttons/disabled"),
        id("buttons/primary/hovered")
    );

    public static final WidgetSprites DARK_BUTTON = new WidgetSprites(
        id("buttons/dark/normal"),
        id("buttons/disabled"),
        id("buttons/dark/hovered")
    );

    public static final WidgetSprites SWITCH = new WidgetSprites(
            id("buttons/switch/off/normal"),
            id("buttons/switch/off/disabled"),
            id("buttons/switch/off/hovered"));

    public static final WidgetSprites SWITCH_ON = new WidgetSprites(
            id("buttons/switch/on/normal"),
            id("buttons/switch/on/disabled"),
            id("buttons/switch/on/hovered")
    );

    public static final WidgetSprites LIST_EDIT = new WidgetSprites(
        id("lists/buttons/edit/normal"),
        id("lists/buttons/edit/disabled"),
        id("lists/buttons/edit/hovered")
    );

    public static final WidgetSprites LIST_DELETE = new WidgetSprites(
        id("lists/buttons/delete/normal"),
        id("lists/buttons/delete/disabled"),
        id("lists/buttons/delete/hovered")
    );

    public static final WidgetSprites LIST_UP = new WidgetSprites(
            id("lists/buttons/up/normal"),
            id("lists/buttons/up/disabled"),
            id("lists/buttons/up/hovered")
    );

    public static final WidgetSprites LIST_DOWN = new WidgetSprites(
            id("lists/buttons/down/normal"),
            id("lists/buttons/down/disabled"),
            id("lists/buttons/down/hovered")
    );

    public static final Component BACK = Component.translatable("olympus.ui.back");
    public static final Component CANCEL = Component.translatable("olympus.ui.cancel");
    public static final Component DELETE = Component.translatable("olympus.ui.delete");
    public static final Component REFRESH = Component.translatable("olympus.ui.refresh");
    public static final Component LOADING = Component.translatable("olympus.ui.loading");

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

}
