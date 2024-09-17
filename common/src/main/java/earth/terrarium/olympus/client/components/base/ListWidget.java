package earth.terrarium.olympus.client.components.base;

import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
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

    private static final int SCROLLBAR_WIDTH = 6;
    private static final int SCROLLBAR_PADDING = 2;
    private static final ResourceLocation SCROLLBAR = UIConstants.id("lists/scroll/bar");
    private static final ResourceLocation SCROLLBAR_THUMB = UIConstants.id("lists/scroll/thumb");

    protected final List<AbstractWidget> items = new ArrayList<>();

    protected double scroll = 0;
    protected int lastHeight = 0;
    protected boolean scrolling = false;
    protected int gap = 0;
    protected int overScroll = 0;

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

        graphics.enableScissor(getX() + 1, getY(), getX() + actualWidth, getY() + this.height);

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

        graphics.disableScissor();

        if (this.lastHeight > this.height) {
            int scrollBarHeight = (int) ((this.height / (double) this.lastHeight) * this.height) - getScrollbarPadding() * 2;

            int scrollBarX = this.getX() + this.width - getScrollbarThumbWidth() - getScrollbarPadding();
            int scrollBarY = this.getY() + getScrollbarPadding() + Math.round(((float) this.scroll / (float) this.lastHeight) * this.height);

            renderScrollbar(graphics, scrollBarX, scrollBarY, scrollBarHeight, mouseX, mouseY, partialTicks);
        }
    }

    public void renderScrollbar(GuiGraphics graphics, int scrollBarX, int scrollBarY, int scrollBarHeight, int mouseX, int mouseY, float partialTicks) {
        graphics.blitSprite(SCROLLBAR,
            scrollBarX + (getScrollbarThumbWidth() - getScrollbarTrackWidth()) / 2,
            this.getY() + getScrollbarPadding(),
                getScrollbarTrackWidth(),
            this.height - getScrollbarPadding() * 2
        );

        graphics.blitSprite(
            SCROLLBAR_THUMB,
            scrollBarX,
            scrollBarY,
            getScrollbarThumbWidth(),
            scrollBarHeight
        );

    }

    public int getScrollbarThumbWidth() {
        return SCROLLBAR_WIDTH;
    }

    public int getScrollbarPadding() {
        return SCROLLBAR_PADDING;
    }

    public int getScrollbarTrackWidth() {
        return SCROLLBAR_WIDTH - SCROLLBAR_PADDING * 2;
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
        if (clicked(mouseX, mouseY)) {
            if (isMouseOverScrollBar(mouseX, mouseY)) {
                this.scrolling = true;
                return true;
            }
            int y = getY() - Mth.floor(this.scroll);
            for (AbstractWidget entry : this.items) {
                int height = entry.getHeight();
                if (mouseY >= y && mouseY <= y + height) {
                    return entry.mouseClicked(mouseX, mouseY, button);
                }
                y += height + gap;
            }
        }
        return false;
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
    public CursorScreen.Cursor getCursor() {
        return CursorScreen.Cursor.POINTER;
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
    }

    public void setHeight(int height) {
        this.height = height;
    }
}