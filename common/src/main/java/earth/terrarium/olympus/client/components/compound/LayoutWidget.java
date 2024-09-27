package earth.terrarium.olympus.client.components.compound;

import com.teamresourceful.resourcefullib.common.utils.TriState;
import earth.terrarium.olympus.client.components.base.BaseParentWidget;
import earth.terrarium.olympus.client.ui.UIConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.function.Consumers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class LayoutWidget<T extends Layout> extends BaseParentWidget {
    private final T layout;
    private boolean arranged = false;

    private int overscroll = 0;

    private int xScroll;
    private int yScroll;

    private TriState scrollableX = TriState.FALSE;
    private TriState scrollableY = TriState.FALSE;

    private boolean draggingScrollbarX = false;
    private boolean draggingScrollbarY = false;

    private final List<BiConsumer<LayoutWidget<T>, T>> widthCallbacks = new ArrayList<>();
    private final List<BiConsumer<LayoutWidget<T>, T>> heightCallbacks = new ArrayList<>();
    private final List<BiConsumer<LayoutWidget<T>, T>> layoutCallbacks = new ArrayList<>();

    public LayoutWidget(T layout) {
        this.layout = layout;
    }

    public LayoutWidget<T> withContents(Consumer<T> consumer) {
        consumer.accept(layout);
        this.clear();
        layout.arrangeElements();
        layout.visitWidgets(this::addRenderableWidget);

        layoutCallbacks.forEach(callback -> callback.accept(this, layout));
        widthCallbacks.forEach(callback -> callback.accept(this, layout));
        heightCallbacks.forEach(callback -> callback.accept(this, layout));
        return this;
    }

    protected boolean isXScrollbarVisible() {
        return scrollableX.isTrue() || (scrollableX.isUndefined() && layout.getWidth() > this.getWidth());
    }

    protected boolean isYScrollbarVisible() {
        return scrollableY.isTrue() || (scrollableY.isUndefined() && layout.getHeight() > this.getHeight());
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (!arranged) {
            withContents(Consumers.nop());
            arranged = true;
        }

        layout.setPosition(getX() - xScroll, getY() - yScroll);

        graphics.enableScissor(getX(), getY(), getX() + getViewWidth(), getY() + getViewHeight());
        super.renderWidget(graphics, mouseX, mouseY, partialTick);
        graphics.disableScissor();

        if (isXScrollbarVisible()) {
            var scrollWidth = (int) (getViewWidth() * (getViewWidth() / (float) layout.getWidth()));
            var scrollX = (int) (xScroll / (float) layout.getWidth() * getViewWidth()) + overscroll;
            graphics.blitSprite(UIConstants.SCROLLBAR, getX(), getY() + this.getHeight() - 4, getViewWidth(), 2);
            graphics.blitSprite(UIConstants.SCROLLBAR_THUMB, getX() + scrollX, getY() + this.getHeight() - 6, scrollWidth, 6);
        }

        if (isYScrollbarVisible()) {
            var scrollHeight = (int) (getViewHeight() * (getViewHeight() / (float) layout.getHeight()));
            var scrollY = (int) (yScroll / (float) layout.getHeight() * getViewHeight()) + overscroll;
            graphics.blitSprite(UIConstants.SCROLLBAR, getX() + this.getWidth() - 4, getY(), 2, getViewHeight());
            graphics.blitSprite(UIConstants.SCROLLBAR_THUMB, getX() + this.getWidth() - 6, getY() + scrollY, 6, scrollHeight);
        }
    }

    public boolean isOverScrollbarX(int mouseX, int mouseY) {
        return isXScrollbarVisible() && mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() + getHeight() - 6 && mouseY <= getY() + getHeight();
    }

    public boolean isOverScrollbarY(int mouseX, int mouseY) {
        return isYScrollbarVisible() && mouseX >= getX() + getWidth() - 6 && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight();
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        var actualWidth = getViewWidth();
        var actualHeight = getViewHeight();

        if (draggingScrollbarX) {
            double scrollBarWidth = (actualWidth / ((double) layout.getWidth() + overscroll * 2)) * actualWidth;
            double scrollBarDragX = dragX / (actualWidth - scrollBarWidth);
            xScroll = Mth.clamp(xScroll + (int) (scrollBarDragX * (layout.getWidth() + overscroll * 2)), -overscroll, Math.max(0, layout.getWidth() + overscroll - actualWidth));
            return true;
        }

        if (draggingScrollbarY) {
            double scrollBarHeight = (actualHeight / ((double) layout.getHeight() + overscroll * 2)) * actualHeight;
            double scrollBarDragY = dragY / (actualHeight - scrollBarHeight);
            yScroll = Mth.clamp(yScroll + (int) (scrollBarDragY * (layout.getHeight() + overscroll * 2)), -overscroll, Math.max(0, layout.getHeight() + overscroll - actualHeight));
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        boolean contentScrolled = super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        if (isMouseOver(mouseX, mouseY) && !contentScrolled) {
            var scrolled = false;
            if (isXScrollbarVisible()) {
                xScroll = Mth.clamp(xScroll - (int) (scrollX * 10), -overscroll, Math.max(0, layout.getWidth() + overscroll - this.getViewWidth()));
                scrolled = true;
            }

            if (isYScrollbarVisible()) {
                yScroll = Mth.clamp(yScroll - (int) (scrollY * 10), -overscroll, Math.max(0, layout.getHeight() + overscroll - this.getViewHeight()));
                scrolled = true;
            }

            return scrolled;
        }
        return contentScrolled;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isMouseOver(mouseX, mouseY)) return false;
        if (isOverScrollbarX((int) mouseX, (int) mouseY) && isXScrollbarVisible()) {
            draggingScrollbarX = true;
            return true;
        } else if (isOverScrollbarY((int) mouseX, (int) mouseY) && isYScrollbarVisible()) {
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

    public LayoutWidget<T> withStretchToContentSize() {
        return withLayoutCallback((widget, layout) -> widget.setSize(layout.getWidth(), layout.getHeight()));
    }

    public LayoutWidget<T> withStretchToContentWidth() {
        return withLayoutCallback((widget, layout) -> widget.setWidth(layout.getWidth()));
    }

    public LayoutWidget<T> withStretchToContentHeight() {
        return withLayoutCallback((widget, layout) -> widget.setHeight(layout.getHeight()));
    }

    public LayoutWidget<T> withContentFillWidth() {
        return withWidthCallback((widget, layout) -> layout.visitWidgets(child -> child.setWidth(widget.getViewWidth())));
    }

    public LayoutWidget<T> withContentFillHeight() {
        return withHeightCallback((widget, layout) -> layout.visitWidgets(child -> child.setHeight(widget.getViewHeight())));
    }

    public LayoutWidget<T> withContentFill() {
        return withContentFillWidth().withContentFillHeight();
    }

    public LayoutWidget<T> withLayoutCallback(BiConsumer<LayoutWidget<T>, T> callback) {
        layoutCallbacks.add(callback);
        callback.accept(this, layout);
        return this;
    }

    public LayoutWidget<T> withWidthCallback(BiConsumer<LayoutWidget<T>, T> callback) {
        widthCallbacks.add(callback);
        callback.accept(this, layout);
        return this;
    }

    public LayoutWidget<T> withHeightCallback(BiConsumer<LayoutWidget<T>, T> callback) {
        heightCallbacks.add(callback);
        callback.accept(this, layout);
        return this;
    }

    public LayoutWidget<T> withScrollableX(TriState scrollableX) {
        this.scrollableX = scrollableX;
        return this;
    }

    public LayoutWidget<T> withScrollableY(TriState scrollableY) {
        this.scrollableY = scrollableY;
        return this;
    }

    public LayoutWidget<T> withScrollable(TriState scrollableX, TriState scrollableY) {
        this.scrollableX = scrollableX;
        this.scrollableY = scrollableY;
        return this;
    }

    public LayoutWidget<T> withScroll(int x, int y) {
        this.xScroll = x;
        this.yScroll = y;
        return this;
    }

    public LayoutWidget<T> withOverscroll(int overscroll) {
        this.overscroll = overscroll;
        return this;
    }

    public LayoutWidget<T> withScrollToBottom() {
        this.yScroll = layout.getHeight() - this.getHeight();
        return this;
    }

    public LayoutWidget<T> withScrollToRight() {
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

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        widthCallbacks.forEach(callback -> callback.accept(this, layout));
        layout.arrangeElements();
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        heightCallbacks.forEach(callback -> callback.accept(this, layout));
        layout.arrangeElements();
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        widthCallbacks.forEach(callback -> callback.accept(this, layout));
        heightCallbacks.forEach(callback -> callback.accept(this, layout));
        layout.arrangeElements();
    }

    public int getViewWidth() {
        return this.getWidth() - (isYScrollbarVisible() ? 6 : 0);
    }

    public int getViewHeight() {
        return this.getHeight() - (isXScrollbarVisible() ? 6 : 0);
    }
}
