package earth.terrarium.olympus.client.pipelines.uniforms;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import net.minecraft.client.renderer.DynamicGpuDataStorage;
import net.minecraft.client.renderer.DynamicGpuDataStorageMapped;
import net.minecraft.client.renderer.DynamicGpuDataStorageNonMapped;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class RenderPipelineUniformsStorage {

    private static final List<DynamicGpuDataStorage<?>> storage = new ArrayList<>();

    public static <T extends DynamicGpuDataStorage.DynamicGpuData> Supplier<DynamicGpuDataStorage<T>> register(
            String name,
            int capacity,
            Std140SizeCalculator size
    ) {
        return Suppliers.memoize(() -> {
            var storage = new DynamicGpuDataStorageMapped<T>(name, size.get(), GpuBuffer.USAGE_UNIFORM, capacity);
            RenderPipelineUniformsStorage.storage.add(storage);
            return storage;
        });
    }

    public static void endFrame() {
        for (var uniformStorage : storage) {
            uniformStorage.endFrame();
        }
    }
}
