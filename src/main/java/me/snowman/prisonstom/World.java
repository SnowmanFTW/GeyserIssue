package me.snowman.prisonstom;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class World extends Command {
    public World() {
        super("world");
        var world = ArgumentType.String("world");

        world.setSuggestionCallback((sender, context, suggestion) -> {
            for(Instance instance: MinecraftServer.getInstanceManager().getInstances()){
                if(instance.getTag(Tag.String("name")) == null) continue;
                suggestion.addEntry(new SuggestionEntry(instance.getTag(Tag.String("name"))));
            }

        });

        addSyntax((sender, context) -> {
            Player player = (Player) sender;
            String worldName = context.get(world);
            for (Instance instance : MinecraftServer.getInstanceManager().getInstances()) {
                if (instance.getTag(Tag.String("name")) == null) continue;
                if (instance.getTag(Tag.String("name")).equals(worldName)) {
                    if (!player.getInstance().getTag(Tag.String("name")).equals(worldName)) {
                        player.setInstance(instance);
                    } else player.sendMessage(Component.text("You are already in world " + worldName));
                }
            }
        }, world);
    }
}
