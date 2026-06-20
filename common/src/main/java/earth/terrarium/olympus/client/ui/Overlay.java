package earth.terrarium.olympus.client.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Overlay extends Screen {

    @Nullable
    protected final Screen background;
    private boolean isInitialized = false;

    protected Overlay(@Nullable Screen background) {
        super(CommonComponents.EMPTY);
        this.background = background;
    }

    @Override
    public void added() {
        super.added();
        if (this.background == null) return;
        this.background.clearFocus();
    }

    @Override
    protected void init() {
        super.init();
        if (!this.isInitialized) {
            this.isInitialized = true;
        }
    }

    @Override
    protected void repositionElements() {
        if (this.background instanceof Overlay overlay) {
            overlay.isInitialized = false;
            overlay.internalResize(this.width, this.height);
        } else if (this.background != null) {
            this.background.resize(this.width, this.height);
        }
        super.repositionElements();
    }

    // Requires as below we our normal resize will close overlays.
    private void internalResize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        // We want to close all overlays when the screen is resized
        Screen screenToGoTo = this.background;
        while (screenToGoTo instanceof Overlay overlay) {
            overlay.onClose();
            screenToGoTo = overlay.background;
        }
        Minecraft.getInstance().gui.setScreen(screenToGoTo);
    }

    @Override
    public void extractBackground(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        if (this.background == null) return;
        this.background.extractRenderStateWithTooltipAndSubtitles(graphics, -1, -1, partialTick);
        graphics.nextStratum();
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().gui.setScreen(this.background);
    }

    @Override
    public boolean isPauseScreen() {
        return this.background != null && this.background.isPauseScreen();
    }
}
