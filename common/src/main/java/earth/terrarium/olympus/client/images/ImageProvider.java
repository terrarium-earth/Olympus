package earth.terrarium.olympus.client.images;

import com.google.common.hash.HashCode;
import com.mojang.blaze3d.platform.NativeImage;
import earth.terrarium.olympus.client.ui.UIConstants;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.Optionull;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ImageProvider<T> {

    private final Map<T, ImageEntry> cache = new HashMap<>();
    private final Object2LongMap<T> cacheAccessTimes = new Object2LongOpenHashMap<>();

    private final String name;
    private final ImageProvider.Factory<T> factory;
    private final ImageProvider.Hasher<T> hasher;
    private final long cacheTimeout;

    ImageProvider(String name, ImageProvider.Factory<T> factory, ImageProvider.Hasher<T> hasher, long cacheTimeout) {
        this.name = name;
        this.factory = factory;
        this.hasher = hasher;
        this.cacheTimeout = cacheTimeout;
    }

    @Nullable
    private CompletableFuture<NativeImage> getImage(T key) {
        try {
            return this.factory.apply(key);
        } catch (Exception e) {
            return null;
        }
    }

    public ResourceLocation get(T key) {
        this.cacheAccessTimes.put(key, System.currentTimeMillis());

        if (this.cache.containsKey(key)) {
            return Optionull.mapOrDefault(this.cache.get(key), ImageEntry::id, MissingTextureAtlasSprite.getLocation());
        } else {
            CompletableFuture<NativeImage> image = this.getImage(key);
            if (image == null) {
                this.cache.put(key, null);
                return MissingTextureAtlasSprite.getLocation();
            } else {
                var hash = this.hasher.apply(key).toString();
                ResourceLocation location = ResourceLocation.fromNamespaceAndPath(
                        UIConstants.MOD_ID,
                        "generated_images/" + this.name + "/" + hash
                );
                ImageEntry entry = new ImageEntry(image, location);
                this.cache.put(key, entry);
                return entry.id();
            }
        }
    }

    protected void checkCaches() {
        long currentTime = System.currentTimeMillis();
        this.cacheAccessTimes.object2LongEntrySet().removeIf(entry -> {
            if (currentTime - entry.getLongValue() > this.cacheTimeout) {
                ImageEntry imageEntry = this.cache.remove(entry.getKey());
                if (imageEntry != null) {
                    imageEntry.release();
                }
                return true;
            }
            return false;
        });
    }

    @FunctionalInterface
    public interface Factory<T> {

        @Nullable CompletableFuture<NativeImage> apply(@NotNull T t) throws Exception;
    }

    @FunctionalInterface
    public interface Hasher<T> extends Function<@NotNull T, @NotNull HashCode> {}
}
