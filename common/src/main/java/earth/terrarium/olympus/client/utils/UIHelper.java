package earth.terrarium.olympus.client.utils;

import com.teamresourceful.resourcefullib.common.color.Color;

public class UIHelper {

    public static int getEnsureAlpha(Color color) {
        int value = color.getValue();
        int alpha = value >> 24 & 255;
        return alpha == 0 ? value | 0xff000000 : value;
    }
}
