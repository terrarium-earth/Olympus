package earth.terrarium.olympus.client.images;

import org.jetbrains.annotations.ApiStatus;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ImageProviders {

    private static final List<ImageProvider<?>> PROVIDERS = new ArrayList<>();
    private static long ticks = 0;

    public static <T> ImageProvider<T> register(String name, ImageProvider.Factory<T> factory, ImageProvider.Hasher<T> hasher, Duration cacheTimeout) {
        ImageProvider<T> provider = new ImageProvider<>(name, factory, hasher, cacheTimeout.toMillis());
        PROVIDERS.add(provider);
        return provider;
    }

    @ApiStatus.Internal
    public static void tick() {
        ticks++;
        if (ticks % 100 == 0) { // 5 seconds
            for (ImageProvider<?> provider : PROVIDERS) {
                provider.checkCaches();
            }
        }
    }
}
