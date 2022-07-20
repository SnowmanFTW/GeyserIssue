package me.snowman.prisonstom;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.hologram.Hologram;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.extras.velocity.VelocityProxy;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.SharedInstance;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.DimensionType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class PrisonStom {
    private static SharedInstance spawn;
    private static SharedInstance mineWorld;

        public static void main(String[] args) throws FileNotFoundException {
            MinecraftServer minecraftServer = MinecraftServer.init();
            File file = new File("secret.txt");
            Scanner scanner = new Scanner(file);
            String secret = "";
            while (scanner.hasNextLine()) {
                secret = scanner.nextLine();
            }
            scanner.close();
            VelocityProxy.enable(secret);
            initInstances();

            MinecraftServer.getGlobalEventHandler().addListener(PlayerLoginEvent.class, event -> {
                event.setSpawningInstance(spawn);
            });

            MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent.class, event -> {
                event.getPlayer().setGameMode(GameMode.CREATIVE);
            });



            registerCommands();
            minecraftServer.start("0.0.0.0", 25565);
        }


    private static void initInstances(){
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        DimensionType dimensionType = DimensionType.builder(NamespaceID.from("minecraft:spawn")).minY(-64).height(384).build();
        DimensionType dimensionType1 = DimensionType.builder(NamespaceID.from("minecraft:mines")).minY(-64).height(384).build();
        MinecraftServer.getDimensionTypeManager().addDimension(dimensionType);
        MinecraftServer.getDimensionTypeManager().addDimension(dimensionType1);
        InstanceContainer spawnContainer = instanceManager.createInstanceContainer(dimensionType);
        InstanceContainer mineContainer = instanceManager.createInstanceContainer(dimensionType1);

        mineWorld = instanceManager.createSharedInstance(mineContainer);
        mineWorld.getInstanceContainer().setChunkLoader(new AnvilLoader("mines"));
        mineWorld.setTag(Tag.String("name"), "mines");
        mineWorld.enableAutoChunkLoad(true);

        spawn = instanceManager.createSharedInstance(spawnContainer);
        spawn.getInstanceContainer().setChunkLoader(new AnvilLoader("spawn"));
        spawn.setTag(Tag.String("name"), "spawn");
        spawn.enableAutoChunkLoad(true);

    }

    private static void registerCommands(){
            MinecraftServer.getCommandManager().register(new World());
    }
}
