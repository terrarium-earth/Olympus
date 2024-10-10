package earth.terrarium.example.examples;

import earth.terrarium.example.base.ExampleScreen;
import earth.terrarium.example.base.OlympusExample;
import earth.terrarium.olympus.client.components.Widgets;
import earth.terrarium.olympus.client.components.renderers.WidgetRenderers;
import earth.terrarium.olympus.client.layouts.Layouts;
import earth.terrarium.olympus.client.shader.builtin.RoundedRectShader;
import earth.terrarium.olympus.client.shader.builtin.RoundedTextureShader;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@OlympusExample(id = "shader", description = "Shader Example")
public class ShaderExample extends ExampleScreen {

    @Override
    protected void init() {
        Layouts.row()
                .withChild(Widgets.button()
                        .withSize(100, 20)
                        .withRenderer(WidgetRenderers.text(Component.literal("Recompile Shaders")))
                        .withCallback(() -> {
                            RoundedRectShader.recompileShader();
                            RoundedTextureShader.recompileShader();
                        })
                )
                .withPosition(this.width / 2 - 50, this.height - 30)
                .build(this::addRenderableWidget);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float f) {
        super.render(graphics, mouseX, mouseY, f);

        RoundedRectShader.fill(
                graphics,
                this.width / 2 - 150, 50,
                100, 100,
                0x80000000, 0xFF0000FF,
                15f, 5
        );

        RoundedTextureShader.blit(
                graphics,
                this.width / 2 + 50, 50,
                100, 100,
                ResourceLocation.fromNamespaceAndPath("olympus", "textures/gui/sprites/modal/modal.png"),
                0f, 0f, 1f, 1f,
                15f
        );
    }
}
