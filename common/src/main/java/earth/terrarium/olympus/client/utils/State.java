package earth.terrarium.olympus.client.utils;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface State<T> extends Consumer<T>, Supplier<T> {

    void set(T value);

    @Override
    default void accept(T value) {
        set(value);
    }

    @Override
    T get();

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
}