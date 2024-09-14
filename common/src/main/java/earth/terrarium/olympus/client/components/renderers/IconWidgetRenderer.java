package earth.terrarium.olympus.client.components.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRenderer;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRendererContext;
import earth.terrarium.olympus.client.constants.MinecraftColors;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.resources.ResourceLocation;

public class IconWidgetRenderer<T extends AbstractWidget> implements WidgetRenderer<T> {

    private final ResourceLocation icon;
    private Color color = MinecraftColors.DARK_GRAY;
    private boolean drawShadow = false;

    private int width = 10, height = 10;

    IconWidgetRenderer(ResourceLocation icon) {
        this.icon = icon;
    }

    @Override
    public void render(GuiGraphics graphics, WidgetRendererContext<T> context, float partialTick) {
        float red = color.getFloatRed();
        float green = color.getFloatGreen();
        float blue = color.getFloatBlue();
        float alpha = color.getFloatAlpha();
        if (alpha == 0f) alpha = 1f;

        int x = context.getX();
        int y = context.getY();
        int width = this.width;
        int height = this.height;

        if (this.width == 0 || this.height == 0) {
            width = context.getWidth();
            height = context.getHeight();
        } else {
            x += (context.getWidth() - this.width) / 2;
            y += (context.getHeight() - this.height) / 2;
        }

        if (drawShadow) {
            RenderSystem.setShaderColor(red / 3f, green / 3f, blue / 3f, alpha);
            graphics.blitSprite(icon, x + 1, y + 1, width, height);
        }

        RenderSystem.setShaderColor(red, green, blue, alpha);
        graphics.blitSprite(icon, x, y, width, height);

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

    public IconWidgetRenderer<T> withShadow() {
        this.drawShadow = true;
        return this;
    }

    public IconWidgetRenderer<T> withColor(Color color) {
        this.color = color;
        return this;
    }

    public IconWidgetRenderer<T> withSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public IconWidgetRenderer<T> withSize(int size) {
        return withSize(size, size);
    }

    public IconWidgetRenderer<T> withStretch() {
        this.width = 0;
        this.height = 0;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
