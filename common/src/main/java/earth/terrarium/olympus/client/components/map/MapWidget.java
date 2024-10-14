package earth.terrarium.olympus.client.components.map;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import com.teamresourceful.resourcefullib.client.CloseablePoseStack;
import earth.terrarium.olympus.client.components.base.BaseWidget;
import earth.terrarium.olympus.client.ui.UIConstants;
import earth.terrarium.olympus.client.utils.State;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

public class MapWidget extends BaseWidget {
    private static final ResourceLocation MAP_ICONS = ResourceLocation.withDefaultNamespace("textures/map/decorations/player.png");

    private final State<MapRenderer> mapRenderer;

    private int scale;
    private boolean initialized = false;
    private ResourceLocation texture = UIConstants.MODAL_INSET;

    public MapWidget(State<MapRenderer> state) {
        super();
        this.mapRenderer = state;
    }

    private void renderLoading(GuiGraphics graphics) {
        var font = Minecraft.getInstance().font;
        graphics.drawCenteredString(font, UIConstants.LOADING, (int) (getX() + getWidth() / 2f), (int) (getY() + getHeight() / 2f), 0xFFFFFF);
    }

    public MapWidget withTexture(ResourceLocation texture) {
        this.texture = texture;
        return this;
    }

    public MapWidget withScale(int scale) {
        this.scale = scale;
        return this;
    }

    public MapWidget withRenderDistanceScale() {
        this.scale = Minecraft.getInstance().options.renderDistance().get() * 8;
        this.scale -= this.scale % 16;
        return this;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        graphics.blitSprite(this.texture, this.getX(), this.getY(), this.getWidth(), this.getHeight());

        if (!initialized) {
            this.refreshMap();
            initialized = true;
        }

        if (mapRenderer.get() == null) {
            this.renderLoading(graphics);
        } else {
            if (mapRenderer.get().getScale() != this.scale * 2 + 16) {
                this.refreshMap();
            }

            var player = Minecraft.getInstance().player;
            if (player == null) return;

            mapRenderer.get().render(graphics, this.getX() + 1, this.getY() + 1, this.getWidth() - 2, this.getHeight() - 2);

            try (var pose = new CloseablePoseStack(graphics)) {
                pose.translate(0f, 0f, 2f);
                this.renderPlayerAvatar(player, graphics);
            }
        }
    }

    public void refreshMap() {
        var player = Minecraft.getInstance().player;
        if (player == null) return;

        var chunkPos = player.chunkPosition();
        int minX = chunkPos.getMinBlockX() - scale;
        int minZ = chunkPos.getMinBlockZ() - scale;
        int maxX = chunkPos.getMaxBlockX() + scale + 1;
        int maxZ = chunkPos.getMaxBlockZ() + scale + 1;

        if (scale / 8 > 12) {
            // If the render distance is greater than 12 chunks, run asynchronously to avoid stuttering.
            CompletableFuture.supplyAsync(() -> MapTopologyAlgorithm.getColors(minX, minZ, maxX, maxZ, player.clientLevel, player)).thenAcceptAsync(colors ->
                    this.mapRenderer.set(new MapRenderer(colors, scale * 2 + 16)), Minecraft.getInstance());
        } else {
            int[][] colors = MapTopologyAlgorithm.getColors(minX, minZ, maxX, maxZ, player.clientLevel, player);
            this.mapRenderer.set(new MapRenderer(colors, scale * 2 + 16));
        }
    }

    private void renderPlayerAvatar(LocalPlayer player, GuiGraphics graphics) {
        float left = this.getWidth() / 2f;
        float top = this.getHeight() / 2f;

        double playerX = player.getX();
        double playerZ = player.getZ();
        double x = (playerX % 16) + (playerX >= 0 ? -8 : 8);
        double y = (playerZ % 16) + (playerZ >= 0 ? -8 : 8);

        x *= this.getWidth() / 144.0;
        y *= this.getHeight() / 144.0;

        try (var pose = new CloseablePoseStack(graphics)) {
            pose.translate(this.getX() + left + x, this.getY() + top + y, 0.0);
            pose.mulPose(Axis.ZP.rotationDegrees(player.getYRot()));
            pose.translate(-4f, -4f, 0f);
            graphics.blit(MAP_ICONS, 0, 0, 0f, 0f, 8, 8, 8, 8);
        }
    }
}