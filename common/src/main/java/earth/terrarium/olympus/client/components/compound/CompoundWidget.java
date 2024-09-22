package earth.terrarium.olympus.client.components.compound;

import com.teamresourceful.resourcefullib.client.components.CursorWidget;
import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import earth.terrarium.olympus.client.components.base.BaseParentWidget;
import earth.terrarium.olympus.client.components.base.BaseWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.FrameLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CompoundWidget extends BaseWidget implements ContainerEventHandler {
    private final FrameLayout layout = new FrameLayout();
    private CursorScreen.Cursor cursor = CursorScreen.Cursor.DEFAULT;
    private boolean isDragging;
    private GuiEventListener focused;
    private boolean arranged = false;

    private final List<AbstractWidget> widgets = new ArrayList<>();

    public CompoundWidget withContents(Consumer<FrameLayout> consumer) {
        consumer.accept(layout);
        layout.visitWidgets(widgets::add);
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
    public int getHeight() {
        return layout.getWidth();
    }

    @Override
    public int getWidth() {
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

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return widgets;
    }

    @Override
    public boolean isDragging() {
        return isDragging;
    }

    @Override
    public void setDragging(boolean isDragging) {
        this.isDragging = isDragging;
    }

    @Override
    public @Nullable GuiEventListener getFocused() {
        return focused;
    }

    @Override
    public void setFocused(@Nullable GuiEventListener focused) {
        if (this.focused != null) {
            this.focused.setFocused(false);
        }

        if (focused != null) {
            focused.setFocused(true);
        }

        this.focused = focused;
    }
}
