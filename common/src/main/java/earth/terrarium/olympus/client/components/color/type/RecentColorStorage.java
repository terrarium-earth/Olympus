package earth.terrarium.olympus.client.components.color.type;

import com.teamresourceful.resourcefullib.common.color.Color;

import java.util.*;

public class RecentColorStorage {

    private static final Set<Color> colors = new LinkedHashSet<>();

    public static void add(Color color) {
        colors.remove(color);
        colors.add(color);
    }

    public static Collection<Color> getRecentColors(boolean withAlpha) {
        List<Color> colors = new ArrayList<>();
        Collections.reverse(colors);
        return colors;
    }

    public static boolean hasValues() {
        return !colors.isEmpty();
    }
}
