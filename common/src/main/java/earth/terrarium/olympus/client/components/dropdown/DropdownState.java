package earth.terrarium.olympus.client.components.dropdown;

import earth.terrarium.olympus.client.components.buttons.Button;
import earth.terrarium.olympus.client.utils.State;

public class DropdownState<T> implements State<T> {
    private Button button;
    private T state;
    private boolean openState;

    public DropdownState(Button button, T state, boolean openState) {
        this.button = button;
        this.state = state;
        this.openState = openState;
    }

    public DropdownState(T state) {
        this(null, state, false);
    }

    public Button getButton() {
        return button;
    }


    public boolean isOpened() {
        return openState;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public void setOpened(boolean openState) {
        this.openState = openState;
    }

    @Override
    public void set(T value) {
        this.state = value;
    }

    @Override
    public T get() {
        return state;
    }
}
