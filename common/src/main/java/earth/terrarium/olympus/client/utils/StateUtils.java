package earth.terrarium.olympus.client.utils;

import earth.terrarium.olympus.client.components.compound.radio.RadioState;
import net.minecraft.util.TriState;

public class StateUtils {

    public static Runnable booleanToggle(State<Boolean> state) {
        return () -> state.set(!state.get());
    }

    public static RadioState<TriState> tristate(TriState state) {
        return new RadioState<>(state, switch (state) {
            case TRUE -> 0;
            case DEFAULT -> 1;
            case FALSE -> 2;
        });
    }
}
