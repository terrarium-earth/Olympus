package earth.terrarium.olympus.client.components.slider;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import earth.terrarium.olympus.client.components.base.BaseWidget;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRenderer;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRendererContext;
import earth.terrarium.olympus.client.components.renderers.WidgetRenderers;
import earth.terrarium.olympus.client.ui.UIConstants;
import earth.terrarium.olympus.client.utils.State;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Slider extends BaseWidget {

    private final State<Float> state;

    private WidgetRenderer<? super Slider> background = WidgetRenderers.sprite(UIConstants.SLIDER_EMPTY);
    private WidgetRenderer<? super Slider> bar = WidgetRenderers.sprite(UIConstants.SLIDER_FILLED);
    private WidgetRenderer<? super Slider> thumb = WidgetRenderers.sprite(UIConstants.BUTTON);

    private boolean isDragging = false;

    public Slider(State<Float> state) {
        this.state = state;
    }

    @Override
    protected void extractWidgetRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        this.background.render(graphics, new WidgetRendererContext<>(this, mouseX, mouseY), partialTick);

        graphics.enableScissor(this.getX(), this.getY(), this.getX() + (int) (this.getWidth() * this.state.get()), this.getBottom());
        this.bar.render(graphics, new WidgetRendererContext<>(this, mouseX, mouseY), partialTick);
        graphics.disableScissor();

        extractThumbRenderState(graphics, mouseX, mouseY, partialTick);

        if (this.isDragging) {
            graphics.requestCursor(CursorTypes.RESIZE_EW);
        } else if (this.isHovered) {
            if (!this.isActive()) {
                graphics.requestCursor(CursorTypes.NOT_ALLOWED);
            } else {
                graphics.requestCursor(CursorTypes.POINTING_HAND);
            }
        }
    }

    // Generics with a super wildcard infers incorrectly so we just use raw types here.
    // This is safe because we know the type of the widget is Slider.
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void extractThumbRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        WidgetRendererContext ctx = new WidgetRendererContext<>(this, mouseX, mouseY);
        ctx.setSize(this.height, this.height);
        ctx.setPosition((int) (this.getX() + this.getWidth() * this.state.get() - this.height / 2f), this.getY());
        this.thumb.render(graphics, ctx, partialTick);
    }

    /**
     * @see #withBackground(WidgetRenderer)
     */
    public Slider withBackground(@Nullable WidgetSprites sprite) {
        this.background = sprite == null ? WidgetRenderer.empty() : WidgetRenderers.sprite(sprite);
        return this;
    }

    public Slider withBackground(WidgetRenderer<? super Slider> renderer) {
        this.background = renderer;
        return this;
    }

    public Slider withBar(@Nullable WidgetSprites sprite) {
        this.bar = sprite == null ? WidgetRenderer.empty() : WidgetRenderers.sprite(sprite);
        return this;
    }

    public Slider withBar(WidgetRenderer<? super Slider> renderer) {
        this.bar = renderer;
        return this;
    }

    public Slider withThumb(@Nullable WidgetSprites sprite) {
        this.thumb = sprite == null ? WidgetRenderer.empty() : WidgetRenderers.sprite(sprite);
        return this;
    }

    public Slider withThumb(WidgetRenderer<? super Slider> renderer) {
        this.thumb = renderer;
        return this;
    }

    @Override
    public Slider withSize(int width, int height) {
        return (Slider) super.withSize(width, height);
    }

    // Implementation

    @Override
    public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
        if (this.isHoveredOrFocused()) {
            this.state.set(Math.clamp(this.state.get() + (float) scrollY * 0.05f, 0, 1));
            return true;
        }
        return super.mouseScrolled(x, y, scrollX, scrollY);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (this.isFocused()) {
            if (event.isLeft()) {
                this.state.set(Math.clamp(this.state.get() - 0.05f, 0, 1));
                return true;
            } else if (event.isRight()) {
                this.state.set(Math.clamp(this.state.get() + 0.05f, 0, 1));
                return true;
            }
        }
        return super.keyPressed(event);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        if (this.isDragging && event.input() == InputConstants.MOUSE_BUTTON_LEFT) {
            this.state.set(Math.clamp((float) (event.x() - this.getX()) / this.getWidth(), 0, 1));
            return true;
        }
        return super.mouseDragged(event, dx, dy);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.input() == InputConstants.MOUSE_BUTTON_LEFT) {
            this.isDragging = true;
            this.state.set(Math.clamp((float) (event.x() - this.getX()) / this.getWidth(), 0, 1));
            return true;
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if (event.input() == InputConstants.MOUSE_BUTTON_LEFT) {
            this.isDragging = false;
            return true;
        }
        return super.mouseReleased(event);
    }
}
