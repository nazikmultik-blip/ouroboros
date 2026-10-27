package dev.ouroboros.gui;

import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.multiplayer.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;

/** Replaces the dirt "Downloading terrain" screen shown while joining a world/server. */
public class GuiOuroLoading extends GuiDownloadTerrain {
    private static final String[] TIPS = {
            "The snake that eats its own tail never runs out of food.",
            "Press Right Shift in game to open the Ouroboros menu.",
            "Toggle the inventory HUD in the Ouroboros menu.",
            "Everything ends where it begins."
    };
    private final ParticleField particles = new ParticleField();
    private final String tip = TIPS[(int) (System.currentTimeMillis() / 1000 % TIPS.length)];

    public GuiOuroLoading(NetHandlerPlayClient handler) {
        super(handler);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawGradientRect(0, 0, width, height, 0xFF05080A, 0xFF0B1A13);
        particles.draw(width, height);

        float time = Theme.time();
        float r = Math.min(34f, height / 8f);
        SnakeLogo.draw(width / 2f, height / 2f - 14, r, time);

        int dots = (int) (time * 2.5f) % 4;
        String text = "Loading world" + "...".substring(0, dots);
        drawCenteredString(fontRendererObj, text, width / 2, (int) (height / 2f + r + 4), Theme.TEXT);

        // sweeping indeterminate bar
        int bw = 140, bx = width / 2 - bw / 2, by = (int) (height / 2f + r + 20);
        drawRect(bx, by, bx + bw, by + 2, 0x30FFFFFF);
        float p = (time * 0.7f) % 1f;
        int seg = 40;
        int sx = bx + (int) ((bw + seg) * p) - seg;
        int x0 = Math.max(bx, sx), x1 = Math.min(bx + bw, sx + seg);
        if (x1 > x0) drawRect(x0, by, x1, by + 2, Theme.ACCENT);

        drawCenteredString(fontRendererObj, tip, width / 2, height - 24, Theme.MUTED);
    }
}
