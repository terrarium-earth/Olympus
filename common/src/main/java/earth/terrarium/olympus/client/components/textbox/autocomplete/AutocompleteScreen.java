package earth.terrarium.olympus.client.components.textbox.autocomplete;

import earth.terrarium.olympus.client.components.base.ListWidget;
import earth.terrarium.olympus.client.components.textbox.TextBox;
import earth.terrarium.olympus.client.ui.Overlay;
import earth.terrarium.olympus.client.ui.UIConstants;
import earth.terrarium.olympus.client.utils.ListenableState;
import earth.terrarium.olympus.client.utils.State;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class AutocompleteScreen<T> extends Overlay {

    private static final ResourceLocation LIST = UIConstants.id("textbox/list");
    private static final int ENTRY_HEIGHT = 12;

    private final List<T> suggestions = new ArrayList<>();
    private final List<AbstractWidget> filteredSuggestions = new ArrayList<>();

    protected final BiPredicate<String, T> filter;
    protected final Function<T, String> mapper;

    protected final AutocompleteTextBox<T> widget;

    protected ListWidget options;
    protected ListenableState<String> text = ListenableState.of("");

    protected AutocompleteScreen(Screen background, AutocompleteTextBox<T> widget) {
        super(background);
        this.filter = widget.filter;
        this.mapper = widget.mapper;
        this.suggestions.addAll(widget.suggestions);
        this.widget = widget;

        text.registerListener(this::filter);
    }

    public int x() {
        return this.widget.getX();
    }

    public int y() {
        int y = this.widget.getY() - this.height();
        if (y < 0) {
            y = this.widget.getY() + this.widget.getHeight();
        }
        return y;
    }

    public int width() {
        return this.widget.getWidth();
    }

    public int height() {
        return Math.min(ENTRY_HEIGHT * 10, this.filteredSuggestions.size() * ENTRY_HEIGHT) + 3;
    }

    @Override
    protected void init() {
        var textBox = addRenderableWidget(new TextBox(this.text).withSize(this.widget.getWidth(), this.widget.getHeight()));
        textBox.setPosition(this.widget.getX(), this.widget.getY());

        ListWidget old = this.options;
        this.options = addRenderableWidget(new ListWidget(this.width() - 3, this.height() - 3));
        this.options.setPosition(this.x() + 1, this.y() + 2);
        this.options.update(old);

        setFocused(textBox);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        this.onClose();
        return true;
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        if (this.filteredSuggestions.isEmpty()) return;
        graphics.blitSprite(LIST, this.x(), this.y(), this.width(), this.height());
    }

    public void clear() {
        this.text.set("");
    }

    public T value() {
        for (T suggestion : this.suggestions) {
            if (this.mapper.apply(suggestion).equals(text())) {
                return suggestion;
            }
        }
        return null;
    }

    public String text() {
        return this.text.get();
    }

    public void filter(String text) {
        this.widget.value = text;

        this.filteredSuggestions.clear();
        if (!text.isEmpty()) {
            for (T suggestion : this.suggestions) {
                if (this.filter.test(text, suggestion)) {
                    String value = this.mapper.apply(suggestion);
                    this.filteredSuggestions.add(new AutocompleteEntry<>(
                        this.width() - 3, ENTRY_HEIGHT,
                        value, () -> {
                            this.text.set(value);
                            this.onClose();
                        }
                    ));
                }
            }
        }

        this.options.set(this.filteredSuggestions);
        this.options.setHeight(this.height() - 3);
        this.options.setPosition(this.x() + 1, this.y() + 2);
    }
}
