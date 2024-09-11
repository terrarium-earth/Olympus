package earth.terrarium.olympus.client.ui.context;

import com.mojang.blaze3d.platform.Window;
import earth.terrarium.olympus.client.components.buttons.TextButton;
import earth.terrarium.olympus.client.ui.ClearableGridLayout;
import earth.terrarium.olympus.client.ui.Overlay;
import earth.terrarium.olympus.client.ui.UIConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ContextMenu extends Overlay {

    private static final int PADDING = 4;

    private final List<Supplier<AbstractWidget>> actions = new ArrayList<>();
    private final int initialX;
    private final int initialY;

    private int x;
    private int y;
    private int contextHeight;
    private int contextWidth;

    private final ClearableGridLayout layout = new ClearableGridLayout();

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
        int i = 0;
        for (Supplier<AbstractWidget> action : this.actions) {
            AbstractWidget widget = action.get();
            this.layout.addChild(widget, i, 0);
            i++;
        }

        this.layout.arrangeElements();
        this.layout.visitWidgets(widget -> widget.setWidth(this.layout.getWidth()));
        this.contextHeight = this.layout.getHeight() + PADDING * 2;
        this.contextWidth = this.layout.getWidth() + PADDING * 2;

        if (this.contextHeight + this.y > this.height) {
            this.y = this.height - this.contextHeight;
        }
        if (this.contextWidth + this.x > this.width) {
            this.x = Math.max(this.initialX - this.contextWidth, 0);
        }
        this.layout.setPosition(this.x + PADDING, this.y + PADDING);
        this.layout.visitWidgets(this::addRenderableWidget);
    }

    public ContextMenu add(Supplier<AbstractWidget> action) {
        this.actions.add(action);
        return this;
    }

    public ContextMenu button(Component text, Button.OnPress action) {
        return this.add(() -> new TextButton(Minecraft.getInstance().font.width(text) + PADDING * 2, 16, 0xFFFFFF, UIConstants.CONTEXT_BUTTON, text, action));
    }

    public ContextMenu dangerButton(Component text, Button.OnPress action) {
        return this.add(() -> new TextButton(Minecraft.getInstance().font.width(text) + PADDING * 2, 16, 0xca3636, 0xFFFFFF, UIConstants.DANGER_CONTEXT_BUTTON, text, action));
    }

    public ContextMenu primaryButton(Component text, Button.OnPress action) {
        return this.add(() -> new TextButton(Minecraft.getInstance().font.width(text) + PADDING * 2, 16, 0x3c8527, 0xFFFFFF, UIConstants.PRIMARY_CONTEXT_BUTTON, text, action));
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
        graphics.blitSprite(UIConstants.MODAL, this.x, this.y, this.contextWidth, this.contextHeight);
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
