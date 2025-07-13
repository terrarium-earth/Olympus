package earth.terrarium.olympus.client.fabric;

import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.gui.render.state.pip.PictureInPictureRenderState;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PictureInPicturePool<T extends PictureInPictureRenderState> implements Closeable {

    private final Map<T, PoolEntry<T>> pool = new HashMap<>();
    private final Supplier<PictureInPictureRenderer<T>> factory;

    public PictureInPicturePool(Supplier<PictureInPictureRenderer<T>> factory) {
        this.factory = factory;
    }

    public void prepare(T state, GuiRenderState gui, int scale) {
        pool.computeIfAbsent(state, $ -> new PoolEntry<>(this.factory.get())).prepare(state, gui, scale);
    }

    public void end() {
        this.pool.values().removeIf(PoolEntry::closeIfUnused);
    }

    @Override
    public void close() {
        for (PoolEntry<T> entry : pool.values()) {
            entry.renderer.close();
        }
        pool.clear();
    }

    public record PoolEntry<T extends PictureInPictureRenderState>(
            MutableBoolean usedThisFrame,
            PictureInPictureRenderer<T> renderer
    ) {

        public PoolEntry(PictureInPictureRenderer<T> renderer) {
            this(new MutableBoolean(false), renderer);
        }

        public boolean closeIfUnused() {
            if (!usedThisFrame.isTrue()) {
                renderer.close();
                return true;
            }
            usedThisFrame.setFalse();
            return false;
        }

        public void prepare(T pictureInPictureRenderState, GuiRenderState guiRenderState, int scale) {
            renderer.prepare(pictureInPictureRenderState, guiRenderState, scale);
            usedThisFrame.setTrue();
        }

        public void end() {
            usedThisFrame.setFalse();
        }
    }
}
