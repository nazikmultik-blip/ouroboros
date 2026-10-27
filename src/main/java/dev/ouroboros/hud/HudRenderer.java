package dev.ouroboros.hud;

import dev.ouroboros.ClientEvents;
import dev.ouroboros.Settings;
import dev.ouroboros.gui.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/** In-game HUD: inventory panel (armor, held item, 27 slots) + FPS / CPS / coordinates. */
public class HudRenderer {
    private static final int SLOT = 18;

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post e) {
        if (e.type != RenderGameOverlayEvent.ElementType.ALL) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.gameSettings.showDebugInfo || mc.gameSettings.hideGUI) return;

        int x = 6, y = 6;
        boolean inv = Settings.INVENTORY.enabled, arm = Settings.ARMOR.enabled;
        if (inv || arm) y = drawInventoryPanel(mc, x, y, inv, arm) + 4;
        drawInfo(mc, x, y);
    }

    private int drawInventoryPanel(Minecraft mc, int x, int y, boolean inv, boolean arm) {
        int w = 9 * SLOT + 8;
        int h = 14 + (arm ? SLOT + 4 : 0) + (inv ? 3 * SLOT + 2 : 0) + 2;
        Gui.drawRect(x, y, x + w, y + h, 0xB00A100D);
        Gui.drawRect(x, y, x + w, y + 1, Theme.ACCENT);
        mc.fontRendererObj.drawStringWithShadow("INVENTORY", x + 4, y + 4, Theme.ACCENT);

        int cy = y + 14;
        int armorY = cy;
        if (arm) cy += SLOT + 4;
        int invY = cy;

        // slot backgrounds first
        if (arm) for (int i = 0; i < 5; i++) slotBg(x + 4 + i * SLOT, armorY);
        if (inv) for (int r = 0; r < 3; r++) for (int c = 0; c < 9; c++) slotBg(x + 4 + c * SLOT, invY + r * SLOT);

        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableRescaleNormal();
        if (arm) {
            for (int i = 0; i < 4; i++) {
                item(mc, mc.thePlayer.inventory.armorInventory[3 - i], x + 5 + i * SLOT, armorY + 1);
            }
            item(mc, mc.thePlayer.getHeldItem(), x + 5 + 4 * SLOT, armorY + 1);
        }
        if (inv) {
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 9; c++) {
                    item(mc, mc.thePlayer.inventory.mainInventory[9 + r * 9 + c], x + 5 + c * SLOT, invY + r * SLOT + 1);
                }
            }
        }
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        return y + h;
    }

    private void slotBg(int sx, int sy) {
        Gui.drawRect(sx, sy, sx + SLOT - 1, sy + SLOT - 1, 0x30FFFFFF);
    }

    private void item(Minecraft mc, ItemStack stack, int ix, int iy) {
        if (stack == null) return;
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, ix, iy);
        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, ix, iy, null);
    }

    private void drawInfo(Minecraft mc, int x, int y) {
        if (Settings.FPS.enabled) {
            line(mc, x, y, "FPS", String.valueOf(Minecraft.getDebugFPS()));
            y += 11;
        }
        if (Settings.CPS.enabled) {
            long now = System.currentTimeMillis();
            while (!ClientEvents.CLICKS.isEmpty() && now - ClientEvents.CLICKS.peekFirst() > 1000L) {
                ClientEvents.CLICKS.pollFirst();
            }
            line(mc, x, y, "CPS", String.valueOf(ClientEvents.CLICKS.size()));
            y += 11;
        }
        if (Settings.COORDS.enabled) {
            line(mc, x, y, "XYZ", (int) Math.floor(mc.thePlayer.posX) + " "
                    + (int) Math.floor(mc.thePlayer.posY) + " " + (int) Math.floor(mc.thePlayer.posZ));
        }
    }

    private void line(Minecraft mc, int x, int y, String label, String value) {
        int w = mc.fontRendererObj.drawStringWithShadow(label, x, y, Theme.ACCENT);
        mc.fontRendererObj.drawStringWithShadow(value, w + 4, y, 0xFFFFFFFF);
    }
}
