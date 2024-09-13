package earth.terrarium.olympus.client.layouts;

import net.minecraft.client.gui.layouts.LinearLayout;

public final class Layouts {

    public static ViewLayout layout() {
        return new ViewLayout();
    }

    public static LinearViewLayout row() {
        return new LinearViewLayout(LinearLayout.Orientation.HORIZONTAL);
    }

    public static LinearViewLayout column() {
        return new LinearViewLayout(LinearLayout.Orientation.VERTICAL);
    }

    public static GridViewLayout rows(int amount) {
        return new GridViewLayout(LinearLayout.Orientation.VERTICAL, amount);
    }

    public static GridViewLayout columns(int amount) {
        return new GridViewLayout(LinearLayout.Orientation.HORIZONTAL, amount);
    }
}
