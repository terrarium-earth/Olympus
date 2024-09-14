package earth.terrarium.olympus.client.ui.context;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import org.joml.Vector2i;

public enum ContextAlignment {
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
    LEFT_TOP,
    LEFT_BOTTOM,
    RIGHT_TOP,
    RIGHT_BOTTOM;

    /*
    case TOP_LEFT -> new Vector2i(widget.left(), widget.top() - contextHeight);
            case TOP_RIGHT -> new Vector2i(widget.right() - contextWidth, widget.top() - contextHeight);
            case BOTTOM_LEFT -> new Vector2i(widget.left(), widget.bottom());
            case BOTTOM_RIGHT -> new Vector2i(widget.right() - contextWidth, widget.bottom());
            case LEFT -> new Vector2i(widget.left() - contextWidth, widget.top());
            case RIGHT -> new Vector2i(widget.right(), widget.top());
     */

    public Vector2i getPos(AbstractWidget widget, int contextWidth, int contextHeight) {
        return switch (this) {
            case TOP_LEFT -> new Vector2i(widget.getX(), widget.getY() - contextHeight);
            case TOP_RIGHT -> new Vector2i(widget.getX() + widget.getWidth() - contextWidth, widget.getY() - contextHeight);
            case BOTTOM_LEFT -> new Vector2i(widget.getX(), widget.getY() + widget.getHeight());
            case BOTTOM_RIGHT -> new Vector2i(widget.getX() + widget.getWidth() - contextWidth, widget.getY() + widget.getHeight());
            case LEFT_TOP -> new Vector2i(widget.getX() - contextWidth, widget.getY());
            case LEFT_BOTTOM -> new Vector2i(widget.getX() - contextWidth, widget.getY() + widget.getHeight() - contextHeight);
            case RIGHT_TOP -> new Vector2i(widget.getX() + widget.getWidth(), widget.getY());
            case RIGHT_BOTTOM -> new Vector2i(widget.getX() + widget.getWidth(), widget.getY() + widget.getHeight() - contextHeight);
        };
    }
}
