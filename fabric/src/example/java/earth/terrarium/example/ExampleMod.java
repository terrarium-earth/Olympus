package earth.terrarium.example;

import earth.terrarium.example.base.ExampleGatherer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ExampleMod implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        var examples = ExampleGatherer.getExamples();
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, access) -> {
            var command = ClientCommandManager.literal("example");
            for (var example : examples) {
                command.then(ClientCommandManager.literal(example.getFirst().id())
                        .then(ClientCommandManager.literal("help").executes(context -> {
                            context.getSource().sendFeedback(Component.literal(
                                    "Example: " + example.getFirst().id() + " - " + example.getFirst().description()
                            ));
                            return 1;
                        }))
                        .executes(context -> {
                            showScreen(example.getSecond().get());
                            return 1;
                        })
                );
            }
            dispatcher.register(ClientCommandManager.literal("olympus").then(command));
        });
    }

    public static void showScreen(Screen screen) {
        Minecraft.getInstance().tell(() -> Minecraft.getInstance().setScreen(screen));
    }
}
