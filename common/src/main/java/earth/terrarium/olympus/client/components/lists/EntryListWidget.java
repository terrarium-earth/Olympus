package earth.terrarium.olympus.client.components.lists;

import com.teamresourceful.resourcefullib.client.utils.RenderUtils;
import earth.terrarium.olympus.client.components.base.BaseWidget;
import earth.terrarium.olympus.client.ui.UIConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class EntryListWidget<T> extends BaseWidget {

    public static final double OVERSCROLL = 5.0D;
    private static final int SCROLLBAR_WIDTH = 6;
    private static final int SCROLLBAR_PADDING = 2;

    private final List<ListEntry<T>> entries = new ArrayList<>();

    private double scroll;
    private int fullHeight;

    public EntryListWidget(@Nullable EntryListWidget<T> list, int width, int height) {
        super(width, height);

        if (list != null) {
            this.scroll = list.scroll;
            this.fullHeight = list.fullHeight;
            this.entries.addAll(list.entries);
            this.entries.forEach(entry -> entry.setList(this));
        } else {
            this.scroll = 0.0D;
            this.fullHeight = this.height;
        }
    }

    public EntryListWidget(int width, int height) {
        super(width, height);

        this.scroll = 0.0D;
        this.fullHeight = this.height;
    }

    public abstract void update();

    public void clear() {
        this.entries.clear();
    }

    public void add(ListEntry<T> entry) {
        this.entries.add(entry);
        entry.setList(this);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        updateScroll(0D);
        this.fullHeight = 0;

        int x = getX();
        int y = getY() - Mth.floor(this.scroll);

        try (var scissor = RenderUtils.createScissor(Minecraft.getInstance(), graphics, getX(), getY(), getWidth(), getHeight())) {
            for (ListEntry<T> entry : this.entries) {

                entry.render(graphics, scissor.stack(), x, y, this.width, mouseX, mouseY, isHovered(), partialTick);

                int height = entry.getHeight(this.width);

                y += height;
                this.fullHeight += height;
            }
        }

        if (this.fullHeight > this.height) {
            int scrollBarHeight = (int) ((this.height / (double) this.fullHeight) * this.height) - SCROLLBAR_PADDING * 2;

            int scrollBarX = this.getX() + this.width;
            int scrollBarY = this.getY() + SCROLLBAR_PADDING + Math.round(((float) this.scroll / (float) this.fullHeight) * this.height);

            graphics.blitSprite(UIConstants.SCROLLBAR,
                scrollBarX + SCROLLBAR_PADDING * 2,
                this.getY() + SCROLLBAR_PADDING,
                SCROLLBAR_WIDTH - SCROLLBAR_PADDING * 2,
                this.height - SCROLLBAR_PADDING * 2
            );

            graphics.blitSprite(
                UIConstants.SCROLLBAR_THUMB,
                scrollBarX + SCROLLBAR_PADDING,
                scrollBarY,
                SCROLLBAR_WIDTH,
                scrollBarHeight
            );
        }
    }

    public void updateScroll(double amount) {
        this.scroll = Mth.clamp(this.scroll - amount * 10, -OVERSCROLL, Math.max(-OVERSCROLL, this.fullHeight - this.height + OVERSCROLL));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        updateScroll(scrollY);
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY)) {
            int y = getY() - Mth.floor(this.scroll);
            for (ListEntry<T> entry : this.entries) {
                int height = entry.getHeight(this.width);
                if (mouseY >= y && mouseY <= y + height) {
                    return entry.mouseClicked(mouseX - getX(), mouseY - y, button, this.width);
                }
                y += height;
            }
        }
        return false;
    }
}
