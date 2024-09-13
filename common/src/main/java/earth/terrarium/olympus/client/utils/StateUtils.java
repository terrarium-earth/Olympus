package earth.terrarium.olympus.client.utils;

public class StateUtils {

    public static Runnable booleanToggle(State<Boolean> state) {
        return () -> state.set(!state.get());
    }
}
