package dev.ouroboros.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.client.GuiModList;

import java.io.IOException;

public class GuiOuroMainMenu extends GuiScreen {
    private final ParticleField particles = new ParticleField();
    private float logoR, logoY, titleY;

    @Override
    public void initGui() {
        buttonList.clear();
        logoR = Math.min(40f, height / 8f);
        logoY = height * 0.22f;
        titleY = logoY + logoR + 12;

        int bw = 200, bh = 22, x = width / 2 - bw / 2;
        int y = (int) titleY + 30;
        buttonList.add(new GuiFlatButton(1, x, y, bw, bh, "Singleplayer"));
        buttonList.add(new GuiFlatButton(2, x, y + 26, bw, bh, "Multiplayer"));
        buttonList.add(new GuiFlatButton(3, x, y + 52, bw, bh, "Mods"));
        buttonList.add(new GuiFlatButton(0, x, y + 78, bw / 2 - 2, bh, "Options"));
        buttonList.add(new GuiFlatButton(5, x + bw / 2 + 2, y + 78, bw / 2 - 2, bh, "Language"));
        buttonList.add(new GuiFlatButton(4, x, y + 104, bw, bh, "Quit Game"));
    }

    @Override
    protected void actionPerformed(GuiButton b) throws IOException {
        switch (b.id) {
            case 0: mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings)); break;
            case 1: mc.displayGuiScreen(new GuiSelectWorld(this)); break;
            case 2: mc.displayGuiScreen(new GuiMultiplayer(this)); break;
            case 3: mc.displayGuiScreen(new GuiModList(this)); break;
            case 4: mc.shutdown(); break;
            case 5: mc.displayGuiScreen(new GuiLanguage(this, mc.gameSettings, mc.getLanguageManager())); break;
            default: break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawGradientRect(0, 0, width, height, 0xFF05080A, 0xFF0B1A13);
        particles.draw(width, height);

        SnakeLogo.draw(width / 2f, logoY, logoR, Theme.time());

        GlStateManager.pushMatrix();
        GlStateManager.translate(width / 2f, titleY, 0);
        GlStateManager.scale(2f, 2f, 1f);
        drawCenteredString(fontRendererObj, "OUROBOROS", 0, 0, Theme.ACCENT);
        GlStateManager.popMatrix();
        drawCenteredString(fontRendererObj, "C L I E N T", width / 2, (int) titleY + 18, Theme.MUTED);

        drawString(fontRendererObj, "Minecraft 1.8.9", 6, height - 12, Theme.MUTED);
        String user = "Logged in as " + mc.getSession().getUsername();
        drawString(fontRendererObj, user, width - fontRendererObj.getStringWidth(user) - 6, height - 12, Theme.MUTED);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char c, int key) throws IOException {
        // no ESC on the title screen
    }
}
