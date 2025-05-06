package earth.terrarium.olympus.client.pipelines;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.ApiStatus;

/**
 * @deprecated use {@link RoundedRectangle} instead.
 */
@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "1.21.6 or 1.22.0")
public class RoundedRectanage {

    public static final RenderPipeline PIPELINE = RoundedRectangle.PIPELINE;

    public static void draw(
            GuiGraphics graphics,
            int x, int y, int width, int height,
            int backgroundColor, int borderColor,
            float borderRadius, int borderWidth
    ) {
        RoundedRectangle.draw(
                graphics,
                x, y, width, height,
                backgroundColor, borderColor,
                borderRadius, borderWidth
        );
    }
}
