package earth.terrarium.olympus.client.components.compound.radio;

import earth.terrarium.olympus.client.utils.State;

public class RadioState<T> implements State<T> {
    private T value;
    private int index = -1;

    public RadioState(T value) {
        this.value = value;
    }

    public static <T> RadioState<T> of(T value) {
        return new RadioState<>(value);
    }

    public static <T> RadioState<T> empty() {
        return new RadioState<>(null);
    }

    @Override
    public void set(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return this.value;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }
}
