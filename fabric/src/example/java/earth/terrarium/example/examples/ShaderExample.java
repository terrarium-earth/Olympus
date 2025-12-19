package earth.terrarium.example.examples;

import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.example.base.ExampleScreen;
import earth.terrarium.example.base.OlympusExample;
import earth.terrarium.olympus.client.pipelines.RoundedRectangle;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

@OlympusExample(id = "shader", description = "Shader Example")
public class ShaderExample extends ExampleScreen {

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float f) {
        super.render(graphics, mouseX, mouseY, f);

        graphics.pose().pushMatrix();
        graphics.pose().translate(Mth.cos(((System.currentTimeMillis() % 60000) / 1000f)) * 30f, 0);

        // ARGB
        RoundedRectangle.draw(
                graphics,
                this.width / 2 - 150, 50,
                100, 100,
                0x80000000,
                0xFF0000FF, 0xFFFF0000,
                0xFF00FF00, 0xFF731f8f,
                15, 2
        );

        RoundedRectangle.draw(
                graphics,
                this.width / 2 + 50, 50,
                100, 100,
                Color.RAINBOW.withAlpha(0x80).getValue(), 0xFF00FF00,
                5f, 2
        );


//        RoundedTexture.draw(
//                graphics,
//                this.width / 2 + 50, 50,
//                100, 100,
//                ResourceLocation.fromNamespaceAndPath("olympus", "textures/gui/sprites/modal/modal.png"),
//                0f, 0f, 1f, 1f,
//                15f
//        );

        graphics.pose().popMatrix();
    }
}
