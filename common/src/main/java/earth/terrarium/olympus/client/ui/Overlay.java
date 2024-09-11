package earth.terrarium.olympus.client.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.teamresourceful.resourcefullib.client.screens.BaseCursorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import org.jetbrains.annotations.Nullable;

public abstract class Overlay extends BaseCursorScreen {

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
        if (this.background != null) this.background.resize(Minecraft.getInstance(), this.width, this.height);
        if (this.background instanceof Overlay overlay) overlay.isInitialized = false;
        if (this.isInitialized) {
            Minecraft.getInstance().setScreen(this.background);
        } else {
            rebuildWidgets();
        }
    }

    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (this.background == null) return;
        this.background.render(graphics, -1, -1, partialTick);
        graphics.flush();
        RenderSystem.clear(256, Minecraft.ON_OSX);
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(this.background);
    }

    /**
     * @deprecated This seems really useless
     */
    @Deprecated(forRemoval = true)
    public void renderWidgets(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return this.background != null && this.background.isPauseScreen();
    }
}
