package earth.terrarium.olympus.client.components.map;

import com.teamresourceful.resourcefullib.client.CloseablePoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class MapRenderer {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("olympus","dynamic_map");
    private final int scale;

    public MapRenderer(int[][] colors, int scale) {
        var textureManager = Minecraft.getInstance().getTextureManager();
        var dynamicTexture = new DynamicTexture("Olympus Map Texture", scale, scale, true);
        textureManager.register(TEXTURE, dynamicTexture);
        updateTexture(dynamicTexture, colors, scale);
        this.scale = scale;
    }

    private void updateTexture(DynamicTexture texture, int[][] colors, int scale) {
        var nativeImage = texture.getPixels();
        if (nativeImage == null) return;

        for (int i = 0; i < scale; i++) {
            for (int j = 0; j < scale; j++) {
                nativeImage.setPixel(i, j, colors[i][j]);
            }
        }

        texture.upload();
    }

    public int getScale() {
        return scale;
    }

    public void render(GuiGraphics graphics, int x, int y, int width, int height) {

        try (var pose = new CloseablePoseStack(graphics)) {
            pose.translate(x, y, 0.01);
            graphics.drawSpecial(source -> {
                Matrix4f matrix4f = graphics.pose().last().pose();
                var consumer = source.getBuffer(RenderType.guiTextured(TEXTURE));
                consumer.addVertex(matrix4f, 0.0f, height, -0.01f).setUv(0.0f, 1.0f).setColor(-1);
                consumer.addVertex(matrix4f, width, height, -0.01f).setUv(1.0f, 1.0f).setColor(-1);
                consumer.addVertex(matrix4f, width, 0.0f, -0.01f).setUv(1.0f, 0.0f).setColor(-1);
                consumer.addVertex(matrix4f, 0.0f, 0.0f, -0.01f).setUv(0.0f, 0.0f).setColor(-1);
            });
        }
    }
}