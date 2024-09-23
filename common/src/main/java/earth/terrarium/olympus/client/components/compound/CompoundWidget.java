package earth.terrarium.olympus.client.components.compound;

import com.teamresourceful.resourcefullib.client.components.CursorWidget;
import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import earth.terrarium.olympus.client.components.base.BaseParentWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.layouts.FrameLayout;

import java.util.function.Consumer;

public class CompoundWidget extends BaseParentWidget implements ContainerEventHandler {
    private final FrameLayout layout = new FrameLayout();
    private CursorScreen.Cursor cursor = CursorScreen.Cursor.DEFAULT;
    private boolean arranged = false;

    public CompoundWidget withContents(Consumer<FrameLayout> consumer) {
        consumer.accept(layout);
        this.clear();
        layout.visitWidgets(this::addRenderableWidget);
        layout.arrangeElements();
        return this;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (!arranged) {
            layout.arrangeElements();
            arranged = true;
        }
        layout.visitWidgets(widget -> {
            widget.render(graphics, mouseX, mouseY, partialTick);
            if(widget.isMouseOver(mouseX, mouseY) && widget instanceof CursorWidget cursorWidget) {
                cursor = cursorWidget.getCursor();
            }
        });
    }

    @Override
    public CursorScreen.Cursor getCursor() {
        return cursor;
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        layout.setMinWidth(width);
        this.arranged = false;
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        layout.setMinHeight(height);
        this.arranged = false;
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        layout.setMinWidth(width);
        layout.setMinHeight(height);
        this.arranged = false;
    }

    @Override
    public int getWidth() {
        return layout.getWidth();
    }

    @Override
    public int getHeight() {
        return layout.getHeight();
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        layout.setX(x);
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        layout.setY(y);
    }
}
