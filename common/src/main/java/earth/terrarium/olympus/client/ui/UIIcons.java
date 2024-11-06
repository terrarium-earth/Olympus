package earth.terrarium.olympus.client.ui;

import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class UIIcons {

    public static final List<ResourceLocation> ICONS = new ArrayList<>();

    public static final ResourceLocation BATTERY_EMPTY = create("battery_empty");
    public static final ResourceLocation BATTERY = BATTERY_EMPTY;

    public static final ResourceLocation BATTERY_LOW = create("battery_low");
    public static final ResourceLocation BATTERY_QUARTER = BATTERY_LOW;

    public static final ResourceLocation BATTERY_MEDIUM = create("battery_medium");
    public static final ResourceLocation BATTERY_HALF = BATTERY_MEDIUM;

    public static final ResourceLocation BATTERY_FULL = create("battery_full");
    public static final ResourceLocation BATTERY_FILLED = BATTERY_FULL;

    public static final ResourceLocation BOOKMARK = create("bookmark");

    public static final ResourceLocation BOX = create("box");
    public static final ResourceLocation ARCHIVE = BOX;

    public static final ResourceLocation CALENDAR = create("calendar");

    public static final ResourceLocation CHECKMARK = create("checkmark");
    public static final ResourceLocation CHECK = CHECKMARK;

    public static final ResourceLocation CLIPBOARD = create("clipboard");

    public static final ResourceLocation CLIPBOARD_TEXT = create("clipboard_text");

    public static final ResourceLocation CONTROLLER = create("controller");
    public static final ResourceLocation GAMEPAD = CONTROLLER;

    public static final ResourceLocation COPY = create("copy");

    public static final ResourceLocation CROP = create("crop");

    public static final ResourceLocation CROSS = create("cross");
    public static final ResourceLocation X = CROSS;

    public static final ResourceLocation DASH = create("dash");
    public static final ResourceLocation MINUS = DASH;

    public static final ResourceLocation DOWNLOAD = create("download");
    public static final ResourceLocation ARROW_DOWN = DOWNLOAD;

    public static final ResourceLocation EDIT = create("edit");
    public static final ResourceLocation PENCIL = EDIT;

    public static final ResourceLocation EXTERNAL_LINK = create("external_link");

    public static final ResourceLocation EYE_DROPPER = create("eye_dropper");
    public static final ResourceLocation COLOR_PICKER = EYE_DROPPER;
    public static final ResourceLocation DROPPER = EYE_DROPPER;

    public static final ResourceLocation FILE = create("file");

    public static final ResourceLocation FOLDER = create("folder");

    public static final ResourceLocation GRID = create("grid");

    public static final ResourceLocation HEART = create("heart");
    public static final ResourceLocation FAVORITE = HEART;

    public static final ResourceLocation LAYOUT = create("layout");
    public static final ResourceLocation TABLE = LAYOUT;

    public static final ResourceLocation LINK = create("link");
    public static final ResourceLocation CHAIN = LINK;

    public static final ResourceLocation LIST = create("list");
    public static final ResourceLocation MENU = LIST;

    public static final ResourceLocation LOGIN = create("login");
    public static final ResourceLocation SIGN_IN = LOGIN;

    public static final ResourceLocation LOGOUT = create("logout");
    public static final ResourceLocation SIGN_OUT = LOGOUT;

    public static final ResourceLocation MAGNIFYING_GLASS = create("magnifying_glass");
    public static final ResourceLocation SEARCH = MAGNIFYING_GLASS;

    public static final ResourceLocation MAGNIFYING_GLASS_MINUS = create("magnifying_glass_minus");
    public static final ResourceLocation SEARCH_MINUS = MAGNIFYING_GLASS_MINUS;

    public static final ResourceLocation MAGNIFYING_GLASS_PLUS = create("magnifying_glass_plus");
    public static final ResourceLocation SEARCH_PLUS = MAGNIFYING_GLASS_PLUS;

    public static final ResourceLocation MAXIMIZE = create("maximize");
    public static final ResourceLocation EXPAND = MAXIMIZE;

    public static final ResourceLocation MINIMIZE = create("minimize");
    public static final ResourceLocation COLLAPSE = MINIMIZE;

    public static final ResourceLocation MODRINTH = create("modrinth");

    public static final ResourceLocation MONITOR = create("monitor");
    public static final ResourceLocation SCREEN = MONITOR;
    public static final ResourceLocation DISPLAY = MONITOR;

    public static final ResourceLocation MOUSE = create("mouse");

    public static final ResourceLocation NEWPAPER = create("newspaper");
    public static final ResourceLocation NEWS = NEWPAPER;

    public static final ResourceLocation PERSON = create("person");
    public static final ResourceLocation USER = PERSON;

    public static final ResourceLocation PERSON_CROSS = create("person_cross");
    public static final ResourceLocation USER_CROSS = PERSON_CROSS;
    public static final ResourceLocation PERSON_X = PERSON_CROSS;
    public static final ResourceLocation USER_X = PERSON_CROSS;

    public static final ResourceLocation PERSON_PLUS = create("person_plus");
    public static final ResourceLocation USER_PLUS = PERSON_PLUS;
    public static final ResourceLocation PERSON_ADD = PERSON_PLUS;
    public static final ResourceLocation USER_ADD = PERSON_PLUS;

    public static final ResourceLocation PERSON_MINUS = create("person_minus");
    public static final ResourceLocation USER_MINUS = PERSON_MINUS;
    public static final ResourceLocation PERSON_REMOVE = PERSON_MINUS;
    public static final ResourceLocation USER_REMOVE = PERSON_MINUS;

    public static final ResourceLocation PLUS = create("plus");
    public static final ResourceLocation ADD = PLUS;

    public static final ResourceLocation SAVE = create("save");
    public static final ResourceLocation DISK = SAVE;

    public static final ResourceLocation SPEAKER = create("speaker");

    public static final ResourceLocation TAG = create("tag");
    public static final ResourceLocation LABEL = TAG;

    public static final ResourceLocation TEST_TUBE = create("test_tube");

    public static final ResourceLocation THUMBS_DOWN = create("thumbs_down");
    public static final ResourceLocation DOWNVOTE = THUMBS_DOWN;

    public static final ResourceLocation THUMBS_UP = create("thumbs_up");
    public static final ResourceLocation UPVOTE = THUMBS_UP;

    public static final ResourceLocation TRASH = create("trash");
    public static final ResourceLocation DELETE = TRASH;

    public static final ResourceLocation TV = create("tv");
    public static final ResourceLocation TELEVISION = TV;

    public static final ResourceLocation VOLUME_HIGH = create("volume_high");

    public static final ResourceLocation VOLUME_LOW = create("volume_low");

    public static final ResourceLocation VOLUME_MEDIUM = create("volume_medium");

    public static final ResourceLocation VOLUME_NONE = create("volume_none");

    public static final ResourceLocation VOLUME_OFF = create("volume_off");
    public static final ResourceLocation MUTE = VOLUME_OFF;

    // These 2 icons are special and are size 10x10 instead of the normal 12x12
    public static final ResourceLocation CHEVRON_DOWN = UIConstants.id("icons/chevron_down");
    public static final ResourceLocation CHEVRON_UP = UIConstants.id("icons/chevron_up");

    private static ResourceLocation create(String name) {
        ResourceLocation icon = UIConstants.id("icons/%s".formatted(name));
        ICONS.add(icon);
        return icon;
    }

    public static List<ResourceLocation> getIcons() {
        return ICONS;
    }
}
