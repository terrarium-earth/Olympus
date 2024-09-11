package earth.terrarium.olympus.client.components.map;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.teamresourceful.resourcefullib.client.CloseablePoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

public class MapRenderer {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("olympus","claimmaptextures");

    public MapRenderer(int[][] colors, int scale) {
        var textureManager = Minecraft.getInstance().getTextureManager();
        var dynamicTexture = new DynamicTexture(scale, scale, true);
        textureManager.register(TEXTURE, dynamicTexture);
        updateTexture(dynamicTexture, colors, scale);
    }

    private void updateTexture(DynamicTexture texture, int[][] colors, int scale) {
        var nativeImage = texture.getPixels();
        if (nativeImage == null) return;

        for (int i = 0; i < scale; i++) {
            for (int j = 0; j < scale; j++) {
                nativeImage.setPixelRGBA(i, j, colors[i][j]);
            }
        }

        texture.upload();
    }

    public void render(GuiGraphics graphics, int x, int y, int width, int height) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        try (var pose = new CloseablePoseStack(graphics)) {
            pose.translate(x, y, 0.01);
            var matrix4f = pose.last().pose();
            var builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            builder.addVertex(matrix4f, 0.0f, height, -0.01f).setUv(0.0f, 1.0f);
            builder.addVertex(matrix4f, width, height, -0.01f).setUv(1.0f, 1.0f);
            builder.addVertex(matrix4f, width, 0.0f, -0.01f).setUv(1.0f, 0.0f);
            builder.addVertex(matrix4f, 0.0f, 0.0f, -0.01f).setUv(0.0f, 0.0f);
            var toDraw = builder.build();
            if (toDraw != null) {
                BufferUploader.drawWithShader(toDraw);
            }
        }
    }
}