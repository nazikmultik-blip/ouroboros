package dev.ouroboros.gui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.util.Random;

/** Floating dots joined by lines: the classic cheat-client background. */
public class ParticleField {
    private static final int COUNT = 70;
    private final float[] x = new float[COUNT], y = new float[COUNT], vx = new float[COUNT], vy = new float[COUNT];
    private boolean ready;
    private long last;

    public void draw(int w, int h) {
        long now = System.nanoTime();
        if (!ready) {
            Random rnd = new Random();
            for (int i = 0; i < COUNT; i++) {
                x[i] = rnd.nextFloat() * w;
                y[i] = rnd.nextFloat() * h;
                vx[i] = (rnd.nextFloat() - 0.5f) * 14f;
                vy[i] = (rnd.nextFloat() - 0.5f) * 14f;
            }
            ready = true;
            last = now;
        }
        float dt = Math.min(0.1f, (now - last) / 1e9f);
        last = now;
        for (int i = 0; i < COUNT; i++) {
            x[i] += vx[i] * dt;
            y[i] += vy[i] * dt;
            if (x[i] < 0) x[i] += w;
            if (x[i] > w) x[i] -= w;
            if (y[i] < 0) y[i] += h;
            if (y[i] > h) y[i] -= h;
        }

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glLineWidth(1f);
        Tessellator t = Tessellator.getInstance();
        WorldRenderer wr = t.getWorldRenderer();
        wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        final float max = 55f;
        for (int i = 0; i < COUNT; i++) {
            for (int j = i + 1; j < COUNT; j++) {
                float dx = x[i] - x[j], dy = y[i] - y[j];
                float d = (float) Math.sqrt(dx * dx + dy * dy);
                if (d < max) {
                    int a = (int) ((1f - d / max) * 70);
                    wr.pos(x[i], y[i], 0).color(61, 255, 138, a).endVertex();
                    wr.pos(x[j], y[j], 0).color(61, 255, 138, a).endVertex();
                }
            }
        }
        t.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();

        for (int i = 0; i < COUNT; i++) {
            Gui.drawRect((int) x[i], (int) y[i], (int) x[i] + 2, (int) y[i] + 2, 0x883DFF8A);
        }
    }
}
