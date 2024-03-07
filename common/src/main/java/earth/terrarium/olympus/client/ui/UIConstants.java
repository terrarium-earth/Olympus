package earth.terrarium.olympus.client.ui;

import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class UIConstants {
    
    public static final String MOD_ID = "olympus";

    public static final ResourceLocation MODAL = new ResourceLocation(MOD_ID, "modal/modal");
    public static final ResourceLocation MODAL_HEADER = new ResourceLocation(MOD_ID, "modal/modal_header");

    public static final ResourceLocation SCROLLBAR = new ResourceLocation(MOD_ID, "lists/scroll/bar");
    public static final ResourceLocation SCROLLBAR_THUMB = new ResourceLocation(MOD_ID, "lists/scroll/thumb");

    public static final WidgetSprites MODAL_CLOSE = new WidgetSprites(
        new ResourceLocation(MOD_ID, "modal/buttons/close/normal"),
        new ResourceLocation(MOD_ID, "modal/buttons/close/normal"),
        new ResourceLocation(MOD_ID, "modal/buttons/close/hovered")
    );

    public static final WidgetSprites MODAL_SAVE = new WidgetSprites(
        new ResourceLocation(MOD_ID, "modal/buttons/save/normal"),
        new ResourceLocation(MOD_ID, "modal/buttons/save/normal"),
        new ResourceLocation(MOD_ID, "modal/buttons/save/hovered")
    );

    public static final WidgetSprites BUTTON = new WidgetSprites(
        new ResourceLocation(MOD_ID, "buttons/normal"),
        new ResourceLocation(MOD_ID, "buttons/disabled"),
        new ResourceLocation(MOD_ID, "buttons/hovered")
    );

    public static final WidgetSprites DANGER_BUTTON = new WidgetSprites(
        new ResourceLocation(MOD_ID, "buttons/danger/normal"),
        new ResourceLocation(MOD_ID, "buttons/disabled"),
        new ResourceLocation(MOD_ID, "buttons/danger/hovered")
    );

    public static final WidgetSprites PRIMARY_BUTTON = new WidgetSprites(
        new ResourceLocation(MOD_ID, "buttons/primary/normal"),
        new ResourceLocation(MOD_ID, "buttons/disabled"),
        new ResourceLocation(MOD_ID, "buttons/primary/hovered")
    );

    public static final WidgetSprites LIST_EDIT = new WidgetSprites(
        new ResourceLocation(MOD_ID, "lists/buttons/edit/normal"),
        new ResourceLocation(MOD_ID, "lists/buttons/edit/disabled"),
        new ResourceLocation(MOD_ID, "lists/buttons/edit/hovered")
    );

    public static final WidgetSprites LIST_DELETE = new WidgetSprites(
        new ResourceLocation(MOD_ID, "lists/buttons/delete/normal"),
        new ResourceLocation(MOD_ID, "lists/buttons/delete/disabled"),
        new ResourceLocation(MOD_ID, "lists/buttons/delete/hovered")
    );

    public static final WidgetSprites LIST_UP = new WidgetSprites(
            new ResourceLocation(MOD_ID, "lists/buttons/up/normal"),
            new ResourceLocation(MOD_ID, "lists/buttons/up/disabled"),
            new ResourceLocation(MOD_ID, "lists/buttons/up/hovered")
    );

    public static final WidgetSprites LIST_DOWN = new WidgetSprites(
            new ResourceLocation(MOD_ID, "lists/buttons/down/normal"),
            new ResourceLocation(MOD_ID, "lists/buttons/down/disabled"),
            new ResourceLocation(MOD_ID, "lists/buttons/down/hovered")
    );

    public static final Component BACK = Component.translatable("olympus.ui.back");
    public static final Component CANCEL = Component.translatable("olympus.ui.cancel");
    public static final Component DELETE = Component.translatable("olympus.ui.delete");

}
