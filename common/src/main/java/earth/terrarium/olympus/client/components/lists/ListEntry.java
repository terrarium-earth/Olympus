package earth.terrarium.olympus.client.components.lists;

import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import net.minecraft.client.gui.GuiGraphics;

public interface ListEntry<T> {

    void render(GuiGraphics graphics, ScissorBoxStack scissor, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks);

    int getHeight(int width);

    default boolean mouseClicked(double mouseX, double mouseY, int mouseButton, int width) {
        return false;
    }

    /**
     * if the entry requires a list to be used this will update the list when needed.
     */
    default void setList(EntryListWidget<T> list) {
    }
}
