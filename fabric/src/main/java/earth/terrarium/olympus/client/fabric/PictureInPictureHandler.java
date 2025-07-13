package earth.terrarium.olympus.client.fabric;

import earth.terrarium.olympus.client.pipelines.pips.RoundedRectanglePIPRenderer;
import earth.terrarium.olympus.client.pipelines.pips.RoundedTexturePIPRenderer;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.gui.render.state.pip.PictureInPictureRenderState;
import net.minecraft.client.renderer.MultiBufferSource;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PictureInPictureHandler implements Closeable {

    private static final Map<Class<? extends PictureInPictureRenderState>, Function<MultiBufferSource.BufferSource, PictureInPictureRenderer<? extends PictureInPictureRenderState>>> RENDERER_FACTORIES = new HashMap<>();
    static {
        RENDERER_FACTORIES.put(RoundedTexturePIPRenderer.State.class, RoundedTexturePIPRenderer::new);
        RENDERER_FACTORIES.put(RoundedRectanglePIPRenderer.State.class, RoundedRectanglePIPRenderer::new);
    }

    private final Map<Class<? extends PictureInPictureRenderState>, PictureInPicturePool<?>> pool = new HashMap<>();
    private final MultiBufferSource.BufferSource source;

    public PictureInPictureHandler(MultiBufferSource.BufferSource source) {
        this.source = source;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public <T extends PictureInPictureRenderState> PictureInPicturePool<PictureInPictureRenderState> getPool(Class<T> stateClass) {
        return (PictureInPicturePool<PictureInPictureRenderState>) pool.computeIfAbsent(stateClass, it -> {
            var factory = RENDERER_FACTORIES.get(it);
            return factory == null ? null : new PictureInPicturePool<>(() -> factory.apply(source));
        });
    }

    public void end() {
        pool.values().forEach(PictureInPicturePool::end);
    }

    @Override
    public void close() {
        pool.values().forEach(PictureInPicturePool::close);
        pool.clear();
    }
}
