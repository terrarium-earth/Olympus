package earth.terrarium.olympus.client.utils;

import earth.terrarium.olympus.client.components.base.renderer.WidgetRenderer;
import net.minecraft.client.gui.components.AbstractWidget;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface State<T> extends Consumer<T>, Supplier<T> {

    void set(T value);

    @Override
    default void accept(T value) {
        set(value);
    }

    @Override
    T get();

    default <W extends AbstractWidget> WidgetRenderer<W> withRenderer(Function<T, WidgetRenderer<W>> factory) {
        return (graphics, context, partialTick) -> factory.apply(get()).render(graphics, context, partialTick);
    }

    static <T> State<T> of(T initial) {
        return new State<>() {

            private T value = initial;

            @Override
            public T get() {
                return this.value;
            }

            @Override
            public void set(T value) {
                this.value = value;
            }
        };
    }

    static <T> State<T> empty() {
        return of(null);
    }
}