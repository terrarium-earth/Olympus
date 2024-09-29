package earth.terrarium.olympus.client.components.base;

import com.teamresourceful.resourcefullib.client.components.CursorWidget;
import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRenderer;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRendererContext;
import earth.terrarium.olympus.client.ui.UIConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ListWidget extends BaseParentWidget {

    private static final ResourceLocation SCROLLBAR = UIConstants.id("lists/scroll/bar");
    private static final ResourceLocation SCROLLBAR_THUMB = UIConstants.id("lists/scroll/thumb");

    protected final List<AbstractWidget> items = new ArrayList<>();

    protected double scroll = 0;
    protected int lastHeight = 0;
    protected boolean scrolling = false;
    protected int gap = 0;
    protected int overScroll = 0;

    protected int scrollWidth = 6;
    protected int scrollbarGap = 2;

    protected WidgetRenderer<ListWidget> scrollbarRenderer = (graphics, context, partialTick) -> {
        var widget = context.getWidget();
        int scrollBarHeight = (int) ((widget.getHeight() / (double) widget.getContentHeight()) * widget.getHeight());
        int scrollBarY = context.getY() + Math.round(((float) widget.getScroll() / (float) widget.getContentHeight()) * context.getHeight());

        graphics.blitSprite(SCROLLBAR,
            context.getX() + (context.getWidth() - 2) / 2,
            context.getY(),
            context.getWidth() - 4,
            context.getHeight()
        );

        graphics.blitSprite(
            SCROLLBAR_THUMB,
            context.getX(),
            scrollBarY,
                context.getWidth(),
            scrollBarHeight
        );
    };

    public ListWidget(int width, int height) {
        super(width, height);
    }

    public void update(@Nullable ListWidget old) {
        if (old == null) return;
        if (this.items.size() != old.items.size()) return;
        if (this.height != old.height) return;
        updateLastHeight();
        if (this.lastHeight != old.lastHeight) return;

        this.scroll = old.scroll;
        this.scrolling = old.scrolling;
    }

    @Override
    public void clear() {
        super.clear();
        this.items.clear();
    }

    public void set(List<? extends AbstractWidget> items) {
        this.items.clear();
        this.items.addAll(items);
        updateScrollBar();
    }

    public void add(AbstractWidget item) {
        items.add(item);
        updateScrollBar();
    }

    @Override
    public @NotNull List<AbstractWidget> children() {
        return items;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        boolean showsScrollBar = this.lastHeight > this.height;
        int actualWidth = getWidth() - (showsScrollBar ? getScrollbarThumbWidth() + getScrollbarPadding() * 2 : 0);

        graphics.enableScissor(getX(), getY(), getX() + actualWidth, getY() + this.height);

        int y = this.getY() - (int) scroll;
        this.lastHeight = 0;

        for (AbstractWidget item : items) {
            item.setWidth(actualWidth);
            item.setX(getX());
            item.setY(y);

            item.render(graphics, this.isHovered ? mouseX : -1, this.isHovered ? mouseY : -1, partialTicks);
            y += item.getHeight() + gap;
            this.lastHeight += item.getHeight() + gap;
        }

        if (isMouseOverContent(mouseX, mouseY)) {
            updateCursor(mouseX, mouseY);
        } else {
            this.cursor = CursorScreen.Cursor.DEFAULT;
        }

        graphics.disableScissor();

        if (this.lastHeight > this.height) {
            this.scrollbarRenderer.render(graphics, new WidgetRendererContext<>(this, mouseX, mouseY).setWidth(scrollWidth).setHeight(getHeight() - scrollbarGap * 2).setY(getY() + scrollbarGap).setX(this.getX() + this.getWidth() - scrollWidth - scrollbarGap), partialTicks);
        }
    }

    public boolean isMouseOverContent(double mouseX, double mouseY) {
        return mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth() - scrollWidth - scrollbarGap * 2 && mouseY >= this.getY() && mouseY <= this.getY() + this.height;
    }


    public int getScrollbarThumbWidth() {
        return scrollWidth;
    }

    public int getScrollbarPadding() {
        return scrollbarGap;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.scrolling) {
            double scrollBarHeight = (this.height / (double) this.lastHeight) * this.height;
            double scrollBarDragY = dragY / (this.height - scrollBarHeight);
            this.scroll = Mth.clamp(
                    this.scroll + scrollBarDragY * this.lastHeight, 0,
                    Math.max(0, this.lastHeight - this.height)
            );
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        this.scroll = Mth.clamp(this.scroll - scrollY * 10, 0, Math.max(0, this.lastHeight - this.height));
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!clicked(mouseX, mouseY)) return false;
        if (isMouseOverScrollBar(mouseX, mouseY)) {
            this.scrolling = true;
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double d, double e, int i) {
        if (i == 0) {
            this.scrolling = false;
        }
        return super.mouseReleased(d, e, i);
    }

    private boolean isMouseOverScrollBar(double mouseX, double mouseY) {
        if (this.lastHeight > this.height) {
            int scrollBarX = this.getX() + this.width - getScrollbarThumbWidth() - getScrollbarPadding();
            return mouseX >= scrollBarX && mouseX <= scrollBarX + getScrollbarThumbWidth() && mouseY >= this.getY() && mouseY <= this.getY() + this.height;
        }
        return false;
    }

    private void updateLastHeight() {
        boolean showsScrollBar = this.lastHeight > this.height;
        int actualWidth = getWidth() - (showsScrollBar ? getScrollbarThumbWidth() + getScrollbarPadding() * 2 : 0);

        this.lastHeight = 0;
        int y = this.getY() - (int) scroll;
        for (AbstractWidget item : items) {
            item.setWidth(actualWidth);
            item.setX(getX());
            item.setY(y);
            this.lastHeight += item.getHeight();
            y += item.getHeight();
        }
    }

    protected void updateScrollBar() {
        updateLastHeight();
        this.scroll = Mth.clamp(this.scroll, 0, Math.max(0, this.lastHeight - this.height + this.overScroll));
    }

    public boolean isScrolling() {
        return this.scrolling;
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getContentHeight() {
        return this.lastHeight;
    }

    public int getScroll() {
        return (int) this.scroll;
    }

    public ListWidget withGap(int gap) {
        this.gap = gap;
        return this;
    }

    public ListWidget withOverScroll(int overScroll) {
        this.overScroll = overScroll;
        return this;
    }

    public ListWidget withScrollbarRenderer(WidgetRenderer<ListWidget> scrollbarRenderer) {
        this.scrollbarRenderer = scrollbarRenderer;
        return this;
    }

    public ListWidget withScrollWidth(int scrollWidth) {
        this.scrollWidth = scrollWidth;
        return this;
    }

    public ListWidget withScrollGap(int gap) {
        this.scrollbarGap = gap;
        return this;
    }
}