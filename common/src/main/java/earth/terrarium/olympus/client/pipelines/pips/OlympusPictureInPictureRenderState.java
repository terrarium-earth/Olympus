package earth.terrarium.olympus.client.pipelines.pips;

import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.gui.render.state.pip.PictureInPictureRenderState;
import net.minecraft.client.renderer.MultiBufferSource;

import java.util.function.Function;

public interface OlympusPictureInPictureRenderState<T extends OlympusPictureInPictureRenderState<T>> extends PictureInPictureRenderState {

    Function<MultiBufferSource.BufferSource, PictureInPictureRenderer<T>> getFactory();
}
