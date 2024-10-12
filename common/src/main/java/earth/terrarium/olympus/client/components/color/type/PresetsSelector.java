package earth.terrarium.olympus.client.components.color.type;

import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.olympus.client.components.base.BaseWidget;
import earth.terrarium.olympus.client.components.color.ColorPresetType;
import earth.terrarium.olympus.client.ui.UIConstants;
import earth.terrarium.olympus.client.utils.State;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class PresetsSelector extends BaseWidget {

    private static final List<HsbColor> MINECRAFT_COLORS = List.of(
            HsbColor.fromRgb(0xFFff5555), HsbColor.fromRgb(0xFFffaa00),
            HsbColor.fromRgb(0xFFffff55), HsbColor.fromRgb(0xFF55ff55),
            HsbColor.fromRgb(0x55ffff), HsbColor.fromRgb(0xFF5555ff),
            HsbColor.fromRgb(0xFFff55ff), HsbColor.fromRgb(0xFFffffff),
            HsbColor.fromRgb(0xFFaaaaaa), HsbColor.fromRgb(0xFFaa0000),
            HsbColor.fromRgb(0xFF00aa00), HsbColor.fromRgb(0xFF00aaaa),
            HsbColor.fromRgb(0xFF0000aa), HsbColor.fromRgb(0xFFaa00aa),
            HsbColor.fromRgb(0xFF555555), HsbColor.fromRgb(0xFF000000)
    );

    private final Color[] presets;
    private final State<ColorPresetType> type;
    private final HsbState state;
    private final boolean withAlpha;

    private Collection<HsbColor> colors = new ArrayList<>();
    private ColorPresetType lastType;

    private ResourceLocation background;

    public PresetsSelector(int width, Color[] presets, State<ColorPresetType> type, HsbState state, boolean withAlpha, ResourceLocation background) {
        super(width, (width / 8 * 2) + 6);
        this.presets = presets;
        this.type = type;
        this.state = state;
        this.getColors();
        this.withAlpha = withAlpha;
        this.background = background;
    }

    private Collection<HsbColor> getColors() {
        if (this.lastType != this.type.get()) {
            this.colors = switch (this.type.get()) {
                case DEFAULTS -> {
                    List<HsbColor> colors = new ArrayList<>();
                    for (Color preset : this.presets) {
                        int color = withAlpha ? preset.getValue() : preset.getValue() | 0xFF000000;
                        colors.add(HsbColor.fromRgb(color));
                    }
                    yield colors;
                }
                case RECENTS -> RecentColorStorage.getRecentColors(this.withAlpha);
                case MC_COLORS -> MINECRAFT_COLORS;
            };
            this.lastType = this.type.get();
        }
        return this.colors;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.blitSprite(UIConstants.MODAL_INSET, this.getX(), this.getY(), this.getWidth(), this.getHeight());
        int size = (this.getWidth() - 18) / 8;

        int i = 0;
        for (HsbColor color : getColors()) {
            if (i >= 16) break; // Only render the first 16 colors 2x8 grid
            int j = i % 8;
            int k = i / 8;
            int x = this.getX() + 4 + (j * size) + (2 * j);
            int y = this.getY() + 4 + (k * size) + (2 * k);
            int rgba = color.toRgba();
            graphics.fill(x, y, x + size, y + size, rgba);
            graphics.renderOutline(x, y, size, size, 0xFFDDDDDD);
            if (mouseX >= x && mouseX <= x + size && mouseY >= y && mouseY <= y + size) {
                graphics.renderOutline(x, y, size, size, 0xFF000000);
                Screen screen = Minecraft.getInstance().screen;
                if (screen != null) {
                    if (!withAlpha) rgba &= 0x00FFFFFF;
                    var text = Component.literal("[").withColor(rgba | 0xFF000000)
                            .append(Component.literal(String.format(Locale.ROOT, "#%06X", rgba)).withColor(0xFFFFFFFF))
                            .append(Component.literal("]")).withColor(rgba | 0xFF000000);

                    screen.setTooltipForNextRenderPass(text);
                }
            }
            i++;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return false;
        int size = (this.getWidth() - 18) / 8;
        int i = 0;
        for (HsbColor color : getColors()) {
            if (i >= 16) break;
            int j = i % 8;
            int k = i / 8;
            int x = this.getX() + 3 + (j * size) + (2 * j);
            int y = this.getY() + 3 + (k * size) + (2 * k);
            if (mouseX >= x && mouseX <= x + size && mouseY >= y && mouseY <= y + size) {
                RecentColorStorage.add(this.state.get());
                this.lastType = null;
                this.state.set(color);
                return true;
            }
            i++;
        }
        return false;
    }
}
