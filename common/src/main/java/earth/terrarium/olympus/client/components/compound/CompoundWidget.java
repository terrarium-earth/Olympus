package earth.terrarium.olympus.client.components.compound;

import com.teamresourceful.resourcefullib.common.utils.TriState;
import earth.terrarium.olympus.client.components.base.BaseParentWidget;
import earth.terrarium.olympus.client.ui.UIConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.util.Mth;

import java.util.function.Consumer;

public class CompoundWidget<T extends Layout> extends BaseParentWidget {
    private final T layout;
    private boolean arranged = false;

    private int overscroll = 0;

    private int xScroll;
    private int yScroll;

    private TriState scrollableX = TriState.FALSE;
    private TriState scrollableY = TriState.FALSE;

    private boolean draggingScrollbarX = false;
    private boolean draggingScrollbarY = false;

    public CompoundWidget(T layout) {
        this.layout = layout;
    }

    public CompoundWidget<T> withContents(Consumer<T> consumer) {
        consumer.accept(layout);
        this.clear();
        layout.visitWidgets(this::addRenderableWidget);
        layout.arrangeElements();
        return this;
    }

    protected boolean isXScrollable() {
        return scrollableX.isTrue() || (scrollableX.isUndefined() && layout.getWidth() > this.getWidth());
    }

    protected boolean isYScrollable() {
        return scrollableY.isTrue() || (scrollableY.isUndefined() && layout.getHeight() > this.getHeight());
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.blitSprite(UIConstants.MODAL_INSET, getX(), getY(), this.getWidth(), this.getHeight());

        if (!arranged) {
            layout.arrangeElements();
            arranged = true;
        }

        layout.setPosition(getX() - xScroll, getY() - yScroll);

        var showScrollX = isXScrollable();
        var showScrollY = isYScrollable();

        var actualWidth = this.getWidth() - (showScrollX ? 6 : 0);
        var actualHeight = this.getHeight() - (showScrollY ? 6 : 0);

        graphics.enableScissor(getX(), getY(), getX() + actualWidth, getY() + actualHeight);
        super.renderWidget(graphics, mouseX, mouseY, partialTick);
        graphics.disableScissor();

        if (showScrollX) {
            var scrollWidth = (int) (actualWidth * (actualWidth / (float) layout.getWidth()));
            var scrollX = (int) (xScroll / (float) layout.getWidth() * actualWidth) + overscroll;
            graphics.blitSprite(UIConstants.SCROLLBAR, getX() + scrollX, getY() + this.getHeight() - 6, scrollWidth, 6);
            graphics.blitSprite(UIConstants.SCROLLBAR_THUMB, getX() + scrollX, getY() + this.getHeight() - 6, scrollWidth, 6);
        }

        if (showScrollY) {
            var scrollHeight = (int) (actualHeight * (actualHeight / (float) layout.getHeight()));
            var scrollY = (int) (yScroll / (float) layout.getHeight() * actualHeight) + overscroll;
            graphics.blitSprite(UIConstants.SCROLLBAR, getX() + this.getWidth() - 6, getY() + scrollY, 6, scrollHeight);
            graphics.blitSprite(UIConstants.SCROLLBAR_THUMB, getX() + this.getWidth() - 6, getY() + scrollY, 6, scrollHeight);
        }
    }

    public boolean isOverScrollbarX(int mouseX, int mouseY) {
        return isXScrollable() && mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() + getHeight() - 6 && mouseY <= getY() + getHeight();
    }

    public boolean isOverScrollbarY(int mouseX, int mouseY) {
        return isYScrollable() && mouseX >= getX() + getWidth() - 6 && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight();
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (draggingScrollbarY) {
            double scrollBarHeight = (this.getHeight() / ((double) layout.getHeight() + overscroll * 2)) * this.getHeight();
            double scrollBarDragY = dragY / (this.getHeight() - scrollBarHeight);
            yScroll = Mth.clamp(yScroll + (int) (scrollBarDragY * (layout.getHeight() + overscroll * 2)), -overscroll, Math.max(0, layout.getHeight() + 6 + overscroll - this.getHeight()));
            return true;
        }

        if (draggingScrollbarX) {
            double scrollBarWidth = (this.getWidth() / ((double) layout.getWidth() + overscroll * 2)) * this.getWidth();
            double scrollBarDragX = dragX / (this.getWidth() - scrollBarWidth);
            xScroll = Mth.clamp(xScroll + (int) (scrollBarDragX * (layout.getWidth() + overscroll * 2)), -overscroll, Math.max(0, layout.getWidth() + 6 + overscroll - this.getWidth()));
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (scrollableX.isTrue()) {
            xScroll = Mth.clamp(xScroll - (int) (scrollX * 10), -overscroll, Math.max(0, layout.getWidth() + 6 + overscroll - this.getWidth()));
        }

        if (scrollableY.isTrue()) {
            yScroll = Mth.clamp(yScroll - (int) (scrollY * 10), -overscroll, Math.max(0, layout.getHeight() + 6 + overscroll - this.getHeight()));
        }

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isOverScrollbarX((int) mouseX, (int) mouseY) && isXScrollable()) {
            draggingScrollbarX = true;
            return true;
        } else if (isOverScrollbarY((int) mouseX, (int) mouseY) && isYScrollable()) {
            draggingScrollbarY = true;
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double d, double e, int i) {
        this.draggingScrollbarX = false;
        this.draggingScrollbarY = false;
        return super.mouseReleased(d, e, i);
    }


    public CompoundWidget<T> withStretchedSize() {
        setSize(layout.getWidth(), layout.getHeight());
        return this;
    }

    public CompoundWidget<T> withStretchedWidth() {
        setWidth(layout.getWidth());
        return this;
    }

    public CompoundWidget<T> withStretchedHeight() {
        setHeight(layout.getHeight());
        return this;
    }

    public CompoundWidget<T> withScrollableX(TriState scrollableX) {
        this.scrollableX = scrollableX;
        return this;
    }

    public CompoundWidget<T> withScrollableY(TriState scrollableY) {
        this.scrollableY = scrollableY;
        return this;
    }

    public CompoundWidget<T> withScrollable(TriState scrollableX, TriState scrollableY) {
        this.scrollableX = scrollableX;
        this.scrollableY = scrollableY;
        return this;
    }

    public CompoundWidget<T> withScroll(int x, int y) {
        this.xScroll = x;
        this.yScroll = y;
        return this;
    }

    public CompoundWidget<T> withOverscroll(int overscroll) {
        this.overscroll = overscroll;
        return this;
    }

    public CompoundWidget<T> withScrollToBottom() {
        this.yScroll = layout.getHeight() - this.getHeight();
        return this;
    }

    public CompoundWidget<T> withScrollToRight() {
        this.xScroll = layout.getWidth() - this.getWidth();
        return this;
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
