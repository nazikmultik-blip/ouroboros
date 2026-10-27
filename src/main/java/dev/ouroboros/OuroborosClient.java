package dev.ouroboros;

import dev.ouroboros.hud.HudRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.input.Keyboard;

@Mod(modid = OuroborosClient.MODID, name = "Ouroboros Client", version = "1.0.0",
        acceptedMinecraftVersions = "[1.8.9]", acceptableRemoteVersions = "*")
public class OuroborosClient {
    public static final String MODID = "ouroboros";
    public static KeyBinding openMenu;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        Settings.load(e.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        openMenu = new KeyBinding("Open Ouroboros menu", Keyboard.KEY_RSHIFT, "Ouroboros Client");
        ClientRegistry.registerKeyBinding(openMenu);
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        MinecraftForge.EVENT_BUS.register(new HudRenderer());
    }
}
