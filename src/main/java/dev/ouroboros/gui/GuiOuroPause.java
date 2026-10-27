package dev.ouroboros.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiShareToLan;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.client.GuiModList;

import java.io.IOException;

/** Pause menu: ouroboros logo on top, flat buttons underneath. */
public class GuiOuroPause extends GuiScreen {
    private float logoR, logoY;
    private int panelTop, panelBottom;

    @Override
    public void initGui() {
        buttonList.clear();
        logoR = Math.min(32f, height / 10f);
        logoY = logoR + 18;
        int bw = 180, bh = 22, x = width / 2 - bw / 2;
        int y = (int) (logoY + logoR + 38);
        panelTop = 6;

        buttonList.add(new GuiFlatButton(4, x, y, bw, bh, "Back to Game"));
        y += 26;
        buttonList.add(new GuiFlatButton(0, x, y, bw / 2 - 2, bh, "Options"));
        GuiFlatButton lan = new GuiFlatButton(7, x + bw / 2 + 2, y, bw / 2 - 2, bh, "Open to LAN");
        lan.enabled = mc.isSingleplayer() && !mc.getIntegratedServer().getPublic();
        buttonList.add(lan);
        y += 26;
        buttonList.add(new GuiFlatButton(5, x, y, bw / 2 - 2, bh, "Achievements"));
        buttonList.add(new GuiFlatButton(6, x + bw / 2 + 2, y, bw / 2 - 2, bh, "Statistics"));
        y += 26;
        buttonList.add(new GuiFlatButton(8, x, y, bw, bh, "Mods"));
        y += 26;
        buttonList.add(new GuiFlatButton(1, x, y, bw, bh,
                mc.isIntegratedServerRunning() ? "Save and Quit to Title" : "Disconnect"));
        panelBottom = y + bh + 8;
    }

    @Override
    protected void actionPerformed(GuiButton b) throws IOException {
        switch (b.id) {
            case 0: mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings)); break;
            case 1:
                boolean local = mc.isIntegratedServerRunning();
                b.enabled = false;
                mc.theWorld.sendQuittingDisconnectingPacket();
                mc.loadWorld(null);
                if (local) mc.displayGuiScreen(new GuiOuroMainMenu());
                else mc.displayGuiScreen(new GuiMultiplayer(new GuiOuroMainMenu()));
                break;
            case 4:
                mc.displayGuiScreen(null);
                mc.setIngameFocus();
                break;
            case 5: mc.displayGuiScreen(new GuiAchievements(this, mc.thePlayer.getStatFileWriter())); break;
            case 6: mc.displayGuiScreen(new GuiStats(this, mc.thePlayer.getStatFileWriter())); break;
            case 7: mc.displayGuiScreen(new GuiShareToLan(this)); break;
            case 8: mc.displayGuiScreen(new GuiModList(this)); break;
            default: break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawGradientRect(0, 0, width, height, 0xB0050A08, 0xD00B1A13);

        int px = width / 2 - 100;
        drawRect(px, panelTop, px + 200, panelBottom, Theme.PANEL);
        drawRect(px, panelTop, px + 200, panelTop + 1, Theme.ACCENT);

        SnakeLogo.draw(width / 2f, logoY, logoR, Theme.time());

        GlStateManager.pushMatrix();
        GlStateManager.translate(width / 2f, logoY + logoR + 12, 0);
        GlStateManager.scale(1.5f, 1.5f, 1f);
        drawCenteredString(fontRendererObj, "OUROBOROS", 0, 0, Theme.ACCENT);
        GlStateManager.popMatrix();
        drawCenteredString(fontRendererObj, "PAUSED", width / 2, (int) (logoY + logoR + 26), Theme.MUTED);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
