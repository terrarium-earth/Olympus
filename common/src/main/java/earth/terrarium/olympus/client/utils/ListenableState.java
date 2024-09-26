package earth.terrarium.olympus.client.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ListenableState<T> implements State<T> {
    protected final List<Consumer<T>> listeners = new ArrayList<>();
    protected T value;

    public ListenableState(T value) {
        this.value = value;
    }

    public static <T> ListenableState<T> of(T initial) {
        return new ListenableState<>(initial);
    }

    public static <T> ListenableState<T> empty() {
        return of(null);
    }

    @Override
    public void set(T value) {
        this.value = value;
        listeners.forEach(listener -> listener.accept(value));
    }

    @Override
    public T get() {
        return value;
    }

    public void registerListener(Consumer<T> listener) {
        listeners.add(listener);
    }

    public void unregisterListener(Consumer<T> listener) {
        listeners.remove(listener);
    }
}
