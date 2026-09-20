package earth.terrarium.example;

import earth.terrarium.example.base.ExampleGatherer;
import earth.terrarium.example.renderdoc.RenderDoc;
import earth.terrarium.olympus.client.dialog.OlympusDialogs;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.nio.file.Path;
import java.util.Collections;

public class ExampleMod implements ClientModInitializer {

    public static void showScreen(Screen screen) {
        Minecraft.getInstance().schedule(() -> Minecraft.getInstance().gui.setScreen(screen));
    }

    @Override
    public void onInitializeClient() {
        RenderDoc.init();

        var examples = ExampleGatherer.getExamples();
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) -> {
            var command = ClientCommands.literal("example");
            for (var example : examples) {
                command.then(ClientCommands.literal(example.getFirst().id())
                        .then(ClientCommands.literal("help").executes(context -> {
                            context.getSource().sendFeedback(Component.literal(
                                    "Example: " + example.getFirst().id() + " - " + example.getFirst().description()
                            ));
                            return 1;
                        }))
                        .executes(_ -> {
                            showScreen(example.getSecond().get());
                            return 1;
                        })
                );
            }
            dispatcher.register(ClientCommands.literal("olympus").then(command));
        });
    }
}
