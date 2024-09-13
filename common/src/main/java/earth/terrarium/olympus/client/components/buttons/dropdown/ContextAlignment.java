package earth.terrarium.olympus.client.components.buttons.dropdown;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import org.joml.Vector2i;

public enum ContextAlignment {
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
    AUTO;

    public Vector2i getPos(AbstractWidget widget, int contextWidth, int contextHeight) {
        return switch (this) {
            case TOP_LEFT -> new Vector2i(widget.getX(), widget.getY() - contextHeight);
            case TOP_RIGHT -> new Vector2i(widget.getX() + widget.getWidth() - contextWidth, widget.getY() - contextHeight);
            case BOTTOM_LEFT -> new Vector2i(widget.getX(), widget.getY() + widget.getHeight());
            case BOTTOM_RIGHT -> new Vector2i(widget.getX() + widget.getWidth() - contextWidth, widget.getY() + widget.getHeight());
            case AUTO -> {
                var screen = Minecraft.getInstance().screen;
                if (screen == null) {
                    yield new Vector2i(widget.getX(), widget.getY());
                }
                if (widget.getX() + contextWidth > screen.width) {
                    widget.setX(screen.width - contextWidth);
                }
                if (widget.getY() + contextHeight > screen.height) {
                    widget.setY(screen.height - contextHeight);
                }
                yield new Vector2i(widget.getX(), widget.getY());
            }
        };
    }

    public Vector2i getPos(int x, int y, int contextWidth, int contextHeight) {
        var screen = Minecraft.getInstance().screen;
        if (screen == null) return new Vector2i(x, y);

        switch (this) {
            case TOP_LEFT:
                return new Vector2i(x, y - contextHeight);
            case TOP_RIGHT:
                return new Vector2i(x - contextWidth, y - contextHeight);
            case BOTTOM_LEFT:
                return new Vector2i(x, y);
            case BOTTOM_RIGHT:
                return new Vector2i(x - contextWidth, y);
            case AUTO:
                if (x + contextWidth > screen.width) {
                    x = screen.width - contextWidth;
                }
                if (y + contextHeight > screen.height) {
                    y = screen.height - contextHeight;
                }
                return new Vector2i(x, y);
            default:
                return new Vector2i(x, y);
        }
    }
}
