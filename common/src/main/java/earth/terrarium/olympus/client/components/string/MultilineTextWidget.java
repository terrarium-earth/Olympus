package earth.terrarium.olympus.client.components.string;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.components.CursorWidget;
import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractStringWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MultilineTextWidget extends AbstractStringWidget implements CursorWidget {

	protected float alignX = 0.5f;
	protected float textAlign = 0f;
	protected boolean shadow;
	protected float scale = 1.0f;

	protected List<FormattedCharSequence> lines;
	protected int maxLineWidth;

	public MultilineTextWidget(int width, Component component, Font font) {
		super(0, 0, width, 0, component, font);

		this.lines = font.split(component, width);
		this.height = font.lineHeight * this.lines.size();
		this.maxLineWidth = this.lines.stream().mapToInt(font::width).max().orElse(0);
	}

	public MultilineTextWidget(Component text, int width) {
		this(width, text, Minecraft.getInstance().font);
	}

	public static MultilineTextWidget create(int width, Component text) {
		return new MultilineTextWidget(width, text, Minecraft.getInstance().font);
	}

	public MultilineTextWidget setColor(int color) {
		super.setColor(color);
		return this;
	}

	public @NotNull MultilineTextWidget alignLeft() {
		this.alignX = 0.0F;
		return this;
	}

	public @NotNull MultilineTextWidget alignCenter() {
		this.alignX = 0.5F;
		return this;
	}

	public @NotNull MultilineTextWidget alignRight() {
		this.alignX = 1.0F;
		return this;
	}

	public @NotNull MultilineTextWidget textAlignLeft() {
		this.textAlign = 0.0F;
		return this;
	}

	public @NotNull MultilineTextWidget textAlignCenter() {
		this.textAlign = 0.5F;
		return this;
	}

	public @NotNull MultilineTextWidget textAlignRight() {
		this.textAlign = 1.0F;
		return this;
	}

	public @NotNull MultilineTextWidget shadow() {
		this.shadow = true;
		return this;
	}

	public @NotNull MultilineTextWidget scale(float scale) {
		this.scale = scale;
		var inverseScale = 1.0f / this.scale;

		this.lines = this.getFont().split(this.getMessage(), (int) Math.ceil(width * inverseScale));
		this.height = (int) Math.ceil(getFont().lineHeight * this.lines.size() * scale);
		this.maxLineWidth = this.lines.stream().mapToInt(it -> (int)  Math.ceil(getFont().width(it) * scale)).max().orElse(0);
		return this;
	}

	@Override
	public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		Font font = this.getFont();

		int x = this.getX() + Math.round(this.alignX * (float)(this.getWidth() - maxLineWidth));
		int y = this.getY();

		PoseStack pose = graphics.pose();
		pose.pushPose();
		pose.translate(x, y, 0);
		pose.scale(this.scale, this.scale, 1f);

		y = 0;

		var invertedScale = 1.0f / this.scale;
		var adjustedMaxLineWidth = (int) Math.ceil(maxLineWidth * invertedScale);

		for (FormattedCharSequence line : this.lines) {
			var xOffset = (int) Math.ceil((adjustedMaxLineWidth - font.width(line)) * textAlign);
			graphics.drawString(font, line, xOffset, y, this.getColor(), this.shadow);
			y += font.lineHeight;
		}

		pose.popPose();
	}

	@Override
	public CursorScreen.Cursor getCursor() {
		return CursorScreen.Cursor.DEFAULT;
	}
}