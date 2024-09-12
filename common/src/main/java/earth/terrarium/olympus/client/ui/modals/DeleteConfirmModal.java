package earth.terrarium.olympus.client.ui.modals;

import earth.terrarium.olympus.client.components.Widgets;
import earth.terrarium.olympus.client.components.renderers.WidgetRenderers;
import earth.terrarium.olympus.client.components.string.MultilineTextWidget;
import earth.terrarium.olympus.client.constants.MinecraftColors;
import earth.terrarium.olympus.client.ui.UIConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class DeleteConfirmModal extends BaseModal {

    private static final int WIDTH = 150;
    private static final int HEIGHT = 100;
    private static final int WIDGET_HEIGHT = 24;

    private final Component description;
    private final Component confirm;
    private final Runnable action;

    private int buttonsHeight = 0;

    protected DeleteConfirmModal(Component title, Component description, Component confirm, Runnable action, Screen background) {
        super(title, background);

        this.description = description;
        this.confirm = confirm;
        this.action = action;

        this.minHeight = HEIGHT;
        this.minWidth = WIDTH;
        this.ratio = 0f;
    }

    public DeleteConfirmModal(Component title, Component description, Runnable action, Screen background) {
        this(title, description, UIConstants.DELETE, action, background);
    }

    @Override
    protected void init() {
        this.modalWidth = Math.round(Math.max(this.minWidth, this.width * this.ratio));
        this.left = (this.width - this.modalWidth) / 2;
        this.modalContentLeft = this.left + INNER_PADDING;
        this.modalContentWidth = this.modalWidth - INNER_PADDING * 2;

        int buttonWidth = (this.modalContentWidth - INNER_PADDING) / 2;

        GridLayout layout = new GridLayout().rowSpacing(INNER_PADDING * 2);

        layout.addChild(
            new MultilineTextWidget(this.modalContentWidth, this.description, this.font).setColor(0xFFFFFF),
            0, 0
        );

        LinearLayout buttons = LinearLayout.horizontal().spacing(INNER_PADDING);

        buttons.addChild(Widgets.button()
                .withCallback(this::onClose)
                .withRenderer(WidgetRenderers.text(UIConstants.CANCEL, MinecraftColors.DARK_GRAY))
                .withSize(buttonWidth, WIDGET_HEIGHT)
        );

        buttons.addChild(Widgets.button()
                .withCallback(() -> {
                    this.action.run();
                    this.onClose();
                })
                .withTexture(UIConstants.DANGER_BUTTON)
                .withRenderer(WidgetRenderers.text(this.confirm, MinecraftColors.WHITE))
                .withSize(buttonWidth, WIDGET_HEIGHT)
        );

        layout.addChild(buttons, 1, 0);
        layout.arrangeElements();

        this.modalHeight = layout.getHeight() + TITLE_BAR_HEIGHT + INNER_PADDING * 2;
        this.top = (this.height - this.modalHeight) / 2;
        this.modalContentTop = this.top + TITLE_BAR_HEIGHT + INNER_PADDING;
        this.modalContentHeight = this.modalHeight - TITLE_BAR_HEIGHT - INNER_PADDING * 2;
        this.buttonsHeight = buttons.getHeight();

        layout.setPosition(this.modalContentLeft, this.modalContentTop);
        layout.visitWidgets(this::addRenderableWidget);
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blitSprite(
                UIConstants.MODAL_FOOTER,
                this.left + 1, this.top + this.modalHeight - this.buttonsHeight - INNER_PADDING * 2 - 1,
                this.modalWidth - 2, this.buttonsHeight + INNER_PADDING * 2
        );
    }

    public static void open(Component title, Component description, Runnable action) {
        open(title, description, UIConstants.DELETE, action);
    }

    public static void open(Component title, Component description, Component confirm, Runnable action) {
        Screen background = Minecraft.getInstance().screen;
        DeleteConfirmModal modal = new DeleteConfirmModal(title, description, confirm, action, background);
        Minecraft.getInstance().setScreen(modal);
    }
}
