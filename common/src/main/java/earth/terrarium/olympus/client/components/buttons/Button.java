package earth.terrarium.olympus.client.components.buttons;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import earth.terrarium.olympus.client.components.base.BaseWidget;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRenderer;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRendererContext;
import earth.terrarium.olympus.client.components.dropdown.DropdownBuilder;
import earth.terrarium.olympus.client.components.dropdown.DropdownState;
import earth.terrarium.olympus.client.components.renderers.WidgetRenderers;
import earth.terrarium.olympus.client.ui.UIConstants;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.sounds.SoundManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public class Button extends BaseWidget {

    private final Int2ObjectMap<Runnable> actions = new Int2ObjectArrayMap<>();

    private WidgetRenderer<? super Button> foreground = WidgetRenderer.empty();
    private WidgetRenderer<? super Button> background = WidgetRenderers.sprite(UIConstants.BUTTON);
    private ButtonShape shape = ButtonShapes.RECTANGLE;
    private boolean playSounds = false;

    public Button() {
        super();
    }

    @Override
    protected void extractWidgetRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        this.isHovered = graphics.containsPointInScissor(mouseX, mouseY) && isMouseOver(mouseX, mouseY);

        this.background.render(graphics, new WidgetRendererContext<>(this, mouseX, mouseY), partialTick);
        this.foreground.render(graphics, new WidgetRendererContext<>(this, mouseX, mouseY), partialTick);

        if (this.isHovered) {
            graphics.requestCursor(CursorTypes.POINTING_HAND);
        }
    }

    /**
     * @see #withForeground(WidgetRenderer)
     */
    public Button withRenderer(WidgetRenderer<? super Button> renderer) {
        return this.withForeground(renderer);
    }

    public Button withForeground(WidgetRenderer<? super Button> renderer) {
        this.foreground = renderer;
        return this;
    }

    /**
     * @see #withBackground(WidgetRenderer)
     */
    public Button withTexture(@Nullable WidgetSprites sprites) {
        this.background = sprites == null ? WidgetRenderer.empty() : WidgetRenderers.sprite(sprites);
        return this;
    }

    public Button withBackground(WidgetRenderer<? super Button> renderer) {
        this.background = renderer;
        return this;
    }

    public Button withCallback(int key, Runnable onPress) {
        this.actions.put(key, onPress);
        return this;
    }

    public Button withCallback(Runnable onPress) {
        this.actions.defaultReturnValue(onPress);
        return this;
    }

    public Button withSounds(boolean playSounds) {
        this.playSounds = playSounds;
        return this;
    }

    @Override
    public Button withSize(int width, int height) {
        return (Button) super.withSize(width, height);
    }

    public <T> DropdownBuilder<T> withDropdown(DropdownState<T> state) {
        state.setButton(this);
        return new DropdownBuilder<>(state);
    }

    public Button withShape(ButtonShape shape) {
        this.shape = shape;
        return this;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        if (!this.active || !this.visible) return false;
        return this.shape.isInside(mouseX - this.getX(), mouseY - this.getY(), this.getWidth(), this.getHeight());
    }

    @Override
    public boolean keyPressed(@NonNull KeyEvent event) {
        if (this.active && this.visible && event.isSelection()) {
            var action = this.actions.get(InputConstants.MOUSE_BUTTON_LEFT);
            if (action != null) {
                this.playDownSound(Minecraft.getInstance().getSoundManager());
                action.run();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseClicked(@NonNull MouseButtonEvent event, boolean bl) {
        if (this.active && this.visible && this.isMouseOver(event.x(), event.y())) {
            var action = this.actions.get(event.input());
            if (action != null) {
                this.playDownSound(Minecraft.getInstance().getSoundManager());
                action.run();
                return true;
            }
        }
        return false;
    }

    @Override
    public void playDownSound(@NotNull SoundManager handler) {
        if (!this.playSounds) return;
        playButtonClickSound(handler);
    }
}
