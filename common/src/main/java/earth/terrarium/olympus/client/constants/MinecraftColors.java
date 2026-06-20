package earth.terrarium.olympus.client.constants;

import com.teamresourceful.resourcefullib.common.color.Color;
import net.minecraft.network.chat.TextColor;

import java.util.Objects;

public class MinecraftColors {

    public static final Color BLACK = create(TextColor.BLACK);
    public static final Color DARK_BLUE = create(TextColor.DARK_BLUE);
    public static final Color DARK_GREEN = create(TextColor.DARK_GREEN);
    public static final Color DARK_AQUA = create(TextColor.DARK_AQUA);
    public static final Color DARK_RED = create(TextColor.DARK_RED);
    public static final Color DARK_PURPLE = create(TextColor.DARK_PURPLE);
    public static final Color GOLD = create(TextColor.GOLD);
    public static final Color GRAY = create(TextColor.GRAY);
    public static final Color DARK_GRAY = create(TextColor.DARK_GRAY);
    public static final Color BLUE = create(TextColor.BLUE);
    public static final Color GREEN = create(TextColor.GREEN);
    public static final Color AQUA = create(TextColor.AQUA);
    public static final Color RED = create(TextColor.RED);
    public static final Color LIGHT_PURPLE = create(TextColor.LIGHT_PURPLE);
    public static final Color YELLOW = create(TextColor.YELLOW);
    public static final Color WHITE = create(TextColor.WHITE);

    public static final Color[] COLORS = new Color[] {
            BLACK, DARK_BLUE, DARK_GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE, GOLD, GRAY,
            DARK_GRAY, BLUE, GREEN, AQUA, RED, LIGHT_PURPLE, YELLOW, WHITE
    };

    private static Color create(TextColor color) {
        return new Color(Objects.requireNonNull(color, "Formatting must be a color.").getValue());
    }
}
