package dev.ouroboros.gui;

import dev.ouroboros.OuroborosClient;
import dev.ouroboros.Settings;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import java.io.IOException;

/** Click-GUI style panel (Right Shift) with switches for the HUD modules. */
public class GuiOuroModules extends GuiScreen {
    private static final int W = 170, HEAD = 34, ROW = 20;
    private final float[] anim = new float[Settings.ALL.length];

    @Override
    public void initGui() {
        for (int i = 0; i < anim.length; i++) anim[i] = Settings.ALL[i].enabled ? 1f : 0f;
    }

    private int px() { return width / 2 - W / 2; }
    private int py() { return height / 2 - (HEAD + ROW * Settings.ALL.length) / 2; }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawGradientRect(0, 0, width, height, 0x90050A08, 0xB00B1A13);
        int x = px(), y = py(), h = HEAD + ROW * Settings.ALL.length;
        drawRect(x, y, x + W, y + h, Theme.PANEL);
        drawRect(x, y, x + W, y + 1, Theme.ACCENT);

        SnakeLogo.draw(x + 18, y + 17, 9f, Theme.time());
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 34, y + 12, 0);
        GlStateManager.scale(1.3f, 1.3f, 1f);
        fontRendererObj.drawStringWithShadow("OUROBOROS", 0, 0, Theme.ACCENT);
        GlStateManager.popMatrix();

        for (int i = 0; i < Settings.ALL.length; i++) {
            Settings.Module m = Settings.ALL[i];
            int ry = y + HEAD + i * ROW;
            boolean hover = mouseX >= x && mouseX < x + W && mouseY >= ry && mouseY < ry + ROW;
            if (hover) drawRect(x, ry, x + W, ry + ROW, 0x30FFFFFF);
            anim[i] += ((m.enabled ? 1f : 0f) - anim[i]) * 0.3f;
            fontRendererObj.drawStringWithShadow(m.name, x + 10, ry + 6, m.enabled ? Theme.TEXT : Theme.MUTED);

            int tx = x + W - 34, ty = ry + 5;
            drawRect(tx, ty, tx + 22, ty + 10, Theme.lerp(0xFF2A332E, Theme.ACCENT_DIM, anim[i]));
            int kx = tx + 1 + (int) (anim[i] * 11);
            drawRect(kx, ty + 1, kx + 9, ty + 9, Theme.lerp(0xFF8A948E, Theme.ACCENT, anim[i]));
        }
        drawCenteredString(fontRendererObj, "Right Shift / ESC to close", width / 2, y + h + 6, Theme.MUTED);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        super.mouseClicked(mouseX, mouseY, button);
        if (button != 0) return;
        int x = px(), y = py();
        if (mouseX < x || mouseX >= x + W || mouseY < y + HEAD) return;
        int i = (mouseY - y - HEAD) / ROW;
        if (i >= 0 && i < Settings.ALL.length) {
            Settings.ALL[i].enabled = !Settings.ALL[i].enabled;
            Settings.save();
        }
    }

    @Override
    protected void keyTyped(char c, int key) throws IOException {
        if (key == OuroborosClient.openMenu.getKeyCode()) {
            mc.displayGuiScreen(null);
            return;
        }
        super.keyTyped(c, key);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
