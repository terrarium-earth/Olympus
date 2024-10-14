package earth.terrarium.olympus.client.components.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRenderer;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRendererContext;
import earth.terrarium.olympus.client.constants.MinecraftColors;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.resources.ResourceLocation;

public class IconWidgetRenderer<T extends AbstractWidget> implements WidgetRenderer<T>, ColorableWidget {

    private final ResourceLocation icon;
    private Color color = MinecraftColors.DARK_GRAY;
    private boolean drawShadow = false;

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

        if (drawShadow) {
            RenderSystem.setShaderColor(red / 3f, green / 3f, blue / 3f, alpha);
            graphics.blitSprite(icon, context.getX() + 1, context.getY() + 1, context.getWidth(), context.getHeight());
        }

        RenderSystem.setShaderColor(red, green, blue, alpha);
        graphics.blitSprite(icon, context.getX(), context.getY(), context.getWidth(), context.getHeight());

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
}
