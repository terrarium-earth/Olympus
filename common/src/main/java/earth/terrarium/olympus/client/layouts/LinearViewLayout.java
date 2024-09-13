package earth.terrarium.olympus.client.layouts;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.layouts.LinearLayout;

import java.util.function.Consumer;

public class LinearViewLayout extends BaseLayout<LinearLayout> {

    LinearViewLayout(LinearLayout.Orientation orientation) {
        super(orientation == LinearLayout.Orientation.HORIZONTAL ? LinearLayout.horizontal() : LinearLayout.vertical());
    }

    public LinearViewLayout withSpacing(int spacing) {
        this.layout.spacing(spacing);
        return this;
    }

    public LinearViewLayout withChild(LayoutElement element) {
        this.layout.addChild(element);
        return this;
    }

    public LinearViewLayout withChildren(LayoutElement... elements) {
        for (LayoutElement element : elements) {
            withChild(element);
        }
        return this;
    }

    @Override
    public LinearViewLayout withPosition(int x, int y) {
        return (LinearViewLayout) super.withPosition(x, y);
    }

    @Override
    public LinearViewLayout build(Consumer<AbstractWidget> adder) {
        return (LinearViewLayout) super.build(adder);
    }
}
