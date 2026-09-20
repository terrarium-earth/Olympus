package earth.terrarium.olympus.client.dialog;

import net.minecraft.Optionull;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.sdl.SDLDialog;
import org.lwjgl.sdl.SDLInit;
import org.lwjgl.sdl.SDLMain;
import org.lwjgl.sdl.SDL_DialogFileCallbackI;
import org.lwjgl.sdl.SDL_DialogFileFilter;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.Pointer;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class OlympusDialogs {

    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    private static final Pattern FILE_FILTER_PATTERN = Pattern.compile("\\*\\.[a-z0-9]+");

    private static void assertValidFileFilters(String... filters) {
        for (String filter : filters) {
            if (!FILE_FILTER_PATTERN.matcher(filter).matches()) {
                throw new IllegalArgumentException("Invalid file type: " + filter);
            }
        }
    }

    private static void openDialog(
            CompletableFuture<Optional<List<Path>>> future,
            boolean allowMany,
            Consumer<SDL_DialogFileCallbackI> factory
    ) {

        factory.accept((_, filelist, _) -> {
            if (MemoryUtil.NULL == filelist) {
                future.complete(Optional.empty());
                return;
            }

            if (allowMany) {
                int count = 0;
                while (MemoryUtil.memGetAddress(filelist + count * Pointer.POINTER_SIZE) != MemoryUtil.NULL) {
                    count++;
                }

                if (count == 0) {
                    future.complete(Optional.empty());
                    return;
                }

                var buffer = MemoryUtil.memPointerBuffer(filelist, count);
                var files = new ArrayList<Path>(count);
                for (int i = 0; i < count; i++) {
                    files.add(Path.of(buffer.getStringUTF8(i)));
                }

                future.complete(Optional.of(files));
            } else {
                var deref = MemoryUtil.memGetAddress(filelist);
                if (MemoryUtil.NULL == deref) {
                    future.complete(Optional.empty());
                    return;
                }

                future.complete(Optional.of(List.of(Path.of(MemoryUtil.memUTF8(deref)))));
            }
        });
    }

    public static CompletableFuture<Optional<List<Path>>> openFileSystemDialog(
            FileSystemDialogType type,
            @Nullable Path path,
            String... filters
    ) {
        assertValidFileFilters(filters);
        var filterBuffer = SDL_DialogFileFilter.calloc(filters.length);

        for (String filter : filters) {
            filterBuffer.put(
                    SDL_DialogFileFilter.create()
                            .name(MemoryStack.stackUTF8(filter))
                            .pattern(MemoryStack.stackUTF8(filter))
            );
        }

        try {
            String defaultPath = Optionull.map(path, p -> p.toAbsolutePath().toString());

            CompletableFuture<Optional<List<Path>>> future = new CompletableFuture<>();
            switch (type) {
                case OPEN_FILE -> openDialog(
                        future, false, callback -> SDLDialog.SDL_ShowOpenFileDialog(
                                callback,
                                0,
                                Minecraft.getInstance().getWindow().handle(),
                                filterBuffer,
                                defaultPath,
                                false
                        ));
                case OPEN_MULTIPLE_FILE -> openDialog(
                        future, true, callback -> {
                            SDLDialog.SDL_ShowOpenFolderDialog(
                                    callback,
                                    0,
                                    Minecraft.getInstance().getWindow().handle(),
                                    defaultPath,
                                    true
                            );
                        });
                case SAVE_FILE -> openDialog(
                        future, false, callback -> SDLDialog.SDL_ShowSaveFileDialog(
                                callback,
                                0,
                                Minecraft.getInstance().getWindow().handle(),
                                filterBuffer,
                                defaultPath
                        ));
                case SELECT_FOLDER -> openDialog(
                        future, false, callback -> SDLDialog.SDL_ShowOpenFolderDialog(
                                callback,
                                0,
                                Minecraft.getInstance().getWindow().handle(),
                                defaultPath,
                                false
                        ));
            }
            return future;
        } finally {
            filterBuffer.free();
        }
    }

    public enum FileSystemDialogType {
        OPEN_FILE,
        OPEN_MULTIPLE_FILE,
        SAVE_FILE,
        SELECT_FOLDER
    }
}
