package earth.terrarium.olympus.client.ui.context;

import com.mojang.blaze3d.platform.Window;
import earth.terrarium.olympus.client.components.base.ListWidget;
import earth.terrarium.olympus.client.components.Widgets;
import earth.terrarium.olympus.client.components.renderers.WidgetRenderers;
import earth.terrarium.olympus.client.constants.MinecraftColors;
import earth.terrarium.olympus.client.ui.Overlay;
import earth.terrarium.olympus.client.ui.UIConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ContextMenu extends Overlay {

    private static final int PADDING = 3;

    private final List<Supplier<AbstractWidget>> actions = new ArrayList<>();
    private final int initialX;
    private final int initialY;

    private int x;
    private int y;
    private int contextHeight;
    private int contextWidth;

    private int maxWidth;
    private int maxHeight;

    private Side side = Side.AUTO;
    private OpeningDirection direction = OpeningDirection.AUTO;

    private final ListWidget layout = new ListWidget(0,0);

    protected ContextMenu(Screen background, int x, int y) {
        super(background);

        this.initialX = x;
        this.initialY = y;
        this.x = x;
        this.y = y;
    }

    @Override
    protected void init() {
        this.layout.clear();
        for (Supplier<AbstractWidget> action : this.actions) {
            AbstractWidget widget = action.get();
            this.layout.add(widget);
        }

        var contentWidth = layout.children().stream().mapToInt(AbstractWidget::getWidth).max().orElse(0);
        var contentHeight = layout.children().stream().mapToInt(AbstractWidget::getHeight).sum();

        this.contextWidth = maxWidth > 0 ? Math.min(maxWidth, contentWidth + 4) : contentWidth + 4;
        this.contextHeight = maxHeight > 0 ? Math.min(maxHeight, contentHeight + 4) : contentHeight + 4;

        if (this.contextHeight + this.y > this.height) {
            this.y = this.height - this.contextHeight;
        }
        if (this.contextWidth + this.x > this.width) {
            this.x = Math.max(this.initialX - this.contextWidth, 0);
        }

        this.layout.setPosition(this.x + 2, this.y + 2);
        this.layout.setSize(this.contextWidth - 4, this.contextHeight - 4);
        this.addRenderableWidget(layout);
    }

    public ContextMenu add(Supplier<AbstractWidget> action) {
        this.actions.add(action);
        return this;
    }

    public ContextMenu button(Component text, Runnable action) {
        return this.add(() -> Widgets.button()
                .withCallback(action)
                .withTexture(UIConstants.LIST_ENTRY)
                .withRenderer(WidgetRenderers.text(text)
                        .withColor(MinecraftColors.WHITE)
                )
                .withSize(font.width(text) + PADDING * 2, font.lineHeight + 1 + PADDING * 2)
        );
    }

    public ContextMenu dangerButton(Component text, Runnable action) {
        return this.add(() -> Widgets.button()
                .withCallback(action)
                .withTexture(UIConstants.LIST_ENTRY)
                .withRenderer(WidgetRenderers.text(text)
                        .withColor(MinecraftColors.RED)
                )
                .withSize(font.width(text) + PADDING * 2, font.lineHeight + 1 + PADDING * 2)
        );
    }

    public ContextMenu primaryButton(Component text, Runnable action) {
        return this.add(() -> Widgets.button()
                .withCallback(action)
                .withTexture(UIConstants.LIST_ENTRY)
                .withRenderer(WidgetRenderers.text(text)
                        .withColor(MinecraftColors.GREEN)
                )
                .withSize(font.width(text) + PADDING * 2, font.lineHeight + 1 + PADDING * 2)
        );
    }

    public ContextMenu withBounds(int x, int y) {
        this.maxWidth = x;
        this.maxHeight = y;
        return this;
    }

    public ContextMenu divider() {
        return this.add(DividerWidget::new);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.onClose();
        super.mouseClicked(mouseX, mouseY, button);
        return true;
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blitSprite(UIConstants.LIST_BG, this.x, this.y, this.contextWidth, this.contextHeight);
    }

    public static void open(Consumer<ContextMenu> consumer) {
        MouseHandler mouse = Minecraft.getInstance().mouseHandler;
        Window window = Minecraft.getInstance().getWindow();
        double mouseX = mouse.xpos() * (double) window.getGuiScaledWidth() / (double) window.getScreenWidth();
        double mouseY = mouse.ypos() * (double) window.getGuiScaledHeight() / (double) window.getScreenHeight();

        open(mouseX, mouseY, consumer);
    }

    public static void open(double x, double y, Consumer<ContextMenu> consumer) {
        open((int) x, (int) y, consumer);
    }

    public static void open(int x, int y, Consumer<ContextMenu> consumer) {
        Minecraft mc = Minecraft.getInstance();
        Screen background = mc.screen;
        ContextMenu menu = new ContextMenu(background, x, y);
        consumer.accept(menu);
        mc.setScreen(menu);
    }

    public static void open(AbstractWidget widget, Consumer<ContextMenu> consumer) {
        open(widget.getX(), widget.getY() + widget.getHeight(), consumer);
    }
}
