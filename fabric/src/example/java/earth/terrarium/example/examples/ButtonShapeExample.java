package earth.terrarium.example.examples;

import com.mojang.blaze3d.vertex.VertexConsumer;
import earth.terrarium.example.base.ExampleScreen;
import earth.terrarium.example.base.OlympusExample;
import earth.terrarium.olympus.client.components.Widgets;
import earth.terrarium.olympus.client.components.buttons.ButtonShapes;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import org.joml.Matrix4f;

@OlympusExample(id = "buttonshape", description = "A simple button shape example")
public class ButtonShapeExample extends ExampleScreen {

    @Override
    protected void init() {
        LinearLayout horizontal = LinearLayout.horizontal().spacing(20);

        horizontal.addChild(Widgets.button()
                .withShape(ButtonShapes.ELLIPSE)
                .withRenderer((graphics, context, partialTicks) -> {
                    Matrix4f matrix = graphics.pose().last().pose();
                    VertexConsumer consumer = graphics.bufferSource().getBuffer(RenderType.debugLineStrip(2));

                    for (int i = 0; i < 360; i++) {
                        float rad = (float) Math.toRadians(i);
                        float x = (float) Math.cos(rad) * 10 + 10 + context.getX();
                        float y = (float) Math.sin(rad) * 10 + 10 + context.getY();
                        consumer.addVertex(matrix, x, y, 100).setColor(1.0F, 0.0F, 0.0F, 1.0F);
                    }

                    graphics.flush();
                })
                .withTexture(null)
                .withTooltip(Component.literal("This is a circle button"))
                .withSize(20, 20)
        );

        horizontal.addChild(Widgets.button()
                .withShape(ButtonShapes.DIAMOND)
                .withRenderer((graphics, context, partialTicks) -> {
                    Matrix4f matrix = graphics.pose().last().pose();
                    VertexConsumer consumer = graphics.bufferSource().getBuffer(RenderType.debugLineStrip(2));

                    int halfWidth = context.getWidth() / 2;
                    int halfHeight = context.getHeight() / 2;

                    consumer.addVertex(matrix, context.getX(), context.getY() + halfHeight, 100).setColor(1.0F, 0.0F, 0.0F, 1.0F);
                    consumer.addVertex(matrix, context.getX() + halfWidth, context.getY(), 100).setColor(1.0F, 0.0F, 0.0F, 1.0F);
                    consumer.addVertex(matrix, context.getX() + context.getWidth(), context.getY() + halfHeight, 100).setColor(1.0F, 0.0F, 0.0F, 1.0F);
                    consumer.addVertex(matrix, context.getX() + halfWidth, context.getY() + context.getHeight(), 100).setColor(1.0F, 0.0F, 0.0F, 1.0F);
                    consumer.addVertex(matrix, context.getX(), context.getY() + halfHeight, 100).setColor(1.0F, 0.0F, 0.0F, 1.0F);

                    graphics.flush();
                })
                .withTexture(null)
                .withTooltip(Component.literal("This is a diamond button"))
                .withSize(20, 20)
        );

        horizontal.arrangeElements();
        FrameLayout.centerInRectangle(horizontal, 0, 0, this.width, this.height);
        horizontal.visitWidgets(this::addRenderableWidget);
    }
}
