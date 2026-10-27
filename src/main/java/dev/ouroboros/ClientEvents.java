package dev.ouroboros;

import dev.ouroboros.gui.GuiOuroLoading;
import dev.ouroboros.gui.GuiOuroMainMenu;
import dev.ouroboros.gui.GuiOuroModules;
import dev.ouroboros.gui.GuiOuroPause;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.util.ArrayDeque;
import java.util.Deque;

public class ClientEvents {
    /** timestamps (ms) of left clicks in the last second, for the CPS counter */
    public static final Deque<Long> CLICKS = new ArrayDeque<Long>();

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent e) {
        if (e.gui == null) return;
        Minecraft mc = Minecraft.getMinecraft();
        Class<?> c = e.gui.getClass();
        if (c == GuiMainMenu.class) {
            e.gui = new GuiOuroMainMenu();
        } else if (c == GuiIngameMenu.class) {
            e.gui = new GuiOuroPause();
        } else if (c == GuiDownloadTerrain.class && mc.getNetHandler() != null) {
            e.gui = new GuiOuroLoading(mc.getNetHandler());
        }
    }

    @SubscribeEvent
    public void onMouse(MouseEvent e) {
        if (e.button == 0 && e.buttonstate) CLICKS.addLast(System.currentTimeMillis());
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent e) {
        Minecraft mc = Minecraft.getMinecraft();
        if (OuroborosClient.openMenu.isPressed() && mc.currentScreen == null && mc.thePlayer != null) {
            mc.displayGuiScreen(new GuiOuroModules());
        }
    }
}
