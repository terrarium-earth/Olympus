package earth.terrarium.example.examples;

import earth.terrarium.example.base.ExampleScreen;
import earth.terrarium.example.base.OlympusExample;
import earth.terrarium.olympus.client.pipelines.RoundedRectangle;
import earth.terrarium.olympus.client.pipelines.RoundedTexture;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@OlympusExample(id = "shader", description = "Shader Example")
public class ShaderExample extends ExampleScreen {

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float f) {
        super.render(graphics, mouseX, mouseY, f);

        RoundedRectangle.draw(
                graphics,
                this.width / 2 - 150, 50,
                100, 100,
                0x80000000, 0xFF0000FF,
                15f, 5
        );

        RoundedTexture.draw(
                graphics,
                this.width / 2 + 50, 50,
                100, 100,
                ResourceLocation.fromNamespaceAndPath("olympus", "textures/gui/sprites/modal/modal.png"),
                0f, 0f, 1f, 1f,
                15f
        );
    }
}
